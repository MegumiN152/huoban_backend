package com.hh.yupao;

import com.hh.yupao.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * @author 黄昊
 * @version 1.0
 **/
@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("yupiString","yupi");
        valueOperations.set("yupiInt",1);
        valueOperations.set("yupiDouble",1.0);
        User user = new User();
        user.setUserAccount("yupi");
        user.setProfile("hhhhh");
        valueOperations.set("yupiUser",user);
        Object yupiUser = valueOperations.get("yupiUser");
        Assertions.assertTrue(yupiUser instanceof User);
        Assertions.assertTrue("yupi".equals(valueOperations.get("yupiString")));
        Assertions.assertTrue(1==(Integer) valueOperations.get("yupiInt"));
        Assertions.assertTrue(1.0==(Double) valueOperations.get("yupiDouble"));
    }
}
