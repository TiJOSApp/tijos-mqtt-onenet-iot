package net.tijos.gas.base.modules;

import java.io.IOException;

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
	public abstract void play() throws IOException;
	public abstract boolean isPlay() throws IOException;
	public abstract void release() throws IOException;
	
	
	

}
