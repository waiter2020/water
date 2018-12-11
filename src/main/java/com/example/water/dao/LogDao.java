package com.example.water.dao;

import com.example.water.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * Created by  waiter on 18-8-2  下午12:41.
 *
 * @author waiter
 */
public interface LogDao extends JpaRepository<Log,Long> {
    Page<Log> findAllByEquipIdAndAndDateAfterAndDateBeforeOrderByDate(Pageable pageable, String equipId, Date starDate, Date endDate);

}
