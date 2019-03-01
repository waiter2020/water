package com.example.water.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by  waiter on 18-8-2  下午12:38.
 *
 * @author waiter
 */
@Entity
@Data
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String equipId;
    private String description;
    private Date date;
    @Column(length = 2000)
    private String equipmentInfo;
    public Log(){}

    public Log(String equipId, String description, Date date) {
        this.equipId = equipId;
        this.description = description;
        this.date = date;
    }

    public Log(String equipId, String description, Date date, String equipmentInfo) {
        this.equipId = equipId;
        this.description = description;
        this.date = date;
        this.equipmentInfo = equipmentInfo;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", equipId=" + equipId +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
