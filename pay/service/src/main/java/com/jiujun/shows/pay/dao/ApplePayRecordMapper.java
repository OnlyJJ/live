package com.jiujun.shows.pay.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.pay.domain.ApplePayRecordDo;

public interface ApplePayRecordMapper extends ICommonMapper<ApplePayRecordDo> {
	ApplePayRecordDo getObjectByTransactionId(String  transactionId);
}
