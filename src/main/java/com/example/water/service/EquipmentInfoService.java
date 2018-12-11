package com.example.water.service;

import com.example.water.dao.EquipmentInfoDao;
import com.example.water.model.EquipmentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EquipmentInfoService {
    @Autowired
    private EquipmentInfoDao equipmentInfoDao;

    public void saveEquipmentInfo(String equipId,int waterUsage, int equipState){
        equipmentInfoDao.save(new EquipmentInfo(equipId,waterUsage,equipState));
    }
    public EquipmentInfo getEquipmentByEquipId(String equipId){
        return equipmentInfoDao.getFirstByEquipId(equipId);
    }

    public EquipmentInfo findByLoginId(String loginId){
        return equipmentInfoDao.findByLoginId(loginId);
    }

    public void save(EquipmentInfo equipmentInfo){
        equipmentInfoDao.save(equipmentInfo);
    }

    public ArrayList<EquipmentInfo> findAll(){
        return (ArrayList<EquipmentInfo>) equipmentInfoDao.findAll();
    }

    public LinkedList<EquipmentInfo> findAllByFamily_Id(int id){
        return equipmentInfoDao.findAllByFamily_Id(id);
    }

    public void saveAll(Iterable<EquipmentInfo> iterable){
        equipmentInfoDao.saveAll(iterable);
    }
}
