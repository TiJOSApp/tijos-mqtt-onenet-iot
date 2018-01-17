package net.tijos.gas;

import java.io.IOException;

import net.tijos.gas.base.GPIO;
import net.tijos.gas.base.Module;
import net.tijos.gas.base.Switch;
import tijos.framework.devicecenter.TiGPIO;
import tijos.framework.transducer.relay.TiRelay1CH;

/**
 * 继电器实体类
 * @author Mars 
 *
 */
public class Relay extends Module implements Switch {
	private TiRelay1CH relay;
	protected Relay(String name, TiGPIO gpio, GPIO.PIN pin) throws IOException {
		super(0, name);
		
		relay = new TiRelay1CH(gpio, pin.getPinId(), true);
		relay.turnOff();
		
	}

	@Override
	public void on() throws IOException {
		relay.turnOn();
	}

	@Override
	public void off() throws IOException {
		relay.turnOff();
	}

	@Override
	public boolean isOff() {
		return !relay.isTurnedOn();
	}

}
