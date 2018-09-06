package com.example.water.service;

import com.example.water.dao.UserDao;
import com.example.water.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserDetailsServiceIml implements UserDetailsService {
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
            if (s.matches("^(1[0-9])\\d{9}$")){
                user = userDao.findUserByPhoneNumber(s);
            }
            if (user==null&&s.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")){
                user=userDao.findUserByEmail(s);
            }
            if (user==null){
                throw new UsernameNotFoundException("user not found");
            }
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPasswd(),user.getAuthorities());
    }

    public LinkedList<User> findAllByFamilyId(int id){
        return userDao.findAllByFamily_Id(id);
    }

    public User findById(int id){
        return userDao.findById(id);
    }

    public void saveAll(Iterable<User> list ){
        userDao.saveAll(list);
    }
}
