package com.example.water;

import com.example.water.service.UserDetailsServiceIml;
import com.example.water.service.WaterConditionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WaterApplicationTests {
    @Autowired
    UserDetailsServiceIml userDetailsServiceIml;
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
