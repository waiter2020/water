package com.example.water.controller;

import com.example.water.model.EquipmentInfo;
import com.example.water.model.User;
import com.example.water.model.WaterCondition;
import com.example.water.service.EquipmentInfoService;
import com.example.water.service.UserService;
import com.example.water.service.WaterConditionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.HttpResource;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Controller
public class WaterConditionController extends HttpServlet {
    @Autowired
    private WaterConditionService waterConditionService;
    @Autowired
    private UserService userService;
    @Autowired
    private EquipmentInfoService equipmentInfoService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 获取某段时间内流量信息
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/GetWaterConditionByDate")
    public void getWaterCondition(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        System.out.println("servlet");
        PrintWriter out = response.getWriter();

        String watermeterId = request.getParameter("watermeter_id");
        String st=request.getParameter("startTime");
        long startTime = Integer.parseInt(st);
        String et=request.getParameter("endTime");
        long endTime = Long.parseLong(et);
        String resultString = waterConditionService.getWaterInfoByDate(watermeterId, new Date(startTime), new Date(endTime));
        out.write(resultString);
        System.out.println(resultString);
        out.flush();
        out.close();
    }
    @PostMapping(value = "/water")
    public String getWater(String equipId, Date startTime, Date endTime,Model model,HttpServletRequest request){
        Collection<WaterCondition> waterInfosByDate = waterConditionService.getWaterInfosByDate(equipId, startTime, endTime);
        logger.info("查询了"+equipId+" 开始时间："+startTime+" 结束时间："+endTime);
        model.addAttribute("waterConditions",waterInfosByDate);
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String startTimes=format0.format(startTime);
        String endTimes=format0.format(endTime);
        model.addAttribute("startTimes",startTimes);
        model.addAttribute("endTimes",endTimes);
        return get(model,request);
    }

    @GetMapping(value = "water")
    public String get(Model model,HttpServletRequest request){
        String remoteUser = request.getRemoteUser();
        User byUserName = (User) userService.findByUserName(remoteUser);
        if(byUserName.getFamily()==null){
            return "water/list";
        }
        LinkedList<EquipmentInfo> allByFamily_id = equipmentInfoService.findAllByFamily_Id(byUserName.getFamily().getId());
        model.addAttribute("equips",allByFamily_id);
        return "water/list";
    }

}
