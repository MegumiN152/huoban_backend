package com.hh.yupao.model.request;

/**
 * @author 黄昊
 * @version 1.0
 **/
import lombok.Data;

import java.io.Serializable;
import java.util.Set;


@Data
public class UpdateTagRequest implements Serializable {
    private static final long serialVersionUID = 5482203079092270874L;
    private long id;
    private Set<String> tagList;
}