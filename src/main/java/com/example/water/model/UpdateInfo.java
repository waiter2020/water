package com.example.water.model;

import lombok.Data;

/**
 * Created by  waiter on 18-8-31  下午2:03.
 *
 * @author waiter
 */
@Data
public class UpdateInfo {
    //app名字
    private String appname;
    //服务器版本
    private double serverVersion;
    //服务器标志
    private String serverFlag;
    //强制升级
    private String lastForce;
    //app最新版本地址
    private String updateUrl;
    //升级信息
    private String upgradeinfo;
    public UpdateInfo(){}
}
