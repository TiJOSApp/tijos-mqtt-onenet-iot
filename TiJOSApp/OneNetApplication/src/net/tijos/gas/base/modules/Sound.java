package net.tijos.gas.base.modules;

import net.tijos.gas.base.Module;
import net.tijos.gas.base.Switch;

/**
 * ÉùÒô²¥·ÅÆ÷³éÏóÀà
 * @author Mars 
 *
 */
public abstract class Sound extends Module {

	public Sound(int id, String name) {
		super(id, name);
	}
	public abstract void play();
	public abstract boolean isPlay();
	public abstract void release();
	
	
	

}
