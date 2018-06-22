package com.example.water.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by  waiter on 18-6-18.
 *
 * @author waiter
 */
@Entity
@Data
@Table(name = "family")
public class Family{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    /**
     * 家庭名称
     */
    private String familyName;


    public Family() {
    }


    @Override
    public String toString() {
        return "EquipmentInfo [id=" + id + ", FamilyName=" + familyName + "]";
    }

}
