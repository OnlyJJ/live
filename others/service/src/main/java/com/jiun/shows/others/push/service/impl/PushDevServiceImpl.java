package com.jiun.shows.others.push.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiun.shows.others.push.biz.PushDevBiz;
import com.jiun.shows.others.push.domain.PushDev;
import com.jiun.shows.others.push.service.IPushDevService;


/**
 * Service -客户端推送消息设备信息记录表
 */
@Service("pushDevServiceImpl")
public class PushDevServiceImpl implements IPushDevService{

	@Resource
	private PushDevBiz pushDevBiz;
	
	@Override
	public void savePushDev(String userId, String token, int appType,
			String pckName) throws Exception {
		pushDevBiz.savePushDev(userId, token, appType, pckName);
	}

	@Override
	public void pushLiveStartMsg(String anchorId, Map<String, Object> custom) {
		pushDevBiz.pushLiveStartMsg(anchorId, custom);
	}

	@Override
	public void pushMSGByConfig(int configId) throws Exception {
		pushDevBiz.pushMSGByConfig(configId);
	}

	@Override
	public PushDev getPushDevFromCache(String userId, String token)
			throws Exception {
		return pushDevBiz.getPushDevFromCache(userId, token);
	}

	@Override
	public void pushAndroidSingleAccount(int deviceType, String userId,
			long accessId, String secretKey, String title, String content) {
		pushDevBiz.pushAndroidSingleAccount(deviceType, userId, accessId, secretKey, title, content);
	}

	@Override
	public void pushIOSSingleAccount(int deviceType, String userId,
			long accessId, String secretKey, String title, String content) {
		pushDevBiz.pushIOSSingleAccount(deviceType, userId, accessId, secretKey, title, content);
	}

	@Override
	public void pushAndroidAccountList(List<String> accountList,
			Map<String, Object> parms, Map<String, Object> custom) {
		pushDevBiz.pushAndroidAccountList(accountList, parms, custom);
	}

	@Override
	public void pushIOSAccountList(List<String> accountList,
			Map<String, Object> parms, Map<String, Object> custom) {
		pushDevBiz.pushIOSAccountList(accountList, parms, custom);
	}
}