package com.example.water.controller;

import com.example.water.model.Family;
import com.example.water.model.User;
import com.example.water.service.FamilyService;
import com.example.water.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping(value = "/family")
    public String familylist(Model model, HttpServletRequest request){
        String username = request.getRemoteUser();
        System.out.println("用户"+username+"获取了家庭组列表");
        User user = (User) userService.findByUserName(username);
        LinkedList<User> users = userService.findAllByFamily_Id(user.getFamily().getId());
        Family family = familyService.findById(user.getFamily().getId());
        model.addAttribute("users",users);
        model.addAttribute("family",family);
        return "family/list";
    }
}
