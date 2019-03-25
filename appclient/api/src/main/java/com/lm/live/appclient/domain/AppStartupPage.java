package com.lm.live.appclient.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lm.live.common.vo.BaseVo;
/**
 * @entity
 * @table t_app_startup_page
 * @date 2016-12-28 18:12:17
 * @author test2
 */
public class AppStartupPage extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** 是否启用,0:不启用;1:启用 */
	private Integer inUse;
	/** 主题类型，1：单张图片自动滚动2：多张图片自动滑动  3：多张图片手动滑动 4：渐变 5：单张图片没动画 */
	private Integer themeType;
	/** 更新时间 */
	private Date updateTime;
	/**判断updateTime，大于或等于 */
	private Date	gtUpdateTime;
	/**判断updateTime，小于或等于 */
	private Date	ltUpdateTime;
	/** 0:不跳转;1:URL跳转;2:房间跳转 */
	private int jumpType;
	/** 跳转目标(根据jumpType而定,url地址或房间号) */
	private String jumpTarget;
	/** 说明 */
	private String comment;
	
	/** 使用开始时间 */
	private Date beginTime;
	
	/** 到期时间 */
	private Date endTime;

	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setInUse(Integer inUse){
		this.inUse = inUse;
	}
	
	public Integer getInUse() {
		return this.inUse;
	}
	
	public void setThemeType(Integer themeType){
		this.themeType = themeType;
	}
	
	public Integer getThemeType() {
		return this.themeType;
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
	
	@JsonIgnore
	public Date getGtUpdateTime() {
		return this.gtUpdateTime;
	}
	
	public void setLtUpdateTime(Date ltUpdateTime){
		this.ltUpdateTime = ltUpdateTime;
	}
	
	@JsonIgnore
	public Date getLtUpdateTime() {
		return this.ltUpdateTime;
	}
	
	public void setJumpType(int jumpType){
		this.jumpType = jumpType;
	}
	
	public int getJumpType() {
		return this.jumpType;
	}
	
	public void setJumpTarget(String jumpTarget){
		this.jumpTarget = jumpTarget;
	}
	
	public String getJumpTarget() {
		return this.jumpTarget;
	}
	
	public void setComment(String comment){
		this.comment = comment;
	}
	
	public String getComment() {
		return this.comment;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}