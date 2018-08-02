package com.example.water.controller;

import com.example.water.model.User;
import com.example.water.service.UserDetailsServiceIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Controller
public class UserController {
    @Autowired
    private UserDetailsServiceIml userDetailsServiceIml;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/find")
    public Object find(String username){
        return userDetailsServiceIml.findByUserName(username);
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
        userDetailsServiceIml.add(new User(userName,email,phoneNumber,bCryptPasswordEncoder.encode(passwd),true));
    }

    @GetMapping(value = "/info")
    public String getInfo(Model model, HttpServletRequest request){
        String remoteUser = request.getRemoteUser();
        User byUserName = (User) userDetailsServiceIml.findByUserName(remoteUser);
        model.addAttribute("info",byUserName);
        return "user/change";
    }

    @PostMapping(value = "/change")
    public String changeInfo(Model model, HttpServletRequest request){
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        String births = request.getParameter("birth");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse=null;
        try {
            parse = simpleDateFormat.parse(births);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        User byUserName = (User) userDetailsServiceIml.findByUserName(request.getRemoteUser());

        byUserName.setBirth(parse);
        byUserName.setPhoneNumber(phoneNumber);
        byUserName.setEmail(email);
        userDetailsServiceIml.save(byUserName);
        model.addAttribute("msg","修改成功");
        return getInfo(model,request);
    }

    @GetMapping(value = "/user/change_pwd")
    public String toChangePwd(){
        return "user/change_pwd";
    }

    @PostMapping(value = "/user/change_pwd")
    public String ChangePwd(Model model,HttpServletRequest request){
        String oldPwd = request.getParameter("oldPwd");
        String newPwd = request.getParameter("newPwd");
        User byUserName = (User) userDetailsServiceIml.findByUserName(request.getRemoteUser());
        String passwd = byUserName.getPasswd();
        boolean matches = bCryptPasswordEncoder.matches(oldPwd, passwd);
        if(matches){
            byUserName.setPasswd(bCryptPasswordEncoder.encode(newPwd));
            model.addAttribute("msg","修改成功");
        }else {
            model.addAttribute("msg","原密码不正确");
            return "user/change_pwd";
        }
        userDetailsServiceIml.save(byUserName);
        return getInfo(model,request);
    }
}
