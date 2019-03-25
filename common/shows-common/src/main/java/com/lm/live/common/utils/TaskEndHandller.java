package com.lm.live.common.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时任务完成后，需要关闭处理
 * @author shao
 *
 */
public class TaskEndHandller extends TimerTask {
	
	private Timer timer;
	
	public TaskEndHandller(Timer timer) {
		this.timer = timer;
	}
	@Override
	public void run() {
		try {
			LogUtil.log.info("######关闭定时任务：timer="+timer.getClass().getName().toString());
			timer.cancel();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.log.error("######关闭定时任务：timer-失败,timer="+timer.getClass().getName().toString());
		}
	}
	
	
}
