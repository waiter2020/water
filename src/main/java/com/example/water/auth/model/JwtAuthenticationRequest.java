package com.example.water.auth.model;


import lombok.Data;

import java.io.Serializable;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Data
public class  JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private String username;
    private String passwd;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPasswd(password);
    }

}