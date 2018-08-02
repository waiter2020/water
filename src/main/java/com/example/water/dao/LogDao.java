package com.example.water.dao;

import com.example.water.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by  waiter on 18-8-2  下午12:41.
 *
 * @author waiter
 */
public interface LogDao extends JpaRepository<Log,Long> {
}
