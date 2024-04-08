package com.hh.yupao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 黄昊
 * @version 1.0
 **/
@Data
public class TeamJoinRequest implements Serializable {
    private static final long serialVersionUID = -9223083929578408635L;

   private Long teamId;
   private String password;
}
