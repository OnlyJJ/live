package com.lm.live.base.domain;


import com.lm.live.common.vo.BaseVo;

/**
 * ip库实体<br>
 * 	t_ip_repositor
 * @author shao.xiang
 * @date 2017-06-05
 */

public class IpRepositor extends BaseVo {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String startIp;
	
	private String startLongIp;
	
	private String endIp;
	
	private String endLongIp;
	
	private String address;
	
	private String operator;
	
	private int inetAtonS;
	
	private int inetAtonE;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStartIp() {
		return startIp;
	}

	public void setStartIp(String startIp) {
		this.startIp = startIp;
	}

	public String getStartLongIp() {
		return startLongIp;
	}

	public void setStartLongIp(String startLongIp) {
		this.startLongIp = startLongIp;
	}

	public String getEndIp() {
		return endIp;
	}

	public void setEndIp(String endIp) {
		this.endIp = endIp;
	}

	public String getEndLongIp() {
		return endLongIp;
	}

	public void setEndLongIp(String endLongIp) {
		this.endLongIp = endLongIp;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getInetAtonS() {
		return inetAtonS;
	}

	public void setInetAtonS(int inetAtonS) {
		this.inetAtonS = inetAtonS;
	}

	public int getInetAtonE() {
		return inetAtonE;
	}

	public void setInetAtonE(int inetAtonE) {
		this.inetAtonE = inetAtonE;
	}
	
}
