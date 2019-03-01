package com.example.water.zhongde.api;

import com.example.water.model.EquipmentInfo;
import com.example.water.model.WaterCondition;
import com.example.water.service.EquipmentInfoService;
import com.example.water.service.WaterConditionService;
import com.example.water.socket.Service;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: waiter
 * @Date: 19-1-26 01:01
 * @Description:
 */
@RestController
@RequestMapping("/zhong_de")
public class EquipInfoController {
    private final EquipmentInfoService equipmentInfoService;
    private final WaterConditionService waterConditionService;
    private final Service service;

    public EquipInfoController(EquipmentInfoService equipmentInfoService, WaterConditionService waterConditionService, Service service) {
        this.equipmentInfoService = equipmentInfoService;
        this.waterConditionService = waterConditionService;
        this.service = service;
    }

    @GetMapping("/get_all_equip")
    public List<EquipmentInfo> equipmentInfos(){
        return equipmentInfoService.findAll();
    }



    @GetMapping("/get_equip/{id}")
    public EquipmentInfo equipmentInfo(@PathVariable String id){
        return equipmentInfoService.getEquipmentByEquipId(id);
    }


    @GetMapping({"/get/water_condition/{id}/{startTime}/{endTime}/{page}/{pageSize}","/get/water_condition/{id}/{startTime}/{endTime}/{page}"})
    public Page<WaterCondition> waterConditions(@PathVariable String id,
                                                @PathVariable Date startTime,
                                                @PathVariable Date endTime,
                                                @PathVariable Integer page,
                                                @PathVariable Integer pageSize){
        if (page==null||page<0){
            page=0;
        }
        if (pageSize==null){
            pageSize=20;
        }
        return waterConditionService.getWaterInfosByDate(page,id, startTime,endTime,pageSize);
    }

    //只需要加上下面这段即可，注意不能忘记注解
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {

        //转换日期
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
    }


   
    @GetMapping(value = "/restart/{equipId}")
    public String restartEquip(@PathVariable String equipId) throws InterruptedException {
        EquipmentInfo equipmentById = equipmentInfoService.getEquipmentByEquipId(equipId);
        if (equipmentById.isOnline()) {
            service.reStart(equipmentById.getLoginId(), equipId);
            equipmentById.setEquipState(5);
            equipmentById.setEndStateTime(new Date());
            equipmentInfoService.save(equipmentById);
        } else {
            return "设备已离线";
        }
        return "请求已发送";
    }
}
