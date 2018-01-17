package net.tijos.gas;

import java.io.IOException;

import net.tijos.gas.base.GPIO;
import net.tijos.gas.base.modules.SmokeDetector;
import tijos.framework.devicecenter.TiADC;
import tijos.framework.devicecenter.TiGPIO;
import tijos.framework.sensor.mq.ITiMQEventListener;
import tijos.framework.sensor.mq.TiMQ;

/**
 * 烟雾传感器MQ2实体类
 * @author Mars 
 *
 */
public class MQ2 extends SmokeDetector implements ITiMQEventListener, Runnable {
	
	
	private TiMQ mq;
	private AlarmListener l;
	private long initTime;
	private volatile boolean cancel;
	private volatile boolean eventNotify;

	protected MQ2(String name, TiGPIO gpio, GPIO.PIN pin) throws IOException {
		super(0, name);
		
		mq = new TiMQ(gpio, pin.getPinId());
	}
	
	protected MQ2(String name, TiGPIO gpio, GPIO.PIN pin, TiADC adc) throws IOException {
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
	public void start() throws IOException {
		cancel();
		
		mq.setEventListener(this);
		initTime = System.currentTimeMillis();
		
		new Thread(this).start();
	}

	@Override
	public void stop() {
		cancel();
		try {
			mq.setEventListener(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setAlarmListener(AlarmListener l) {
		this.l = l;
	}

	@Override
	public boolean isAlarm() throws IOException {
		return mq.isGreaterThanThreshold();
	}

	@Override
	public synchronized void onThresholdNotify(TiMQ mq) {
		eventNotify = true;
		notify();
	}

	@Override
	public synchronized void run() {
		cancel = false;
		
		
		
		while (!cancel) {
			try {	
				while (true) {
					eventNotify = false;
					boolean threshold = mq.isGreaterThanThreshold();
					try {
						wait(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if (eventNotify) {
						continue;
					}
					
					if (mq.isGreaterThanThreshold() == threshold) {
						if (mq.isGreaterThanThreshold() == threshold) {
							l.onAlarm(this);
						}else {
							l.onRecovery(this);
						}
					}else {
						break;
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				
				continue;
			}
			
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private synchronized void cancel() {
		cancel = true;
		notifyAll();
	}

}
