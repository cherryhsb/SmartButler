package com.ssc.smartbutler.entity;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.entity
 *  文件名：    ExpressData
 *  创建者：    SSC
 *  创建时间：   2018/7/31 7:29
 *  描述：     快递查询实体类
 */

public class ExpressData {

    //时间,状态,城市
    private String datetime;
    private String remark;
    private String zone;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "ExpressData{" +
                "datetime='" + datetime + '\'' +
                ", remark='" + remark + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}
