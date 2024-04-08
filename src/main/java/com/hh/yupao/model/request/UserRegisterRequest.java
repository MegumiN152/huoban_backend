package com.hh.yupao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 黄昊
 * @version 1.0
 **/
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3148176768559230877L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;

    private String avatarUrl="https://i0.hdslb.com/bfs/article/c6eb13fef7303e43578842809f68a9bfd1f98a96.jpg@!web-article-pic.avif";
}
