package com.example.water.service;

import com.example.water.dao.LogDao;
import com.example.water.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by  waiter on 18-8-2  下午12:42.
 *
 * @author waiter
 */
@Service
public class LogService {
    @Autowired
    private LogDao logDao;

    public void save(Log log){
        logDao.save(log);
    }
}
