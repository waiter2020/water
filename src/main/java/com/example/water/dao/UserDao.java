package com.example.water.dao;

import com.example.water.model.User;
import org.springframework.data.repository.CrudRepository;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
public interface UserDao extends CrudRepository<User,Integer> {
    /**
     * 通过用户名查找用户
     * @param userName
     * @return
     */
    public User findByUsername(String userName);
}
