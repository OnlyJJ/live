package com.jiun.shows.others.push.dao;

import java.util.List;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiun.shows.others.push.domain.PushDev;

/**
 * 
 * @author shao.xiang
 * @Date 2017-06-15
 *
 */
public interface PushDevMapper extends ICommonMapper<PushDev> {
	
	PushDev getPushDev(String userId, String token);
	
	List<String> listAndroidFans(String anchorId);
	
	List<String> listIOSFans(String anchorId, String pckName);
	
	List<String> listByApptype(int appType);
}
