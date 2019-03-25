package com.jiujun.shows.pay.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.pay.domain.EcoPayNotifyRecord;

public interface EcoPayNotifyRecordMapper extends ICommonMapper<EcoPayNotifyRecord> {

	EcoPayNotifyRecord getObjectByOrderId(String merchOrderId);
}
