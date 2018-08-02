package com.example.water.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private int equipId;
    private String description;
    private Date date;
    public Log(){}

    public Log(int equipId, String description, Date date) {
        this.equipId = equipId;
        this.description = description;
        this.date = date;
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
