package com.example.water.dao;

import com.example.water.model.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
public interface RoleDao extends CrudRepository<Role,Integer> {
    /**
     * 通过id获取权限信息
     * @param id
     * @return
     */
    Role findById(int id);
}
