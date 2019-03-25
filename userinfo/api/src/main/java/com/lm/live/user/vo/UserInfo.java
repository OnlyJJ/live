package com.lm.live.user.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.vo.UserBaseInfo;
import com.lm.live.decorate.domain.Decorate;
import com.lm.live.decorate.vo.DecoratePackageVo;
import com.lm.live.pet.vo.PetVo;

/**
 * 用户信息
 * 
 * @author shao.xiang
 * @date 2018年3月13日
 *
 */
public class UserInfo extends UserBaseInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 拥有的金币数 */
	private int gold;
	/** 关注人数 */
	private int attentionCount;
	/** 粉丝数（被关注人数） */
	private int fans;
	/** 是否首次登录，0-否，1-是 */
	private int isFirttimeLogin;
	/** 生日 */
	private String brithday;
	/** 地址 */
	private String address;
	/** 个性签名 */
	private String remark;
	/** 用户等级 */
	private long userPoint;
	/** 主播等级 */
	private long anchorPoint;
	/** 下一用户等级所需经验 */
	private long nextLevelUserPoint;
	/** 下一主播等级所需经验 */
	private long nextLevelAnchorPoint;
	/** 星座 */
	private String constellatory;
	/** 正在使用的座驾 */
	private PetVo petVo;
	/** 拥有的勋章 */
	private List<Decorate> decorate;

	// 字段key
	private static final String u_gold = "a";
	private static final String u_attentionCount = "b";
	private static final String u_fans = "c";
	private static final String u_isFirttimeLogin = "d";
	private static final String u_brithday = "e";
	private static final String u_address = "f";
	private static final String u_remark = "g";
	private static final String u_userPoint = "h";
	private static final String u_anchorPoint = "i";
	private static final String u_nextLevelUserPoint = "j";
	private static final String u_nextLevelAnchorPoint = "k";
	private static final String u_constellatory = "l";
	private static final String u_petVo = "m";
	private static final String u_decorate = "n";

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		json = super.buildJson();
		try {
			setInt(json, u_gold, gold);
			setInt(json, u_attentionCount, attentionCount);
			setInt(json, u_fans, fans);
			setInt(json, u_isFirttimeLogin, isFirttimeLogin);
			setString(json, u_brithday, brithday);
			setString(json, u_address, address);
			setString(json, u_remark, remark);
			setLong(json, u_userPoint, userPoint);
			setLong(json, u_anchorPoint, anchorPoint);
			setLong(json, u_nextLevelUserPoint, nextLevelUserPoint);
			setLong(json, u_nextLevelAnchorPoint, nextLevelAnchorPoint);
			setString(json, u_constellatory, constellatory);
			// 宠物座驾json
			if (petVo != null) {
				json.put(u_petVo, petVo.buildJson());
			}
			List<JSONObject> decorateListVo = new ArrayList<JSONObject>();
			if (decorate != null) {
				for (Decorate c : decorate) {
					DecoratePackageVo d = new DecoratePackageVo();
					d.setDecorateId(c.getId());
					d.setName(c.getName());
					d.setLightenImg(c.getLightenimg());
					decorateListVo.add(d.buildJson());
				}
			}
			setList(json, u_decorate, decorateListVo);

			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null)
			return;
		try {
			isFirttimeLogin = getInt(json, u_isFirttimeLogin);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		}
	}

	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public int getIsFirttimeLogin() {
		return isFirttimeLogin;
	}

	public void setIsFirttimeLogin(int isFirttimeLogin) {
		this.isFirttimeLogin = isFirttimeLogin;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getAttentionCount() {
		return attentionCount;
	}

	public void setAttentionCount(int attentionCount) {
		this.attentionCount = attentionCount;
	}

	public int getFans() {
		return fans;
	}

	public void setFans(int fans) {
		this.fans = fans;
	}

	public String getBrithday() {
		return brithday;
	}

	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getUserPoint() {
		return userPoint;
	}

	public void setUserPoint(long userPoint) {
		this.userPoint = userPoint;
	}

	public long getAnchorPoint() {
		return anchorPoint;
	}

	public void setAnchorPoint(long anchorPoint) {
		this.anchorPoint = anchorPoint;
	}

	public long getNextLevelUserPoint() {
		return nextLevelUserPoint;
	}

	public void setNextLevelUserPoint(long nextLevelUserPoint) {
		this.nextLevelUserPoint = nextLevelUserPoint;
	}

	public long getNextLevelAnchorPoint() {
		return nextLevelAnchorPoint;
	}

	public void setNextLevelAnchorPoint(long nextLevelAnchorPoint) {
		this.nextLevelAnchorPoint = nextLevelAnchorPoint;
	}

	public String getConstellatory() {
		return constellatory;
	}

	public void setConstellatory(String constellatory) {
		this.constellatory = constellatory;
	}

	public PetVo getPetVo() {
		return petVo;
	}

	public void setPetVo(PetVo petVo) {
		this.petVo = petVo;
	}

	public List<Decorate> getDecorate() {
		return decorate;
	}

	public void setDecorate(List<Decorate> decorate) {
		this.decorate = decorate;
	}

}
