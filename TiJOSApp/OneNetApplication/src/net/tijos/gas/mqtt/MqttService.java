package net.tijos.gas.mqtt;

import java.io.IOException;

import net.tijos.gas.base.Listener;
import tijos.framework.net.mqtt.MqttClient;
import tijos.framework.net.mqtt.MqttClientListener;
import tijos.framework.net.mqtt.MqttConnectOptions;
import tijos.framework.net.mqtt.MqttException;

public abstract class MqttService implements MqttClientListener {
	
	
	public interface SubscribeListener extends Listener {
		void onSubscribe(String topic, String content);
	}
	
	
	private MqttClient mqttClient;
	private SubscribeListener listener;
	
	
	public abstract String getBroker();
	public abstract String getClientId();
	public abstract String getUserName();
	public abstract String getPassword();
	public abstract boolean isAutomaticReconnect();

	
	public void start() {
		MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(getUserName());
        connOpts.setPassword(getPassword());
        connOpts.setCleanSession(false);
        //允许自动重新连接
        connOpts.setAutomaticReconnect(isAutomaticReconnect());
        
		mqttClient = new MqttClient(getBroker(), getClientId());
		mqttClient.SetMqttClientListener(this);
		//连接MQTT服务器
        try {
			mqttClient.connect(connOpts, mqttClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void subscribe(String topic, SubscribeListener listener) throws MqttException {
		this.listener = listener;
		
		mqttClient.subscribe(topic, 1);
		
	}
	
	public void subscribe(String[] topics, SubscribeListener listener) throws MqttException {
		this.listener = listener;
		
		for (String topic : topics) {
			mqttClient.subscribe(topic, 1);
		}
		
	}
	
	public void publish(String topic, String content) throws IOException {
		System.err.println("publish: " + topic);
		
		mqttClient.publish(topic, content.getBytes(), 1, false);
	}
	
	
	public void publish(String topic, String content, boolean retained) throws IOException {
		System.err.println("publish: " + topic);
		
		mqttClient.publish(topic, content.getBytes(), 1, retained);
	}
	
	
	
	public void stop() {
		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void messageArrived(Object arg0, String topic, byte[] content) {
		if (listener != null) {
			listener.onSubscribe(topic, new String(content).trim());
		}
	}

	@Override
	public void publishCompleted(Object arg0, int arg1, String arg2, int arg3) {
		System.err.println("publishCompleted");
	}


	@Override
	public void subscribeCompleted(Object arg0, int arg1, String arg2, int arg3) {
		System.err.println("subscribeCompleted");
	}


	@Override
	public void unsubscribeCompleted(Object arg0, int arg1, String arg2, int arg3) {
		System.err.println("unsubscribeCompleted");
	}
	
	@Override
	public void connectComplete(Object arg0, boolean arg1) {
		System.err.println("connectComplete");
		
	}
	@Override
	public void connectionLost(Object arg0) {
		System.err.println("connectionLost");
	}
	@Override
	public void onMqttConnectFailure(Object arg0, int arg1) {
		System.err.println("onMqttConnectFailure");
	}
	@Override
	public void onMqttConnectSuccess(Object arg0) {
		System.err.println("onMqttConnectSuccess");
	}
	

}
