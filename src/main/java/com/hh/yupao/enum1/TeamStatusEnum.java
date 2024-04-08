package com.hh.yupao.enum1;

import com.hh.yupao.common.ErrorCode;
import com.hh.yupao.exception.BusinessException;

/**
 * @author 黄昊
 * @version 1.0
 **/
public enum TeamStatusEnum {
    PUBLIC(0, "公开"),
    PRIVATE(1, "私有"),
    SECRET(2, "加密");
    private int value;
    private String text;

    TeamStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public static TeamStatusEnum getEnumByValue(Integer value){
        if (value==null){
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum teamStatusEnum : values) {
            if (teamStatusEnum.getValue()==value){
                return teamStatusEnum;
            }
        }
        return null;
    }
}
