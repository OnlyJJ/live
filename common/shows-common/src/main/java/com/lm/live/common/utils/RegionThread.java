package com.lm.live.common.utils;

import com.lm.live.common.constant.MCPrefix;

/**
 * 根据ip加载归属地
 * @author Administrator
 */
public class RegionThread extends Thread {
	public RegionThread(String ip) {
		super(ip);
	}
	public void run() {
		LogUtil.log.info("###RegionThread start...");
		String ip = super.getName();
		if (!StrUtil.isNullOrEmpty(ip)) {
			String code = IpUtils.getTaobaoRegionByIp(ip);//淘宝获取归属地省市id
			if (!StrUtil.isNullOrEmpty(code)) {
				String[] ip_split = ip.split("\\.");
				String shortIp = ip_split[0] + "." + ip_split[1] + "." + ip_split[2];//取前三段ip缓存
				String ipKey = MCPrefix.USER_IP_REGION_CACHE+shortIp;
				MemcachedUtil.set(ipKey, code);
				LogUtil.log.info(String.format("###RegionThread select code=%s,cacheKey=%s",code,ipKey));
			}
			LogUtil.log.info(String.format("###RegionThread code=%s",code));
		}
		LogUtil.log.info("###RegionThread end...");
	}
}
