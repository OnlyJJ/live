package com.jiujun.shows.rank.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.common.utils.JsonParseInterface;
import com.jiujun.shows.common.utils.LogUtil;

/**
 * 榜单实体
 * @author shao.xiang
 * @date 2017-08-03
 *
 */
public class Rank extends JsonParseInterface implements Serializable{
	
	private static final long serialVersionUID = 2368152398366185592L;
	private static String r_typeId = "a";
	private static String r_kind = "b";
	private static String r_toUserId="w";
	
	private static String r_acitvitykName="c";
	private static String r_rankName="d";
	
	private int typeId;//1为明星榜、2为财富榜、3为手动刷新富豪榜
	private String kind;//时间段：d为日榜、w为周榜、m为月榜、t为总榜  ,yesterday昨天的榜单
	private String toUserId;
	
	/** 活动名称 */
	private String acitvitykName;
	
	/** 排行榜名称 */
	private String rankName;

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, r_typeId, typeId);
			setString(json, r_kind, kind);
			setString(json, r_toUserId, toUserId);
			setString(json, r_acitvitykName, acitvitykName);
			setString(json, r_rankName, rankName);
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null) 
			return ;
		try {
			typeId = getInt(json, r_typeId);
			kind = getString(json, r_kind);
			toUserId=getString(json, r_toUserId);
			acitvitykName =getString(json, r_acitvitykName);
			rankName =getString(json, r_rankName);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getAcitvitykName() {
		return acitvitykName;
	}

	public void setAcitvitykName(String acitvitykName) {
		this.acitvitykName = acitvitykName;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	
	
	
}
