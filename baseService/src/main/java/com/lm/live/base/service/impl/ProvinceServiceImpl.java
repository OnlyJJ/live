package com.lm.live.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.base.constant.Constants;
import com.lm.live.base.constant.MCPrefix;
import com.lm.live.base.dao.ProvinceMapper;
import com.lm.live.base.domain.Province;
import com.lm.live.base.service.IProvinceService;
import com.lm.live.common.constant.MCTimeoutConstants;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.MemcachedUtil;
import com.lm.live.common.utils.RegionThread;
import com.lm.live.common.utils.StrUtil;


/**
 * @serviceimpl
 * @table t_province
 * @date 2016-12-21 09:50:53
 * @author province
 */
@Service("provinceService")
public class ProvinceServiceImpl extends CommonServiceImpl<ProvinceMapper,Province> implements IProvinceService {

	
	@Override
	public void getProvinceSetCache() {
		LogUtil.log.info("###getProvinceSetCache 开始设置省市缓存 start...");
		String loadCacheKey = MCPrefix.PROVINCE_TIME_CACHE;
		Object obj = MemcachedUtil.get(loadCacheKey);
		if (obj!=null) {
			LogUtil.log.info("###getProvinceSetCache 加载省市记录过于频繁，请求间隔为24h");
		}else {
			// 获取所有省市记录
			List<Province> list = dao.getListByAll();
			if (list!=null&&list.size()>0) {
				for (Province vo : list) {
					MemcachedUtil.set(MCPrefix.PROVINCE_CODE_CACHE + vo.getCode(), JSONObject.toJSON(vo));
				}
			}else {
				LogUtil.log.info("###getProvinceSetCache select db list is null");
			}
			MemcachedUtil.set(loadCacheKey, 1, MCTimeoutConstants.DEFAULT_TIMEOUT_24H);
		}
		LogUtil.log.info("###getProvinceSetCache 设置省市缓存结束 end...");
	}
	
	@Override
	public String getProviceBy(String ip) {
		String region = Constants.DEFAULT_VISITOR_NAME;//ip归属地(如：广东)
		if (!StrUtil.isNullOrEmpty(ip)) {
			String[] ip_split = ip.split("\\.");
			String shortIp = ip_split[0] + "." + ip_split[1] + "." + ip_split[2];//取前三段ip缓存
			String ipKey = MCPrefix.USER_IP_REGION_CACHE + shortIp;
			Object regionObj = MemcachedUtil.get(ipKey);
			if (regionObj == null) {
				LogUtil.log.info(String.format("###开启线程重新获取省份信息,ip:%s",ip));
				//开条线程根据ip获取归属地省id并缓存
				RegionThread thread = new RegionThread(ip);
				thread.start();
			}else {
				//根据province_code_+code获取省市信息
				String code = regionObj.toString();
				LogUtil.log.info(String.format("###从缓存获取省份信息,cache cacheKey=%s,code=%s,ip:%s",ipKey,code,ip));
				code = code.substring(0, 2)+"0000";//获取省级
				String codeKey = MCPrefix.PROVINCE_CODE_CACHE+code;
				regionObj = MemcachedUtil.get(codeKey);
				LogUtil.log.info(String.format("######从缓存获取省份信息, cache cacheCodeKey=%s,regionObj=%s,ip:%s",codeKey,regionObj,ip));
				if (regionObj!=null) {
					JSONObject json = null;
					try {
						json = (JSONObject) JSONObject.toJSON(regionObj);
						region = json.getString("region");
						if (!StrUtil.isNullOrEmpty(region)) {
							//省份过滤（省 市  特别行政区   壮族自治区   回族自治区 维吾尔自治区   自治区几个关键字）
							region = region.replaceAll("(?:省|市|特别行政区|壮族自治区|回族自治区|维吾尔自治区|自治区)", "");
						}
					} catch (Exception e) {
						LogUtil.log.error("###getAndSetPesudoUserName 解析json error,json="+json);
					}
				}else {
					//重新设置省市缓存
					this.getProvinceSetCache();
					LogUtil.log.info(String.format("###getAndSetPesudoUserName 重新设置省市缓存,ip=%s,code=%s",ip,code));
				}
				LogUtil.log.info(String.format("###getAndSetPesudoUserName cache region=%s,cacheKey=%s",region,ipKey));
			}
		}
		return region;
	}

}
