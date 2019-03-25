package com.jiun.shows.tools.box.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.common.service.impl.CommonServiceImpl;
import com.jiujun.shows.tools.box.domain.BoxGiftConf;
import com.jiujun.shows.tools.box.service.IBoxGiftConfService;
import com.jiun.shows.tools.box.biz.BoxGiftConfBiz;
import com.jiun.shows.tools.box.dao.BoxGiftConfMapper;


/**
 * 
 * @author shao.xiang
 * @date 2016-08-20 
 */
@Service("boxGiftConfService")
public class BoxGiftConfServiceImpl extends CommonServiceImpl<BoxGiftConfMapper, BoxGiftConf> implements IBoxGiftConfService {

	@Resource
	private BoxGiftConfBiz boxGiftConfBiz;
	
	@Override
	public List<BoxGiftConf> findBoxGiftConfInAscSortByRate(int boxId) {
		return boxGiftConfBiz.findBoxGiftConfInAscSortByRate(boxId);
	}
	
}
