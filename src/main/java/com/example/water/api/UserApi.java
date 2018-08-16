package com.example.water.api;

import com.example.water.model.User;
import com.example.water.service.UserDetailsServiceIml;
import com.example.water.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by  waiter on 18-8-7  下午9:54.
 *
 * @author waiter
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserApi {
    @Autowired
    UserDetailsServiceIml userDetailsServiceIml;

    @GetMapping(value = "/get_info")
    public User getUser(HttpServletRequest request){
        UserDetails user = (UserDetails) request.getSession().getAttribute("user");
        User userName = (User) userDetailsServiceIml.findByUserName(user.getUsername());
        return userName;
    }
}
