package com.lm.live.login.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.login.dao.UuidBlackListMapper;
import com.lm.live.login.domain.UuidBlackList;
import com.lm.live.login.enums.ErrorCode;
import com.lm.live.login.exceptions.LoginBizException;
import com.lm.live.login.service.IUuidBlackListService;

@Service("uuidBlackListServiceImpl")
public class UuidBlackListServiceImpl extends CommonServiceImpl<UuidBlackListMapper, UuidBlackList> implements IUuidBlackListService {

	@Override
	public void checkBlackList(String uuid) throws Exception {
		UuidBlackList blackList = dao.getBlackListByUuid(uuid);
		if (blackList != null) {
			if(blackList.getAddTime() == null || blackList.getEndTime() == null){//没配置开始结束时间的，直接抛异常
				if(blackList.getEndTime() == null){
					throw new LoginBizException(ErrorCode.ERROR_103);
				}
			}
			
			if(blackList.getAddTime() != null && blackList.getEndTime() != null){
				Date now = new Date();
				if(now.after(blackList.getAddTime()) && now.before(blackList.getEndTime())){
					throw new LoginBizException(ErrorCode.ERROR_103);
				}
			}
		}
		
	}

}
