package com.example.water.controller;

import com.example.water.model.Family;
import com.example.water.model.User;
import com.example.water.service.FamilyService;
import com.example.water.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;

/**
 * Created by  waiter on 18-6-18.
 *
 * @author waiter
 */
@Controller
public class FamilyController {
    @Autowired
    FamilyService familyService;
    @Autowired
    UserService userService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/family")
    public String familylist(Model model, HttpServletRequest request){
        String username = request.getRemoteUser();
        logger.info("用户"+username+"获取了家庭组列表");
        User user = (User) userService.findByUserName(username);
        LinkedList<User> users = userService.findAllByFamily_Id(user.getFamily().getId());
        Family family = familyService.findById(user.getFamily().getId());
        model.addAttribute("users",users);
        model.addAttribute("family",family);

        return "family/list";
    }

    @DeleteMapping(value = "/family/{id}")
    public String deleteUser(@PathVariable("id")Integer id,Model model){
        User user = userService.findById(id);
        user.setFamily(null);
        logger.info("从家庭组里移除了"+user.getUsername());
        userService.save(user);
        model.addAttribute("familymsg","成功从家庭组里移除了"+user.getUsername());
        return "redirect:/family";
    }

    @PostMapping(value = "/family/add")
    public String addUser(String userName ,Model model, HttpServletRequest request){
        String name = request.getRemoteUser();
        User byName = (User) userService.findByUserName(name);
        User byUserName = (User) userService.findByUserName(userName);
        if(byUserName==null){
            model.addAttribute("addmsg","用户不存在，请检查用户名");
            logger.error("用户不存在");
            return "/family/add";
        }else if(byUserName.getFamily()!=null&&byUserName.getFamily().getId()!=byName.getFamily().getId()){
            model.addAttribute("addmsg","用户已加入其他家庭组");
            logger.error("用户不存在");
            return "/family/add";
        }
        byUserName.setFamily(byName.getFamily());
        userService.save(byUserName);
        model.addAttribute("familymsg","成功将"+byUserName.getUsername()+"加入家庭组");
        return "redirect:/family";
    }
    @GetMapping(value = "/family/add")
    public String add(){
        return "/family/add";
    }

}
