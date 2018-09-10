package com.example.water.component;

import com.example.water.model.User;

import com.example.water.service.UserDetailsServiceImpl;
import com.example.water.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 *
 * 登录验证过滤器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService ;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public JwtAuthenticationTokenFilter(UserDetailsServiceImpl userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        System.out.println(request.getRemoteUser());
            //System.out.println("进来了 JwtAuthenticationTokenFilter");

            // 得到 请求头的 认证信息 authToken
            String authToken = request.getHeader(this.tokenHeader);

            // 解析 authToken 得到 用户名
            String username = jwtTokenUtil.getUsernameFromToken(authToken);

            //System.out.println("checking authentication for user " + username);

            if (username != null&&!username.trim().equals("")&& SecurityContextHolder.getContext().getAuthentication() == null) {

                // 根据用户名从数据库查找用户信息
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username.trim());
                // 检验token是否有效，并检验其保存的用户信息是否正确
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    // token 有效，为该请求装载 用户权限信息
                    request.getSession().setAttribute("user",userDetails);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            //System.out.println("出去了 JwtAuthenticationTokenFilter");

        chain.doFilter(request, response);

    }
}
