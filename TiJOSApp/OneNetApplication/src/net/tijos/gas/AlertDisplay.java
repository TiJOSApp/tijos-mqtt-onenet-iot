package net.tijos.gas;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import net.tijos.gas.base.modules.Display;
import tijos.framework.devicecenter.TiI2CMaster;
import tijos.framework.transducer.oled.TiOLED_UG2864;

/**
 * Display具体实现类，用于显示各种警告信息、当前温湿度等。
 * 信息输出到屏幕过程比较缓慢，为了不阻塞写入函数和打印函数，
 * @author Mars 
 *
 */
public class AlertDisplay extends Display implements Runnable {
	
	private LinkedList<Wrapper> printQueue = new LinkedList<Wrapper>();
	private TiOLED_UG2864 oled;
	
	protected AlertDisplay(String name, TiI2CMaster i2c, int address) throws IOException {
		super(0, name);
		
		oled = new TiOLED_UG2864(i2c, address);
		oled.turnOn();
		oled.clear();
		
		new Thread(this).start();
		
	}

	@Override
	public void print(Object o) {
		putAndNotify(new Wrapper(String.valueOf(o)));

	}

	@Override
	public void print(String s) {
		putAndNotify(new Wrapper(s));
	}

	@Override
	public void print(char c) {
		putAndNotify(new Wrapper(String.valueOf(c)));
	}

	@Override
	public void print(char[] s) {
		putAndNotify(new Wrapper(String.valueOf(s)));

	}

	@Override
	public void print(double x) {
		putAndNotify(new Wrapper(String.valueOf(x)));

	}

	@Override
	public void print(float x) {
		putAndNotify(new Wrapper(String.valueOf(x)));

	}

	@Override
	public void print(int x) {
		putAndNotify(new Wrapper(String.valueOf(x)));

	}

	@Override
	public void print(long x) {
		putAndNotify(new Wrapper(String.valueOf(x)));
	}

	@Override
	public void println() {
		putAndNotify(new Wrapper("", true));
	}

	@Override
	public void println(Object o) {
		putAndNotify(new Wrapper(String.valueOf(o), true));
	}

	@Override
	public void println(String s) {
		putAndNotify(new Wrapper(String.valueOf(s), true));
	}

	@Override
	public void println(char c) {
		putAndNotify(new Wrapper(String.valueOf(c), true));
	}

	@Override
	public void println(char[] s) {
		putAndNotify(new Wrapper(String.valueOf(s), true));
	}

	@Override
	public void println(double x) {
		putAndNotify(new Wrapper(String.valueOf(x), true));
	}

	@Override
	public void println(float x) {
		putAndNotify(new Wrapper(String.valueOf(x), true));
	}

	@Override
	public void println(int x) {
		putAndNotify(new Wrapper(String.valueOf(x), true));
	}

	@Override
	public void println(long x) {
		putAndNotify(new Wrapper(String.valueOf(x), true));
	}

	@Override
	public void write(char[] buf, int off, int len) {
		putAndNotify(new Wrapper(String.valueOf(Arrays.copyOfRange(buf, off, off + len))));
	}

	@Override
	public void write(int c) {
		putAndNotify(new Wrapper(String.valueOf(c)));
	}

	@Override
	public void write(char[] buf) {
		putAndNotify(new Wrapper(String.valueOf(buf)));
	}

	@Override
	public void write(String s, int off, int len) {
		putAndNotify(new Wrapper(String.valueOf(s.subSequence(off, off + len))));
	}

	@Override
	public void write(String s) {
		putAndNotify(new Wrapper(s));
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() {
		clearWrapper();
		
		synchronized (oled) {
			try {
				oled.turnOff();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getWidth() {
		return 16;
	}

	@Override
	public int getHeight() {
		return 4;
	}


	@Override
	public void setPosition(int lineId, int columnId) {
		putAndNotify(new Wrapper(lineId, columnId));
	}


	@Override
	public void clear() {
		putAndNotify(new Wrapper());
		
	}

	@Override
	public synchronized void run() {
		while (true) {
			
			Wrapper w = getWrapper();
			if (w == null) {
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {}
				}
			}else {
				switch (w.getCmd()) {
				case Clear:
					synchronized (oled) {
						try {
							oled.clear();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					break;
				case PrintWrite:
					synchronized (oled) {
						try {
							oled.output(w.getValue());
							if (w.isWrap()) {
								int line = oled.getPositionLine();
								oled.setPosition(++line, 0);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}		
					break;
				case SetPosition:
					synchronized (oled) {
						oled.setPosition(w.getLineId(), w.getColumnId());
						
					}
					break;
				default:
					break;
				}
			}
			
		}
		
	}
	
	private void putAndNotify(Wrapper w) {
		addWrapper(w);
		
		synchronized (this) {
			notify();
		}
		
	}
	
	
	private void addWrapper(Wrapper w) {
		synchronized (printQueue) {
			printQueue.add(w);
		}
	}
	
	private Wrapper getWrapper() {
		synchronized (printQueue) {
			try {
				return printQueue.remove();
			} catch (Exception e) {
				return null;
			}
			
		}
	}
	
	private void clearWrapper() {
		synchronized (printQueue) {
			printQueue.clear();
		}
	}
	
	private static class Wrapper {
		public static enum Cmd {
			Clear, //清屏
			PrintWrite, //输出
			SetPosition;	//设置偏移地址
		};
		
		private Cmd cmd;
		private int lineId;
		private int columnId;
		private String value;
		private boolean wrap = false;
		
		public Wrapper(String value) {
			this.cmd = Cmd.PrintWrite;
			this.value = value;
			this.wrap = false;
		}
		
		public Wrapper(String value, boolean wrap) {
			this.cmd = Cmd.PrintWrite;
			this.value = value;
			this.wrap = wrap;
		}
		
		public Wrapper(int lineId, int columnId) {
			this.cmd = Cmd.SetPosition;
			this.lineId = lineId;
			this.columnId = columnId;
		}
		
		public Wrapper() {
			this.cmd = Cmd.Clear;
		}
		
		public Cmd getCmd() {
			return cmd;
		}
		
		public int getLineId() {
			return lineId;
		}
		
		public int getColumnId() {
			return columnId;
		}
		
		
		
		public String getValue() {
			return value;
		}
		
		public boolean isWrap() {
			return wrap;
		}
	}
	
	

}
