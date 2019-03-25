package com.lm.live.tools.vo;

import java.io.File;
import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.constant.BaseConstants;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class ToolVo  extends JsonParseInterface implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3977344269230063939L;
	//字段key
	private static final String t_type = "a";
	private static final String t_name = "b";
	private static final String t_info = "c"; 
	private static final String t_image = "d";
	private static final String t_gold = "e";
	private static final String t_number = "f"; 
	private static final String t_buyAble = "g";
	private static final String t_toolId = "h";
	
	/** 类型,3:大喇叭(传送门),4:钥匙(用户等级宝箱),5:用户等级宝箱，6蜜桃种子，7隔空喊话*/
	private int type; 
	/** 名称  */
	private String name; 
	/** 说明 */
	private String info; 
	/** 图片 */
	private String image;
	/** 花费金币  */
	private int gold;
	
	/** 数量 */
	private int number;
	
	/** 是否可购买  */
	private int buyAble;
	
	/** 工具id  */
	private int toolId;
	
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try{
			setInt(json, t_type, type);
			setString(json, t_name, name);
			setString(json, t_info , info);
			if( null != image ) {
				if(image.indexOf(BaseConstants.TOOL_IMG_FILE_URI) == -1) {
					setString(json, t_image, BaseConstants.cdnPath+File.separator+BaseConstants.TOOL_IMG_FILE_URI+File.separator+image);
				} else {
					setString(json, t_image, image);
				}
			}
			setInt(json, t_gold , gold);
			setInt(json, t_number, number);
			setInt(json, t_buyAble, buyAble);
			setInt(json, t_toolId, toolId);
		}catch(Exception e){
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if(json == null) return;
		try{
			type = getInt(json , t_type);
			name = getString(json , t_name);
			info = getString(json , t_info);
			image = getString(json , t_image);
			gold = getInt(json , t_gold);
			number = getInt(json , t_number);
		}catch(Exception e){
			LogUtil.log.error(e.getMessage(),e);
		}
		
	}

	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
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

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}
	
}
