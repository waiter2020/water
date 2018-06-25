package com.example.water.auth.service.impl;

import com.example.water.auth.service.AuthService;
import com.example.water.dao.UserDao;
import com.example.water.model.Role;
import com.example.water.model.User;
import com.example.water.service.UserDetailsServiceIml;
import com.example.water.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 *
 * 验证服务
 */
@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private UserDao userRepository;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsServiceIml userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            UserDao userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }


    @Override
    public User register(User userToAdd) {
        final String username = userToAdd.getUsername();
        if(userRepository.findByUsername(username)!=null) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userToAdd.getPassword();
        userToAdd.setPasswd(encoder.encode(rawPassword));
        LinkedList<Role> roles =new LinkedList<>();
        roles.add(new Role("USER"));
        userToAdd.setAuthorities(roles);
        userToAdd.setEnabled(true);
        return userRepository.save(userToAdd);

    }

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = (User) userDetailsService.loadUserByUsername(username);
        if(user==null||!user.isEnabled()){
            return "";
        }
            return jwtTokenUtil.refreshToken(token);

    }
}
