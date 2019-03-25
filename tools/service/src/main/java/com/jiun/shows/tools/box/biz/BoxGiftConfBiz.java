package com.jiun.shows.tools.box.biz;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.tools.box.domain.BoxGiftConf;
import com.jiun.shows.tools.box.dao.BoxGiftConfMapper;


/**
 * 
 * @author shao.xiang
 * @date 2017-08-20
 */
@Service("boxGiftConfBiz")
public class BoxGiftConfBiz {

	@Resource
	private BoxGiftConfMapper boxGiftConfMapper;

	public List<BoxGiftConf> findBoxGiftConfInAscSortByRate(int boxId) {
		return boxGiftConfMapper.findBoxGiftConfInAscSortByRate(boxId);
	}
	
}
