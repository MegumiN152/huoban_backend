package com.hh.yupao.job;

/**
 * @author 黄昊
 * @version 1.0
 **/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hh.yupao.mapper.UserMapper;
import com.hh.yupao.model.domain.User;
import com.hh.yupao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热
 */
@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;
    @Resource
    private RedissonClient redissonClient;
    private List<Long> mainUserList = Arrays.asList(1l);

    @Scheduled(cron = "0 24 16 * *  *")
    public void docacheRecommender() {
        RLock lock = redissonClient.getLock("hh:precachejob:docache:lock");
        try {
            if (lock.tryLock(0, 30000L, TimeUnit.MILLISECONDS)) {
                System.out.println("getlock: "+Thread.currentThread().getId());
                for (Long userId : mainUserList) {
                    // 缓存推荐
                    String redisKey = String.format("hh:user:recommend:%s", userId);
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    //无缓存，查数据库
                    QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), objectQueryWrapper);
                    try {
                        valueOperations.set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        log.error("缓存用户推荐列表失败", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            if (lock.isHeldByCurrentThread()){
                System.out.println("unlock: "+Thread.currentThread().getId());
                lock.unlock();
            }
        }

    }
}
