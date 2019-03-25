package com.jiujun.shows.pay.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.pay.domain.AliPayNotifyRecord;

public interface AliPayNotifyRecordMapper extends ICommonMapper<AliPayNotifyRecord> {

	AliPayNotifyRecord getObjectByOrderId(String orderId);
}
