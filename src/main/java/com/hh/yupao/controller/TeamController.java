package com.hh.yupao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.yupao.common.BaseResponse;
import com.hh.yupao.common.ErrorCode;
import com.hh.yupao.common.ResultUtils;
import com.hh.yupao.exception.BusinessException;
import com.hh.yupao.model.domain.Team;
import com.hh.yupao.model.domain.User;
import com.hh.yupao.model.domain.UserTeam;
import com.hh.yupao.model.dto.TeamQuery;
import com.hh.yupao.model.request.*;
import com.hh.yupao.model.vo.TeamUserVO;
import com.hh.yupao.service.TeamService;
import com.hh.yupao.service.UserService;
import com.hh.yupao.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * @author 黄昊
 * @version 1.0
 **/
@RestController
@RequestMapping("/team")
@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
@Slf4j
public class TeamController {
    @Resource
    private UserService userService;
    @Resource
    private TeamService teamService;
    @Resource
    private UserTeamService userTeamService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request){
        if (teamAddRequest == null){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest,team);
        Long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }
//    @PostMapping("/delete")
//    public BaseResponse<Boolean> delTeam(long id){
//        if (id <= 0){
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        boolean remove = teamService.removeById(id);
//        if (!remove){
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除失败喵");
//        }
//        return ResultUtils.success(true);
//    }
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest,HttpServletRequest request){
        if (teamUpdateRequest== null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest, loginUser);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败喵");
        }
        return ResultUtils.success(true);
    }
    @GetMapping("/get")
    public BaseResponse<Team> searchTeam(long id){
        if (id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team==null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"查询为空喵");
        }
        return ResultUtils.success(team);
    }
//    @GetMapping("/list")
//    public BaseResponse<List<Team>> listTeam(TeamQuery teamQuery){
//        if (teamQuery == null){
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        Team team = new Team();
//        BeanUtils.copyProperties(teamQuery,team);
//        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
//        List<Team> teamList  = teamService.list(queryWrapper);
//        return ResultUtils.success(teamList);
//    }
@GetMapping("/list")
public BaseResponse<List<TeamUserVO>> listTeam(TeamQuery teamQuery,HttpServletRequest request){
    if (teamQuery == null){
        throw new BusinessException(ErrorCode.PARAMS_ERROR);
    }
    //1.查询队伍列表
    boolean isAdmin = userService.isAdmin(request);
    List<TeamUserVO> teamList  = teamService.listTeams(teamQuery,isAdmin);
    List<TeamUserVO> extracted = teamService.extracted(request, teamList);
    return ResultUtils.success(extracted);
}
    @GetMapping("/list/my/create")
    public BaseResponse<List<TeamUserVO>> listMyTeams(TeamQuery teamQuery,HttpServletRequest request){
        if (teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        teamQuery.setUserId(loginUser.getId());
        teamQuery.setStatus(0);
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery, true);
        teamQuery.setStatus(1);
        List<TeamUserVO> teamUserVOList1 = teamService.listTeams(teamQuery, true);
        teamQuery.setStatus(2);
        List<TeamUserVO> teamUserVOList2 = teamService.listTeams(teamQuery, true);
        teamList.addAll(teamUserVOList1);
        teamList.addAll(teamUserVOList2);
        List<TeamUserVO> extracted = teamService.extracted(request, teamList);
        return ResultUtils.success(extracted);
    }
    @GetMapping("/list/my/join")
    public BaseResponse<List<TeamUserVO>> listTeamJoin(TeamQuery teamQuery,HttpServletRequest request){
        if (teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<UserTeam> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("userId",loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(objectQueryWrapper);
        Map<Long, List<UserTeam>> listMap = userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        ArrayList<Long> idList=new ArrayList<>(listMap.keySet());
        if (CollectionUtils.isEmpty(idList)){
            return ResultUtils.success(new ArrayList<>());
        }
        teamQuery.setIdList(idList);
        teamQuery.setStatus(0);
        List<TeamUserVO> teamUserVOList = teamService.listTeams(teamQuery, true);
        teamQuery.setStatus(1);
        List<TeamUserVO> teamUserVOList1 = teamService.listTeams(teamQuery, true);
        teamQuery.setStatus(2);
        List<TeamUserVO> teamUserVOList2 = teamService.listTeams(teamQuery, true);
        teamUserVOList.addAll(teamUserVOList1);
        teamUserVOList.addAll(teamUserVOList2);
        List<TeamUserVO> extracted = teamService.extracted(request, teamUserVOList);
        return ResultUtils.success(extracted);
    }
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamByPage(TeamQuery teamQuery){
        if (teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(team,teamQuery);
        Page<Team> objectPage = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> page = teamService.page(objectPage, queryWrapper);
        return ResultUtils.success(page);
    }
    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest joinRequest,HttpServletRequest request){
        if (joinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(joinRequest, loginUser);
        return ResultUtils.success(result);
    }
    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest,HttpServletRequest request){
        if (teamQuitRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResultUtils.success(result);
    }
    @PostMapping("/delete")
    public  BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRquest, HttpServletRequest request){
        if (deleteRquest==null ||deleteRquest.getId()<=0){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        long id=deleteRquest.getId();
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.deleteTeam(id, loginUser);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除失败");
        }
        return ResultUtils.success(true);
    }

}
