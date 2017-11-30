package net.tijos.gas.mqtt;

import java.util.Formatter;

public class OneNetMqttService extends MqttService {
	
	//设备ID
	private String clientId = "21754979";
	//broker地址，OneNET固定IP为183.230.40.39
	private String broker = "tcp://183.230.40.39:6002";
	//产品ID
	private String name = "111150";
	//APIKey
	private String pass = "i=pM1kyJK7qK8qXgJOOL1KWvhKw=";

	@Override
	public String getBroker() {
		return broker;
	}

	@Override
	public String getClientId() {
		return clientId;
	}

	@Override
	public String getUserName() {
		return name;
	}

	@Override
	public String getPassword() {
		return pass;
	}

	@Override
	public boolean isAutomaticReconnect() {
		return true;
	}
	
	public static String format(String format, Object... args) {
	       return new Formatter().format(format, args).toString();
	    }

	
}
