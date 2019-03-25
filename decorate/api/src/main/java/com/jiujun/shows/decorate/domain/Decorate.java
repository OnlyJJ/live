package com.jiujun.shows.decorate.domain;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * 勋章表
 * @entity
 * @table t_decorate
 * @author shao.xiang
 * @date -2017-06-08
 */

public class Decorate extends BaseVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	private Integer id;
	/**
	 * 勋章类型,0:人马勋章,1:女神／男神精,3:歌神勋章,4:魅惑勋章,5:可爱勋章,6:皇室勋章,7:气质勋章
	 */
	private Integer type;
	/**
	 * 勋章名称
	 */
	private String name;
	/**
	 * 是否可购买,0:不可,1:可以购买
	 */
	private Integer buyAble;
	/**
	 * 点亮的图片
	 */
	private String lightenImg;
	/**
	 * 灰色的图片
	 */
	private String grayImg;
	/**
	 * 点亮时长 
	 */
	private Integer lightenDay;
	/**
	 * 收礼有效期，在该时间内收到对应的礼物数量才能点亮相应的勋章
	 */
	private Integer validDay;
	
	/**
	 * 勋章来源，默认0-送礼统计，1-摘蜜桃
	 */
	private int resource;
	
	/** 分类,0:主播勋章,1:普通用户勋章  */
	private int category;
	
	/**
	 * 描述
	 * @return
	 */
	private String remark;
	
	/** 排序权值,值越大越优先 */
	private int sortWeight;
	
	/** 未点亮时，是否显示，默认0-显示 */
	private int isShow;
	
	
	/** 是否佩戴用户勋章,y 或 n  */
	private String isAdornUserDecorate;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return this.type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public Integer getBuyable() {
		return this.buyAble;
	}
	
	public void setBuyable(Integer buyAble) {
		this.buyAble = buyAble;
	}
	public String getLightenimg() {
		return this.lightenImg;
	}
	
	public void setLightenimg(String lightenImg) {
		this.lightenImg = lightenImg;
	}
	public String getGrayimg() {
		return this.grayImg;
	}
	
	public void setGrayimg(String grayImg) {
		this.grayImg = grayImg;
	}

	public Integer getLightenDay() {
		return lightenDay;
	}

	public void setLightenDay(Integer lightenDay) {
		this.lightenDay = lightenDay;
	}

	public Integer getValidDay() {
		return validDay;
	}

	public void setValidDay(Integer validDay) {
		this.validDay = validDay;
	}

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getBuyAble() {
		return buyAble;
	}

	public void setBuyAble(Integer buyAble) {
		this.buyAble = buyAble;
	}

	public String getGrayImg() {
		return grayImg;
	}

	public void setGrayImg(String grayImg) {
		this.grayImg = grayImg;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getSortWeight() {
		return sortWeight;
	}

	public void setSortWeight(int sortWeight) {
		this.sortWeight = sortWeight;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	public String getIsAdornUserDecorate() {
		return isAdornUserDecorate;
	}

	public void setIsAdornUserDecorate(String isAdornUserDecorate) {
		this.isAdornUserDecorate = isAdornUserDecorate;
	}

}
