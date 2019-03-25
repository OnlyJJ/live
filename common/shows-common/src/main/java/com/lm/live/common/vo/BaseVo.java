package com.lm.live.common.vo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize()
public abstract class BaseVo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6542701380619247792L;
	
	protected static final Logger log = Logger.getLogger(BaseVo.class);

	private String orderBy;
	
	private String descOrAsc;
	
	private int refreshType;
	
	private String appendWhere;
	
	private int pageNo; // 当前页码
	
	private static int   defaultPageNo = 1;
	
	private int pageSize; // 每页记录数
	
	private static int   defaultPageSize = 10;
	
	private int offset; //分页时的起点偏移量
	
	@JsonIgnore
	public int getRefreshType() {
		return refreshType;
	}

	public void setRefreshType(int refreshType) {
		this.refreshType = refreshType;
	}
	@JsonIgnore
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	@JsonIgnore
	public String getDescOrAsc() {
		return descOrAsc;
	}

	public void setDescOrAsc(String descOrAsc) {
		this.descOrAsc = descOrAsc;
	}
	
	@JsonIgnore
	public String getAppendWhere() {
		return appendWhere;
	}

	public void setAppendWhere(String appendWhere) {
		this.appendWhere = appendWhere;
	}

	
	public String orderSql(String defaultOrder){
		String orderBy = " order by ";
		if(StringUtils.isBlank(this.getOrderBy()))
			orderBy += defaultOrder;
		else 
			orderBy += this.getOrderBy();
		if(StringUtils.isNotBlank(this.getDescOrAsc()))
			orderBy += " " + this.getDescOrAsc();
		else
			orderBy += " desc ";
		return orderBy;
	}

	public int getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(int pageNo) {
		int val = 0;
		if(pageNo<=0){
			val = defaultPageNo;
		}else{
			val = pageNo;
		}
		this.pageNo = val;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		int val = 0;
		if(pageSize<=0){
			val =  defaultPageSize;
		}else{
			val =  pageSize;
		}
		this.pageSize = val;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getOffset() {
		return (getPageNo()-1)*getPageSize();
	}


}
