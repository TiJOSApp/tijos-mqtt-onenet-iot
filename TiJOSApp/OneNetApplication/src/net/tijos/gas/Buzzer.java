package net.tijos.gas;

import java.io.IOException;

import net.tijos.gas.base.GPIO;
import net.tijos.gas.base.modules.Sound;
import tijos.framework.devicecenter.TiGPIO;
import tijos.framework.transducer.buzzer.TiBuzzer;

/**
 * ∑‰√˘∆˜ µÃÂ¿‡
 * @author Mars 
 *
 */
public class Buzzer extends Sound {
	
	private TiBuzzer buzzer;

	protected Buzzer(String name, TiGPIO gpio, GPIO.PIN pin) throws IOException {
		super(0, name);
		
		buzzer = new TiBuzzer(gpio, pin.getPinId(), false);
		buzzer.turnOff();
	}

	@Override
	public void play() throws IOException {
		buzzer.turnOn();
	}

	@Override
	public void release() throws IOException {
		buzzer.turnOff();
	}

	@Override
	public boolean isPlay() {
		return buzzer.isTurnedOn();
	}

}
