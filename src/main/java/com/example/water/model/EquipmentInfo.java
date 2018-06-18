package com.example.water.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Entity
@Data
public class EquipmentInfo {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private int equipId;
	private double locLongitude;
	private double locLatitude;
	private int waterUsage;
	private int equipState;
	
	public EquipmentInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

    public EquipmentInfo(int equipId, double locLongitude, double locLatitude, int waterUsage, int equipState) {
        this.equipId = equipId;
        this.locLongitude = locLongitude;
        this.locLatitude = locLatitude;
        this.waterUsage = waterUsage;
        this.equipState = equipState;
    }

    public EquipmentInfo(int equipId, int waterUsage, int equipState) {
        this.equipId = equipId;
        this.waterUsage = waterUsage;
        this.equipState = equipState;
    }
    

    @Override
	public String toString() {
		return "EquipmentInfo [equip_id=" + equipId + ", loc_longitude="
				+ locLongitude + ", loc_latitude=" + locLatitude
				+ ", water_usage=" + waterUsage + ", equipment_state="
				+ equipState + "]";
	}
	
	
	

}
