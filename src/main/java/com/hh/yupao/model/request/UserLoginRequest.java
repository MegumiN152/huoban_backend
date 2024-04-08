package com.hh.yupao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 黄昊
 * @version 1.0
 **/
@Data
public class UserLoginRequest implements Serializable {

        private static final long serialVersionUID =4358176768559230877L;

        private String userAccount;

        private String userPassword;

}
