package com.example.water.service;

import com.example.water.dao.FamilyDao;
import com.example.water.model.Family;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  waiter on 18-6-18.
 *
 * @author waiter
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FamilyService {
    @Autowired
    FamilyDao familyDao;

    /**
     *
     * @param id
     * @return
     */
    public Family findById(int id){
        return familyDao.findById(id);
    }

    public Family save(Family family){
        return familyDao.save(family);
    }

    public Family findByAdmin(String admin){
        return familyDao.findByAdmin(admin);
    }

    public void remove(int id){
        familyDao.removeById(id);
    }

}
