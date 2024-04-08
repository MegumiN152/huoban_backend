package com.hh.yupao.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @author 黄昊
 * @version 1.0
 **/
@Data
public class TeamUpdateRequest implements java.io.Serializable{
    private static final long serialVersionUID = 3191241716373120793L;
    private Long id;
    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;
}
