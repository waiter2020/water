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

	private double volumn;
	private long timeUse;
	private Date startDate;
	private Date endDate;
	public WaterCondition() {

	}


	public WaterCondition(String watermeterId, double volumn, long timeUse, Date startDate, Date endDate) {
		this.watermeterId = watermeterId;
		this.volumn = volumn;
		this.timeUse = timeUse;
		this.startDate = (Date) startDate.clone();
		this.endDate = (Date) endDate.clone();
	}

	@Override
	public String toString() {
		return "WaterCondition [watermeter_id=" + watermeterId
				+ ", startTime=" + startDate + ", endTime=" + endDate
				+ ", volumn=" + volumn + ", time_use=" + timeUse + "]";
	}


	public Date getStartDate() {
		return (Date) startDate.clone();
	}

	public void setStartDate(Date startDate) {
		if(startDate!=null) {
			this.startDate = (Date) startDate.clone();
		}else {
			this.startDate=null;
		}
	}

	public Date getEndDate() {
		return (Date) endDate.clone();
	}

	public void setEndDate(Date endDate) {
		if(endDate!=null) {
			this.endDate = (Date) endDate.clone();
		}else {
			this.endDate=null;
		}
	}
}
