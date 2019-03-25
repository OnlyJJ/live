package com.lm.live.tools.pack.domain;

import com.jiujun.shows.common.vo.BaseVo;
/**
 * @entity
 * @table t_user_package
 * @author shao.xiang
 * @date 2017-07-02
 */
public class UserPackage extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private Integer id;
	/** 用户ID */
	private String userId;
	/** 礼物ID */
	private Integer giftId;
	/** 礼物数量 */
	private Integer number;
	/** 是否具备有效期：0-长期有效，1-有效期内有效 */
	private Integer isPeriod;
	/** 有效期结束时间 */
	private String endTime;
	/** 添加进背包中的时间 */
	private String addTime;
	/** 是否有效：0-无效，1-有效 */
	private Integer isValid;

	
	/** 关联 t_gift 表 */
	/** 礼物名称 */
	private String name;
	
	/** 关联 t_user_info 表 */
	/** 用户账号 */
	private String userAccount;
	private String bindEmail;
	private String bindMobile;
	
	public String getBindEmail() {
		return bindEmail;
	}

	public void setBindEmail(String bindEmail) {
		this.bindEmail = bindEmail;
	}

	public String getBindMobile() {
		return bindMobile;
	}

	public void setBindMobile(String bindMobile) {
		this.bindMobile = bindMobile;
	}
	
	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setGiftId(Integer giftId){
		this.giftId = giftId;
	}
	
	public Integer getGiftId() {
		return this.giftId;
	}
	
	public void setNumber(Integer number){
		this.number = number;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	
	public void setIsPeriod(Integer isPeriod){
		this.isPeriod = isPeriod;
	}
	
	public Integer getIsPeriod() {
		return this.isPeriod;
	}
	
	public void setEndTime(String endTime){
		this.endTime = endTime;
	}
	
	public String getEndTime() {
		return this.endTime;
	}
	
	public void setAddTime(String addTime){
		this.addTime = addTime;
	}
	
	public String getAddTime() {
		return this.addTime;
	}
	

}