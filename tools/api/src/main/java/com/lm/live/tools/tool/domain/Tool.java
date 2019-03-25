package com.lm.live.tools.tool.domain;

import com.jiujun.shows.common.vo.BaseVo;
/**
 * @entity
 * @table t_tool
 * @author shao.xiang
 * @date 2017-06-29
 */
public class Tool extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** 类型,3:传送门,4:钥匙(用户等级宝箱),5:用户等级宝箱，6蜜桃种子，7隔空喊话*/
	private Integer type;
	/** 名称 */
	private String name;
	/** 说明 */
	private String info;
	/** 图片 */
	private String image;
	/** 花费金币 */
	private Integer gold;
	/** 用户获得经验 */
	private Integer userPoint;
	/** 添加时间 */
	private String addTime;
	
	private int number;
	
	private int buyAble;

	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setType(Integer type){
		this.type = type;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setInfo(String info){
		this.info = info;
	}
	
	public String getInfo() {
		return this.info;
	}
	
	public void setImage(String image){
		this.image = image;
	}
	
	public String getImage() {
		return this.image;
	}
	
	public void setGold(Integer gold){
		this.gold = gold;
	}
	
	public Integer getGold() {
		return this.gold;
	}
	
	public void setUserPoint(Integer userPoint){
		this.userPoint = userPoint;
	}
	
	public Integer getUserPoint() {
		return this.userPoint;
	}
	
	public void setAddTime(String addTime){
		this.addTime = addTime;
	}
	
	public String getAddTime() {
		return this.addTime;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getBuyAble() {
		return buyAble;
	}

	public void setBuyAble(int buyAble) {
		this.buyAble = buyAble;
	}
	
}