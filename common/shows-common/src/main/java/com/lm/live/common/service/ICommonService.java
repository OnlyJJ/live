package com.lm.live.common.service;

/**
 * service基类，各个service需要继承此类
 * @author shao.xiang
 * @date 2017-06-03
 */
public interface ICommonService<R> {

	public R getObjectById(Object id);

	public R insert(R vo);

	public R update(R vo);
	
	public void removeById(Object id);

}
