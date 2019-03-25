package com.jiujun.shows.pay.dao;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.pay.domain.AppleProductDo;

public interface AppleProductMapper extends ICommonMapper<AppleProductDo> {

	AppleProductDo getObjectByProductId(String  productId);	

}
