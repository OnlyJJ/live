package com.jiun.shows.guard.biz;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.common.constant.MCPrefix;
import com.jiujun.shows.common.constant.MCTimeoutConstants;
import com.jiujun.shows.common.utils.MemcachedUtil;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.guard.domain.GuardWorkConf;
import com.jiujun.shows.guard.enums.ErrorCode;
import com.jiujun.shows.guard.exception.GuardBizException;
import com.jiun.shows.guard.dao.GuardWorkConfMapper;
import com.mysql.jdbc.StringUtils;


/**
 * 守护配置服务
 * @author shao.xiang
 * @date 2017-06-27
 */
@Service("guardWorkConfBiz")
public class GuardWorkConfBiz {

	@Resource
	private GuardWorkConfMapper guardWorkConfMapper;

	/**
	 * 获取房间守护配置信息,缓存
	 * @return
	 * @throws Exception
	 */
	public ServiceResult<GuardWorkConf> getGuardWorkConfDataCache(String roomId) throws Exception {
		ServiceResult<GuardWorkConf> srt = new ServiceResult<GuardWorkConf>();
		srt.setSucceed(false);
		if(StringUtils.isNullOrEmpty(roomId)) {
			Exception e = new GuardBizException(ErrorCode.ERROR_12001);
			throw e;
		}
		GuardWorkConf conf = null;
		String key = MCPrefix.GUARD_WORK_CONF_INFO_DB_CACHE + roomId;
		Object obj = MemcachedUtil.get(key);
		if(obj != null) {
			conf = (GuardWorkConf) obj;
		} else {
			MemcachedUtil.delete(key);
			conf = guardWorkConfMapper.getGuardWorkConfData(roomId);
			if(conf != null) {
				MemcachedUtil.set(key, conf, MCTimeoutConstants.GUARD_WORK_CONF_CACHE_TIME);
			}
		}
		srt.setSucceed(true);
		srt.setData(conf);
		return srt;
	}
	
}
