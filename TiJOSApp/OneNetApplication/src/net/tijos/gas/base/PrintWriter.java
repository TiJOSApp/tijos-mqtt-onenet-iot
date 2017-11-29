package net.tijos.gas.base;

/**
 * ≥ÈœÛ¥Ú”°
 * @author Mars 
 *
 */
public interface PrintWriter {
	public void print(Object obj);
	public void print(String s);
	public void print(char c);
	public void print(char[] s);
	public void print(double x);
	public void print(float x);
	public void print(int x);
	public void print(long x);
	
	
	public void println();
	public void println(Object obj);
	public void println(String s);
	public void println(char c);
	public void println(char[] s);
	public void println(double x);
	public void println(float x);
	public void println(int x);
	public void println(long x);
	
	
	public void write(char[] buf,
            int off,
            int len);
	public void write(int c);
	public void write(char[] buf);
	public void write(String s,
            int off,
            int len);
	public void write(String s);
	
	public void flush();
	public void close();
}
