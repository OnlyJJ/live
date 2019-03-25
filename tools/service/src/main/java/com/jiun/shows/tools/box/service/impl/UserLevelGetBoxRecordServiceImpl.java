package com.jiun.shows.tools.box.service.impl;


import org.springframework.stereotype.Service;

import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.tools.box.domain.UserLevelGetBoxRecord;
import com.jiujun.shows.tools.box.service.IUserLevelGetBoxRecordService;
import com.jiun.shows.tools.box.dao.UserLevelGetBoxRecordMapper;


/**
 * 
 * @author shao.xiang
 * @date 2017-08-21
 */
@Service("userLevelGetBoxRecordService")
public class UserLevelGetBoxRecordServiceImpl extends CommonServiceImpl<UserLevelGetBoxRecordMapper,UserLevelGetBoxRecord> implements IUserLevelGetBoxRecordService {

}
