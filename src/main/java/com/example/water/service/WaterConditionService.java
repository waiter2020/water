package com.example.water.service;

import com.example.water.dao.WaterConditionDao;
import com.example.water.model.WaterCondition;
import com.example.water.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void saveWaterInfo(String watermeterId, Date startDate, Date endDate, int volumn){
        waterConditionDao.save(new WaterCondition(watermeterId,volumn,endDate.getTime()-startDate.getTime(), startDate,endDate));
    }

    public String getWaterInfoByDate(String watermeterId,Date startTime,Date endTime){
        return DataUtils.createJsonString(waterConditionDao.getWaterConditionsByWatermeterIdAndStartDateAfterAndEndDateBeforeOrderByStartDateDesc(watermeterId,startTime,endTime));
    }
    public Collection<WaterCondition> getWaterInfosByDate(String watermeterId, Date startTime, Date endTime){
        return waterConditionDao.getWaterConditionsByWatermeterIdAndStartDateAfterAndEndDateBeforeOrderByStartDateDesc(watermeterId,startTime,endTime);
    }
    public ArrayList<WaterCondition> findAll(){
        return (ArrayList<WaterCondition>) waterConditionDao.findAll();
    }
    public void saveAll(ArrayList<WaterCondition> waterConditions){
        waterConditionDao.saveAll(waterConditions);
    }
}
