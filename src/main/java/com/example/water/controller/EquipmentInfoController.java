package com.example.water.controller;

import com.example.water.service.EquipmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by waiter on 18-6-18.
 * @author waiter
 */
@RestController
@RequestMapping(value = "/equipmentinfo")
public class EquipmentInfoController {
    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @RequestMapping(value = "/findall")
    public Object findAll(){
        return equipmentInfoService.findAll();
    }
}
