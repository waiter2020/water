package com.example.water.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Entity
@Data
@Table(name = "equipment_info")
public class EquipmentInfo {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String equipId;
	private String name;
    /**
     * 阈门开关
     */
	private boolean isOpen;
    /**
     * 阈值类型
     */
	private int thresholdType;
    /**
     * 阈值大小
     */
	private int thresholdValue;
    /**
     * 检漏模式
     */
	private int model;
    /**
     * 是否在线
     */
    private boolean online;
    /**
     * 连接id
     */
    private String loginId;
	private double locLongitude;
	private double locLatitude;
	private double waterUsage;
	private int equipState;
    /**
     * 最后一个状态的时间
     */
	private Date endStateTime;

    private boolean lowQuantityOfElectricity;
    //是否已强制禁止用水
    @Column(name = "is_lock")
    private Boolean lock;

	/**
	 * 标明此表属于哪个家庭
	 */
	@ManyToOne
	private Family family;
	public EquipmentInfo() {
		super();
	}

    public EquipmentInfo(String equipId, double locLongitude, double locLatitude, int waterUsage, int equipState) {
        this.equipId = equipId;
        this.locLongitude = locLongitude;
        this.locLatitude = locLatitude;
        this.waterUsage = waterUsage;
        this.equipState = equipState;
    }

    public EquipmentInfo(String equipId, int waterUsage, int equipState) {
        this.equipId = equipId;
        this.waterUsage = waterUsage;
        this.equipState = equipState;
    }

    @Override
    public String toString() {
        return "EquipmentInfo{" +
                "id=" + id +
                ", equipId='" + equipId + '\'' +
                ", name='" + name + '\'' +
                ", isOpen=" + isOpen +
                ", thresholdType=" + thresholdType +
                ", thresholdValue=" + thresholdValue +
                ", model=" + model +
                ", online=" + online +
                ", loginId='" + loginId + '\'' +
                ", locLongitude=" + locLongitude +
                ", locLatitude=" + locLatitude +
                ", waterUsage=" + waterUsage +
                ", equipState=" + equipState +
                ", endStateTime=" + endStateTime +
                ", lowQuantityOfElectricity=" + lowQuantityOfElectricity +
                ", family=" + family +
                '}';
    }
}
