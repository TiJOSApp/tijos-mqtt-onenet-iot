package net.tijos.gas.base;

import java.io.IOException;

/**
 * 开关抽象类
 * @author Mars 
 *
 */
public interface Switch {
	
	public void on() throws IOException;
	public void off() throws IOException;
	public boolean isOff();

}
