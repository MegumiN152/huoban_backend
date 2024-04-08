//package com.hh.yupao.easyExcel;
//import java.util.Date;
//
//import com.hh.yupao.mapper.UserMapper;
//import com.hh.yupao.model.domain.User;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StopWatch;
//
//import javax.annotation.Resource;
//
///**
// * @author 黄昊
// * @version 1.0
// **/
//@Component
//public class InsertUsers {
//    @Resource
//    private UserMapper userMapper;
//    @Scheduled(fixedDelay = 5000)
//    public void insertUsers() {
//        // 插入用户
//        final  int NUM=100000;
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        for (int i = 0; i < NUM; i++) {
//            User user = new User();
//            user.setUsername("假用户hh");
//            user.setUserAccount("fakehh"+i);
//            user.setAvatarUrl("https://i2.hdslb.com/bfs/face/853c4a846793dfbdafd8f16a40d6d8065bdb7c6f.jpg@240w_240h_1c_1s_!web-avatar-space-header.avif");
//            user.setGender(0);
//            user.setUserPassword("12345678");
//            user.setPhone("123");
//            user.setEmail("123@qq.com");
//            user.setUserStatus(0);
//            user.setUserRole(0);
//            user.setPlanetCode("11111");
//            user.setTags("[]");
//            userMapper.insert(user);
//        }
//        stopWatch.stop();
//        System.out.println("插入"+NUM+"条数据耗时："+stopWatch.getTotalTimeMillis()+"ms");
//    }
//}
