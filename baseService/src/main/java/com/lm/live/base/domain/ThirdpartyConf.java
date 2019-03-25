package com.lm.live.base.domain;

import java.util.Date;


import com.lm.live.common.vo.BaseVo;

/**
 * @entity
 * @table t_thirdparty_conf_copy
 * @author shao.xiang
 * @date 2017年6月29日
 *
 */
public class ThirdpartyConf extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private int id;
	/** 登录是否启用,0:停用;1:启用 */
	private int inUseLogin;
	/** 支付下单是否启用,0:停用;1:启用 */
	private int inUsePayCreateOrder;
	/** 支付成功通知加金币是否启用,0:停用;1:启用 */
	private int inUsePaySuccessNotify;
	/** 第三方类型,0:qq;1:微信;2:微博 */
	private int thirdpartyType;
	/** 包名,为空则默认为官方包 */
	private String packageName;
	/** 客户端类型,0:三端通用;1:web;2:android;3:ios; */
	private int clientType;
	/** 申请接入时获得的appId */
	private String appId;
	/** 申请登录接入时获得的Key */
	private String loginKey;
	/** 申请支付接入时获得的商户号 */
	private String payMchId;
	/** 申请支付接入时获得的key */
	private String payKey;
	/** 说明 */
	private String conment;
	/** 修改时间 */
	private Date updateTime;
	/**判断updateTime，大于或等于 */
	private Date	gtUpdateTime;
	/**判断updateTime，小于或等于 */
	private Date	ltUpdateTime;

	public void setId(int id){
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setInUseLogin(int inUseLogin){
		this.inUseLogin = inUseLogin;
	}
	
	public int getInUseLogin() {
		return this.inUseLogin;
	}
	
	public void setInUsePayCreateOrder(int inUsePayCreateOrder){
		this.inUsePayCreateOrder = inUsePayCreateOrder;
	}
	
	public int getInUsePayCreateOrder() {
		return this.inUsePayCreateOrder;
	}
	
	public void setInUsePaySuccessNotify(int inUsePaySuccessNotify){
		this.inUsePaySuccessNotify = inUsePaySuccessNotify;
	}
	
	public int getInUsePaySuccessNotify() {
		return this.inUsePaySuccessNotify;
	}
	
	public void setThirdpartyType(int thirdpartyType){
		this.thirdpartyType = thirdpartyType;
	}
	
	public int getThirdpartyType() {
		return this.thirdpartyType;
	}
	
	public void setPackageName(String packageName){
		this.packageName = packageName;
	}
	
	public String getPackageName() {
		return this.packageName;
	}
	
	public void setClientType(int clientType){
		this.clientType = clientType;
	}
	
	public int getClientType() {
		return this.clientType;
	}
	
	public void setAppId(String appId){
		this.appId = appId;
	}
	
	public String getAppId() {
		return this.appId;
	}
	
	public void setLoginKey(String loginKey){
		this.loginKey = loginKey;
	}
	
	public String getLoginKey() {
		return this.loginKey;
	}
	
	public void setPayMchId(String payMchId){
		this.payMchId = payMchId;
	}
	
	public String getPayMchId() {
		return this.payMchId;
	}
	
	public void setPayKey(String payKey){
		this.payKey = payKey;
	}
	
	public String getPayKey() {
		return this.payKey;
	}
	
	public void setConment(String conment){
		this.conment = conment;
	}
	
	public String getConment() {
		return this.conment;
	}
	
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	
	public Date getUpdateTime() {
		return this.updateTime;
	}
	
	public void setGtUpdateTime(Date gtUpdateTime){
		this.gtUpdateTime = gtUpdateTime;
	}
	
//	@JsonIgnore
	public Date getGtUpdateTime() {
		return this.gtUpdateTime;
	}
	
	public void setLtUpdateTime(Date ltUpdateTime){
		this.ltUpdateTime = ltUpdateTime;
	}
	
//	@JsonIgnore
	public Date getLtUpdateTime() {
		return this.ltUpdateTime;
	}
	

}