package com.example.water.service;

import com.example.water.dao.WaterConditionDao;
import com.example.water.model.WaterCondition;
import com.example.water.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Service
public class WaterConditionService {
    @Autowired
    private WaterConditionDao waterConditionDao;

    public void saveWaterInfo(String watermeterId, Date startDate, Date endDate, double volumn){
        waterConditionDao.save(new WaterCondition(watermeterId,volumn,endDate.getTime()-startDate.getTime(), startDate,endDate));
    }

    public String getWaterInfoByDate(String watermeterId,Date startTime,Date endTime){
        Pageable pageable = PageRequest.of(1, 20);
        return DataUtils.createJsonString(waterConditionDao.getWaterConditionsByWatermeterIdAndStartDateAfterAndEndDateBeforeOrderByStartDateDesc(pageable,watermeterId,startTime,endTime));
    }
    public Page<WaterCondition> getWaterInfosByDate(int page,String watermeterId, Date startTime, Date endTime){
        Pageable pageable = PageRequest.of(page, 5);
        return waterConditionDao.findAllByWatermeterIdAndAndStartDateAfterAndEndDateBeforeOrderByStartDate(pageable,watermeterId,startTime,endTime);
    }
    public ArrayList<WaterCondition> findAll(){
        return (ArrayList<WaterCondition>) waterConditionDao.findAll();
    }
    public void saveAll(ArrayList<WaterCondition> waterConditions){
        waterConditionDao.saveAll(waterConditions);
    }
}
