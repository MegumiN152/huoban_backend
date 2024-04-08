package com.hh.yupao;

import com.hh.yupao.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 黄昊
 * @version 1.0
 **/
@SpringBootTest
public class UserTest {
    @Resource
    private UserMapper userMapper;
    @Test
    public void testSelect() {
        boolean anyBlank = StringUtils.isAnyBlank("123");
        System.out.println(anyBlank);
    }
}
