package com.lm.live.appclient.domain;

import com.lm.live.common.vo.BaseVo;

public class AppCheckConf extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private int id;
	/** 包名 */
	private String packageName;
	/**版本1*/
	private String version1;
	/**版本2*/
	private String version2;
	/** 签名 */
	private String signatures;
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getSignatures() {
		return signatures;
	}

	public void setSignatures(String signatures) {
		this.signatures = signatures;
	}

	public String getVersion1() {
		return version1;
	}

	public void setVersion1(String version1) {
		this.version1 = version1;
	}

	public String getVersion2() {
		return version2;
	}

	public void setVersion2(String version2) {
		this.version2 = version2;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}