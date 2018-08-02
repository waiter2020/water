package com.example.water.service;

import com.example.water.dao.GreetingDao;
import com.example.water.model.Greeting;
import com.example.water.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by  waiter on 18-8-1  下午6:53.
 *
 * @author waiter
 */
@Service
public class GreetingService {
    @Autowired
    private GreetingDao greetingDao;

    public void save(Greeting message){
        greetingDao.save(message);
    }
}
