package com.example.water.api;

import com.example.water.model.WaterCondition;
import com.example.water.service.UserDetailsServiceIml;
import com.example.water.service.WaterConditionService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by  waiter on 18-8-9  下午3:24.
 *
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

    @RequestMapping(value = "/GetWaterConditionByDate")
    public Object getWater(@PathVariable(name = "page",required = false)Integer page , String equipId, Date startTime, Date endTime){
        if(page==null){
            page=0;
        }else if(page<0){
            page=0;
        }

        Page<WaterCondition> waterInfosByDate = waterConditionService.getWaterInfosByDate(page,equipId, startTime, endTime);

        return waterInfosByDate;
    }
}
