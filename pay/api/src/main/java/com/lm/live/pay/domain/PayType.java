package com.lm.live.pay.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
* PayType
 * 充值方式
*/
public class PayType extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	/**
	 * 充值名称：支付宝充值
	 */
	private String name;
	/**
	 * 充值说明：充一送十
	 */
	private String info;
	/**
	 * 充值显示图标
	 */
	private String image;
	/**
	 * 充值回调地址：支付宝回调到我们服务器的地址
	 */
	private String notifyUrl;
	/**
	 * 是否启用该充值方式，0-停用，1-启用
	 */
	private int isUse;
	/**
	 * 添加时间
	 */
	private Date addTime;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return this.info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	public String getImage() {
		return this.image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	public String getNotifyurl() {
		return this.notifyUrl;
	}
	
	public void setNotifyurl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public int getIsuse() {
		return this.isUse;
	}
	
	public void setIsuse(int isUse) {
		this.isUse = isUse;
	}
	public Date getAddtime() {
		return this.addTime;
	}
	
	public void setAddtime(Date addTime) {
		this.addTime = addTime;
	}
}
