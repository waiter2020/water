package com.example.water;

import com.example.water.server.ServerThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaterApplication.class, args);
        Thread thread = new Thread(new ServerThread());
        thread.start();
    }
}
