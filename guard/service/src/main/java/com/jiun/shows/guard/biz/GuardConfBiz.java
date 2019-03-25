package com.jiun.shows.guard.biz;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiujun.shows.common.constant.MCPrefix;
import com.jiujun.shows.common.constant.MCTimeoutConstants;
import com.jiujun.shows.common.utils.MemcachedUtil;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.guard.constant.Constants;
import com.jiujun.shows.guard.domain.GuardConf;
import com.jiun.shows.guard.dao.GuardConfMapper;


/**
 * Service -守护配置表
 */
@Service("guardConfBiz")
public class GuardConfBiz {
	
	private static final Logger log = Logger.getLogger(Constants.LOG_GUARD_SERVICE);

	@Resource
	private GuardConfMapper guardConfMapper;

	
	public ServiceResult<GuardConf> getGuardConfData(int guardType, int priceType) throws Exception {
		ServiceResult<GuardConf> srt = new ServiceResult<GuardConf>();
		srt.setSucceed(true);
		GuardConf gc = guardConfMapper.getGuardConfData(guardType, priceType);
		srt.setData(gc);
		return srt;
	}

	
	public ServiceResult<List<GuardConf>> getGuardConfAllDataCache() throws Exception {
		ServiceResult<List<GuardConf>> srt = new ServiceResult<List<GuardConf>>();
		srt.setSucceed(true);
		List<GuardConf> list = null;
		String key = MCPrefix.GUARD_CONF_ALL_CACHE;
		Object obj = MemcachedUtil.get(key);
		if(obj != null) {
			list = (List<GuardConf>) obj;
		} else {
			MemcachedUtil.delete(key);
			list = guardConfMapper.getGuardConfAllData();
			if(list != null) {
				MemcachedUtil.set(key, list, MCTimeoutConstants.GUARD_CONF_ALL_DATA_CACHE);
			}
		}
		srt.setData(list);
		return srt;
	}
}
