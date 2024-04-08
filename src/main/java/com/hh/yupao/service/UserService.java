package com.hh.yupao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.yupao.model.domain.User;
import com.hh.yupao.model.request.UpdateTagRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
* @author ybb
* @description 针对表【user】的数据库操作Service
* @createDate 2024-03-07 21:31:34
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param planetCode    星球编号
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode,String avatarUrl);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    Page<User> getRecommendUserByRedis(String redisKey);

    void setRecommendUserByRedis(String redisKey, Page<User> userPage);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    // [加入编程导航](https://t.zsxq.com/0emozsIJh) 深耕编程提升【两年半】、国内净值【最高】的编程社群、用心服务【20000+】求学者、帮你自学编程【不走弯路】

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    List<User> searchUsersByTags(List<String> tags);

    List<User> searchUsersByTagsByMemory(List<String> tagNameList);
    int updateUser(User user,User loginUser);
    public User getLoginUser(HttpServletRequest request);
   public boolean isAdmin(HttpServletRequest request);
    public boolean isAdmin(User loginUser);

    List<User> matchUsers(long num, User loginUser);

    int updateTagById(UpdateTagRequest tagRequest, User currentUser);

    Object redisFormat(Long id);
}
