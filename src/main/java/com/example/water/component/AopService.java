package com.example.water.component;

import com.example.water.service.MailClientService;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @Author: waiter
 * @Date: 18-12-11 21:51
 * @Description:
 */
@Aspect
@Component
public class AopService {
    private final MailClientService mailClientService;

    @Autowired
    public AopService(MailClientService mailClientService) {
        this.mailClientService = mailClientService;
    }

    @Pointcut("execution(* com.example.water.service..*(..))&&!bean(mailClientService)||execution(* com.example.water.socket.Service.*(..))")
    public void myAop(){}

    @AfterThrowing(value = "myAop()",throwing = "ex")
    public void myLog(Throwable ex){
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        //System.out.println(result.toString());
        mailClientService.prepareAndSend("1403976416@qq.com",result.toString(),"终端水系统异常信息");
    }
}
