package com.example.water;

import com.example.water.model.EquipmentInfo;
import com.example.water.model.User;
import com.example.water.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class WaterApplicationTests {
    @Autowired
    UserDetailsServiceImpl userDetailsServiceIml;
    @Autowired
    WaterConditionService waterConditionService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    MailClientService mailClientService;
    @Autowired
    EquipmentInfoService equipmentInfoService;
    @Autowired
    JpushService jpushService;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void contextLoads() {
        User byUserName = (User) userDetailsServiceIml.findByUserName("123456");
        byUserName.setPasswd(bCryptPasswordEncoder.encode("123456"));
        userDetailsServiceIml.save(byUserName);

    }
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    @Test
    public void T1() {

        Date endTime = null;
        try {
            endTime = simpleDateFormat.parse("2018111517422300093");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(endTime);
    }
    @Test
    public void T2(){
        LinkedList<EquipmentInfo> allByFamily_id = equipmentInfoService.findAllByFamily_Id(18);
        mailClientService.sendWarnMessage(allByFamily_id.remove(0),"hfidhsuvi");
    }

    @Test
    public void T3(){
        jpushService.sendToAppByFamilyId(18+"","漏失提醒","检测到漏控仪："+"12"+";名称："+"未命名"+"发生大漏失");
    }

    @Test
    public void T4(){
        Date date = new Date(1547756125000L+8*60*60*1000);
        System.out.println(simpleDateFormat.format(date));
    }
}
