package net.huansi.hsgmtapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;


import net.huansi.hsgmtapp.R;
import net.huansi.hsgmtapp.event.BackgroundEvent;
import net.huansi.hsgmtapp.model.JsUploadSubmitText;
import net.huansi.hsgmtapp.view.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huansi.net.qianjingapp.entity.WsData;
import huansi.net.qianjingapp.entity.WsEntity;
import huansi.net.qianjingapp.utils.JSONEntity;

/**
 * Created by Administrator on 2016/6/19 0019.
 */
public class WsUtil {
//    private static WsUtil wsUtil;
//    private WsUtil(){}
//
//    public static WsUtil getInstance(){
//        if(wsUtil==null){
//            wsUtil=new WsUtil();
//        }
//        return wsUtil;
//    }

//    /**
//     * 返回1，表示采用getJsonData
//     * 返回2，表示采用getCustJsonData
//     */
//    public static int chooseWsFounction(String founctionCode,Context context){
//        String values=getJsonDataAsync(context,"spappGetAccessServer","iProjectId=" + Data.getCustomerProejctNo(context)+
//                ",sFunctionCode="+founctionCode);
//        if(values.isEmpty()||values.equalsIgnoreCase(context.getResources().getString(R.string.net_no_active))){
//            return -1;
//        }
//        WsData data= JSONEntity.GetWsData(values, JsonDataOrCustJsonData.class.getName());
//        if(data==null) return -1;
//        if(!data.SSTATUS.equalsIgnoreCase("0")) return -1;
//        List<WsEntity> entities=data.LISTWSDATA;
//        if(entities==null||entities.size()==0) return -1;
//        JsonDataOrCustJsonData jsonData= (JsonDataOrCustJsonData) entities.get(0);
//
//        return Integer.valueOf(jsonData.ISERVERTYPE);
//    }


//
//    /**
//     * 通过调用getJsonData的方法调用websrvice
//     */
//    public static String getCustJsonDataAsync(Context context,String str,String sParaStr){
//        if(NetUtil.getInstance().isNetworkAvailable(context)) {
//            WebServices webServices = new WebServices("HS",context);
//            Map<String, String> map = new HashMap<>();
//            map.put("iProjectId", Data.getCustomerProejctNo(context));
//            map.put("sCustCheckCode", Data.getCustomerCheckCode(context));
//            map.put("sSeparator", "");
//            map.put("str", str);
//            map.put("sParaStr", sParaStr);
//            return webServices.GetData("GetCustJsonData", map);
//        }else {
//            return context.getResources().getString(R.string.net_no_active);
//        }
//    }


    /**
     * 通过调用getJsonData的方法调用websrvice
     */
    public static String  getJsonDataAsync(Context context,String str,String sParaStr){
        if(NetUtil.getInstance().isNetworkAvailable(context)) {
            WebServices webServices = new WebServices(context);
            Map<String, String> map = new HashMap<>();
            map.put("str", str);
            map.put("sParaStr", sParaStr);
            return webServices.GetData("GetJsonData", map);
        }else {
            return context.getResources().getString(R.string.net_no_active);
        }
    }

    /**
     * 通过调用getJsonData的方法调用websrvice
     */
    public static String  getJsonDataAsyncByCustomer(Context context,String str,String sParaStr){
        if(NetUtil.getInstance().isNetworkAvailable(context)) {
            WebServices webServices = new WebServices(context);
            Map<String, String> map = new HashMap<>();
            map.put("str", str);
            map.put("sParaStr", sParaStr);
            return webServices.GetData("GetJsonData", map);
        }else {
            return context.getResources().getString(R.string.net_no_active);
        }
    }

//
//    /**
//     * 通过调用getJsonData的方法调用websrvice
//     */
//    public static String  getJsonDataAsyncByCustomer(Context context,String str,String sParaStr){
//        if(NetUtil.getInstance().isNetworkAvailable(context)) {
//            WebServices webServices = new WebServices(context);
//            Map<String, String> map = new HashMap<>();
//            map.put("str", str);
//            map.put("sParaStr", sParaStr);
//            return webServices.GetData("GetJsonData", map);
//        }else {
//            return context.getResources().getString(R.string.net_no_active);
//        }
//    }
//
//    /**
//     * 通过调用getJsonData的方法调用websrvice
//     */
//    public static String getCustJsonDataAsyncByCustomer(Context context,String str,String sParaStr){
//        if(NetUtil.getInstance().isNetworkAvailable(context)) {
//            WebServices webServices = new WebServices(context);
//            Map<String, String> map = new HashMap<>();
//            map.put("iProjectId", Data.getCustomerCheckCode(context));
//            map.put("sCustCheckCode", Data.getCustomerCheckCode(context));
//            map.put("sSeparator", "");
//            map.put("str", str);
//            map.put("sParaStr", sParaStr);
//            return webServices.GetData("GetCustJsonData", map);
//        }else {
//            return context.getResources().getString(R.string.net_no_active);
//        }
//    }

//    /**
//     * 采用getJsonData还是getCustJsonData
//     * @param functionCode
//     * @param str
//     * @param sParaStr
//     * @return
//     */
//    public static String getDataFromWs(int functionCode,String str,String sParaStr,Context context){
//        switch (functionCode) {
//            //getJsonData
//            case 1:
//                return getJsonDataAsync(context,str, sParaStr);
//            //getCustJsonData
//            case 0:
//                return getCustJsonDataAsync(context,str, sParaStr);
//            default:
//                return "";
//        }
//    }


    /**
     * 根据解析出来的数据是不是json数据
     * @param context
     * @param values
     * @return
     */
    public static String getErrorFromWs(Context context,String values){
        if(values.isEmpty()){
            return context.getResources().getString(R.string.connect_server_error);
        }
        if(values.equalsIgnoreCase(context.getResources().getString(R.string.net_no_active))){
            return values;
        }
        return "";
    }

    /**
     * 根据解析出来的WsData,来判断是不是取得正常的数据
     * @param context
     * @param wsData
     * @param isSearch 是查询数据还是提交数据
     * @return
     */
    public static String getErrorFromWs(Context context, WsData wsData, boolean isSearch){
        if(wsData==null){
            return  context.getResources().getString(R.string.connect_server_error);
        }
        if(!wsData.SSTATUS.equalsIgnoreCase("0")){
            return (wsData.SMESSAGE);
        }
        List<WsEntity> entities=wsData.LISTWSDATA;
        if(entities==null||entities.size()==0){
            if(isSearch){
                return "查询无结果";
            }
        }
        return "";
    }

    /**
     * 发送异步事件
     */
    public static void toAsync(int index,Class aClass){
        toAsync(index,aClass,null);
    }

    /**
     * 发送异步事件
     */
    public static void toAsync(int index,Class aClass,String str1){
        BackgroundEvent event=new BackgroundEvent();
        event.index=index;
        event.str1=str1;
        event.aClass=aClass;
        EventBus.getDefault().post(event);
    }

//    /**
//     * 发送异步事件
//     */
//    public static void toAsync(int index,Class aClass,boolean flag1){
//        AsyncEvent event=new AsyncEvent();
//        event.index=index;
//        event.aClass=aClass;
//        event.flag1=flag1;
//        EventBus.getDefault().post(event);
//    }
//
//    /**
//     * 发送异步事件
//     */
//    public static void toAsync(int index,Class aClass,int arg1){
//        AsyncEvent event=new AsyncEvent();
//        event.index=index;
//        event.arg1=arg1;
//        event.aClass=aClass;
//        EventBus.getDefault().post(event);
//    }
//
//
//    /**
//     * 发送异步事件
//     */
//    public static void toAsync(int index,Class aClass,int arg1,Object object){
//        AsyncEvent event=new AsyncEvent();
//        event.index=index;
//        event.arg1=arg1;
//        event.aClass=aClass;
//        event.object=object;
//        EventBus.getDefault().post(event);
//    }
//
//    /**
//     * 发送异步事件
//     */
//    public static void toAsync(int index,Class aClass,String str1){
//        AsyncEvent event=new AsyncEvent();
//        event.index=index;
//        event.str1=str1;
//        event.aClass=aClass;
//        EventBus.getDefault().post(event);
//    }
//
//    /**
//     * 发送异步事件
//     */
//    public static void toAsync(int index,Class aClass,String str1,int arg1){
//        AsyncEvent event=new AsyncEvent();
//        event.index=index;
//        event.str1=str1;
//        event.arg1=arg1;
//        event.aClass=aClass;
//        EventBus.getDefault().post(event);
//    }
//
//    /**
//     * 发送异步事件
//     */
//    public static void toAsync(int index,Class aClass,String str,Object o){
//        AsyncEvent event=new AsyncEvent();
//        event.index=index;
//        event.str1=str;
//        event.object=o;
//        event.aClass=aClass;
//        EventBus.getDefault().post(event);
//    }
//
//    /**
//     * 发送异步事件
//     */
//    public static void toAsync(int index,Class aClass,String str1,String str2){
//        AsyncEvent event=new AsyncEvent();
//        event.index=index;
//        event.str1=str1;
//        event.str2=str2;
//        event.aClass=aClass;
//        EventBus.getDefault().post(event);
//    }
//
//    public static void toAsync(int index,Class aClass,String str1,String str2,Object o){
//        AsyncEvent event=new AsyncEvent();
//        event.index=index;
//        event.aClass=aClass;
//        event.str1=str1;
//        event.str2=str2;
//        event.object=o;
//        EventBus.getDefault().post(event);
//    }
//
//
//    /**
//     * 发送异步事件
//     */
//    public static void toAsync(int index,Class aClass,String str1,String str2,String str3,String str4){
//        AsyncEvent event=new AsyncEvent();
//        event.index=index;
//        event.str1=str1;
//        event.str2=str2;
//        event.str3=str3;
//        event.str4=str4;
//        event.aClass=aClass;
//        EventBus.getDefault().post(event);
//    }

    /**
     * 发送附件头表的异步
     */
    public static  String sendTextAsync(String iWorkTeamId,String dataKey,String title,String content,String userNo,Context context){
        WebServices webServices=new WebServices(context);
        Map<String,String> map=new HashMap<>();
        map.put("iWorkTeamId",iWorkTeamId);
        map.put("sAppUserNo", userNo);
        map.put("sDataKey", dataKey);
        map.put("sTitle", title);
        map.put("sContents", content);
        String values = webServices.GetData("AttachHdrSubmit", map);
        return values;
    }


    /**
     * 发送附件头表的UI
     */
    public static Serializable sendTextMain(String values, Context context){
        String error="";
        error=getErrorFromWs(context,values);
        if(!error.isEmpty()) return error;
        WsData wsData= JSONEntity.GetWsData(values,JsUploadSubmitText.class.getName());
        error=getErrorFromWs(context,wsData,false);
        if(!error.isEmpty()) return error;
        JsUploadSubmitText jsUploadSubmitText= (JsUploadSubmitText) wsData.LISTWSDATA.get(0);
        return jsUploadSubmitText;
    }


    /**
    * 发送图片和附件的异步
    */
    public static String sendExAsync(List<String> mPathList,String iden,String dataType,Context context) throws IOException {
        WebServices webServices=new WebServices(context);
        String error="";
        for(int i=0;i<mPathList.size();i++){
            Map<String,String> map=new HashMap();
            map.put("iSeq",i+"");
            map.put("sAttachId",iden);
            map.put("sDataType",dataType);
            map.put("sLocalPicPath",mPathList.get(i));
            map.put("byteArray", OthersUtil.compressImage(BitmapCache.getInstance().getBitmap(mPathList.get(i))));
            String values=webServices.GetData("AttachDtlSubmit",map);
            error=WsUtil.getErrorFromWs(context,values);
            if (!error.isEmpty())return error;
            WsData data = JSONEntity.GetWsData(values, WsData.class.getName());
            error=WsUtil.getErrorFromWs(context,data,false);
            if (!error.isEmpty()) return error;
        }
        return error;
    }

    /**
     * 发送图片和附件的UI操作
     */
    public static void sendExMain(LoadProgressDialog dialog, Context context, StringBuffer sbError,
                                  String sendErrorShow, TextView tvSend, String successStr){
        if(dialog!=null)OthersUtil.dismissLoadDialog(dialog);
        if(!sbError.toString().isEmpty()) {
            OthersUtil.ToastMsg(context,sbError.toString());
            if(tvSend!=null) {
               if(sendErrorShow!=null&&!sendErrorShow.isEmpty()) tvSend.setText(sendErrorShow);
                tvSend.setTextColor(Color.parseColor("#FF0000"));
            }
            //全部上传成功
        }else {
            OthersUtil.ToastMsg(context,successStr);
        }
    }


}
