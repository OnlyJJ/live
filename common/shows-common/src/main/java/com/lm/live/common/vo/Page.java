package com.lm.live.common.vo;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class Page extends JsonParseInterface implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2229022950311287560L;
	private String count;//	a	项目总数
	private String pageNum;//	b	页码
	private String pagelimit;//	c	单页容量
	private String dataJsonStr; // 返回查询数据
	private List data;
	
	private static final String u_count = "a";
	private static final String u_pageNum = "b";
	private static final String u_pagelimit = "c";
	private static final String u_dataJsonStr = "d";
	
	/* (non-Javadoc)
	 * @see com.lm.live.common.utils.JsonParseInterface#buildJson()
	 */
	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, u_count, count);
			setString(json, u_pageNum, pageNum);
			setString(json, u_pagelimit, pagelimit);
			setString(json, u_dataJsonStr, dataJsonStr);
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
			count = getString(json, u_count);
			pageNum = getString(json, u_pageNum);
			pagelimit = getString(json, u_pagelimit);
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
		return "Page [count=" + count + ", pageNum=" + pageNum + ", pagelimit="
				+ pagelimit +  "]";
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getPagelimit() {
		return pagelimit;
	}

	public void setPagelimit(String pagelimit) {
		this.pagelimit = pagelimit;
	}

	public String getDataJsonStr() {
		return dataJsonStr;
	}

	public void setDataJsonStr(String dataJsonStr) {
		this.dataJsonStr = dataJsonStr;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}
	

}
