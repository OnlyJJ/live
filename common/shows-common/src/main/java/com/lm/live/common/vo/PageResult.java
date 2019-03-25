package com.lm.live.common.vo;

/**
 * 分页返回数据 
 *
 */
public class PageResult {
	
	/** 分页信息  */
	private Object page;
	
	/** 实际数据  */
	private Object data;

	public Object getPage() {
		return page;
	}

	public void setPage(Object page) {
		this.page = page;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


}
