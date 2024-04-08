package com.hh.yupao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 黄昊
 * @version 1.0
 **/
@Data
public class TeamQuitRequest implements Serializable {
    private static final long serialVersionUID = -4498522998017608218L;
    /**
     * 队伍id
     */
    private Long teamId;
}
