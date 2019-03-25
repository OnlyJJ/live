package com.lm.live.account.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;

/**
 * 用户账户(金币、钻石)加减流水记录汇总 
 * @table t_user_account_book
 * @author shao.xiang
 * @date 2017-06-06
 */

public class UserAccountBook extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3028999775859680615L;
	/**
	 * id
	 */
	private long id;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 礼物id(可为Null,加减礼物时使用)
	 */
	private int giftId;
	/**
	 * 用户使用礼物的数量(可为Null,加减礼物时使用)
	 */
	private int sendGiftNum;
	/**
	 * 送礼之前相应礼物包裹剩余量(可为Null,加减礼物时使用)
	 */
	private int giftPackagePreRemainNum;
	/**
	 * 改变用户包裹,增加为+,减少为-
	 */
	private int changeGiftNum;
	/**
	 * 送礼之后相应礼物包裹剩余量(可为Null,加减礼物时使用)
	 */
	private int giftPackageSufRemainNum;
	/**
	 * 改变前账户剩余金币数
	 */
	private long preRemainGolds;
	/**
	 * 改变账户金币数,增加为+,减少为-
	 */
	private int changeGolds;
	/**
	 * 改变后账户剩余金币数
	 */
	private long sufRemainGolds;
	/**
	 * 关联具体业务模块记录id，如系统任务完成任务时记录表里的id
	 */
	private String sourceId;
	/**
	 * 具体业务模块描述，如系统任务中的连续登录任务
	 */
	private String sourceDesc;
	/**
	 * 记录时间
	 */
	private Date recordTime;
	/**
	 *  备注说明
	 */
	private String content;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getGiftId() {
		return giftId;
	}
	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}
	public int getGiftPackagePreRemainNum() {
		return giftPackagePreRemainNum;
	}
	public void setGiftPackagePreRemainNum(int giftPackagePreRemainNum) {
		this.giftPackagePreRemainNum = giftPackagePreRemainNum;
	}
	public int getChangeGiftNum() {
		return changeGiftNum;
	}
	public void setChangeGiftNum(int changeGiftNum) {
		this.changeGiftNum = changeGiftNum;
	}
	public int getGiftPackageSufRemainNum() {
		return giftPackageSufRemainNum;
	}
	public void setGiftPackageSufRemainNum(int giftPackageSufRemainNum) {
		this.giftPackageSufRemainNum = giftPackageSufRemainNum;
	}
	public long getPreRemainGolds() {
		return preRemainGolds;
	}
	public void setPreRemainGolds(long preRemainGolds) {
		this.preRemainGolds = preRemainGolds;
	}
	public int getChangeGolds() {
		return changeGolds;
	}
	public void setChangeGolds(int changeGolds) {
		this.changeGolds = changeGolds;
	}
	public long getSufRemainGolds() {
		return sufRemainGolds;
	}
	public void setSufRemainGolds(long sufRemainGolds) {
		this.sufRemainGolds = sufRemainGolds;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceDesc() {
		return sourceDesc;
	}
	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}
	public Date getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getSendGiftNum() {
		return sendGiftNum;
	}
	public void setSendGiftNum(int sendGiftNum) {
		this.sendGiftNum = sendGiftNum;
	}
	
}
