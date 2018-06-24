package com.example.water.dao;

import com.example.water.model.Family;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by  waiter on 18-6-18.
 *
 * @author waiter
 */
public interface FamilyDao extends CrudRepository<Family,Integer> {
    /**
     * 通过id查找
     * @param id
     * @return
     */
    Family findById(int id);
    Family findByAdmin(String admin);
}
