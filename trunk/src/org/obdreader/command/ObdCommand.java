package org.obdreader.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ObdCommand extends Thread {

	protected InputStream in = null;
	protected OutputStream out = null;
	protected ArrayList<Byte> buff = null;
	protected String cmd = null;
	protected String desc = null;
	protected String resType = null;
	protected Exception error;
	
	public ObdCommand(String cmd, String desc, String resType) {
		this.cmd = cmd;
		this.desc = desc;
		this.resType = resType;
		this.buff = new ArrayList<Byte>();
	}
	public ObdCommand(ObdCommand other) {
		this(other.cmd, other.desc, other.resType);
	}
	public void setInputStream(InputStream in) {
		this.in = in;
	}
	public void setOutputStream(OutputStream out) {
		this.out = out;
	}
	public void run() {
		sendCmd(cmd);
		readResult();
	}
	protected void sendCmd(String cmd) {
		try {
			cmd += "\r\n";
			out.write(cmd.getBytes());
			out.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	protected void readResult() {
		byte c = 0;
		this.buff.clear();
		try {
			while ((char)(c = (byte)in.read()) != '>') {
				buff.add(c);
			}
		} catch (IOException e) {
		}
	}
	public String getResult() {
		return new String(getByteArray());
	}
	public byte[] getByteArray() {
		byte[] data = new byte[this.buff.size()];
		for (int i = 0; i < this.buff.size(); i++) {
			data[i] = this.buff.get(i);
		}
		return data;
	}
	public String formatResult() {
		String res = getResult();
		String[] ress = res.split("\r");
		res = ress[0].replace(" ","");
		return res;
	}
	public InputStream getIn() {
		return in;
	}
	public OutputStream getOut() {
		return out;
	}
	public ArrayList<Byte> getBuff() {
		return buff;
	}
	public String getCmd() {
		return cmd;
	}
	public String getDesc() {
		return desc;
	}
	public String getResType() {
		return resType;
	}
	public void setError(Exception e) {
		error = e;
	}
	public Exception getError() {
		return error;
	}
}
