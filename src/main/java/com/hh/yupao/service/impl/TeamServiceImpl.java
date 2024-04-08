package com.hh.yupao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hh.yupao.common.ErrorCode;
import com.hh.yupao.enum1.TeamStatusEnum;
import com.hh.yupao.exception.BusinessException;
import com.hh.yupao.mapper.TeamMapper;
import com.hh.yupao.model.domain.Team;
import com.hh.yupao.model.domain.User;
import com.hh.yupao.model.domain.UserTeam;
import com.hh.yupao.model.dto.TeamQuery;
import com.hh.yupao.model.request.TeamJoinRequest;
import com.hh.yupao.model.request.TeamQuitRequest;
import com.hh.yupao.model.request.TeamUpdateRequest;
import com.hh.yupao.model.vo.TeamUserVO;
import com.hh.yupao.model.vo.UserVO;
import com.hh.yupao.service.TeamService;
import com.hh.yupao.service.UserService;
import com.hh.yupao.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ybb
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2024-03-13 18:35:50
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {
    @Resource
    private UserTeamService userTeamService;
    @Resource
    private UserService userService;
    @Resource
    private RedissonClient redissonClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addTeam(Team team, User loginUser) {
//        1. 请求参数是否为空？
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        2. 是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
//        3. 校验信息
//        a. 队伍人数 > 1 且 <= 20
        Integer maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不符合要求");
        }
//        b. 队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不合法");
        }
//        c. 描述 <= 512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述不合法");
        }
//        d. status 是否公开（int）不传默认为 0（公开）
        //  d. status 是否公开（int）不传默认为 0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
//        e. 如果 status 是加密状态，一定要有密码，且密码 <= 32
        //  e. 如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
            }
        }
        //  f. 超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间 > 当前时间");
        }
//        g. 校验用户最多创建 5 个队伍
        if (this.count(new QueryWrapper<Team>().eq("userId", loginUser.getId())) >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍数量超过限制");
        }
//        4. 插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(loginUser.getId());
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result || teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "插入失败");
        }
//        5. 插入用户 => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(loginUser.getId());
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        return teamId;
    }

    @Override
    public List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin) {
        QueryWrapper<Team> objectQueryWrapper = new QueryWrapper<>();
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            if (id != null && id > 0) {
                objectQueryWrapper.eq("id", id);
            }
            List<Long> idList = teamQuery.getIdList();
            if (!CollectionUtils.isEmpty(idList)){
                objectQueryWrapper.in("id",idList);
            }
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                objectQueryWrapper.like("name", searchText).or().like("description", searchText);
            }
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)) {
                objectQueryWrapper.like("name", name);
            }
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
            if (statusEnum == null) {
                statusEnum = TeamStatusEnum.PUBLIC;
            }
            if (!isAdmin && statusEnum.equals(TeamStatusEnum.PRIVATE)) {
                throw new BusinessException(ErrorCode.NO_AUTH, "没有权限");
            }
            objectQueryWrapper.eq("status", statusEnum.getValue());
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                objectQueryWrapper.like("description", description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0) {
                objectQueryWrapper.eq("maxNum", maxNum);
            }
            Long userId = teamQuery.getUserId();
            if (userId != null && userId > 0) {
                objectQueryWrapper.eq("userId", userId);
            }
        }
        // 不展示已过期的队伍
        // expireTime is null or expireTime > now()
        objectQueryWrapper.and(qw -> qw.gt("expireTime", new Date()).or().isNull("expireTime"));
        List<Team> teamList = this.list(objectQueryWrapper);
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        List<Team> list = this.list(objectQueryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        //关联查询创建人的用户信息
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        for (Team team : list) {
            Long userId = team.getUserId();
            if (userId == null) {
                continue;
            }
            //查询队伍中的所有参与者
            Long id = team.getId();
            List<UserTeam> userTeamList = userTeamService.list(new QueryWrapper<UserTeam>().eq("teamId", id));
            List<UserVO> userVOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(userTeamList)){
                for (UserTeam userTeam : userTeamList) {
                    Long userId1 = userTeam.getUserId();
                    if (userId1!=null) {
                        User user = userService.getById(userId1);
                        User safetyUser = userService.getSafetyUser(user);
                        UserVO userVO = new UserVO();
                        BeanUtils.copyProperties(safetyUser, userVO);
                        userVOList.add(userVO);
                    }
                }

            }
            User user = userService.getById(userId);
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team, teamUserVO);
            //脱敏用户信息
            if (user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                teamUserVO.setCreateUserVO(userVO);
            }
            teamUserVO.setUserList(userVOList);
            teamUserVOList.add(teamUserVO);
        }
        return teamUserVOList;
    }
    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        if (id==null||id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = this.getById(id);
        if (oldTeam==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!oldTeam.getUserId().equals(loginUser.getId())&&!userService.isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
        if (statusEnum.equals(TeamStatusEnum.SECRET)){
            if (StringUtils.isBlank(teamUpdateRequest.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"加密房间必须要有密码");
            }
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamUpdateRequest, team);
        return this.updateById(team);
    }

    @Override
    public boolean joinTeam(TeamJoinRequest joinRequest, User loginUser) {
        //判断传入的参数是否为空
        if (joinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = joinRequest.getTeamId();
        //判断队伍id是否为空
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        //判断队伍是否存在
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍不存在");
        }
        //判断队伍是否过期
        Date expireTime = team.getExpireTime();
        if (expireTime!=null&&expireTime.before(new Date())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍已过期");
        }
        //判断加入的是否是私有队伍
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (TeamStatusEnum.PRIVATE.equals(statusEnum)) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"禁止加入私有队伍");
        }
        //判断队伍是否加密，如果加密则判断密码是否相同
        String password = joinRequest.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)){
            if (StringUtils.isBlank(password)||!password.equals(team.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
            }
        }
        //判断用户是否超过限制
        Long userId  = loginUser.getId();
        RLock lock=redissonClient.getLock("yupao:join_team");
        try {
            while(true){
                if (lock.tryLock(0,-1, TimeUnit.MILLISECONDS)){
                    System.out.println("getLock:"+Thread.currentThread().getId());
                    QueryWrapper<UserTeam> objectQueryWrapper = new QueryWrapper<>();
                    objectQueryWrapper.eq("userId",userId);
                    long hasJoinNum = userTeamService.count(objectQueryWrapper);
                    if (hasJoinNum>=5){
                        throw new BusinessException(ErrorCode.PARAMS_ERROR,"加入队伍数量超过限制");
                    }
                    //判断是否加入过队伍
                    objectQueryWrapper=new QueryWrapper<>();
                    objectQueryWrapper.eq("teamId",teamId);
                    objectQueryWrapper.eq("userId",userId);
                    long hasUserJoinNum  = userTeamService.count(objectQueryWrapper);
                    if (hasUserJoinNum>0){
                        throw  new BusinessException(ErrorCode.PARAMS_ERROR,"已经加入过该队伍");
                    }
                    //判断队伍人数是否满了
                    long joinNum = this.countTeamUserByTeamId(teamId);
                    if(joinNum>=team.getMaxNum()){
                        throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍人数已满");
                    }
                    UserTeam userTeam = new UserTeam();
                    userTeam.setUserId(userId);
                    userTeam.setTeamId(teamId);
                    userTeam.setJoinTime(new Date());
                    return userTeamService.save(userTeam);
                }
            }
        } catch (InterruptedException e) {
           log.error("doCacheRecommendUser error",e);
           return false;
        }finally {
            if (lock.isHeldByCurrentThread()){
                System.out.println("unlock:"+Thread.currentThread().getId());
                lock.unlock();
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {
       if (teamQuitRequest==null){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
       }
        Long teamId = teamQuitRequest.getTeamId();
        Team team = getTeamById(teamId);
        Long userId = loginUser.getId();
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        QueryWrapper<UserTeam> objectQueryWrapper = new QueryWrapper<>(userTeam);
        long count = userTeamService.count(objectQueryWrapper);
        if (count==0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"未加入队伍");
        }
        long teamHasJoinNum  = this.countTeamUserByTeamId(teamId);
        if (teamHasJoinNum==1){
            this.removeById(teamId);
        }else {
            if (team.getUserId().equals(userId)){
                QueryWrapper<UserTeam> objectQueryWrapper1 = new QueryWrapper<>();
                objectQueryWrapper1.eq("teamId",teamId);
                objectQueryWrapper1.last("order by id asc limit 2");
                List<UserTeam> userTeamList = userTeamService.list(objectQueryWrapper);
                if (CollectionUtils.isEmpty(userTeamList)||userTeamList.size()<=1){
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
                UserTeam nextUserTeam = userTeamList.get(1);
                Long nextTeamLeaderId = nextUserTeam.getUserId();
                Team updateTeam = new Team();
                updateTeam.setId(teamId);
                updateTeam.setUserId(nextTeamLeaderId);
                boolean result = this.updateById(updateTeam);
                if (!result){
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新队长失败");
                }
            }
        }
        return userTeamService.remove(objectQueryWrapper);
    }

    private Team getTeamById(Long teamId) {
        if (teamId ==null|| teamId <=0){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Team team = this.getById(teamId);
        if (team==null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"队伍不存在");
        }
        return team;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeam(Long id, User loginUser) {
        Team team=getTeamById(id);
        Long teamId = team.getId();
        if (!team.getUserId().equals(loginUser.getId())){
           throw new BusinessException(ErrorCode.NO_AUTH,"无访问权限");
        }
        QueryWrapper<UserTeam> userTeamQueryWrapper=new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId",teamId);
        boolean result = userTeamService.remove(userTeamQueryWrapper);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除队伍相关联信息失败");
        }
        return  this.removeById(teamId);
    }

    private long countTeamUserByTeamId(long teamId){
        QueryWrapper<UserTeam> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("teamId",teamId);
        return userTeamService.count(objectQueryWrapper);
    }
    @Override
    public List<TeamUserVO> extracted(HttpServletRequest request, List<TeamUserVO> teamList) {
        List<Long> teamIdList= teamList.stream().map(TeamUserVO::getId).collect(Collectors.toList());
        //2.判断当前用户是否已加入队伍
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        try {
            User loginUser = userService.getLoginUser(request);
            userTeamQueryWrapper.eq("userId",loginUser.getId());
            userTeamQueryWrapper.in("teamId",teamIdList);
            List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
            //已加入的队伍id集合
            Set<Long> hasJoinTeamSet=userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
            teamList.forEach(team->{
                boolean hasjoin = hasJoinTeamSet.contains(team.getId());
                team.setHasJoin(hasjoin);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        QueryWrapper<UserTeam> userTeamJoinQueryWrapper = new QueryWrapper<>();
        userTeamJoinQueryWrapper.in("teamId",teamIdList);
        List<UserTeam> userTeamList = userTeamService.list(userTeamJoinQueryWrapper);
        //队伍id=>加入这个队伍的用户列表
        Map<Long,List<UserTeam>> teamIdUserTeamList=userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        teamList.forEach(team-> team.setHasJoinNum(teamIdUserTeamList.getOrDefault(team.getId(),new ArrayList<>()).size()));
        return teamList;
    }
}




