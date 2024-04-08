package com.hh.yupao;


import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 黄昊
 * @version 1.0
 **/
@SpringBootTest
public class RedissionTest {

    @Resource
    private RedissonClient redissonClient;
    @Test
    void test(){
        //list
        RList<String> yupiList = redissonClient.getList("yupiList");
        yupiList.add("yupi");
        System.out.println("hh1:"+yupiList.get(0));
        yupiList.remove(0);
        //set
        //map
        //lock
    }
    @Test
    void testWatchDog() {
        RLock lock = redissonClient.getLock("yupao:precachejob:docache:lock");
        try {
            // 只有一个线程能获取到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                Thread.sleep(300000);//todo 实际要执行的代码
                System.out.println("getLock: " + Thread.currentThread().getId());
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            // 只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }
}
