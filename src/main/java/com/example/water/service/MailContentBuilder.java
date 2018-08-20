package com.example.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


/**
 * Created by  waiter on 18-8-9  上午7:39.
 *
 * @author waiter
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MailContentBuilder {
    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(String message,String template) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process(template, context);
    }

    public String build(Object message,String template) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process(template, context);
    }

    public String build(Iterable<? extends Object> message,String template) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process(template, context);
    }
}
