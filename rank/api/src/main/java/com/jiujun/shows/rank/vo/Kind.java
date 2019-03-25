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
public class Kind extends JsonParseInterface implements Serializable{
	
	private static final long serialVersionUID = 2229022950311287560L;
	private static final String k_kindId = "a";
	private static final String k_type = "b";
	
	/** 类别id 对应 t_common_tree 的treeId*/
	private Integer kindId;
	/** 哪个种类  1:直播 */
	private Integer type;
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setInt(json, k_kindId, kindId);
			setInt(json, k_type, type);
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
			kindId = getInt(json, k_kindId);
			type = getInt(json, k_type);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	@Override
	public String toString() {
		return "Kind [kindId=" + this.kindId + ", type=" + this.type +"]";
	}

	public Integer getKindId() {
		return kindId;
	}

	public void setKindId(Integer kindId) {
		this.kindId = kindId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
