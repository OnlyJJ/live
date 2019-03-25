package com.lm.live.tools.gift.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;
/**
 * @entity
 * @table t_pay_gift_out
 * @author shao.xiang
 * @date 2017-06-25
 */
public class PayGiftOut extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private Integer id;
	/** 消费订单号(yyyyMMddHHmmssSSS+rand(0000~9999)) */
	private String orderId;
	/** 送出礼物的用户ID */
	private String userId;
	/** 接收礼物的用户ID */
	private String toUserId;
	/** 礼物ID，关联礼物表Id */
	private Integer giftId;
	/** 礼物数量 */
	private Integer number;
	/** 收到礼物对应的金币价格 */
	private int diamond;
	/** 送礼物时间 */
	private Date resultTime;
	/** 备注信息 */
	private String remark;
	/** 收到礼物对应的蓝钻数量(钻石跟金币对应比例) */
	private Integer price;
	private Integer gold;
	
	/** 关联表 t_gift */
	/** 礼物名称 name */
	private String name;
	/** 礼物对应金币价 */
	private String giftPrice;
	/** 礼物金币价值与主播钻石收入比例 */
	private String giftRate;
	

	/** 关联表  t_gift_out  */
	/** 送礼用户的昵称   */
	private String nickName;
	/** 收礼用户的昵称   */
	private String toNickName;
	
	/**判断送礼时间，大于或等于 */
	private String startTime;
	/**判断送礼时间，小于或等于 */
	private String endTime;
	
	
	/**主播收礼次数*/
	private Integer giftRecordCount;
	/** 收到礼物总数  */
	private Integer giftNumberSum;
	/** 收到钻石总数 */
	private Long diamondNumberSum;
	/** 钻石余额，关联t_user_account表*/
	private Long diamondNow;
	
	/** 查询条件  */
	private String groupBy;
	
	private String selectTime;
	
	private String income;

	/**来源类型,默认0:礼物,1:守护 */
	private int sourceType;

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getSelectTime() {
		return selectTime;
	}

	public void setSelectTime(String selectTime) {
		this.selectTime = selectTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setOrderId(String orderId){
		this.orderId = orderId;
	}
	
	public String getOrderId() {
		return this.orderId;
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
	
	public void setGiftId(Integer giftId){
		this.giftId = giftId;
	}
	
	public Integer getGiftId() {
		return this.giftId;
	}
	
	public void setNumber(Integer number){
		this.number = number;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	
	public int getDiamond() {
		return diamond;
	}

	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}

	public void setResultTime(Date resultTime){
		this.resultTime = resultTime;
	}
	
	public Date getResultTime() {
		return this.resultTime;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}
	
	public String getRemark() {
		return this.remark;
	}
	
	public Integer getGiftRecordCount() {
		return giftRecordCount;
	}

	public void setGiftRecordCount(Integer giftRecordCount) {
		this.giftRecordCount = giftRecordCount;
	}
	
	public Integer getGiftNumberSum() {
		return giftNumberSum;
	}

	public void setGiftNumberSum(Integer giftNumberSum) {
		this.giftNumberSum = giftNumberSum;
	}

	public Long getDiamondNumberSum() {
		return diamondNumberSum;
	}

	public void setDiamondNumberSum(Long diamondNumberSum) {
		this.diamondNumberSum = diamondNumberSum;
	}

	public Long getDiamondNow() {
		return diamondNow;
	}

	public void setDiamondNow(Long diamondNow) {
		this.diamondNow = diamondNow;
	}
	
	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getToNickName() {
		return toNickName;
	}

	public void setToNickName(String toNickName) {
		this.toNickName = toNickName;
	}

	
	public String getGiftPrice() {
		return giftPrice;
	}

	public void setGiftPrice(String giftPrice) {
		this.giftPrice = giftPrice;
	}

	public String getGiftRate() {
		return giftRate;
	}

	public void setGiftRate(String giftRate) {
		this.giftRate = giftRate;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}
	

}