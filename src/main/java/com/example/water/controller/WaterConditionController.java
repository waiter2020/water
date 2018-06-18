package com.example.water.controller;

import com.example.water.service.WaterConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.HttpResource;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@RestController
public class WaterConditionController extends HttpServlet {
    @Autowired
    private WaterConditionService waterConditionService;

    /**
     * 获取某段时间内流量信息
     * @param request
     * @param response
     * @throws Exception
     */
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
        String resultString = waterConditionService.getWaterInfoByDate(watermeterId, startTime, endTime);
        out.write(resultString);
        System.out.println(resultString);
        out.flush();
        out.close();

    }
}
