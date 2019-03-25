package com.lm.live.common.service.impl;


import com.lm.live.common.dao.ICommonMapper;
import com.lm.live.common.service.ICommonService;

/**
 * 基类，service服务基类实现需继承此类
 * @author shao.xiang
 * @date 2017-06-03
 * @param <D>
 * @param <R>
 */
public abstract class CommonServiceImpl<D extends ICommonMapper<R>, R> implements ICommonService<R> {

	protected D dao;

	public R getObjectById(Object id) {
		return dao.getObjectById(id);
	}

	public void removeById(Object id) {
		dao.removeById(id);
	}

	public R insert(R vo) {
		return dao.insert(vo);
	}

	public R update(R vo) {
		return dao.update(vo);
	}

}
