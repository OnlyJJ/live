package com.jiun.shows.others.report.service;


/**
 * Service - 应用统计管理表
 * @author shao.xiang
 * @date 2017-06-15
 */
public interface IDayStatMgrService {
	/**
	 * 说明：<br/>
	 * 		此方法只能由定时任务调用，其他业务不得调用<br/>
	 * 功能：<br/>
	 * 		1、处理统计表，每天生成一张以日期为结尾的表;<br/>
	 * 		2、当此类型表总数超过10张时，删除超过10天的表（只保存10天内的表）
	 * @throws Exception
	 */
	void handleTableTask() throws Exception;
}
