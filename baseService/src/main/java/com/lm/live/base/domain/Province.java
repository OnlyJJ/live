package com.lm.live.base.domain;

import com.lm.live.common.vo.BaseVo;

public class Province extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** 省级 */
	private String region;
	/** 市级 */
	private String city;
	/** code */
	private String code;
	/** postcode */
	private String postcode;

	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setRegion(String region){
		this.region = region;
	}
	
	public String getRegion() {
		return this.region;
	}
	
	public void setCity(String city){
		this.city = city;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public void setPostcode(String postcode){
		this.postcode = postcode;
	}
	
	public String getPostcode() {
		return this.postcode;
	}
	

}