package net.tijos.gas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.tijos.gas.base.GPIO;
import net.tijos.gas.base.Module;
import net.tijos.gas.base.modules.Button;
import net.tijos.gas.base.modules.Display;
import net.tijos.gas.base.modules.SmokeDetector;
import net.tijos.gas.base.modules.Sound;
import tijos.framework.devicecenter.TiADC;
import tijos.framework.devicecenter.TiGPIO;
import tijos.framework.devicecenter.TiI2CMaster;

/**
 * 全局Context
 * @author Mars 
 *
 */
public class Context {
	
	private static Context context;
	
	public enum ModuleId {
		ResetButton("ResetButton"), 
		AlertDisplay("AlertDisplay"), 
		Buzzer("Buzzer"), 
		DHT("DHT"), 
		MQ2("MQ2"), 
		Relay("Relay"), 
		LED("LED");
		
		private String name;
		ModuleId(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
	}
	
	
	private List<Module> modules = new ArrayList<Module>();
	
	private Context() throws IOException {
		/*
		 * 定义使用的TiI2CMaster port和TiGPIO port
		 */
		int gpioPort0 = 0;
		int i2cPort0 = 0;
		int adcPort0 = 0;
		/*
		 * 定义所使用的gpio pin列表
		 * */
		int[] pinIDList = {GPIO.PIN.PIN2.getPinId(), GPIO.PIN.PIN3.getPinId(), GPIO.PIN.PIN4.getPinId(), GPIO.PIN.PIN5.getPinId(), GPIO.PIN.PIN6.getPinId(), GPIO.PIN.PIN7.getPinId()};
		/*
		 * 资源分配， 将i2cPort0分配给TiI2CMaster实例i2c0
		 * 将gpioPort0分配给TiGPIO实例gpio0
		 */
		TiI2CMaster i2c = TiI2CMaster.open(i2cPort0);
		TiGPIO gpio = TiGPIO.open(gpioPort0, pinIDList);
		TiADC adc = TiADC.open(adcPort0);

		/*
		 * 资源绑定 
		 * 创建TiOLED_UG2864实例oled并将i2c0与其绑定
		 * 创建TiRelay1CH实例relay并将gpioPinID2与其绑定
		 * 创建TiMQ2实例led并将gpioPinID3与其绑定
		 * 创建TiLED实例mq2并将gpioPinID4与其绑定
		 * 创建TiDHT实例dht11并将gpioPinID5与其绑定
		 * 创建TiBuzzer实例buzzer并将gpioPinID6与其绑定
		 * 创建TiButton实例button并将gpioPinID7与其绑定
		 */
		
		Display display = new AlertDisplay(ModuleId.AlertDisplay.getName(), i2c, 0x78);
		modules.add(display);
		
		Button button = new ResetButton(ModuleId.ResetButton.getName(), gpio, GPIO.PIN.PIN7);
		modules.add(button);
		
		Sound buzzer = new Buzzer(ModuleId.Buzzer.getName(), gpio, GPIO.PIN.PIN6);
		modules.add(buzzer);
		
		DHT dht = new DHT(ModuleId.DHT.getName(), gpio, GPIO.PIN.PIN5);
		modules.add(dht);
		
		SmokeDetector mq2 = new MQ2(ModuleId.MQ2.getName(), gpio, GPIO.PIN.PIN4, adc);
		modules.add(mq2);
		
		Led led = new Led(ModuleId.LED.getName(), gpio, GPIO.PIN.PIN3);
		modules.add(led);
		
		Relay relay = new Relay(ModuleId.Relay.getName(), gpio, GPIO.PIN.PIN2);
		modules.add(relay);
		
	}
	
	
	public static Context getContext() {
		if (context == null) {
			try {
				context = new Context();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return context;
	}
	
	public Module getModule(ModuleId id) {
		for (Module module : modules) {
			if (module.getName().equals(id.getName())) {
				return module;
			}
		}
		
		return null;
	}

}
