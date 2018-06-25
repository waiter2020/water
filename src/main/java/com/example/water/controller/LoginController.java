package com.example.water.controller;

import com.example.water.auth.service.AuthService;
import com.example.water.model.User;
import com.example.water.service.UserDetailsServiceIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Controller
public class LoginController {
    @Autowired
    private UserDetailsServiceIml userDetailsServiceIml;
    @Value("${jwt.header}")
    private String tokenHeader;
    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/loginsuccess")
    public String loginsuccess(HttpServletRequest request, HttpServletResponse response){

        String name = request.getParameter("username");
        User user=(User) userDetailsServiceIml.findByUserName(name);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        response.setHeader(tokenHeader,authService.login(user.getUsername(), user.getPasswd()));
        return "index";
    }
}
