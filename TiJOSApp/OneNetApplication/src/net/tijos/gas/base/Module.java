package net.tijos.gas.base;

/**
 * 抽象模组超类
 * @author Mars
 *
 */
public abstract class Module {
	
	private int id;
	private String name;
	
	public Module(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

}
