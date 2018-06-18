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
public class WaterCondition {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String watermeterId;
	
	private long startTime ;
	private long endTime;
	private int volumn;
	private long timeUse;
	public WaterCondition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WaterCondition(String watermeterId, long startTime, long endTime, int volumn, long timeUse) {
		this.watermeterId = watermeterId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.volumn = volumn;
		this.timeUse = timeUse;
	}

	@Override
	public String toString() {
		return "WaterCondition [watermeter_id=" + watermeterId
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", volumn=" + volumn + ", time_use=" + timeUse + "]";
	}
	
	
	
	

}
