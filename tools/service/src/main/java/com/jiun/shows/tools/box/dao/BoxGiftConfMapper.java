package com.jiun.shows.tools.box.dao;


import java.util.List;

import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.tools.box.domain.BoxGiftConf;

/**
 * 
 * @author shao.xiang
 * @Date 2017-08-20
 *
 */
public interface BoxGiftConfMapper extends ICommonMapper<BoxGiftConf> {
	
	List<BoxGiftConf> findBoxGiftConfInAscSortByRate(int boxIdOfBronze);
}
