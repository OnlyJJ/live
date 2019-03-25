package com.jiujun.shows.car.vo;

import java.io.File;
import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.car.constant.Constants;
import com.jiujun.shows.common.constant.BaseConstants;
import com.jiujun.shows.common.utils.JsonParseInterface;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.utils.SpringContextListener;

/**
 * 座驾
 *
 */
public class CarVo extends JsonParseInterface implements Serializable{

	private static final long serialVersionUID = 6236801150374732138L;

	/** 商品汽车id */
	private int  carId ;
	
	/** 座驾名称 */
	private String carName;
	
	/** 座驾类型 ,0:购买型;1:系统赠送型*/
	private int  carType ;
	
	/** 座驾花费的金币  */
	private int  carSpendGolds;
	
	/** 座驾花费的金币 (容错)*/
	private double  carSpendMoney;

	/** 座驾有效天数  */
	private int carEffectiveDays ;
	
	/** 座驾说明 */
	private String carComment;
	
	/** 座驾启用时间  */
	private String beginTime;
	
	/** 座驾到期时间  */
	private String endTime;
	
	/** 座驾状态 ，状态,0:未启用,1:已启用,2:已过期,3:未购买 */
	private int status;
	
	/** 赠送者userId */
	private String sendUserId;
	
	/** 受赠者userId */
	private String receiveUserId;
	
	/** 赠送数量  */
	private int  num;
	
	/** 赠送时间   */
	private String sendTime;
	
	/** 赠言   */
	private String sendComment;
	
	/** 用户获取的到座驾id  */
	private int userCarPortId;
	
	/** 是否正在使用 ,0:否,1:是  */
	private int inUse;
	
	/** 剩余天数  */
	private int remainDays;
	
	/** 剩余时间倒计时,x天x小时x分...  */
	private String remainTimes;
	
	private String carImg;
	
	/** 过期 是否需要显示 */
	private int showStatus;
	
	
	
	// 字段key
	private static final String c_carId = "a";
	private static final String c_carName = "b";
	private static final String c_carType = "c";
	private static final String c_carSpendGolds = "d";
	private static final String c_carSpendMoney = "e";
	private static final String c_carEffectiveDays = "f";
	private static final String c_carComment = "g";
	private static final String c_beginTime = "h";
	private static final String c_endTime = "i";
	private static final String c_status = "j";
	private static final String c_sendUserId = "k";
	private static final String c_receiveUserId = "l";
	private static final String c_num = "m";
	private static final String c_sendTime = "n";
	private static final String c_sendComment = "o";
	private static final String c_userCarPortId = "p";
	private static final String c_inUse = "q";
	private static final String c_remainDays = "r";
	private static final String c_remainTimes = "s";
	private static final String c_carImg = "t";
	private static final String c_showStatus = "u";
	

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, c_carId, carId); 
			setString(json, c_carName, carName);
			setInt(json, c_carType, carType); 
			setInt(json, c_carSpendGolds, carSpendGolds);
			setDouble(json, c_carSpendMoney, carSpendMoney); 
			setInt(json, c_carEffectiveDays, carEffectiveDays); 
			setString(json, c_carComment, carComment);
			setString(json, c_beginTime, beginTime);
			setString(json, c_endTime, endTime);
			setInt(json, c_status, status); 
			setString(json, c_sendUserId, sendUserId);
			setString(json, c_receiveUserId, receiveUserId);
			setInt(json, c_num, num); 
			setString(json, c_sendTime, sendTime);
			setString(json, c_sendComment, sendComment);
			setInt(json, c_userCarPortId, userCarPortId);
			setInt(json, c_inUse, inUse); 
			setInt(json, c_remainDays, remainDays); 
			setString(json, c_remainTimes, remainTimes);
			if(null != carImg) {
				if(carImg.indexOf(Constants.CAR_IMG_FILE_URI) == -1) {
					setString(json, c_carImg, Constants.cdnPath+File.separator+Constants.CAR_IMG_FILE_URI+File.separator+carImg);
				} else {
					setString(json, c_carImg, carImg); 
				}
			}
			setInt(json, c_showStatus, showStatus);
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null) 
			return ;
		try {
			this.carId = getInt(json, c_carId);
			this.carName = getString(json, c_carName);
			this.carSpendGolds = getInt(json, c_carSpendGolds);
			this.carSpendMoney = getDouble(json, c_carSpendMoney);
			this.carEffectiveDays = getInt(json, c_carEffectiveDays);
			this.carComment = getString(json, c_carComment);
			this.beginTime = getString(json, c_beginTime);
			this.endTime = getString(json, c_endTime);
			this.status = getInt(json, c_status);
			this.sendUserId = getString(json, c_sendUserId);
			this.receiveUserId = getString(json, c_receiveUserId);
			this.num = getInt(json, c_num);
			this.sendTime = getString(json, c_sendTime);
			this.sendComment = getString(json, c_sendComment);
			this.userCarPortId = getInt(json, c_userCarPortId);
			this.inUse = getInt(json, c_inUse);
			this.carImg = getString(json, c_carImg);
			this.showStatus = getInt(json, c_showStatus);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public int getCarType() {
		return carType;
	}

	public void setCarType(int carType) {
		this.carType = carType;
	}

	public int getCarSpendGolds() {
		return carSpendGolds;
	}

	public void setCarSpendGolds(int carSpendGolds) {
		this.carSpendGolds = carSpendGolds;
	}

	public double getCarSpendMoney() {
		return carSpendMoney;
	}

	public void setCarSpendMoney(double carSpendMoney) {
		this.carSpendMoney = carSpendMoney;
	}

	public int getCarEffectiveDays() {
		return carEffectiveDays;
	}

	public void setCarEffectiveDays(int carEffectiveDays) {
		this.carEffectiveDays = carEffectiveDays;
	}

	public String getCarComment() {
		return carComment;
	}

	public void setCarComment(String carComment) {
		this.carComment = carComment;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendComment() {
		return sendComment;
	}

	public void setSendComment(String sendComment) {
		this.sendComment = sendComment;
	}

	public int getUserCarPortId() {
		return userCarPortId;
	}

	public void setUserCarPortId(int userCarPortId) {
		this.userCarPortId = userCarPortId;
	}

	public int getInUse() {
		return inUse;
	}

	public void setInUse(int inUse) {
		this.inUse = inUse;
	}

	public int getRemainDays() {
		return remainDays;
	}

	public void setRemainDays(int remainDays) {
		this.remainDays = remainDays;
	}

	public String getRemainTimes() {
		return remainTimes;
	}

	public void setRemainTimes(String remainTimes) {
		this.remainTimes = remainTimes;
	}

	public String getCarImg() {
		return carImg;
	}

	public void setCarImg(String carImg) {
		this.carImg = carImg;
	}

	public int getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(int showStatus) {
		this.showStatus = showStatus;
	}
	
	
}
