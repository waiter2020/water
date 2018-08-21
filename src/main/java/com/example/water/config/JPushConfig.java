package com.example.water.config;

import cn.jpush.api.JPushClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by  waiter on 18-8-21  下午10:09.
 *
 * @author waiter
 */
@Configuration
@ConfigurationProperties(prefix = "water-app")
public class JPushConfig {
    private String masterSecret;
    private String appKey;

    public String getMasterSecret() {
        return masterSecret;
    }

    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }


    @Bean
    public JPushClient jPushClient(){
        return new JPushClient(masterSecret,appKey);
    }
}
