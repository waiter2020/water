package com.example.water;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class WaterApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(WaterApplication.class, args);
//        Thread thread = new Thread(new ServerThread());
//        thread.start();
//        server.startServer();
    }

    @Override
    public void run(String... strings) {
    }
}
