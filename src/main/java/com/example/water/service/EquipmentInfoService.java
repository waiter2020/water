package com.example.water.service;

import com.example.water.dao.EquipmentInfoDao;
import com.example.water.model.EquipmentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Service
public class EquipmentInfoService {
    @Autowired
    private EquipmentInfoDao equipmentInfoDao;

    public void saveEquipmentInfo(String equipId,int waterUsage, int equipState){
        equipmentInfoDao.save(new EquipmentInfo(Integer.parseInt(equipId),waterUsage,equipState));
    }
    public EquipmentInfo getEquipmentById(String equipId){
        return equipmentInfoDao.getFirstByEquipId(Integer.parseInt(equipId));
    }
    public LinkedList<EquipmentInfo> findAll(){
        return (LinkedList<EquipmentInfo>) equipmentInfoDao.findAll();
    }
}
