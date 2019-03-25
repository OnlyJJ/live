package com.jiun.shows.others.report.service;

import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiun.shows.others.report.vo.ReportVo;

/**
 * Service - 每日统计数据表
 */
public interface IDayStatService {
	/**
	 * 处理app使用信息，安装、登录、注册、激活
	 * @param vo
	 * @param dp
	 * @throws Exception
	 */
	void handleReport(ReportVo vo, DeviceProperties dp) throws Exception;
}
