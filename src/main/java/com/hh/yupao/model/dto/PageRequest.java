package com.hh.yupao.model.dto;

import lombok.Data;

/**
 * @author 黄昊
 * @version 1.0
 **/
@Data
public class PageRequest{
    /**
     * 页码
     */
    private int pageSize=10;
    /**
     * 每页大小
     */
    private int pageNum=1;
}
