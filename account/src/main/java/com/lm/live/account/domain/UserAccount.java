package com.lm.live.account.domain;

/**
* 个人账户实体
* @table t_user_account
* @author shao.xiang
* @date 2017-06-02
*/
public class UserAccount {

	/** 主键自增ID */
	private Integer id;
	/** 用户ID */
	private String userId;
	/** 人民币帐户(元) */
	private Float rmb;
	/** 金币数量 */
	private Long gold;
	/** (蓝钻)钻石数量 */
	private Long diamond;
	/** 主播获得经验（点） */
	private Long anchorPoint;
	/** 主播：子等级:S1。普通用户: V1，关联t_level.level & type=2 */
	private String anchorLevel;
	/** '用户获得经验（点） */
	private Long userPoint;
	/** 普通用户子等级: V1，，关联t_level.level & type=1 */
	private String userLevel;
	private Long renqPoint; // 普通用户人气等级经验
	private String renqLevel; // 普通用户人气等级，R0~R46，关联level等级
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Float getRmb() {
		return rmb;
	}
	public void setRmb(Float rmb) {
		this.rmb = rmb;
	}

	public Long getGold() {
		return gold;
	}
	public void setGold(Long gold) {
		this.gold = gold;
	}
	public Long getDiamond() {
		return diamond;
	}
	public void setDiamond(Long diamond) {
		this.diamond = diamond;
	}
	public Long getAnchorPoint() {
		return anchorPoint;
	}
	public void setAnchorPoint(Long anchorPoint) {
		this.anchorPoint = anchorPoint;
	}
	public String getAnchorLevel() {
		return anchorLevel;
	}
	public void setAnchorLevel(String anchorLevel) {
		this.anchorLevel = anchorLevel;
	}
	public Long getUserPoint() {
		return userPoint;
	}
	public void setUserPoint(Long userPoint) {
		this.userPoint = userPoint;
	}
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	public Long getRenqPoint() {
		return renqPoint;
	}
	public void setRenqPoint(Long renqPoint) {
		this.renqPoint = renqPoint;
	}
	public String getRenqLevel() {
		return renqLevel;
	}
	public void setRenqLevel(String renqLevel) {
		this.renqLevel = renqLevel;
	}
	
	
}
