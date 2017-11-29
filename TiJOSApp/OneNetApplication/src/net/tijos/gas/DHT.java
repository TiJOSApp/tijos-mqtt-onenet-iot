package net.tijos.gas;

import net.tijos.gas.base.Collector;
import net.tijos.gas.base.GPIO;
import net.tijos.gas.base.Hygrometer;
import net.tijos.gas.base.Listener;
import net.tijos.gas.base.Thermometer;
import tijos.framework.devicecenter.TiGPIO;
import tijos.framework.sensor.humiture.TiDHT;

/**
 * ÎÂÊª¶È¼Æ
 * @author Mars 
 *
 */
public class DHT extends Collector implements Hygrometer, Thermometer, Runnable {
	
	private TiDHT dht11;
	private ChangedListener l;
	
	private Thread t;
	
	private double lastTemperature;
	private double lastHumidity;

	protected DHT(String name, TiGPIO gpio, GPIO.PIN pin) {
		super(0, name);
		dht11 = new TiDHT(gpio, pin.getPinId());
	}

	@Override
	public double getTemperature() {
		return lastTemperature;
	}

	@Override
	public double getHumidity() {
		return lastHumidity;
	}

	@Override
	public void start() {
		dht11.measure();
		
		lastTemperature = dht11.getTemperature();
		lastHumidity = dht11.getHumidity();
		
		t = new Thread(this);
		t.start();
		
	}

	@Override
	public void stop() {
		
	}
	
	
	public void setChangedListener(ChangedListener l) {
		this.l = l;
	}
	

	public interface ChangedListener extends Listener {
		void onHumidityChanged(Hygrometer hygrometer);
		void onTemperatureChanged(Thermometer thermometer);
	}
	

	@Override
	public void run() {
		while (true) {
			dht11.measure();
			
			if (l != null) {
				double temperature = dht11.getTemperature();
				double humidity =  dht11.getHumidity();
								
				if (temperature != lastTemperature) {
					lastTemperature = temperature;
					l.onTemperatureChanged(this);
				}
				 
				if (humidity != lastHumidity) {
					lastHumidity = humidity;
					l.onHumidityChanged(this);
				}
				
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {}
		}
		
	}

}
