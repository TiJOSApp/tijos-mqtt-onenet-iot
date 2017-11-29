package net.tijos.gas.base.modules;

import net.tijos.gas.base.Listener;
import net.tijos.gas.base.Module;

/**
 * ³éÏó°´Å¥
 * @author Mars
 *
 */
public abstract class Button extends Module {
	
	

	public interface OnClickListener extends Listener {
		void onClick(Module m);
	}
	
	public interface OnLongClickListener extends Listener {
		void onLongClick(Module m);
	}
	
	public Button(int id, String name) {
		super(id, name);
	}
	
	public abstract boolean isEnabled();
	
	public abstract void setEnabled(boolean enabled);
	public abstract void setOnClickListener(OnClickListener l);
	public abstract void setOnLongClickListener(OnLongClickListener onLongClickListener);
	
	public boolean isClickable() {
		return false;
	}
}
