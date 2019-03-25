package com.lm.live.common.utils;

import java.util.Timer;
import java.util.TimerTask;


public class NoticeEndTask extends TimerTask {
	
	private Timer timer;
	
	public NoticeEndTask(Timer timer) {
		this.timer = timer;
	}
	@Override
	public void run() {
		try {
			LogUtil.log.info("######关闭后台配置系统公告timer="+timer.getClass().getName().toString());
			timer.cancel();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.log.error("######关闭后台配置系统公告timer-失败,timer="+timer.getClass().getName().toString());
		}
	}
	
	
}
