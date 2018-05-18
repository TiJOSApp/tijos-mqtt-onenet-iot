package net.tijos.app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import net.tijos.gas.Context;
import net.tijos.gas.DHT;
import net.tijos.gas.DHT.ChangedListener;
import net.tijos.gas.Led;
import net.tijos.gas.Relay;
import net.tijos.gas.base.Hygrometer;
import net.tijos.gas.base.Module;
import net.tijos.gas.base.Thermometer;
import net.tijos.gas.base.modules.Button;
import net.tijos.gas.base.modules.Display;
import net.tijos.gas.base.modules.SmokeDetector;
import net.tijos.gas.base.modules.SmokeDetector.AlarmListener;
import net.tijos.gas.mqtt.OneNetMqttService;
import net.tijos.gas.mqtt.MqttService;
import net.tijos.gas.mqtt.MqttService.SubscribeListener;
import tijos.framework.networkcenter.TiDNS;
import tijos.framework.networkcenter.TiWLAN;
import tijos.framework.networkcenter.ntp.NTPUDPClient;
import tijos.framework.networkcenter.ntp.TimeInfo;
import tijos.framework.util.json.JSONObject;
import net.tijos.gas.base.modules.Sound;
import net.tijos.gas.base.modules.Button.OnClickListener;
import net.tijos.gas.base.modules.Button.OnLongClickListener;

public class Main implements OnLongClickListener, OnClickListener, AlarmListener, ChangedListener, SubscribeListener {
	
	private static long timeOffset;
	
	private MqttService mqtt;
	
	private static final String PUB_TOPIC_ALARM		= "Alarm";
	private static final String PUB_TOPIC_TEMP		= "Temperature";
	private static final String PUB_TOPIC_HUMI		= "Humidity";
	private static final String SUB_TOPIC_OTA		= "OTA";
	private static final String SUB_TOPIC_ACTION	= "Action";

	
	private boolean init = false;
	
	private Led led;
	private Relay relay;
	private Sound buzzer;
	private DHT dht;
	private Display display;
	private SmokeDetector detector;
	private Button btn;
	
	
	
	
	
	public void start() throws IOException {
		startNetwork();
		
		timeOffset = getNdpOffset();
		
		Context context = Context.getContext();
		
		mqtt = new OneNetMqttService();
		mqtt.start();
		mqtt.subscribe(new String[]{SUB_TOPIC_OTA, SUB_TOPIC_ACTION}, this);

				
		
		display = (Display) context.getModule(Context.ModuleId.AlertDisplay);

		/**
		 * Welcome UI
		 */
		display.setPosition(1, 4);
		display.print(Messages.Welcome);
		display.setPosition(2, 0);
		display.print(Messages.Model);
		
		
		
		led = (Led) context.getModule(Context.ModuleId.LED);
		relay = (Relay) context.getModule(Context.ModuleId.Relay);
		buzzer = (Sound) context.getModule(Context.ModuleId.Buzzer);
		
		dht = (DHT) context.getModule(Context.ModuleId.DHT);
		dht.setChangedListener(this);
		dht.start();
		
		detector = (SmokeDetector) context.getModule(Context.ModuleId.MQ2);
		detector.setAlarmListener(this);
		detector.start();
		
		
		btn = (Button) context.getModule(Context.ModuleId.ResetButton);
		btn.setOnClickListener(this);
		btn.setOnLongClickListener(this);
		
		
		
		while (!detector.isReady()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
		String temp = String.valueOf(dht.getTemperature());
		String humi = String.valueOf(dht.getHumidity());

		display.clear();
		display.println(Messages.Temptrue);
		display.println(Messages.Humidity);
		display.println(Messages.Mode);
		display.setPosition(3, 3);
		display.print(Messages.Prepare);
		
		
		display.setPosition(0, 5);
		display.print(temp + "C ");
		display.setPosition(1, 5);
		display.print(humi + "% ");
		
		init = true;
		
		if (detector.isAlarm()) {
			onAlarm(null);
			
		}else {
			onRecovery(null);
		}
		
	}
	



	@Override
	public void onLongClick(Module m) {
		
	}



	@Override
	public void onClick(Module m) {
		try {
			buzzer.release();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public void onAlarm(Module m) {
		System.err.println("onAlarm");
		try {
			buzzer.play();
			led.on();
			relay.off();
			display.setPosition(2, 5);
			display.print(Messages.Alarm);
			display.setPosition(3, 3);
			display.print("WARNING!!! ");
			
	        JSONObject json = new JSONObject();
	        json.put("requestId", UUID.randomUUID().toString());
	        json.put("Alarm", true);
	        json.put("Timer", System.currentTimeMillis() + timeOffset);
	        
	        System.err.println(json.toString());  
        
        
			mqtt.publish(PUB_TOPIC_ALARM, json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}



	@Override
	public void onRecovery(Module m) {
		System.err.println("onRecovery");
		
		try {
			buzzer.release();
			led.off();
			relay.on();
			display.setPosition(2, 5);
			display.print("Safe ");
			display.setPosition(3, 3);
			display.print("WORKING ^_^");
			
			JSONObject json = new JSONObject();
	        json.put("requestId", UUID.randomUUID().toString());
	        json.put("Alarm", false);
	        json.put("Timer", System.currentTimeMillis() + timeOffset);
	        
	        System.err.println(json.toString());  
        
        
			mqtt.publish(PUB_TOPIC_ALARM, json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws IOException {
		
		new Main().start();
		
		while (true) {
			try {
				Thread.sleep(1000000);
			} catch (InterruptedException e) {}
		}
	}




	@Override
	public void onHumidityChanged(Hygrometer hygrometer) {
		if (init) {
			double humi = hygrometer.getHumidity();
			display.setPosition(1, 5);
			display.print(humi + "% ");
			
	        JSONObject json = new JSONObject();
	        
	        json.put("requestId", UUID.randomUUID().toString());
	        json.put("Humidity", humi);
	        json.put("Timer", System.currentTimeMillis() + timeOffset);
	          
	        System.err.println(json.toString());  
	        
	        
	        try {
				mqtt.publish(PUB_TOPIC_HUMI, json.toString(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	
	
	@Override
	public void onTemperatureChanged(Thermometer thermometer) {
		
		if (init) {
			double temp = thermometer.getTemperature();
			display.setPosition(0, 5);
			display.print(temp + "C ");
			
	        JSONObject json = new JSONObject();
	        json.put("requestId", UUID.randomUUID().toString());
	        json.put("Temperature", temp);
	        json.put("Timer", System.currentTimeMillis() + timeOffset);
	        
	        System.err.println(json.toString());  
	        
	        try {
				mqtt.publish(PUB_TOPIC_TEMP, json.toString(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void startNetwork() throws IOException {
		//Æô¶¯WLAN¼°DNS
		TiWLAN.getInstance().startup(10);
		TiDNS.getInstance().startup();
	}
	
	public static long getNdpOffset() throws IOException {
		NTPUDPClient ntpcli = new NTPUDPClient();
		long interval = 0;
		try {
			InetAddress host = InetAddress.getByName("202.112.29.82"); //NTP Server IP, get it from http://ntp.org.cn/
			TimeInfo tm = ntpcli.getTime(host);
			interval = tm.getOffset();
		} catch (UnknownHostException e) {
			 
			e.printStackTrace();
		}
		
		return interval;
	}

	@Override
	public void onSubscribe(String topic, String content) {
		System.err.println(topic + " - " + content);
		if (SUB_TOPIC_ACTION.equals(topic)) {
			JSONObject json = new JSONObject(content);
			String action = json.getString("Buzzer");
			if ("off".equals(action)) {
				try {
					buzzer.release();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
