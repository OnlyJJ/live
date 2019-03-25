package com.lm.live.common.utils;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * pojo类抽象方法
 * @author RSun
 * @Date 2013-6-15上午11:40:09
 */
public abstract class JsonParseInterface {
	
	/** 组织JSON数据方法 **/
	public abstract JSONObject buildJson();
	
	/** 解析JSON数据 **/
	public abstract void parseJson(JSONObject json);
	
	/** 对象标识 **/
//	public abstract String getShortName();
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	
	/** set int数据  **/
	protected void setInt(JSONObject json, String key, int value) throws Exception{
		if(!StrUtil.isNullOrEmpty(key)){
			json.put(key, value);
		}
	}
	
	/** set long数据  **/
	protected void setLong(JSONObject json, String key, long value) throws Exception{
		if(!StrUtil.isNullOrEmpty(key)){
			json.put(key, value);
		}
	}
	
	/** set boolean数据  **/
	protected void setBoolean(JSONObject json, String key, boolean value) throws Exception{
		if(!StrUtil.isNullOrEmpty(key)){
			json.put(key, value);
		}
	}
	
	/** set long数据  **/
	protected void setLong(JSONObject json, String key, Long value) throws Exception{
		if(!StrUtil.isNullOrEmpty(key)&&value!=null){
			json.put(key, value);
		}
	}
	
	/** set double数据  **/
	protected void setDouble(JSONObject json, String key, double value) throws Exception{
		if(!StrUtil.isNullOrEmpty(key) && 0 != value){
			json.put(key, value);
		}
	}
	
	/** set String数据  **/
	protected void setString(JSONObject json, String key, String value) throws Exception{
		if(!StrUtil.isNullOrEmpty(key) && !StrUtil.isNullOrEmpty(value)){
			json.put(key, value);
		}
	}
	
	protected void setBigDecimal(JSONObject json, String key,
			BigDecimal value) {
		if(!StrUtil.isNullOrEmpty(key) ){
			json.put(key, value);
		}
		
	}

	
	/** set JSONArray数据  **/
	protected void setJSONArray(JSONObject json, String key, JSONArray value) throws Exception{
		if(!StrUtil.isNullOrEmpty(key) && value!=null && value.size()>0){
			json.put(key, value);
		}
	}
	
	/** set JSONArray数据  **/
	protected void setList(JSONObject json, String key, List value) throws Exception{
		if(!StrUtil.isNullOrEmpty(key) && value!=null){
			json.put(key, value);
		}
	}
	
	/** get int数据  **/
	protected int getInt(JSONObject json, String key) throws Exception{
		int value = 0;
		if(!StrUtil.isNullOrEmpty(key) && json.containsKey(key)){
			try {
				value = json.getIntValue(key);
			} catch (Exception e) {
				
			}
		}
		return value;
	}
	
	/** get long数据  **/
	protected long getLong(JSONObject json, String key) throws Exception{
		long value = 0;
		if(!StrUtil.isNullOrEmpty(key) && json.containsKey(key)){
			value = json.getIntValue(key);
		}
		return value;
	}
	
	/** get long数据  **/
	protected boolean getBoolean(JSONObject json, String key) throws Exception{
		Boolean value = false;
		if(!StrUtil.isNullOrEmpty(key) && json.containsKey(key)){
			value = json.getBoolean(key);
		}
		return value;
	}
	
	/** get double数据  **/
	protected double getDouble(JSONObject json, String key) throws Exception{
		double value = 0;
		if(!StrUtil.isNullOrEmpty(key) && json.containsKey(key)){
			value = json.getDouble(key);
		}
		return value;
	}
	
	/** get String数据  **/
	protected String getString(JSONObject json, String key) throws Exception{
		String value = null;
		if(!StrUtil.isNullOrEmpty(key) && json.containsKey(key)){
			value = json.getString(key);
		}
		return value;
	}
	/** get JSONArray数据  **/
	protected JSONArray getJSONArray(JSONObject json, String key) throws Exception{
		JSONArray value = null;
		if(!StrUtil.isNullOrEmpty(key) && json.containsKey(key)){
			value = json.getJSONArray(key);
		}
		return value;
	}
	
	/** get JSONObject数据  **/
	protected JSONObject getJSONObject(JSONObject json, String key) throws Exception{
		JSONObject value = null;
		if(!StrUtil.isNullOrEmpty(key) && json.containsKey(key)){
			value = json.getJSONObject(key);
		}
		return value;
	}
}
