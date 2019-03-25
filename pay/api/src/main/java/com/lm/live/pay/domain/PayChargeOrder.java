package com.lm.live.pay.domain;

import java.util.Date;

import com.lm.live.common.vo.BaseVo;


/**
 * @entity
 * @table t_pay_charge_order
 * @date 2015-12-01 16:32:40
 * @author charge
 */
public class PayChargeOrder extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** id */
	private Integer id;
	/** 充值订单号：yyyyMMddHHmmssSSS+rand(0000~9999) */
	private String orderId;
	/** 充值方式 */
	private Integer pay_type;
	/** 用户ID */
	private String userId;
	/** 订单状态:1-生成订单，2-提交订单，3-充值失败，4-充值成功，5-同步成功 */
	private Integer orderStatus;
	/** 选择充值金额，单位：分 */
	private Integer selectMoney;
	/** 实际充值金额，单位：分 */
	private Integer money;
	/** 充值时间 */
	private Date chargeTime;
	/** 充值成功时间 */
	private Date resultTime;
	/** 充值信息 */
	private String remark;
	/** 同步(发货/兑换金币)时间 */
	private Date syncTime;
	/** 获得金币数量 */
	private Integer golds;
	/** 用户名*/
	private String userAccount;
	/** 邮箱*/
	private String bindEmail;
	/** 手机号码*/
	private String bindMobile;
	/** 支付方式名称*/
	private String payTypeName;
	/** 生成订单环境 1.web 2.安卓  3.ios*/
	private int createType;
	
	/**查询条件*/
	private String code;
	
	private String startTime;
	private String endTime;
	
	/** 渠道号 */
	private String channelId;
	
	/** 订单是否通过沙箱服务认证充值成功  */
	private Integer isPayBySanBox;
	
	/** 充值代理人userId  */
	private String agentUserId;
	
	/** 生成订单时的ip  */
	private String generateOrderIp;
	
	/** 生成订单时的地址  */
	private String generateOrderAddr;
	
	/** 充值成功时的ip  */
	private String paySuccessNotifyIp;
	
	/** 充值成功时的地址  */
	private String paySuccessNotifyAddr;
	
	/** 对接端服务器的订单号  */
	private String transactionId;
	/** 包名 */
	private String pkgName;
	/** 打包id，渠道+包名 */
	private String douId;
	/** 留存天数，以注册日期为起点，到当前充值时间 */
	private int retentionVaild;
	
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
	
	public void setPay_type(Integer pay_type){
		this.pay_type = pay_type;
	}
	
	public Integer getPay_type() {
		return this.pay_type;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getUserId() {
		return this.userId;
	}
	

	
	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setSelectMoney(Integer selectMoney){
		this.selectMoney = selectMoney;
	}
	
	public Integer getSelectMoney() {
		return this.selectMoney;
	}
	
	public void setMoney(Integer money){
		this.money = money;
	}
	
	public Integer getMoney() {
		return this.money;
	}
		
	public Date getChargeTime() {
		return chargeTime;
	}

	public void setChargeTime(Date chargeTime) {
		this.chargeTime = chargeTime;
	}

	public Date getResultTime() {
		return resultTime;
	}

	public void setResultTime(Date resultTime) {
		this.resultTime = resultTime;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}
	
	public String getRemark() {
		return this.remark;
	}
	
	public void setSyncTime(Date syncTime){
		this.syncTime = syncTime;
	}
	
	public Date getSyncTime() {
		return this.syncTime;
	}
	
	public void setGolds(Integer golds){
		this.golds = golds;
	}
	
	public Integer getGolds() {
		return this.golds;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getBindEmail() {
		return bindEmail;
	}

	public void setBindEmail(String bindEmail) {
		this.bindEmail = bindEmail;
	}

	public String getBindMobile() {
		return bindMobile;
	}

	public void setBindMobile(String bindMobile) {
		this.bindMobile = bindMobile;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
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

	public int getCreateType() {
		return createType;
	}

	public void setCreateType(int createType) {
		this.createType = createType;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getIsPayBySanBox() {
		return isPayBySanBox;
	}

	public void setIsPayBySanBox(Integer isPayBySanBox) {
		this.isPayBySanBox = isPayBySanBox;
	}

	public String getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(String agentUserId) {
		this.agentUserId = agentUserId;
	}

	public String getGenerateOrderIp() {
		return generateOrderIp;
	}

	public void setGenerateOrderIp(String generateOrderIp) {
		this.generateOrderIp = generateOrderIp;
	}

	public String getPaySuccessNotifyIp() {
		return paySuccessNotifyIp;
	}

	public void setPaySuccessNotifyIp(String paySuccessNotifyIp) {
		this.paySuccessNotifyIp = paySuccessNotifyIp;
	}

	public String getGenerateOrderAddr() {
		return generateOrderAddr;
	}

	public void setGenerateOrderAddr(String generateOrderAddr) {
		this.generateOrderAddr = generateOrderAddr;
	}

	public String getPaySuccessNotifyAddr() {
		return paySuccessNotifyAddr;
	}

	public void setPaySuccessNotifyAddr(String paySuccessNotifyAddr) {
		this.paySuccessNotifyAddr = paySuccessNotifyAddr;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public String getDouId() {
		return douId;
	}

	public void setDouId(String douId) {
		this.douId = douId;
	}

	public int getRetentionVaild() {
		return retentionVaild;
	}

	public void setRetentionVaild(int retentionVaild) {
		this.retentionVaild = retentionVaild;
	}
	
}