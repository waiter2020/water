package com.example.water;

import com.example.water.model.User;
import com.example.water.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WaterApplicationTests {
    @Autowired
    UserService userService;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void contextLoads() {

    }
    @Test
    public void T1(){
        System.out.println(new Date());
    }
}
