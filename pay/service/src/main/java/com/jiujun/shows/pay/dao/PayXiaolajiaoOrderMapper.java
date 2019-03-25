package com.jiujun.shows.pay.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.pay.domain.PayXiaolajiaoOrder;

public interface PayXiaolajiaoOrderMapper extends ICommonMapper<PayXiaolajiaoOrder> {

	PayXiaolajiaoOrder getByOrderId(String paramOrderId);

}
