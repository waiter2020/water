package com.example.water.controller;

import com.example.water.model.EquipmentInfo;
import com.example.water.model.Log;
import com.example.water.model.User;
import com.example.water.service.EquipmentInfoService;
import com.example.water.service.LogService;
import com.example.water.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by  waiter on 18-8-3  下午3:40.
 *
 * @author waiter
 */
@Controller
public class LogController {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceIml;
    @Autowired
    private EquipmentInfoService equipmentInfoService;
    @Autowired
    private LogService logService;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostMapping(value = {"/logs","/logs/{page}"})
    public String getWater(@PathVariable(name = "page",required = false)Integer page , String equipId, Date startTime, Date endTime, Model model, HttpServletRequest request){
        if(page==null){
            page=new Integer(0);
        }else if(page<0){
            page=0;
        }

        Page<Log> waterInfosByDate = logService.getLogsByDate(page,equipId, startTime, endTime);
        logger.info("查询了"+equipId+" 开始时间："+startTime+" 结束时间："+endTime);
        List<Log> content = waterInfosByDate.getContent();
        model.addAttribute("page",waterInfosByDate);
        model.addAttribute("logs",waterInfosByDate);
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String startTimes=format0.format(startTime);
        String endTimes=format0.format(endTime);
        model.addAttribute("startTimes",startTimes);
        model.addAttribute("endTimes",endTimes);
        model.addAttribute("equipId",equipId);
        return get(model,request);
    }

    @GetMapping(value = "/logs")
    public String get(Model model,HttpServletRequest request){
        String remoteUser = request.getRemoteUser();
        User byUserName = (User) userDetailsServiceIml.findByUserName(remoteUser);
        if(byUserName.getFamily()==null){
            return "equip_logs/list";
        }
        LinkedList<EquipmentInfo> byFamilyId = equipmentInfoService.findAllByFamily_Id(byUserName.getFamily().getId());
        model.addAttribute("equips",byFamilyId);
        return "equip_logs/list";
    }
}
