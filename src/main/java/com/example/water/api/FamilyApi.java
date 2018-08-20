package com.example.water.api;

import com.example.water.model.EquipmentInfo;
import com.example.water.model.Family;
import com.example.water.model.User;
import com.example.water.service.EquipmentInfoService;
import com.example.water.service.FamilyService;
import com.example.water.service.UserDetailsServiceIml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by  waiter on 18-8-9  下午3:31.
 *
 * @author waiter
 */
@RestController
@RequestMapping(value = "/api/family")
public class FamilyApi {
    @Autowired
    UserDetailsServiceIml userDetailsServiceIml;
    @Autowired
    private FamilyService familyService;
    @Autowired
    private EquipmentInfoService equipmentInfoService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/family")
    public LinkedList<User> familylist(HttpServletRequest request){
        UserDetails user1 = (UserDetails) request.getSession().getAttribute("user");
        logger.info("用户"+user1.getUsername()+"获取了家庭组列表");
        User user = (User) userDetailsServiceIml.findByUserName(user1.getUsername());
        if(user.getFamily()==null){
            return null;
        }
        LinkedList<User> users = userDetailsServiceIml.findAllByFamilyId(user.getFamily().getId());
        return users==null?new LinkedList<>():users;
    }

    @Transactional
    @PostMapping(value = "/create")
    public User create(@RequestBody Family family){
        User byUserName = (User) userDetailsServiceIml.findByUserName(family.getAdmin());
        if(byUserName.getFamily()==null) {

            familyService.save(family);
            family = familyService.findByAdmin(byUserName.getUsername());
            byUserName.setFamily(family);
            userDetailsServiceIml.save(byUserName);
            logger.info(byUserName + "创建了家庭组" + family);
        }
        return byUserName;
    }

    @PostMapping(value = "/add")
    public String addUser(@RequestBody Map<String,String> map, HttpServletRequest request){
        UserDetails user1 = (UserDetails) request.getSession().getAttribute("user");
        User user = (User) userDetailsServiceIml.findByUserName(user1.getUsername());
        String userName = map.get("userName");
        if (userName==null){
            userName=123+"";
        }
        User byUserName = (User) userDetailsServiceIml.findByUserName(userName);
        if(byUserName==null){
            return "用户不存在，请检查用户名";
        }else if(byUserName.getFamily()!=null&&byUserName.getFamily().getId()!=user.getFamily().getId()){
            return "用户已加入其他家庭组";
        }
        byUserName.setFamily(user.getFamily());
        userDetailsServiceIml.save(byUserName);
        return null;
    }

    @PostMapping(value = "/delete_user")
    public String deleteUser(@RequestBody Map<String,String> map,HttpServletRequest request){
        UserDetails user1 = (UserDetails) request.getSession().getAttribute("user");
        String id = map.get("id");
        User byUserName = (User) userDetailsServiceIml.findByUserName(user1.getUsername());
        User user = userDetailsServiceIml.findById(Integer.parseInt(id));

        user.setFamily(null);
        logger.info("从家庭组里移除了"+user.getUsername());
        userDetailsServiceIml.save(user);
        return "成功从家庭组里移除了";
    }

    @Transactional
    @PostMapping(value = "/remove")
    public String remove(HttpServletRequest request) throws Exception {
        UserDetails user1 = (UserDetails) request.getSession().getAttribute("user");
        User byUserName = (User) userDetailsServiceIml.findByUserName(user1.getUsername());
        if (byUserName.getFamily().getAdmin().equals(byUserName.getUsername())) {

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
                return "成功解散家庭组";
            } catch (Exception e) {

                throw new Exception();
            }
        }else {
            logger.warn("用户"+byUserName+"尝试非法解散群组");
            return "无权操作";
        }
    }

    @PostMapping(value = "/exit")
    public String exit(HttpServletRequest request){
        UserDetails user1 = (UserDetails) request.getSession().getAttribute("user");
        User byUserName = (User) userDetailsServiceIml.findByUserName(user1.getUsername());
        logger.warn("用户"+byUserName+"退出了家庭组"+byUserName.getFamily());
        byUserName.setFamily(null);
        userDetailsServiceIml.save(byUserName);
        return "成功退出家庭组";
    }

}
