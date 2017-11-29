package net.tijos.gas;

import net.tijos.gas.base.GPIO;
import net.tijos.gas.base.modules.SmokeDetector;
import tijos.framework.devicecenter.TiADC;
import tijos.framework.devicecenter.TiGPIO;
import tijos.framework.sensor.gas.ITiMQEventListener;
import tijos.framework.sensor.gas.TiMQ;

/**
 * 烟雾传感器MQ2实体类
 * @author Mars 
 *
 */
public class MQ2 extends SmokeDetector implements ITiMQEventListener {
	
	
	private TiMQ mq;
	private AlarmListener l;
	private long initTime;

	protected MQ2(String name, TiGPIO gpio, GPIO.PIN pin) {
		super(0, name);
		
		mq = new TiMQ(gpio, pin.getPinId());
	}
	
	protected MQ2(String name, TiGPIO gpio, GPIO.PIN pin, TiADC adc) {
		super(0, name);
		
		mq = new TiMQ(gpio, pin.getPinId(), adc);

		
	}

	@Override
	public boolean isReady() {
				
		if (System.currentTimeMillis() - initTime > 5000) {
			return true;
		}
		return false;
	}

	@Override
	public void start() {
		mq.setEventListener(this);
		initTime = System.currentTimeMillis();
	}

	@Override
	public void stop() {

	}

	@Override
	public void setAlarmListener(AlarmListener l) {
		this.l = l;
	}

	@Override
	public boolean isAlarm() {
		return mq.getAnalogVoltage() > 3.5;
	}

	@Override
	public void OnAlarm(TiMQ mq2) {
		if (l != null) {
			l.onAlarm(this);
		}
	}

	@Override
	public void OnRecovery(TiMQ mq2) {
		if (l != null) {
			l.onRecovery(this);
		}
		
	}

}
