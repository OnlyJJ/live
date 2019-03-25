package com.lm.live.tools.tool.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiujun.shows.common.vo.BaseVo;
/**
 * @entity
 * @table t_tool_his
 * @author shao.xiang
 * @date 2017-06-29
 */
public class ToolHis extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private long id;
	/** 用户userId */
	private String userId;
	/** toolId */
	private int toolId;
	/** num */
	private int num;
	/** 关联操作的表记录id */
	private String refId;
	/** 关联操作的表说明 */
	private String refDesc;
	/** recordTime */
	private Date recordTime;
	/**判断recordTime，大于或等于 */
	private Date	gtRecordTime;
	/**判断recordTime，小于或等于 */
	private Date	ltRecordTime;
	/** comment */
	private String comment;
	/** 0:减少,1:添加 */
	private int type;

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
	
	public void setToolId(int toolId){
		this.toolId = toolId;
	}
	
	public int getToolId() {
		return this.toolId;
	}
	
	public void setNum(int num){
		this.num = num;
	}
	
	public int getNum() {
		return this.num;
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
	
	public void setGtRecordTime(Date gtRecordTime){
		this.gtRecordTime = gtRecordTime;
	}
	
	@JsonIgnore
	public Date getGtRecordTime() {
		return this.gtRecordTime;
	}
	
	public void setLtRecordTime(Date ltRecordTime){
		this.ltRecordTime = ltRecordTime;
	}
	
	@JsonIgnore
	public Date getLtRecordTime() {
		return this.ltRecordTime;
	}
	
	public void setComment(String comment){
		this.comment = comment;
	}
	
	public String getComment() {
		return this.comment;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
	

}