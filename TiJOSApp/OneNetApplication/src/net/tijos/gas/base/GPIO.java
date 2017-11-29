package net.tijos.gas.base;

/**
 * GPIOµÄPIN
 * @author Mars 
 *
 */
public interface GPIO {
	public enum PIN {
		PIN0(0), PIN1(1), PIN2(2), PIN3(3), PIN4(4), PIN5(5), PIN6(6), PIN7(7);
		
		
		private int pinId = -1;
		PIN(int pinId) {
			this.pinId = pinId;
		}
		
		public int getPinId() {
			return pinId;
		}
	}
}
