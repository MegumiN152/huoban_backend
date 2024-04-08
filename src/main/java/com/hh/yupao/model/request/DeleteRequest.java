package com.hh.yupao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 黄昊
 * @version 1.0
 **/
@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = -8413932855187527411L;
    private Long id;
}
