package com.example.water;

import com.example.water.model.User;
import com.example.water.service.UserDetailsServiceIml;
import com.example.water.service.WaterConditionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WaterApplicationTests {
    @Autowired
    UserDetailsServiceIml userDetailsServiceIml;
    @Autowired
    WaterConditionService waterConditionService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void contextLoads() {
        User byUserName = (User) userDetailsServiceIml.findByUserName("123456");
        byUserName.setPasswd(bCryptPasswordEncoder.encode("123456"));
        userDetailsServiceIml.save(byUserName);

    }

    @Test
    public void T1() {
        System.out.println(String.format("%0" + 3 + "d", Integer.parseInt("1") + 1));
    }
}
