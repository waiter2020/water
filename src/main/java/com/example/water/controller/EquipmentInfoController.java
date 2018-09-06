package com.example.water.controller;

import com.example.water.model.EquipmentInfo;
import com.example.water.model.Family;
import com.example.water.model.User;
import com.example.water.service.EquipmentInfoService;
import com.example.water.service.UserDetailsServiceIml;
import com.example.water.socket.Service;
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
 * Created by waiter on 18-6-18.
 *
 * @author waiter
 */
@Controller
public class EquipmentInfoController {
    @Autowired
    private EquipmentInfoService equipmentInfoService;
    @Autowired
    private UserDetailsServiceIml userDetailsServiceIml;
    @Autowired
    private Service service;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 设备列表
     * @return
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/findall")
    public Object findAll() {
        return equipmentInfoService.findAll();
    }

    /**
     * 显示设备列表页面
     * @param model
     * @param request
     * @return
     */
    @GetMapping(value = "/equip")
    public String equip(Model model, HttpServletRequest request) {
        String remoteUser = request.getRemoteUser();
        User byUserName = (User) userDetailsServiceIml.findByUserName(remoteUser);
        if (byUserName.getFamily() == null) {
            return "equip/list";
        }
        LinkedList<EquipmentInfo> allByFamily_id = equipmentInfoService.findAllByFamily_Id(byUserName.getFamily().getId());
        model.addAttribute("equips", allByFamily_id);
        model.addAttribute("family", byUserName.getFamily());
        return "equip/list";
    }

    /**
     * 添加设备
     * @param model
     * @param equipId
     * @param request
     * @return
     */
    @Transactional
    @PostMapping(value = "equip/add")
    public String addEquip(Model model, String equipId, HttpServletRequest request) {
        String remoteUser = request.getRemoteUser();
        User byUserName = (User) userDetailsServiceIml.findByUserName(remoteUser);
        if (byUserName.getFamily() == null) {
            return "redirect:/family/create";
        }
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);

        if (equipmentById == null) {
            logger.warn("用户" + byUserName + "尝试绑定设备" + equipId + "失败 " + "原因：设备不存在");
            model.addAttribute("addmsg", "设备不存在请检查设备id");
            return "equip/add";
        } else if (equipmentById.getFamily() != null && equipmentById.getFamily().getId() != byUserName.getFamily().getId()) {
            logger.warn("用户" + byUserName + "尝试绑定设备" + equipmentById + "失败 " + "原因：已被其它账号绑定");
            model.addAttribute("addmsg", "设备已被他人绑定");
            return "equip/add";
        }

        //权限鉴定
        if (!byUserName.getFamily().getAdmin().equals(byUserName.getUsername())) {
            return "redirect:/equip";
        }
        equipmentById.setFamily(byUserName.getFamily());
        equipmentInfoService.save(equipmentById);
        logger.info("家庭组" + byUserName.getFamily().toString() + "绑定了设备" + equipmentById.getEquipId());
        model.addAttribute("equipmsg", "绑定成功");

        return equip(model, request);
    }

    /**
     * 删除设备
     * @param equipId
     * @param model
     * @param request
     * @return
     */
    @Transactional
    @DeleteMapping(value = "/equip/{id}")
    public String deleteEquip(@PathVariable("id") String equipId, Model model, HttpServletRequest request) {
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);
        Family family = equipmentById.getFamily();
        User byUserName = (User) userDetailsServiceIml.findByUserName(request.getRemoteUser());
        //权限鉴定
        if (byUserName.getFamily().getId() != family.getId() || !byUserName.getFamily().getAdmin().equals(byUserName.getUsername())) {
            return "redirect:/equip";
        }
        equipmentById.setFamily(null);
        logger.info("家庭组" + family.getFamilyName() + "与设备" + equipmentById.getEquipId() + "解绑");
        equipmentInfoService.save(equipmentById);
        model.addAttribute("equipmsg", "成功从家庭组里移除了" + equipmentById.getEquipId());
        return equip(model, request);
    }

    @GetMapping(value = "/equip/add")
    public String add() {
        return "equip/add";
    }

    /**
     * 重启设备
     * @param equipId
     * @param model
     * @param request
     * @return
     */
    @GetMapping(value = "/equip/restart/{id}")
    public String restartEquip(@PathVariable("id") String equipId, Model model, HttpServletRequest request) {
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);
        if (equipmentById.isOnline()) {
            service.reStart(equipmentById.getLoginId(), equipId);
            model.addAttribute("equipmsg", "成功重启设备" + equipId);
        } else {
            model.addAttribute("equipmsg", "设备已离线");
        }
        return equip(model, request);
    }

    /**
     * 修改设备
     * @param equipId
     * @param model
     * @param request
     * @return
     */
    @GetMapping(value = "/equip/change/{id}")
    public String toChangeEquip(@PathVariable("id") String equipId, Model model, HttpServletRequest request) {
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);

        User byUserName = (User) userDetailsServiceIml.findByUserName(request.getRemoteUser());
        if (byUserName.getFamily().getId() != equipmentById.getFamily().getId()) {
            return equip(model, request);
        }
        model.addAttribute("equip", equipmentById);

        model.addAttribute("equipmsg", "设备已离线");

        return "equip/change";
    }

    @PostMapping(value = "/equip/change")
    public String changeEquip(Model model, HttpServletRequest request) {
        String equipId = request.getParameter("equipId");
        String locLatitude = request.getParameter("locLatitude");
        String locLongitude = request.getParameter("locLongitude");
        String thresholdType = request.getParameter("thresholdType");
        String thresholdValue = request.getParameter("thresholdValue");
        String name = request.getParameter("name");
        String model1 = request.getParameter("model");


        EquipmentInfo equipmentByEquipId = equipmentInfoService.getEquipmentByEquipId(equipId);
        equipmentByEquipId.setLocLatitude(Double.parseDouble(locLatitude));
        equipmentByEquipId.setLocLongitude(Double.parseDouble(locLongitude));
        equipmentByEquipId.setThresholdType(Integer.parseInt(thresholdType));
        equipmentByEquipId.setThresholdValue(Integer.parseInt(thresholdValue));
        equipmentByEquipId.setModel(Integer.parseInt(model1));
        equipmentByEquipId.setName(name);
        if (equipmentByEquipId.isOnline()) {

            service.setThresholdValue(equipmentByEquipId.getLoginId(), equipmentByEquipId.getEquipId() + "", equipmentByEquipId.getThresholdValue() + "", equipmentByEquipId.getThresholdType());
            service.setModel(equipmentByEquipId.getLoginId(), equipmentByEquipId.getEquipId() + "", equipmentByEquipId.getModel());
            equipmentInfoService.save(equipmentByEquipId);

        }
        model.addAttribute("equipmsg", "修改成功");
        return equip(model, request);
    }

    /**
     * 设置阀门开关
     * @param equipId
     * @param model
     * @param request
     * @return
     */
    @GetMapping(value = "/equip/open_close/{id}")
    public String openOrCloseEquip(@PathVariable("id") String equipId, Model model, HttpServletRequest request) {
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);
        if (equipmentById.isOnline()) {
            equipmentById.setOpen(!equipmentById.isOpen());
            service.openOrClose(equipmentById.getLoginId(), equipmentById.getEquipId() + "", equipmentById.isOpen());
            equipmentInfoService.save(equipmentById);
        } else {
            model.addAttribute("equipmsg", "设备已离线");
        }
        return "redirect:/equip";
    }

    /**
     * 请求上传数据
     * @param equipId
     * @param model
     * @param request
     * @return
     */
    @GetMapping(value = "/equip/getdata/{id}")
    public String getData(@PathVariable("id") String equipId, Model model, HttpServletRequest request) {
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);
        if (equipmentById.isOnline()) {
            service.getData(equipmentById.getLoginId(), equipmentById.getEquipId() + "");
            model.addAttribute("equipmsg", "请求已发送");
        } else {
            model.addAttribute("equipmsg", "设备已离线");
        }
        return equip(model, request);
    }
}
