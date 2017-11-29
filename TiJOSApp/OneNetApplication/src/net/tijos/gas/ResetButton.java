package net.tijos.gas;

import net.tijos.gas.base.GPIO;
import net.tijos.gas.base.modules.Button;
import tijos.framework.devicecenter.TiGPIO;
import tijos.framework.sensor.button.ITiButtonEventListener;
import tijos.framework.sensor.button.TiButton;

public class ResetButton extends Button implements ITiButtonEventListener {
	
	private TiButton btn;
	private boolean enabled = true;
	private volatile boolean isClick = false;
	private OnClickListener clickListener;
	private OnLongClickListener longClickListener;
	
	private static long onLongClickInterval = 3000;
	
	private volatile long lastPressed;
	private volatile long lastReleased;
	
	private boolean notified = false;
	
	
	private Thread t;
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {			
			while(true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				
				if (isClickable() && !notified) {
					
					if ((System.currentTimeMillis() - lastPressed/1000) > onLongClickInterval) {						
						notified = true;
						longClickListener.onLongClick(ResetButton.this);
						
					}
				}
			}
		}
	};

	protected ResetButton(String name, TiGPIO gpio, GPIO.PIN pin) {
		super(0, name);
		
		btn = new TiButton(gpio, pin.getPinId());
		btn.setEventListener(this);
	
	
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.clickListener = l;
	}
	
	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		this.longClickListener = l;
		if (l != null) {
			if (t == null) {
				t = new Thread(runnable);
				t.start();
			}
			
		}
		
	}
	@Override
	public void onPressed(TiButton btn) {
		isClick = true;
		notified = false;
		
		lastPressed = btn.getEventTime();
		
		if (clickListener != null) {
			clickListener.onClick(this);
		}
		
		
		
		
	}
	@Override
	public void onReleased(TiButton btn) {
		lastReleased = btn.getEventTime();
		
		isClick = false;
	}
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public boolean isClickable() {
		return isClick;
	}
}
