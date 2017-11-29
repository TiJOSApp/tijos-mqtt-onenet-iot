package net.tijos.gas.mqtt;

import java.util.Formatter;

public class OneNetMqttService extends MqttService {
	
	private static final String CLIENT_ID = "21754979";
	private static final String BROKER = "tcp://183.230.40.39:6002";;
	private static final String USER_NAME = "111150";
	private static final String USER_PASS = "i=pM1kyJK7qK8qXgJOOL1KWvhKw=";
	
	

	@Override
	public String getBroker() {
		return BROKER;
	}

	@Override
	public String getClientId() {
		return CLIENT_ID;
	}

	@Override
	public String getUserName() {
		return USER_NAME;
	}

	@Override
	public String getPassword() {
		return USER_PASS;
	}

	@Override
	public boolean isAutomaticReconnect() {
		return true;
	}
	
	public static String format(String format, Object... args) {
	       return new Formatter().format(format, args).toString();
	    }

	
}
