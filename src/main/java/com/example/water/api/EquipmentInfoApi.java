package com.example.water.api;

import com.example.water.model.EquipmentInfo;
import com.example.water.model.Family;
import com.example.water.model.User;
import com.example.water.service.EquipmentInfoService;
import com.example.water.service.UserDetailsServiceImpl;
import com.example.water.socket.Service;
import com.example.water.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.SessionScope;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by  waiter on 18-8-7  下午10:29.
 * 设备控制相关接口
 * @author waiter
 */
@RestController
@RequestMapping(value = "/api/equip")
public class EquipmentInfoApi {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceIml;
    @Autowired
    private EquipmentInfoService equipmentInfoService;
    @Autowired
    private Service service;

    /**
     * 获取设备列表
     * @param request
     * @return
     */
    @GetMapping(value = "/get_infos")
    public LinkedList<EquipmentInfo> getEquips(HttpServletRequest request){
        UserDetails user = (UserDetails) request.getSession().getAttribute("user");
        User userName = (User) userDetailsServiceIml.findByUserName(user.getUsername());
        if (userName.getFamily()==null){
            return new LinkedList<>();
        }
        return equipmentInfoService.findAllByFamily_Id(userName.getFamily().getId());
    }

    /**
     * 通过设备id获取设备信息
     * @param request
     * @return
     */
    @GetMapping(value = "/equip_by_id")
    public EquipmentInfo getEquip(HttpServletRequest request){
        String equipId = request.getParameter("equipId");
        return equipmentInfoService.getEquipmentByEquipId(equipId);
    }

    /**
     * 添加设备
     * @param map
     * @param request
     * @return
     */
    @Transactional
    @PostMapping(value = "/add")
    public String  addEquip(@RequestBody Map<String,String> map, HttpServletRequest request) {

        UserDetails user = (UserDetails) request.getSession().getAttribute("user");
        User userName = (User) userDetailsServiceIml.findByUserName(user.getUsername());
        String equipId = map.get("equipId");
        if (equipId==null||"".equals(equipId)){
            equipId=0+"";
        }
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId.trim());

        if (equipmentById == null) {

            return "设备不存在请检查设备id";
        } else if (equipmentById.getFamily() != null && equipmentById.getFamily().getId() != userName.getFamily().getId()) {
            return "设备已被他人绑定";
        }

        //权限鉴定
        if (!userName.getFamily().getAdmin().equals(user.getUsername())) {
            return "";
        }
        equipmentById.setFamily(userName.getFamily());
        equipmentInfoService.save(equipmentById);

        return null;
    }

    /**
     * 删除设备
     * @param map
     * @param request
     * @return
     */
    @Transactional
    @PostMapping(value = "/delete")
    public String deleteEquip(@RequestBody Map<String,String> map, HttpServletRequest request) {
        UserDetails user = (UserDetails) request.getSession().getAttribute("user");
        User userName = (User) userDetailsServiceIml.findByUserName(user.getUsername());
        String equipId = map.get("equipId");
        if (equipId==null){
            equipId=0+"";
        }

        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);
        if (userName.getFamily().getId()!=equipmentById.getFamily().getId()){
            return null;
        }
        equipmentById.setFamily(null);
        equipmentInfoService.save(equipmentById);
        return "成功从家庭组里移除了";
    }


    /**
     * 阀门开关
     * @param map
     * @param request
     * @return
     */
    @PostMapping(value = "/open_close")
    public String openOrCloseEquip(@RequestBody Map<String,String> map, HttpServletRequest request) {
        String equipId = map.get("equipId");
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);
        if (equipmentById.isOnline()) {
            equipmentById.setOpen(!equipmentById.isOpen());
            service.openOrClose(equipmentById.getLoginId(), equipmentById.getEquipId() + "", equipmentById.isOpen());
            equipmentInfoService.save(equipmentById);
    } else {
        return "设备离线";
    }
        return "操作成功";
    }

    /**
     * 改变检漏模式
     * @param map
     * @param request
     * @return
     */
    @PostMapping(value = "/change_model")
    public String changeModel(@RequestBody Map<String,String> map, HttpServletRequest request) {
        String equipId = map.get("equipId");
        String models = map.get("model");
        int model = Integer.parseInt(models);
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);
        equipmentById.setModel(model);
        equipmentInfoService.save(equipmentById);
        if (equipmentById.isOnline()) {
            service.setModel(equipmentById.getLoginId(),equipmentById.getEquipId(),equipmentById.getModel());
        }
        return "操作成功";
    }

    /**
     * 修改设备名
     * @param map
     * @param request
     * @return
     */
    @PostMapping(value = "/change_name")
    public String changeName(@RequestBody Map<String,String> map, HttpServletRequest request) {
        String equipId = map.get("equipId");
        String name = map.get("name");
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);
        equipmentById.setName(name);
        equipmentInfoService.save(equipmentById);
        return "操作成功";
    }

    /**
     * 改变阈值
     * @param map
     * @param request
     * @return
     */
    @PostMapping(value = "/change_threshold")
    public String changeThreshold(@RequestBody Map<String,String> map, HttpServletRequest request) {
        String equipId = map.get("equipId");
        String thresholdTypes = map.get("thresholdType");
        int thresholdType = Integer.parseInt(thresholdTypes);
        String thresholdValue = map.get("thresholdValue");
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);
        equipmentById.setThresholdType(thresholdType);
        equipmentById.setThresholdValue(Integer.parseInt(thresholdValue));
        equipmentInfoService.save(equipmentById);
        if (equipmentById.isOnline()) {
            service.setThresholdValue(equipmentById.getLoginId(),equipmentById.getEquipId(),equipmentById.getThresholdValue()+"",equipmentById.getThresholdType());
        }
        return "操作成功";
    }

    /**
     * 请求设备上传数据
     * @param map
     * @param request
     * @return
     */
    @PostMapping(value = "/getdata")
    public String getData(@RequestBody Map<String,String> map, HttpServletRequest request) {
        String equipId = map.get("equipId");
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);
        if (equipmentById.isOnline()) {
            service.getData(equipmentById.getLoginId(), equipmentById.getEquipId() + "");
        } else {
            return "设备离线";
        }
        return "请求已发送";
    }


    /**
     * 重启设备
     * @param map
     * @param request
     * @return
     */
    @PostMapping(value = "/restart")
    public String restartEquip(@RequestBody Map<String,String> map, HttpServletRequest request) {
        String equipId = map.get("equipId");
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);
        if (equipmentById.isOnline()) {
            service.reStart(equipmentById.getLoginId(), equipId);
        } else {
            return "设备已离线";
        }
        return "请求已发送";
    }
}
