package net.tijos.gas.base.modules;

import net.tijos.gas.base.Module;
import net.tijos.gas.base.PrintWriter;

/**
 * ³éÏóÏÔÊ¾Æ÷
 * @author Mars
 *
 */
public abstract class Display extends Module implements PrintWriter {

	public Display(int id, String name) {
		super(id, name);
	}
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract void clear();
	public abstract void setPosition(int lineId, int columnId);
	
	
	
}
