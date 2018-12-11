package com.example.water.auth.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Data
public class JwtAuthenticationResponse implements Serializable {
    String token;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }
}
