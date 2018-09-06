package com.example.water.service;

import com.example.water.dao.LogDao;
import com.example.water.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by  waiter on 18-8-2  下午12:42.
 *
 * @author waiter
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LogService {
    @Autowired
    private LogDao logDao;

    public void save(Log log){
        logDao.save(log);
    }

    public Page<Log> getLogsByDate(int page, String equipId, Date startTime, Date endTime){
        Pageable pageable = PageRequest.of(page, 20);
        return logDao.findAllByEquipIdAndAndDateAfterAndDateBeforeOrderByDate(pageable,equipId,startTime,endTime);
    }
}
