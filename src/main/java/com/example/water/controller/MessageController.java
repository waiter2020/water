package com.example.water.controller;

import com.example.water.model.Greeting;
import com.example.water.model.Message;
import com.example.water.model.User;
import org.springframework.http.HttpRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;



/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@RestController
public class MessageController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Message m) throws Exception {
        Thread.sleep(1000);
        System.out.println(m);
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(m.getContent()) + "!");
    }
}
