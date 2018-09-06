package com.example.water.api;

import com.example.water.model.WaterCondition;
import com.example.water.service.UserDetailsServiceIml;
import com.example.water.service.WaterConditionService;
import com.example.water.utils.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by  waiter on 18-8-9  下午3:24.
 * 用水数据相关接口
 * @author waiter
 */
@RestController
@RequestMapping(value = "/api/WaterCondition")
public class WaterConditionApi {
    @Autowired
    UserDetailsServiceIml userDetailsServiceIml;
    @Autowired
    private WaterConditionService waterConditionService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取用水数据
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/GetWaterConditionByDate")
    public Object getWater(HttpServletRequest request) throws ParseException {
        //@PathVariable(name = "page",required = false)Integer page , String equipId, Date startTime, Date endTime
        String pages = request.getParameter("page");
        String equipId = request.getParameter("equipId");
        String startTimes = request.getParameter("startTime");
        String endTimes = request.getParameter("endTime");
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date startTime = format0.parse(startTimes);
        Date endTime = format0.parse(endTimes);
        int page;
        if (pages==null){
            page=0;
        }else {
            page=Integer.parseInt(pages);
        }
        if(page<0){
            page=0;
        }

        Page<WaterCondition> waterInfosByDate = waterConditionService.getWaterInfosByDate(page,equipId, startTime, endTime);
        PageBean pageBean = new PageBean();
        pageBean.setTotalPage(waterInfosByDate.getTotalPages());
        pageBean.setTotalCount((int)waterInfosByDate.getTotalElements());
        pageBean.setCurrentPage(page);
        pageBean.setPageData(waterInfosByDate.getContent());
        pageBean.setPageCount(waterInfosByDate.getSize());
        return pageBean;
    }
}
