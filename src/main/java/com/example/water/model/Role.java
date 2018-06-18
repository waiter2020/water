package com.example.water.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 *
 * 用户权限
 */
@Entity
@Table(name = "role")
@Data
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id ;
    private String role;
    Role(){}

    public Role(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }


}
