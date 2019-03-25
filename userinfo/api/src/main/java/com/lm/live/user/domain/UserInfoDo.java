package com.lm.live.user.domain;

import java.util.Date;

import org.springframework.util.StringUtils;

import net.sf.json.JSONObject;

import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.SensitiveWordUtil;
import com.lm.live.common.vo.BaseVo;

public class UserInfoDo  extends BaseVo{
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private Integer id;
	/** 用户id */
	private String userId;
	/** 用户名**/
	private String userAccount;
	/** 用户密码 */
	private String pwd;
	/** 绑定手机号 */
	private String bindMobile;
	/** 绑定邮箱 */
	private String bindEmail;
	/** 创建时间 */
	private Date addTime;
	/** 激活时间 */
	private String actTime;
	/** 最后登录时间 */
	private String lastTime;
	/** 最后登录IP */
	private String lastIp;
	/** 最后登录IP对应的地理位置 */
	private String lastArea;
	/** 用户自定义昵称 */
	private String nickName;
	/** 男 OR 女 OR 未知 */
	private String sex;
	/** 用户自定义头像 */
	private String icon;
	/** 是否已经成为主播,0-不是，1-是 */
	private int isAnchor;
	
	/** 渠道号 */
	private String channelId;
	
	/** app用户来源包 */
	private String appPackage;
	
	/** uuid */
	private String uuid;
	
	/** 登录方式 */
	private int loginType;
	
	/** 用户状态，1-正常; 0-停用; */
	private Integer userStatus;
	private String address;
	private String remark;
	private Date brithday;
	private String sf;
	private String cs;
	private String qy;
	
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
	
	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public void setPwd(String pwd){
		this.pwd = pwd;
	}
	
	public String getPwd() {
		return this.pwd;
	}
	
	public void setBindMobile(String bindMobile){
		this.bindMobile = bindMobile;
	}
	
	public String getBindMobile() {
		return this.bindMobile;
	}
	
	public void setBindEmail(String bindEmail){
		this.bindEmail = bindEmail;
	}
	
	public String getBindEmail() {
		return this.bindEmail;
	}
	
	public void setAddTime(Date addTime){
		this.addTime = addTime;
	}
	
	public Date getAddTime() {
		return this.addTime;
	}
	
	public void setActTime(String actTime){
		this.actTime = actTime;
	}
	
	public String getActTime() {
		return this.actTime;
	}
	
	public void setLastTime(String lastTime){
		this.lastTime = lastTime;
	}
	
	public String getLastTime() {
		return this.lastTime;
	}
	
	public void setLastIp(String lastIp){
		this.lastIp = lastIp;
	}
	
	public String getLastIp() {
		return this.lastIp;
	}
	
	public void setLastArea(String lastArea){
		this.lastArea = lastArea;
	}
	
	public String getLastArea() {
		return this.lastArea;
	}
	
	public void setNickName(String nickName){
		if(!StringUtils.isEmpty(nickName)){
			// 替换昵称中的特殊符号
			nickName=nickName.replaceAll("\n|\r", " ");
			nickName=nickName.replaceAll("'|\"|<|>|\\\\", "*");
			//替换换RTLO
			nickName=nickName.replaceAll("\u202E", "*");
			
			//  替换昵称中敏感字字符为 "*"
			nickName = SensitiveWordUtil.replaceSensitiveWord(nickName);
		}
		this.nickName = nickName;
	}
	
	public String getNickName() {
		return this.nickName;
	}
	
	public void setSex(String sex){
		this.sex = sex;
	}
	
	public String getSex() {
		return this.sex;
	}
	
	public void setIcon(String icon){
		this.icon = icon;
	}
	
	public String getIcon() {
		return this.icon;
	}
	
	public void setIsAnchor(int isAnchor){
		this.isAnchor = isAnchor;
	}
	
	public int getIsAnchor() {
		return this.isAnchor;
	}
	
	public void setUserStatus(Integer userStatus){
		this.userStatus = userStatus;
	}
	
	public Integer getUserStatus() {
		return this.userStatus;
	}
	
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			json.put("userId", userId);
			json.put("bindMobile", bindMobile);
			json.put("bindEmail", bindEmail);
		} catch (Exception e) {
			LogUtil.log.error("buildJson fail");
		}
		return json;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getBrithday() {
		return brithday;
	}

	public void setBrithday(Date brithday) {
		this.brithday = brithday;
	}

	public String getSf() {
		return sf;
	}

	public void setSf(String sf) {
		this.sf = sf;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public String getQy() {
		return qy;
	}

	public void setQy(String qy) {
		this.qy = qy;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}


}