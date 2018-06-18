package com.example.water.service;

import com.example.water.dao.WaterConditionDao;
import com.example.water.model.WaterCondition;
import com.example.water.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Service
public class WaterConditionService {
    @Autowired
    private WaterConditionDao waterConditionDao;

    public void saveWaterInfo(String watermeterId,long startTime,long endTime,int volumn){
        waterConditionDao.save(new WaterCondition(watermeterId,startTime,endTime,volumn,endTime-startTime));
    }

    public String getWaterInfoByDate(String watermeterId,long startTime,long endTime){
        return DataUtils.createJsonString(waterConditionDao.getWaterConditionsByWatermeterIdAndStartTimeAfterAndEndTimeBeforeOrderByStartTime(watermeterId,startTime,endTime));
    }
}
