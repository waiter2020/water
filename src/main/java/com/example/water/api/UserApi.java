package com.example.water.api;

import com.example.water.model.User;
import com.example.water.service.UserDetailsServiceImpl;
import com.example.water.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by  waiter on 18-8-7  下午9:54.
 * 用户相关接口
 * @author waiter
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserApi {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceIml;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 获取用户信息
     * @param request
     * @return
     */
    @GetMapping(value = "/get_info")
    public User getUser(HttpServletRequest request){
        UserDetails user = (UserDetails) request.getSession().getAttribute("user");
        User userName = (User) userDetailsServiceIml.findByUserName(user.getUsername());
        return userName;
    }

    /**
     * 修改用户信息
     * @param map
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/change")
    public String changeInfo(@RequestBody Map<String,String> map, HttpServletRequest request){
        String phoneNumber = map.get("phoneNumber");
        String email = map.get("email");
        String births = map.get("birth");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date parse=null;
        try {
            parse = simpleDateFormat.parse(births);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        UserDetails user = (UserDetails) request.getSession().getAttribute("user");
        User byUserName = (User) userDetailsServiceIml.findByUserName(user.getUsername());

        byUserName.setBirth(parse);
        byUserName.setPhoneNumber(phoneNumber);
        byUserName.setEmail(email);
        userDetailsServiceIml.save(byUserName);
        return "修改成功";
    }


    /**
     * 修改密码
     * @param map
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/change_pwd")
    public String ChangePwd(@RequestBody Map<String,String> map,HttpServletRequest request){
        UserDetails user = (UserDetails) request.getSession().getAttribute("user");
        User byUserName = (User) userDetailsServiceIml.findByUserName(user.getUsername());
        String newPwd = map.get("pwd");
        byUserName.setPasswd(bCryptPasswordEncoder.encode(newPwd));
        userDetailsServiceIml.save(byUserName);
        return "修改成功";
    }
}
