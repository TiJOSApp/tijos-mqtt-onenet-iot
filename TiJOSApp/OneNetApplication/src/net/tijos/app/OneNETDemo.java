package net.tijos.app;

import java.io.IOException;

import tijos.framework.networkcenter.onenet.IOneNetMqttEventListener;
import tijos.framework.networkcenter.onenet.OneJSONProperty;
import tijos.framework.networkcenter.onenet.OneNetMqttClient;
import tijos.framework.platform.lte.TiLTE;
import tijos.framework.util.Delay;
import tijos.framework.util.json.JSONObject;

class OnenetEventListner implements IOneNetMqttEventListener {

	OneNetMqttClient onenet;
	
	public OnenetEventListner(OneNetMqttClient onenet)
	{
		this.onenet = onenet;
	}
	
	@Override
	public void onMqttConnected() {
		// TODO Auto-generated method stub
		System.out.println("onMqttConnected ");

	}

	@Override
	public void onMqttDisconnected(int error) {
		System.out.println("onMqttDisconnected " + error);

	}

	@Override
	public void onPropertyReportReply(String msgId, int code, String message) {
		System.out.println("onPropertyReportReply " + msgId + " " + code + " " + message);

	}

	@Override
	public void onEventReportReply(String msgId, int code, String message) {
		System.out.println("onEventReportReply " + msgId + " " + code + " " + message);

	}

	@Override
	public void onPropertySetArrived(String msgId, JSONObject params) {
		System.out.println("msgId " + msgId + " " + params.toString());

		try {
			onenet.propertySetReply(msgId, 200, "OK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onServiceInvokeArrived(String msgId, String serviceId, JSONObject params) {
		System.out.println("onServiceInvokeArrived " + "msgId " + msgId + " serviceId " + serviceId + " "
				+ params.toString());

		JSONObject response = new JSONObject();

		try {
			onenet.serviceInvokeReply(msgId, serviceId, 200, "OK", response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

public class OneNETDemo {

	public static void main(String[] args) {
		System.out.println("Start ...");
		try {
			TiLTE.getInstance().startup(30);
			
			//设备密钥信息	
			String productId = "xt0Tv3A1gs";
			//设备名称使用IMEI
			String deviceName = TiLTE.getInstance().getIMEI();
			//产品密钥
			String productKey = "T98fK6VXP1WzkxeHMYMl38Hl4Suf3XkXFYv30/ElYzs=";

			//OneNET Studio平台客户端
			OneNetMqttClient onenet = new OneNetMqttClient(productId, deviceName, productKey);

			//启动连接并设置事件监听
			onenet.connect(new OnenetEventListner(onenet));

			//上报当前设备地理位置， 需要在物模型中加入基站定位系统功能点$OneNET_LBS
			onenet.lbsCellLocationReport();

			//每10秒上报属性值
			OneJSONProperty property = new OneJSONProperty();
			while (true) {
			    //获取实际设备数据设置相应属性
			    
				property.setProperty("state", System.currentTimeMillis(), 1);			
			    onenet.propertyReport(property.toJSON());

			    Delay.msDelay(10000);
			}

			


		} catch (Exception ex) {
			ex.printStackTrace();
			
			
		}

	}

}
