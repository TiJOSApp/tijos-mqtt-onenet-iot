package net.tijos.relay;

import tijos.framework.devicecenter.TiGPIO;
import tijos.framework.net.mqtt.MqttClient;
import tijos.framework.net.mqtt.MqttClientListener;
import tijos.framework.net.mqtt.MqttConnectOptions;
import tijos.framework.networkcenter.TiDNS;
import tijos.framework.networkcenter.TiWLAN;
import tijos.framework.sensor.humiture.TiDHT;
import tijos.util.json.JSONObject;

public class Main implements MqttClientListener {
	
	private MqttClient mqttClient;
	
	private String clientId = "21754979";
	private String broker = "tcp://183.230.40.39:6002";;
	private String name = "111150";
	private String pass = "i=pM1kyJK7qK8qXgJOOL1KWvhKw=";
	private String topic = "/dht/data";
	
	private TiDHT dht11;
	
	public void start() {
		
		TiWLAN.getInstance().startup(10000);
		TiDNS.getInstance().startup();
		
		TiGPIO gpio = TiGPIO.open(0, 5);
		dht11 = new TiDHT(gpio, 5);
		dht11.measure();
		
		MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(name);
        connOpts.setPassword(pass);
        connOpts.setConnectionTimeout(30);
        connOpts.setCleanSession(false);
        //允许自动重新连接
        connOpts.setAutomaticReconnect(true);
        
        
        mqttClient = new MqttClient(broker, clientId);
		mqttClient.SetMqttClientListener(this);
		//连接MQTT服务器
        try {
			mqttClient.connect(connOpts, mqttClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        
        while (true) {
        	dht11.measure();
        	double temperature = dht11.getTemperature();
        	double humidity = dht11.getHumidity();
        	
        	JSONObject json = new JSONObject();
            json.put("Humidity", humidity);
            json.put("Temperature", temperature);
            byte[] payload = json.toString().getBytes();
        	try {
        		mqttClient.publish(topic, payload, 1, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
        	
        	try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {}
		}
        
        
        
	}


	@Override
	public void connectComplete(Object userContext, boolean reconnect) {
		System.out.println("connectComplete");
	}


	@Override
	public void connectionLost(Object userContext) {
		System.out.println("connectionLost");
	}


	@Override
	public void messageArrived(Object userContext, String topic, byte[] payload) {
		System.out.println("messageArrived");
	}


	@Override
	public void onMqttConnectFailure(Object userContext, int cause) {
		System.out.println("onMqttConnectFailure");
	}


	@Override
	public void onMqttConnectSuccess(Object userContext) {
		System.out.println("onMqttConnectSuccess");
	}


	@Override
	public void publishCompleted(Object userContext, int msgId, String topic, int result) {
		System.out.println("publishCompleted");
	}


	@Override
	public void subscribeCompleted(Object userContext, int msgId, String topic, int result) {
		System.out.println("subscribeCompleted");
	}


	@Override
	public void unsubscribeCompleted(Object userContext, int msgId, String topic, int result) {
		System.out.println("unsubscribeCompleted");
	}
	
	
	
	
	
	
	
	public static void main(String[] args) {
		new Main().start();
		
	}

}
