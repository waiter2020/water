package com.example.water.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 *
 * MVC配置
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/").setViewName("wellcome");
        registry.addViewController("/logout").setViewName("logout");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/family/add").setViewName("family/add");
        registry.addViewController("/family/list").setViewName("family/list");
        registry.addViewController("/commons/bar").setViewName("commons/bar");
        registry.addViewController("/equip/add").setViewName("equip/add");
        registry.addViewController("/equip/list").setViewName("equip/list");
        registry.addViewController("/water/list").setViewName("water/list");
        registry.addViewController("/family/create_family").setViewName("family/create_family");
    }
}
