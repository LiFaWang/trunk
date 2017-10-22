package net.huansi.hsgmtapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Map;

public class WebServices {
	//public String FServerAddr = "http://192.168.2.61";
	//public String FEndPoint = FServerAddr + "/APPWS/AppWS.asmx";

//	public String FServerAddr = "http://202.181.229.247";
//	public String FEndPoint = FServerAddr + "/MobileQA/AppWS.asmx";
	//HS服务器端的地址；

	public String FEndPoint = "";
	public String FNameSpace = "http://tempuri.org/";

	//---------------------------------------------------------------------------------------
	public WebServices(Context context) {
		try {
			FEndPoint = OthersUtil.getWebserviceUrl(context);
			FEndPoint = FEndPoint + "/hsgmtwebservice.asmx";
		}catch (Exception e) {

		}

	}


	public String GetData(String sFunctionName, Map<String, String> map) {
		// 命名空间
		String nameSpace = FNameSpace;
		// 调用的方法名称
		String methodName = sFunctionName;
		// EndPoint
		String endPoint = FEndPoint;
		// SOAP Action
		String soapAction = nameSpace + methodName;

		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
//	    rpc.addProperty("sCheckCode", FCheckCode);
//	    rpc.addProperty("sUserNo", sUserNo);
//	    rpc.addProperty("sPassword", sPassword);
		String sParmName = "";
		String sParaValue = "";
//		rpc.addProperty("sCheckCode", FCheckCode);
		if (map != null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				sParmName = entry.getKey();
				sParaValue = entry.getValue();
				rpc.addProperty(sParmName, sParaValue);
			}
		}
		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE transport = new HttpTransportSE(endPoint, 1000 * 60);
		try {
			// 调用WebService
			transport.call(soapAction, envelope);
			// 获取返回的数据
			SoapObject object = (SoapObject) envelope.bodyIn;
//			SoapObject object=envelope.sObject.getProperty("return");
//			SoapObject soReturn = (SoapObject)object.getProperty("return");
//			soReturn.getProperty("insurer").toString();
			String result = "";
			// 获取返回的结果
			if (object != null) {
				result = object.getProperty(0).toString();
			}
			return (result == null || result.isEmpty() || result.equals("anyType{}")) ? "" : result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}



}
