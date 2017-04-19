package pers.tunsuy.pts.model;

import com.google.protobuf.AbstractMessage;

public class UserData {
	private String pbName;
	private short srvCode;
	private String infName;
	private String infRspName;
	private int srvop;
	private String param;

	public void setPbName(String pbName) {
		this.pbName = pbName;
	}

	public String getPbName() {
		return this.pbName;
	}
	
	public void setSrvCode(short srvCode) {
		this.srvCode = srvCode;
	}
	
	public short getSrvCode() {
		return this.srvCode;
	}

	public void setInfName(String infName) {
		this.infName = infName;
	}

	public String getInfName() {
		return  this.infName;
	}
	
	public void setInfRspName(String infRspName) {
		this.infRspName = infRspName;
	}
	
	public String getInfRspName() {
		return this.infRspName;
	} 

	public void setSrvop(int srvop) {
		this.srvop = srvop;
	}
	
	public int getSrvop() {
		return this.srvop;
	}
	
	public void setParam(String param) {
		this.param = param;
	}

	public String getParam() {
		return this.param;
	}
    
}
