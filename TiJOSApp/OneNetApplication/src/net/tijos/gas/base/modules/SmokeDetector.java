package net.tijos.gas.base.modules;

import java.io.IOException;

import net.tijos.gas.base.Collector;
import net.tijos.gas.base.Listener;
import net.tijos.gas.base.Module;

/**
 * ÑÌÎí±¨¾¯Æ÷³éÏóÀà
 * @author Mars 
 *
 */
public abstract class SmokeDetector extends Collector {
	
	
	public interface AlarmListener extends Listener {
		void onAlarm(Module m);
		void onRecovery(Module m);
	}
	

	public SmokeDetector(int id, String name) {
		super(id, name);
	}
	
	public abstract boolean isReady();
	
	public abstract void setAlarmListener(AlarmListener l) throws IOException;
	public abstract boolean isAlarm() throws IOException;
	
}
