package com.lm.live.common.dao;


/**
 * service服务基类
 * @author shao.xiang
 * @param <R>
 * @date 2016-05-23
 */

public interface ICommonMapper<R> {

	public R getObjectById(Object id);

	public R insert(R vo);

	public R update(R vo);

	public void removeById(Object id);

}
