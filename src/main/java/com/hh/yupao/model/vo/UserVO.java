package com.hh.yupao.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

/**
 * @author 黄昊
 * @version 1.0
 **/
@Data
public class UserVO {


    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;


    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


    /**
     * 用户角色 0-普通用户 1-管理员
     */
    private Integer userRole;
    /**
     * 星球编号
     */
    private String planetCode;
    /**
     * 用户签名
     */
    private String tags;
    /**
     * 个人简介
     */
    private String profile;

}
