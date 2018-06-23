package com.example.water.dao;

import com.example.water.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedList;


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
     User findByUsername(String userName);

    /**
     * 通过家庭id查找所有成员
     * @param id
     * @return
     */
     LinkedList<User> findAllByFamily_Id(int id);

    /**
     *
     * @param id
     * @return
     */
    User findById(int id);
}
