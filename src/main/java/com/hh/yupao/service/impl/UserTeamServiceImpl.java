package com.hh.yupao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hh.yupao.mapper.UserTeamMapper;
import com.hh.yupao.model.domain.UserTeam;
import com.hh.yupao.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
* @author ybb
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-03-13 18:42:16
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService {

}




