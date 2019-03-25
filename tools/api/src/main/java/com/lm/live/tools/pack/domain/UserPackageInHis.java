package com.lm.live.tools.pack.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;
/**
 * @entity
 * @table t_user_package_in_his
 * @author shao.xiang
 * @date 2017-07-02
 */
public class UserPackageInHis extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private long id;
	/** 用户userId */
	private String userId;
	/** giftId */
	private int giftId;
	/** inNum */
	private int inNum;
	/** 关联操作的表记录id */
	private String refId;
	/** 关联操作的表说明 */
	private String refDesc;
	/** recordTime */
	private Date recordTime;
	/** comment */
	private String comment;

	public void setId(long id){
		this.id = id;
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setGiftId(int giftId){
		this.giftId = giftId;
	}
	
	public int getGiftId() {
		return this.giftId;
	}
	
	public void setInNum(int inNum){
		this.inNum = inNum;
	}
	
	public int getInNum() {
		return this.inNum;
	}
	
	public void setRefId(String refId){
		this.refId = refId;
	}
	
	public String getRefId() {
		return this.refId;
	}
	
	public void setRefDesc(String refDesc){
		this.refDesc = refDesc;
	}
	
	public String getRefDesc() {
		return this.refDesc;
	}
	
	public void setRecordTime(Date recordTime){
		this.recordTime = recordTime;
	}
	
	public Date getRecordTime() {
		return this.recordTime;
	}
	
	
	public void setComment(String comment){
		this.comment = comment;
	}
	
	public String getComment() {
		return this.comment;
	}
	

}