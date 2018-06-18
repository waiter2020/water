package com.example.water.dao;

import com.example.water.model.WaterCondition;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
public interface WaterConditionDao extends CrudRepository<WaterCondition,Long> {
    /**
     * 通过漏失仪id查找所有用水信息
     * @param watermeterId
     * @return
     */
    List<WaterCondition> getAllByWatermeterId(String watermeterId);

    /**
     * 获取某段时间内的用水信息
     * @param watermeterId
     * @param starTime
     * @param endTime
     * @return
     */
    List<WaterCondition> getWaterConditionsByWatermeterIdAndStartTimeAfterAndEndTimeBeforeOrderByStartTime(String watermeterId,long starTime,long endTime);
}
