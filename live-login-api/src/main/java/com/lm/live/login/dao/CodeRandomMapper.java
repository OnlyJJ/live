package com.lm.live.login.dao;

import java.util.List;

import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.login.domain.CodeRandom;

public interface CodeRandomMapper extends ICommonMapper<CodeRandom> {
	
	/**
	 * 随机获取一组code
	 * @return
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	List<CodeRandom> listCodeRandom();
	
	/**
	 * 更新code使用状态
	 * @param code
	 * @author shao.xiang
	 * @date 2018年3月10日
	 */
	void updateStatus(String code);
}
