package com.hh.yupao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.yupao.model.domain.Team;
import com.hh.yupao.model.domain.User;
import com.hh.yupao.model.dto.TeamQuery;
import com.hh.yupao.model.request.TeamJoinRequest;
import com.hh.yupao.model.request.TeamQuitRequest;
import com.hh.yupao.model.request.TeamUpdateRequest;
import com.hh.yupao.model.vo.TeamUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TeamService extends IService<Team> {

    Long addTeam(Team team, User loginUser);


    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);
    
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    boolean joinTeam(TeamJoinRequest joinRequest, User loginUser);

    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    boolean deleteTeam(Long id, User loginUser);

    List<TeamUserVO> extracted(HttpServletRequest request, List<TeamUserVO> teamList);
}
