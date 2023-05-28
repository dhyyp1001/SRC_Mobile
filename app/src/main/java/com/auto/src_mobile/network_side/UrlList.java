package com.auto.src_mobile.network_side;

public class UrlList {
    //로컬에서는 IP 변경하기
    public String onvifControllerUrl(){ return "http://192.168.0.2:8080/java_onvif_web/OnvifController";}
    public String controllerWebPageUrl(){ return "http://192.168.0.2:8080/java_onvif_web/";}
    public String modbusConnectionUrl(){ return "http://192.168.0.2:8080/java_onvif_web/ModbusConnection";}
    public String networkUserConnectionUrl(){ return "http://192.168.0.2:8080/userCon";}
    public String networkOnvifControllerUrl(){ return "http://192.168.0.2:8080/onvifCont";}
}
