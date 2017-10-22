package net.huansi.hsgmtapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/5/3.
 */
public class NetUtil {
    private NetUtil(){}
    private static NetUtil netUtil;
    public static NetUtil getInstance(){
        if(netUtil==null){
            netUtil=new NetUtil();
        }
        return netUtil;
    }
    /**
     * 判断是否有网络连接
     * @return
     */
    public  boolean isNetworkAvailable(Context context) {
        boolean isActive=false;
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        if(networkInfo[i].getState() ==connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()){
                            Data.wificonnected=true;
                        }else {
                            Data.wificonnected=false;
                        }
                        isActive=true;
                        break;
                    }
                }
            }
        }
        return isActive;
    }

    /**
     * 通过正则判断是不是网址
     * @param url
     * @return
     */

    public static boolean isUrlByString(String url){
        if(url==null||url.isEmpty()) return false;
        Pattern pattern = Pattern
                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(url).matches();
    }
}

