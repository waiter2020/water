package com.example.water.controller;



import com.example.water.model.EquipmentInfo;
import com.example.water.model.Family;
import com.example.water.model.User;
import com.example.water.service.EquipmentInfoService;
import com.example.water.service.FamilyService;


import com.example.water.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

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
    private FamilyService familyService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceIml;
    @Autowired
    private EquipmentInfoService equipmentInfoService;

    private final Logger logger = LoggerFactory.getLogger(FamilyController.class);

    /**
     * 家庭组列表
     * @param model
     * @param request
     * @return
     */
    @GetMapping(value = "/family")
    public String familylist(Model model, HttpServletRequest request){
        String username = request.getRemoteUser();
        logger.info("用户"+username+"获取了家庭组列表");
        User user = (User) userDetailsServiceIml.findByUserName(username);
        if(user.getFamily()==null){
            model.addAttribute("familymsg","您还没有加入家庭组，请先创建或加入家庭组");
            return "family/list";
        }
        LinkedList<User> users = userDetailsServiceIml.findAllByFamilyId(user.getFamily().getId());
        Family family = familyService.findById(user.getFamily().getId());
        model.addAttribute("users",users);
        model.addAttribute("family",family);

        return "family/list";
    }

    /**
     * 删除成员
     * @param id
     * @param model
     * @param request
     * @return
     */
    @DeleteMapping(value = "/family/{id}")
    public String deleteUser(@PathVariable("id")Integer id,Model model ,HttpServletRequest request){
        User byUserName = (User) userDetailsServiceIml.findByUserName(request.getRemoteUser());
        User user = userDetailsServiceIml.findById(id);
        //权限鉴定
        if(user.getFamily().getId()!=byUserName.getFamily().getId()){
            model.addAttribute("addmsg","无权添加");
            logger.warn("用户"+byUserName+"尝试越权删除成员"+user);
            return "redirect:/family";
        }
        user.setFamily(null);
        logger.info("从家庭组里移除了"+user.getUsername());
        userDetailsServiceIml.save(user);
        model.addAttribute("familymsg","成功从家庭组里移除了"+user.getUsername());
        return familylist(model,request);
    }

    /**
     * 添加成员
     * @param userName
     * @param model
     * @param request
     * @return
     */
    @PostMapping(value = "/family/add")
    public String addUser(String userName ,Model model, HttpServletRequest request){
        String name = request.getRemoteUser();
        User byName = (User) userDetailsServiceIml.findByUserName(name);
        User byUserName = (User) userDetailsServiceIml.findByUserName(userName);
        //权限检查
        if(!byName.getFamily().getAdmin().equals(name)){
            model.addAttribute("addmsg","无权添加");
            logger.warn("用户"+byName+"尝试越权添加成员"+byUserName);
            return "redirect:/family";
        }

        if(byUserName==null){
            model.addAttribute("addmsg","用户不存在，请检查用户名");
            logger.error("用户不存在");
            return "/family/add";
        }else if(byUserName.getFamily()!=null&&byUserName.getFamily().getId()!=byName.getFamily().getId()){
            model.addAttribute("addmsg","用户已加入其他家庭组");
            logger.error("用户不存在");
            return "family/add";
        }
        byUserName.setFamily(byName.getFamily());
        userDetailsServiceIml.save(byUserName);
        model.addAttribute("familymsg","成功将"+byUserName.getUsername()+"加入家庭组");
        return familylist(model,request);
    }


    @GetMapping(value = "/family/add")
    public String add(){
        return "family/add";
    }

    /**
     * 新建家庭组
     * @param model
     * @param family
     * @return
     */
    @Transactional
    @PostMapping(value = "/family/create")
    public String create(Model model,Family family){
        User byUserName = (User) userDetailsServiceIml.findByUserName(family.getAdmin());
        if(byUserName.getFamily()!=null){
            return "redirect:/family";
        }
        family=familyService.save(family);
        byUserName.setFamily(family);
        userDetailsServiceIml.save(byUserName);
        model.addAttribute("familymsg","创建家庭组成功，快邀请成员加入吧");
        logger.info(byUserName+"创建了家庭组"+family);
        return "family/list";
    }

    /**
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/family/create")
    public String getCreateView(Model model){
        model.addAttribute("create_msg","请先创建家庭组");
        return "family/create_family";
    }

    /**
     * 解散家庭组
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional
    @GetMapping(value = "/family/remove")
    public String remove(Model model,HttpServletRequest request) throws Exception {
        String remoteUser = request.getRemoteUser();
        User byUserName = (User) userDetailsServiceIml.findByUserName(remoteUser);
        if (byUserName.getFamily().getAdmin().equals(byUserName.getUsername())) {
            if (byUserName.getFamily() == null) {
                return "redirect:/family";
            }
            try {
                LinkedList<User> allByFamilyId = userDetailsServiceIml.findAllByFamilyId(byUserName.getFamily().getId());

                logger.warn(byUserName + "解散了家庭组" + byUserName.getFamily());

                int id = byUserName.getFamily().getId();
                for (User a :
                        allByFamilyId) {
                    a.setFamily(null);
                }
                userDetailsServiceIml.saveAll(allByFamilyId);

                LinkedList<EquipmentInfo> byFamilyId = equipmentInfoService.findAllByFamily_Id(id);
                for (EquipmentInfo a :
                        byFamilyId) {
                    a.setFamily(null);
                }
                equipmentInfoService.saveAll(byFamilyId);
                familyService.remove(id);
                model.addAttribute("familymsg", "成功解散家庭组");
            } catch (Exception e) {

                throw new Exception();
            }
        }else {
            logger.warn("用户"+byUserName+"尝试非法解散群组");
            model.addAttribute("familymsg","无权操作");
        }
        return familylist(model,request);
    }

    /**
     * 退出家庭组
     * @param model
     * @param request
     * @return
     */
    @GetMapping(value = "/family/exit")
    public String exit(Model model,HttpServletRequest request){
        String remoteUser = request.getRemoteUser();
        User byUserName = (User) userDetailsServiceIml.findByUserName(remoteUser);
        logger.warn("用户"+byUserName+"退出了家庭组"+byUserName.getFamily());
        byUserName.setFamily(null);
        userDetailsServiceIml.save(byUserName);
        model.addAttribute("familymsg","成功退出家庭组");
        return familylist(model,request);
    }
}
