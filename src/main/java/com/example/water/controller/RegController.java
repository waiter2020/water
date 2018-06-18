package com.example.water.controller;

import com.example.water.model.Role;
import com.example.water.model.User;
import com.example.water.service.RoleService;
import com.example.water.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 *
 * 网页端注册控制器
 */
@org.springframework.stereotype.Controller
public class RegController {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /***
     *
     * @param model
     * @param username 用户名
     * @param email 邮箱
     * @param phone 电话
     * @param passwd 密码
     * @return
     */
    @RequestMapping(value = "/registeraction",method = RequestMethod.POST)
    public Object register(Model model,
                           @ModelAttribute("username")String username,
                           @ModelAttribute("email")String email,
                           @ModelAttribute("phone")String phone,
                           @ModelAttribute("passwd")String passwd){
        User user=(User) userService.findByUserName(username);

        try {
            if(user!=null){
                throw new Exception("用户名已被使用");
            }
            LinkedList<Role> roles = new LinkedList<Role>();
            //添加权限
            roles.add(new Role("USER"));
            //保存用户，对密码加密
            userService.add(new User(username, email, phone, bCryptPasswordEncoder.encode(passwd), true,roles));
        }catch (Exception e){

            model.addAttribute("regerror",e.getCause()+",注册失败");
            return "register";
        }
        model.addAttribute("regss","注册成功");
        //返回登录页面
        return "login";
    }


}
