package com.hh.yupao.easyExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 黄昊
 * @version 1.0
 **/
@Data
@Setter
@Getter
public class XingQIuUserInfo {
    @ExcelProperty("成员编号")
    private Long id;
    @ExcelProperty("成员昵称")
    private String username;
}
