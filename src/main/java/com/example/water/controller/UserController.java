package com.example.water.controller;

import com.example.water.model.User;
import com.example.water.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    @RequestMapping(value = "/find")
    public Object find(String username){
        return userService.findByUserName(username);
    }

    /**
     * 添加用户
     * @param userName
     * @param email
     * @param phoneNumber
     * @param passwd
     */
    @RequestMapping(value = "/add")
    public void add(String userName,String email,String phoneNumber,String passwd){
        userService.add(new User(userName,email,phoneNumber,bCryptPasswordEncoder.encode(passwd),true));
    }



}
