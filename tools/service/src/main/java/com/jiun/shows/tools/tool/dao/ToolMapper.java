package com.jiun.shows.tools.tool.dao;


import com.jiujun.shows.common.dao.ICommonMapper;
import com.jiujun.shows.tools.enums.GiftTypeBusinessEnum;
import com.jiujun.shows.tools.tool.domain.Tool;

/**
 * 
 * @author shao.xiang
 * @Date 2017-08-20
 *
 */
public interface ToolMapper extends ICommonMapper<Tool> {
	/**
	 * 查找用户的工具箱数量
	 * @param userId
	 * @param type
	 * @return
	 */
	int getUserToolNum(String userId, GiftTypeBusinessEnum type);
}
