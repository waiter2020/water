package com.example.water.service;

import com.example.water.dao.UserDao;
import com.example.water.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    public Object findByUserName(String username){
        return userDao.findByUsername(username);
    }

    public void add(User user){
        userDao.save(user);
    }



    public void save(User user){userDao.save(user);}

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findByUsername(s);
        if(user == null){
            throw new UsernameNotFoundException("user not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPasswd(),user.getAuthorities());
    }

    public LinkedList<User> findAllByFamily_Id(int id){
        return userDao.findAllByFamily_Id(id);
    }

    public User findById(int id){
        return userDao.findById(id);
    }
}
