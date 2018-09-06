package com.example.water.socket;

import com.example.water.WaterApplication;
import com.example.water.model.EquipmentInfo;
import com.example.water.model.Log;
import com.example.water.service.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by  waiter on 18-7-26  下午2:59.
 *
 * @author waiter
 */
@Async
@org.springframework.stereotype.Service
@Transactional(rollbackFor = Exception.class)
public class Service {
    @Autowired
    private OnlineDevices onlineDevices;
    @Autowired
    private EquipmentInfoService equipmentInfoService;
    @Autowired
    private WaterConditionService waterConditionService;
    @Autowired
    private LogService logService;
    @Autowired
    private MailClientService mailClientService;
    @Autowired
    private JpushService jpushService;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    private final char c1 = 0x0D;
    private final char c2 = 0x0A;

    private final String ends = "" + c1 + c2;

    public void initDevice(ChannelHandlerContext ctx, String msg) {
        String equipId = msg.substring(1, 4);
        log.info("equipId:" + equipId);
        onlineDevices.addDevice(ctx.channel());
        logService.save(new Log(equipId,"连接",new Date()));
        log.info(onlineDevices.getSize() + "");

        respond(ctx.channel(), equipId);

        EquipmentInfo equipmentByEquipId = equipmentInfoService.getEquipmentByEquipId(equipId);
        if (equipmentByEquipId == null) {
            equipmentByEquipId = new EquipmentInfo();
            equipmentByEquipId.setName("未命名");
            equipmentByEquipId.setEquipId(equipId);
            equipmentByEquipId.setOpen(false);
            equipmentByEquipId.setModel(0);
            equipmentByEquipId.setEndStateTime(new Date());
            equipmentByEquipId.setThresholdType(0);
            equipmentByEquipId.setThresholdValue(1000);
        }
        equipmentByEquipId.setOnline(true);
        equipmentByEquipId.setLoginId(ctx.channel().id().asLongText());
        equipmentByEquipId.setLowQuantityOfElectricity(false);

        equipmentByEquipId.setEquipState(5);


        equipmentInfoService.save(equipmentByEquipId);


        /**
         * 设置阀门开关
         */
        openOrClose(ctx.channel().id().asLongText(),equipId,equipmentByEquipId.isOpen());
        /**
         * 设置阈值
         */
        setThresholdValue(ctx.channel().id().asLongText(),equipId,equipmentByEquipId.getThresholdValue()+"",equipmentByEquipId.getThresholdType());
        /**
         * 设置检漏，模式
         */
        setModel(ctx.channel().id().asLongText(),equipId,equipmentByEquipId.getModel());
        /**
         * 设置时间
         */
        setTime(ctx.channel(), equipId);
        /**
         * 请求上传数据
         */
        getData(ctx.channel().id().asLongText(),equipId);
    }



    public void logout(Channel channel) {
        EquipmentInfo byLoginId = equipmentInfoService.findByLoginId(channel.id().asLongText());
        byLoginId.setLoginId(null);
        byLoginId.setOnline(false);
        logService.save(new Log(byLoginId.getEquipId(),"断开连接",new Date()));
        onlineDevices.removeDevice(channel.id().asLongText());
        equipmentInfoService.save(byLoginId);
        mailClientService.sendWarnMessage(byLoginId,"漏控仪断开连接");
    }


    public void respond(Channel channel, String equipId) {
        equipId = String.format("%0" + 3 + "d", Integer.parseInt(equipId) );
        channel.writeAndFlush("z" + equipId + "b" + ends);
    }


    public void setTime(Channel channel, String equipId) {
        String format = simpleDateFormat.format(new Date());
        channel.writeAndFlush("z" + equipId + "a" + format + ends);
    }

    public void deviceData(Channel channel, String msg) {
        EquipmentInfo byLoginId = equipmentInfoService.findByLoginId(channel.id().asLongText());
        switch (msg.charAt(0)) {
            case '0':
                logService.save(new Log(byLoginId.getEquipId(),"开始用水",new Date()));
                byLoginId.setEquipState(0);
                byLoginId.setOpen(true);
                try {
                    byLoginId.setEndStateTime(simpleDateFormat.parse(msg.substring(1, 15)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            /**
             * 小漏
             */
            case '1':
                logService.save(new Log(byLoginId.getEquipId(),"小漏失",new Date()));
                byLoginId.setEquipState(1);
                byLoginId.setOpen(false);
                openOrClose(byLoginId.getLoginId(),byLoginId.getEquipId()+"",byLoginId.isOpen());
                try {
                    byLoginId.setEndStateTime(simpleDateFormat.parse(msg.substring(1, 15)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mailClientService.sendWarnMessage(byLoginId,"漏控仪检测到小漏失");
                jpushService.sendToAppByFamilyId(byLoginId.getFamily().getId()+"","漏失提醒","检测到漏控仪："+byLoginId.getEquipId()+";名称："+byLoginId.getName()+"发生小漏失");
                break;
            /**
             * 大漏
             */
            case '2':
                logService.save(new Log(byLoginId.getEquipId(),"大漏失",new Date()));
                byLoginId.setEquipState(2);
                byLoginId.setOpen(false);
                openOrClose(byLoginId.getLoginId(),byLoginId.getEquipId()+"",byLoginId.isOpen());
                try {
                    byLoginId.setEndStateTime(simpleDateFormat.parse(msg.substring(1, 15)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mailClientService.sendWarnMessage(byLoginId,"漏控仪检测到大漏失");
                jpushService.sendToAppByFamilyId(byLoginId.getFamily().getId()+"","漏失提醒","检测到漏控仪："+byLoginId.getEquipId()+";名称："+byLoginId.getName()+"发生大漏失");

                break;
            /**
             * 低电量
             */
            case '4':
                logService.save(new Log(byLoginId.getEquipId(),"低电量",new Date()));
                byLoginId.setLowQuantityOfElectricity(true);
                byLoginId.setEndStateTime(new Date());
                mailClientService.sendWarnMessage(byLoginId,"漏控仪电量低");
                break;
            case '5':
                logService.save(new Log(byLoginId.getEquipId(),"停止用水",new Date()));
                if (0 == byLoginId.getEquipState()) {
                    double volumn = Integer.parseInt(msg.substring(15, 20));
                    Date endTime = null;
                    try {
                        endTime = simpleDateFormat.parse(msg.substring(1, 15));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    byLoginId.setWaterUsage(byLoginId.getWaterUsage()+volumn/100);
                    waterConditionService.saveWaterInfo(byLoginId.getEquipId(), byLoginId.getEndStateTime(), endTime, volumn / 100);
                }
                byLoginId.setEquipState(5);
                byLoginId.setEndStateTime(new Date());
                break;

            default:
                break;
        }
        respond(channel, byLoginId.getEquipId() + "");
        equipmentInfoService.save(byLoginId);
    }

    /**
     * 开关关水
     */
    public void openOrClose(String loginId, String equipId, boolean f) {
        Channel device = onlineDevices.getDevice(loginId);
        equipId = String.format("%0" + 3 + "d", Integer.parseInt(equipId) );
        if (f) {
            device.writeAndFlush("z" + equipId + "0" + ends);
        } else {
            device.writeAndFlush("z" + equipId + "9" + ends);
        }
    }

    /**
     * 漏失重启
     */
    public void reStart(String loginId, String equipId) {
        Channel device = onlineDevices.getDevice(loginId);
        equipId = String.format("%0" + 3 + "d", Integer.parseInt(equipId) );
        device.writeAndFlush("z" + equipId + "q" + ends);
    }

    /**
     * 控制流量阈值和时间阈值的转换
     *
     * @param loginId
     * @param equipId
     */
    public void changeThresholdType(String loginId, String equipId) {
        Channel device = onlineDevices.getDevice(loginId);
        equipId = String.format("%0" + 3 + "d", Integer.parseInt(equipId) );
        device.writeAndFlush("z" + equipId + "n" + ends);
    }

    /**
     * 设置阈值大小
     * @param loginId
     * @param equipId
     * @param value
     * @param type
     */
    public void setThresholdValue(String loginId, String equipId, String value, int type) {
        equipId = String.format("%0" + 3 + "d", Integer.parseInt(equipId) );
        value = String.format("%0" + 4 + "d", Integer.parseInt(value) );
        Channel device = onlineDevices.getDevice(loginId);
        /**
         * 0为流量1为时间
         */
        if (0 == type) {
            device.writeAndFlush("z" + equipId + "x" + value + ends);
        } else if (1 == type) {
            device.writeAndFlush("z" + equipId + "s" + value + ends);
        }
    }

    /**
     * 设置检漏模式
     * @param loginId
     * @param equipId
     * @param model
     */
    public void setModel(String loginId, String equipId,int model){
        Channel device = onlineDevices.getDevice(loginId);
        device.writeAndFlush("z"+equipId+"m0"+model+ends);
    }

    /**
     * 请求上传信息
     * @param loginId
     * @param equipId
     */
    public void getData(String loginId, String equipId){
        Channel device = onlineDevices.getDevice(loginId);
        equipId = String.format("%0" + 3 + "d", Integer.parseInt(equipId) );
        device.writeAndFlush("z"+equipId+"d"+ends);
    }

}
