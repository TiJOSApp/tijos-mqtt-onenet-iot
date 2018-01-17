package net.tijos.gas.base;

import java.io.IOException;

/**
 * 采集器抽象类
 * @author Mars 
 *
 */
public abstract class Collector extends Module {
	


	public Collector(int id, String name) {
		super(id, name);
	}

	public abstract void start() throws IOException;
	public abstract void stop();
	
}
