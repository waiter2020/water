package com.example.water.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

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

	private int volumn;
	private long timeUse;
	private Date startDate;
	private Date endDate;
	public WaterCondition() {
		super();
		// TODO Auto-generated constructor stub
	}


	public WaterCondition(String watermeterId, int volumn, long timeUse, Date startDate, Date endDate) {
		this.watermeterId = watermeterId;
		this.volumn = volumn;
		this.timeUse = timeUse;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "WaterCondition [watermeter_id=" + watermeterId
				+ ", startTime=" + startDate + ", endTime=" + endDate
				+ ", volumn=" + volumn + ", time_use=" + timeUse + "]";
	}
	
	
	
	

}
