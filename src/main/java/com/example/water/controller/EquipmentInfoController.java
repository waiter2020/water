package com.example.water.controller;

import com.example.water.model.EquipmentInfo;
import com.example.water.model.Family;
import com.example.water.model.User;
import com.example.water.service.EquipmentInfoService;
import com.example.water.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.LinkedList;


/**
 * Created by waiter on 18-6-18.
 * @author waiter
 */
@Controller
public class EquipmentInfoController {
    @Autowired
    private EquipmentInfoService equipmentInfoService;
    @Autowired
    private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @RequestMapping(value = "/findall")
    public Object findAll(){
        return equipmentInfoService.findAll();
    }

    @GetMapping(value = "/equip")
    public String equip(Model model, HttpServletRequest request){
        String remoteUser = request.getRemoteUser();
        User byUserName = (User) userService.findByUserName(remoteUser);
        if(byUserName.getFamily()==null){
            return "equip/list";
        }
        LinkedList<EquipmentInfo> allByFamily_id = equipmentInfoService.findAllByFamily_Id(byUserName.getFamily().getId());
        model.addAttribute("equips",allByFamily_id);
        model.addAttribute("family",byUserName.getFamily());
        return "equip/list";
    }

    @Transactional
    @PostMapping(value = "equip/add")
    public String addEquip(Model model,String equipId,HttpServletRequest request){
        String remoteUser = request.getRemoteUser();
        User byUserName = (User) userService.findByUserName(remoteUser);
        if(byUserName.getFamily()==null){
            return "redirect:/family/create";
        }
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentById(equipId);

        if(equipmentById==null){
            logger.warn("用户"+byUserName+"尝试绑定设备"+equipmentById+"失败 "+"原因：设备不存在");
            model.addAttribute("addmsg","设备不存在请检查设备id");
            return "equip/add";
        }else if(equipmentById.getFamily()!=null&&equipmentById.getFamily().getId()!=byUserName.getFamily().getId()){
            logger.warn("用户"+byUserName+"尝试绑定设备"+equipmentById+"失败 "+"原因：已被其它账号绑定");
            model.addAttribute("addmsg","设备已被他人绑定");
            return "equip/add";
        }

        //权限鉴定
        if(!byUserName.getFamily().getAdmin().equals(byUserName.getUsername())){
            return "redirect:/equip";
        }
        equipmentById.setFamily(byUserName.getFamily());
        equipmentInfoService.save(equipmentById);
        logger.info("家庭组"+byUserName.getFamily().toString()+"绑定了设备"+equipmentById.getEquipId());
        model.addAttribute("equipmsg","绑定成功");

        return equip(model,request);
    }

    @Transactional
    @DeleteMapping(value = "/equip/{id}")
    public String deleteEquip(@PathVariable("id")String equipId,Model model,HttpServletRequest request){
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentById(equipId);
        Family family = equipmentById.getFamily();
        User byUserName = (User) userService.findByUserName(request.getRemoteUser());
        //权限鉴定
        if(byUserName.getFamily().getId()!=family.getId()||!byUserName.getFamily().getAdmin().equals(byUserName.getUsername())){
            return "redirect:/equip";
        }
        equipmentById.setFamily(null);
        logger.info("家庭组"+family.getFamilyName()+"与设备"+equipmentById.getEquipId()+"解绑");
        equipmentInfoService.save(equipmentById);
        model.addAttribute("equipmsg","成功从家庭组里移除了"+equipmentById.getEquipId());
        return equip(model,request);
    }

    @GetMapping(value = "/equip/add")
    public String add(){
        return "/equip/add";
    }
}
