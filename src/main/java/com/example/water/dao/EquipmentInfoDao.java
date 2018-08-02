package com.example.water.dao;

import com.example.water.model.EquipmentInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
public interface EquipmentInfoDao extends CrudRepository<EquipmentInfo,Long> {
    /**
     * 根据设备id获取设备信息
     * @param equipId
     * @return
     */
    EquipmentInfo getFirstByEquipId(int equipId);

    /**
     * 通过家庭组id查找设备
     * @param id
     * @return
     */
    LinkedList<EquipmentInfo> findAllByFamily_Id(int id);

    EquipmentInfo findByLoginId(String loginId);
}
