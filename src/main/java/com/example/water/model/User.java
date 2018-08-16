package com.example.water.model;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Entity
@Table(name = "user")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private String phoneNumber;
    private String email;
    private String passwd;
    /**
     *     生日
     */
    private Date birth;
    private boolean enabled;
    /**
     * 标明是哪个家庭
     */
    @ManyToOne
    private Family family;
    /**
     * 权限列表
     */
    @OneToMany(targetEntity = Role.class,cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Role> authorities ;


    public User(){}

    public User(String username,String email,String phoneNumber, String passwd, boolean enabled) {
        this.username = username;
        this.phoneNumber=phoneNumber;
        this.passwd = passwd;
        this.enabled = enabled;
        this.email=email;
    }

    public User(String username,String phoneNumber,String email, String passwd, boolean enabled, List<Role> authorities) {
        this.username = username;
        this.phoneNumber=phoneNumber;
        this.passwd = passwd;
        this.enabled=enabled;
        this.authorities=authorities;
        this.email=email;
    }

    @Override
    public Collection<Role> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwd;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Date getBirth() {
        return (Date) birth.clone();
    }

    public void setBirth(Date birth) {
        if(birth!=null) {
            this.birth = (Date) birth.clone();
        }else {
            this.birth=null;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", passwd='" + passwd + '\'' +
                ", birth=" + birth +
                ", enabled=" + enabled +
                ", family=" + family +
                ", authorities=" + authorities +
                '}';
    }
}
