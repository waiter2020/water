package com.example.water.service;

import com.example.water.dao.RoleDao;
import com.example.water.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    public Role findById(int id){
        return roleDao.findById(id);
    }
}
