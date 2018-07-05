package com.example.water.dao;

import com.example.water.model.WaterCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
public interface WaterConditionDao extends PagingAndSortingRepository<WaterCondition,Long> {
    /**
     * 通过漏失仪id查找所有用水信息
     * @param watermeterId
     * @return
     */
    List<WaterCondition> getAllByWatermeterId(String watermeterId);

    /**
     * 获取某段时间内的用水信息
     * @param watermeterId
     * @param starDate
     * @param endDate
     * @return
     */
    Page<WaterCondition> getWaterConditionsByWatermeterIdAndStartDateAfterAndEndDateBeforeOrderByStartDateDesc(Pageable pageable,String watermeterId, Date starDate, Date endDate);
    Page<WaterCondition> findAllByWatermeterIdAndAndStartDateAfterAndEndDateBeforeOrderByStartDate(Pageable pageable,String watermeterId, Date starDate, Date endDate);
}
