package net.huansi.hsgmtapp.bean;

import huansi.net.qianjingapp.entity.WsData;

/**
 * Created by Tony on 2017/8/24.
 * 18:10
 */

public class ClassGroupBean extends WsData {
//    {
//        "SWORKTEAMNAME": "流水线5",
//            "SEMPLOYEENO": "320300006067",
//            "SEMPLOYEENAMECN": "戴疆博",
//            "SWORKTYPE": "",
//            "GPIC": ""
//    }
    public String SWORKTEAMNAME="";
    public String SEMPLOYEENO="";
    public String SEMPLOYEENAMECN="";
    public String SWORKTYPE="";
    public String GPIC="";
    public boolean isSelected;

    @Override
    public String toString() {
        return "ClassGroupBean{" +
                "SWORKTEAMNAME='" + SWORKTEAMNAME + '\'' +
                ", SEMPLOYEENO='" + SEMPLOYEENO + '\'' +
                ", SEMPLOYEENAMECN='" + SEMPLOYEENAMECN + '\'' +
                ", SWORKTYPE='" + SWORKTYPE + '\'' +
                ", GPIC='" + GPIC + '\'' +
                '}';
    }
}
