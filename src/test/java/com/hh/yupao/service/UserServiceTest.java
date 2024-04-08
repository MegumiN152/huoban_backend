//package com.hh.yupao.service;
//
//import com.hh.yupao.model.domain.User;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * @author 黄昊
// * @version 1.0
// **/
//@SpringBootTest
//class UserServiceTest {
//    @Resource
//    private UserService userService;
//
//    @Test
//    public void testSelect() {
//        User user=new User();
//        user.setUsername("doghh");
//        user.setUserAccount("123");
//        user.setAvatarUrl("https://baomidou.com/img/logo.svg");
//        user.setGender(0);
//        user.setUserPassword("123");
//        user.setPhone("18770257601");
//        user.setEmail("31057@qq.com");
//        user.setUserStatus(0);
//        user.setIsDelete(0);
//        boolean result = userService.save(user);
//        System.out.println(userService.getById(1));
//        assertTrue(result);
//    }
//
//    @Test
//    void userRegister() {
//        //判断非空
//        String userAccount = "yupi5";
//        String password ="";
//        String checkPassword = "123456";
//        String planetCode="5";
//        long result = userService.userRegister(userAccount, password, checkPassword, planetCode);
//        Assertions.assertEquals(-1,result);
//        //账户长度不小于4位
//        userAccount="yu";
//        result = userService.userRegister(userAccount, password, checkPassword, planetCode);
//        Assertions.assertEquals(-1,result);
//        //密码长度不小于8位
//        userAccount="yupi";
//        password="123456";
//        result = userService.userRegister(userAccount, password, checkPassword, planetCode);
//        Assertions.assertEquals(-1,result);
//        //判断账户是否重复
//        userAccount="123";
//        password="12345678";
//        result = userService.userRegister(userAccount, password, checkPassword, planetCode);
//        Assertions.assertEquals(-1,result);
//        //判断账户是否包含特殊字符
//        userAccount="dog yu";
//        result = userService.userRegister(userAccount, password, checkPassword, planetCode);
//        Assertions.assertEquals(-1,result);
//        //判断密码与校验密码是否一致
//        userAccount="yupit";
//        password="12345678";
//        checkPassword="123456789";
//        result = userService.userRegister(userAccount, password, checkPassword, planetCode);
//        Assertions.assertEquals(-1,result);
//        userAccount="yupi5";
//        password="12345678";
//        checkPassword="12345678";
//        result = userService.userRegister(userAccount, password, checkPassword, planetCode);
//        Assertions.assertTrue(result>0);
//    }
//
//    @Test
//    void searchUsersByTags() {
//        List<String> tags= Arrays.asList("java","c++");
//        List<User> users = userService.searchUsersByTags(tags);
//        for (User user : users) {
//            System.out.println(user);
//        }
//    }
//
//    @Test
//    void searchUsersByTagsByMemory() {
//        List<String> tags= Arrays.asList("java","c++");
//        List<User> users = userService.searchUsersByTagsByMemory(tags);
//        for (User user : users) {
//            System.out.println(user);
//        }
//    }
//}