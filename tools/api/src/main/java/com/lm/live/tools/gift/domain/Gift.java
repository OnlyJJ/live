package com.lm.live.tools.gift.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;
/**
 * @entity
 * @table t_gift
 * @author shao.xiang
 * @date 2017-06-29
 */
public class Gift extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private Integer id;
	/** 礼物名称：法拉利 */
	private String name;
	/** 礼物说明：假一送十 */
	private String info;
	/** 礼物图片 */
	private String image;
	/** 单价，单位：金币 */
	private Integer price;
	/** 主播分成比例:0.55 */
	private Double rate;
	/** 蓝钻价值=price*rate，对应主播收入，单位：钻石 */
	private Integer diamond;
	/** 蓝钻对应RMB价值，单位：分 */
	private Double priceRMB;
	/** 主播获得经验(点) */
	private Integer anchorPoint;
	/** 用户获得经验(点) */
	private Integer userPoint;
	/** 是否启用该礼物/商品，0-停用，1-启用 */
	private Integer isUse;
	/** 启用之后开始生效时间，为空表示即时生效 */
	private Date startTime;
	/** 停用时间，为空表示长期有效 */
	private Date endTime;
	/** 添加时间 */
	private Date addTime;
	/**判断添加时间，大于或等于 */
	private Date	gtAddTime;
	/**判断添加时间，小于或等于 */
	private Date	ltAddTime;
	/**flash显示效果ID*/
	private Integer showType;
	/**flash显示效果名称*/
	private String showName;
	
	/** 桃子成长魅力值  */
	private int meili;
	
	/** 桃子成长人气值  */
	private Integer renqi;
	
	/** 桃子成长女神值 */
	private Integer nvshen;

	/** 是否可以购买 ,0-可以，1-不可以*/
	private int buyable;
	
	/** 礼物分类，默认0-经典，1-活动，5-守护,6-幸运礼物,7-用户礼物,8-奢华 */
	private int giftType;
	/**守护礼物对应的守护等级，默认1,1级守护可赠送1级的礼物，2级守护可以赠送2以下的，以此类推 */
	private int guardLevel;
	/**特殊标记，默认0(无),1:指定为送出后滚屏礼物 */
	private int specialFlag;
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
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
	
	public void setPrice(Integer price){
		this.price = price;
	}
	
	public Integer getPrice() {
		return this.price;
	}
	
	public void setRate(Double rate){
		this.rate = rate;
	}
	
	public Double getRate() {
		return this.rate;
	}
	
	public void setDiamond(Integer diamond){
		this.diamond = diamond;
	}
	
	public Integer getDiamond() {
		return this.diamond;
	}
	
	public void setPriceRMB(Double priceRMB){
		this.priceRMB = priceRMB;
	}
	
	public Double getPriceRMB() {
		return this.priceRMB;
	}
	
	public void setAnchorPoint(Integer anchorPoint){
		this.anchorPoint = anchorPoint;
	}
	
	public Integer getAnchorPoint() {
		return this.anchorPoint;
	}
	
	public void setUserPoint(Integer userPoint){
		this.userPoint = userPoint;
	}
	
	public Integer getUserPoint() {
		return this.userPoint;
	}
	
	public void setIsUse(Integer isUse){
		this.isUse = isUse;
	}
	
	public Integer getIsUse() {
		return this.isUse;
	}
	
	public void setStartTime(Date startTime){
		this.startTime = startTime;
	}
	
	public Date getStartTime() {
		return this.startTime;
	}
	
	public void setEndTime(Date endTime){
		this.endTime = endTime;
	}
	
	public Date getEndTime() {
		return this.endTime;
	}
	
	public void setAddTime(Date addTime){
		this.addTime = addTime;
	}
	
	public Date getAddTime() {
		return this.addTime;
	}
	
	public void setGtAddTime(Date gtAddTime){
		this.gtAddTime = gtAddTime;
	}
	
	public Date getGtAddTime() {
		return this.gtAddTime;
	}
	
	public void setLtAddTime(Date ltAddTime){
		this.ltAddTime = ltAddTime;
	}
	
	public Date getLtAddTime() {
		return this.ltAddTime;
	}
	
	
	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public Integer getRenqi() {
		return renqi;
	}

	public void setRenqi(Integer renqi) {
		this.renqi = renqi;
	}

	public Integer getNvshen() {
		return nvshen;
	}

	public void setNvshen(Integer nvshen) {
		this.nvshen = nvshen;
	}

	public int getBuyable() {
		return buyable;
	}

	public void setBuyable(int buyable) {
		this.buyable = buyable;
	}

	public int getGiftType() {
		return giftType;
	}

	public void setGiftType(int giftType) {
		this.giftType = giftType;
	}

	public int getGuardLevel() {
		return guardLevel;
	}

	public void setGuardLevel(int guardLevel) {
		this.guardLevel = guardLevel;
	}

	public int getMeili() {
		return meili;
	}

	public void setMeili(int meili) {
		this.meili = meili;
	}

	public int getSpecialFlag() {
		return specialFlag;
	}

	public void setSpecialFlag(int specialFlag) {
		this.specialFlag = specialFlag;
	}
	
}