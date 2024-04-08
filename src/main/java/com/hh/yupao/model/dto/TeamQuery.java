package com.hh.yupao.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author 黄昊
 * @version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class TeamQuery extends PageRequest{
    private Long id;
    /**
     * 队伍名称
     */
    private String name;

    /**
     * 最大人数
     */
    private Integer maxNum;
    /**
     *id列表
     */
    private List<Long> idList;
    /**
     * 查询信息
     */
    private String searchText;
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
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 描述
     */
    private String description;

}
