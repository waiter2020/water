package com.example.water;

import com.example.water.model.User;
import com.example.water.model.WaterCondition;
import com.example.water.service.UserService;
import com.example.water.service.WaterConditionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WaterApplicationTests {
    @Autowired
    UserService userService;
    @Autowired
    WaterConditionService waterConditionService;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void contextLoads() {

    }

    @Test
    public void T1() {

    }
}
