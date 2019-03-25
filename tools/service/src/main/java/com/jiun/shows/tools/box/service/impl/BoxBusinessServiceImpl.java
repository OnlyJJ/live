package com.jiun.shows.tools.box.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.tools.box.service.IBoxBusinessService;
import com.jiujun.shows.tools.vo.ToolVo;
import com.jiun.shows.tools.box.biz.BoxBusinessBiz;



@Service
public class BoxBusinessServiceImpl implements IBoxBusinessService {

	@Resource
	private BoxBusinessBiz boxBusinessBiz;
	@Override
	public void openLevelBox(String userId, String roomId, String imToken,
			ToolVo toolsVo) throws Exception {
		boxBusinessBiz.openLevelBox(userId, roomId, imToken, toolsVo);
	}

}
