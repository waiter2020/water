package com.example.water.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by  waiter on 18-8-21  下午10:20.
 *
 * @author waiter
 */
@Service
public class JpushService {
    @Autowired
    private JPushClient jPushClient;



    @Async
    public  void sendToAppByFamilyId (String familyId, String title, String content) {

        PushPayload build = PushPayload.newBuilder().setPlatform(Platform.android())
                //向指定的组推送
                .setAudience(Audience.tag(familyId))
                .setNotification(Notification.android(content, title, null)).build();
        try {
            jPushClient.sendPush(build);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
