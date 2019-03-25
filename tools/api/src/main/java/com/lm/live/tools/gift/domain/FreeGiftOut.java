package com.lm.live.tools.gift.domain;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * @entity
 * @table t_gift_free_out
 * @author shao.xiang
 * @date 2017-06-29
 */
public class FreeGiftOut extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** 送出礼物的用户ID */
	private String userId;
	/** 送出礼物的用户ID */
	private String toUserId;
	/** 礼物数量 */
	private Integer number;
	/** 送礼物时间 */
	private String resultTime;
	/** 备注信息 */
	private String remark;

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
	
	public void setToUserId(String toUserId){
		this.toUserId = toUserId;
	}
	
	public String getToUserId() {
		return this.toUserId;
	}
	
	public void setNumber(Integer number){
		this.number = number;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	
	public void setResultTime(String resultTime){
		this.resultTime = resultTime;
	}
	
	public String getResultTime() {
		return this.resultTime;
	}
	
	public void setRemark(String remark){
		this.remark = remark;
	}
	
	public String getRemark() {
		return this.remark;
	}
	

}