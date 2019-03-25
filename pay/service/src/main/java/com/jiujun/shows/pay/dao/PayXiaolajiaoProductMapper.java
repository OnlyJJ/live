package com.jiujun.shows.pay.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.pay.domain.PayXiaolajiaoProduct;

public interface PayXiaolajiaoProductMapper extends ICommonMapper<PayXiaolajiaoProduct> {

	PayXiaolajiaoProduct getByProductBizId(String productBizId);
	
	PayXiaolajiaoProduct getByProductPackId(String productPackId);
}
