package com.example.water.dao;

import com.example.water.model.Greeting;
import com.example.water.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by  waiter on 18-8-1  下午6:52.
 *
 * @author waiter
 */
public interface GreetingDao extends JpaRepository<Greeting,Integer> {
}
