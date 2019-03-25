package com.jiun.shows.others.report.domain;

import java.util.Date;

import com.jiujun.shows.common.vo.BaseVo;



/**
* DayStatMgr
 * 应用统计管理表
 * @entity
 * @table t_day_statmgr
 * @author shao.xiang
 * @date 2017-06-15
*/
public class DayStatMgr extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4434070003493869953L;
	
	private int id;
	/**
	 * 表名
	 */
	private String tbName;
	/**
	 * 添加时间
	 */
	private Date addTime;
	
	private String tableName;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTbname() {
		return this.tbName;
	}
	
	public void setTbname(String tbName) {
		this.tbName = tbName;
	}
	public Date getAddtime() {
		return this.addTime;
	}
	
	public void setAddtime(Date addTime) {
		this.addTime = addTime;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
