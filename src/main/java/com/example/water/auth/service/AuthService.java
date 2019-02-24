package com.example.water.auth.service;

import com.example.water.model.User;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
public interface AuthService {
    /**
     * 注册一个用户
     * @param userToAdd
     * @return
     */
    User register(User userToAdd);

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password);

    /**
     * 刷新token
     * @param oldToken
     * @return
     */
    String refresh(String oldToken);
}
