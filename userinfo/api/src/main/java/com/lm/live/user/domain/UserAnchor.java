package com.lm.live.user.domain;

import com.lm.live.common.vo.BaseVo;


/**
 * @entity
 * @table t_user_anchor
 * @date 2015-12-08 14:31:50
 * @author user
 */
public class UserAnchor extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private Integer id;
	/** 用户ID */
	private String userId;
	/** 房间号ID，系统自动生成烂号，或者主播花钱购买靓号 */
	private String roomId;
	/** 真实姓名 */
	private String name;
	/** 男 OR 女 OR 未知 */
	private String sex;
	/** 身份证号码 */
	private String idCardNO;
	/** 身份证照片(正面) */
	private String idCardImg1;
	/** 身份证照片(反面) */
	private String idCardImg2;
	/** 身份证照片(手持身份证正面上半身照) */
	private String idCardImg3;
	/** 开户银行名称 */
	private String bank;
	/** 开户人姓名 */
	private String bankAccount;
	/** 开户支行名称 */
	private String subBankAddr;
	/** 银行卡号 */
	private String bankNO;
	/** 联系人电话 */
	private String mobile;
	/** 联系人QQ号 */
	private String qq;
	/** 联系人地址 */
	private String address;
	/** 直播视频截图 */
	private String showImg;
	/** 家族ID，关联家族表ID */
	private Integer familyId;
	/** 主播申请签约时间 */
	private String addTime;
	/** 审核通过时间 */
	private String actTime;
	/** 主播状态，0-正常; 1-停用; 2-待审核; 3-审核不通过 */
	private Integer anchorStatus;
	/** 开户银行地址(省市) */
	private String bankAddr;

	private String remark;

	/** 是否禁播 */
	private Integer isForbidden;
	/** 是否显示主播等级图标 */
	private boolean isLevelIcon;

	/** 粉丝数量 (被关注) */
	private long fansCount;
	
	/** 主播分类，默认0-无分类，1-女神，2-好声音 */
	private int anchorStyle;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomId() {
		return this.roomId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return this.sex;
	}

	public void setIdCardNO(String idCardNO) {
		this.idCardNO = idCardNO;
	}

	public String getIdCardNO() {
		return this.idCardNO;
	}

	public void setIdCardImg1(String idCardImg1) {
		this.idCardImg1 = idCardImg1;
	}

	public String getIdCardImg1() {
		return this.idCardImg1;
	}

	public void setIdCardImg2(String idCardImg2) {
		this.idCardImg2 = idCardImg2;
	}

	public String getIdCardImg2() {
		return this.idCardImg2;
	}

	public void setIdCardImg3(String idCardImg3) {
		this.idCardImg3 = idCardImg3;
	}

	public String getIdCardImg3() {
		return this.idCardImg3;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBank() {
		return this.bank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public void setSubBankAddr(String subBankAddr) {
		this.subBankAddr = subBankAddr;
	}

	public String getSubBankAddr() {
		return this.subBankAddr;
	}

	public void setBankNO(String bankNO) {
		this.bankNO = bankNO;
	}

	public String getBankNO() {
		return this.bankNO;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getQq() {
		return this.qq;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setShowImg(String showImg) {
		this.showImg = showImg;
	}

	public String getShowImg() {
		return this.showImg;
	}

	public void setFamilyId(Integer familyId) {
		this.familyId = familyId;
	}

	public Integer getFamilyId() {
		return this.familyId;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getAddTime() {
		return this.addTime;
	}

	public void setActTime(String actTime) {
		this.actTime = actTime;
	}

	public String getActTime() {
		return this.actTime;
	}

	public void setAnchorStatus(Integer anchorStatus) {
		this.anchorStatus = anchorStatus;
	}

	public Integer getAnchorStatus() {
		return this.anchorStatus;
	}

	public String getBankAddr() {
		return bankAddr;
	}

	public void setBankAddr(String bankAddr) {
		this.bankAddr = bankAddr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getFansCount() {
		return fansCount;
	}

	public void setFansCount(long fansCount) {
		this.fansCount = fansCount;
	}

	public Integer getIsForbidden() {
		if (isForbidden == null) {
			return 0;
		} else {
			return isForbidden;
		}

	}

	public void setIsForbidden(Integer isForbidden) {
		if (isForbidden == null) {
			this.isForbidden = 0;
		} else {
			this.isForbidden = isForbidden;
		}
	}

	public boolean getIsLevelIcon() {
		return isLevelIcon;
	}

	public void setIsLevelIcon(boolean isLevelIcon) {
		this.isLevelIcon = isLevelIcon;
	}

	public int getAnchorStyle() {
		return anchorStyle;
	}

	public void setAnchorStyle(int anchorStyle) {
		this.anchorStyle = anchorStyle;
	}

}