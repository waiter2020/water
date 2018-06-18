package com.example.water.model;/**
 * Created by waiter on 18-6-18.
 */



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
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String content;
    private String sender;
    private String sendDate;
    Message(){}

    public Message(String content, String sender, String sendDate) {
        this.content = content;
        this.sender = sender;
        this.sendDate = sendDate;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", sendDate=" + sendDate +
                '}';
    }
}
