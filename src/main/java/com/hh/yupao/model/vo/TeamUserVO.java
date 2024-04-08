package com.hh.yupao.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 黄昊
 * @version 1.0
 **/
@Data
public class TeamUserVO implements Serializable {
    private static final long serialVersionUID = -3532546232506834076L;
    /**
     * id
     */
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
     * 创建时间
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;
    /**
     * 入队用户列表
     */
    List<UserVO> userList;
    /**
     * 创建人用户
     */
    UserVO createUserVO;
    /**
     * 是否加入队伍
     */
    private boolean hasJoin;
    /**
     * 已加入用户数量
     */
    private Integer hasJoinNum;
}
