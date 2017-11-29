package net.tijos.gas.base;

/**
 * 采集器抽象类
 * @author Mars 
 *
 */
public abstract class Collector extends Module {
	


	public Collector(int id, String name) {
		super(id, name);
	}

	public abstract void start();
	public abstract void stop();
	
}
