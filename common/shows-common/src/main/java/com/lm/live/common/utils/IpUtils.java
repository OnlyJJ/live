package com.lm.live.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;


/**
 * 此类用于获取ip
 * @author xiaoyin
 * @date 2012-2-26下午04:26:12
 */
public class IpUtils{
	
  protected static final Logger LOG = LoggerFactory.getLogger(IpUtils.class);
  
  private static final String header_key_real_ip_2 = "X-Real-IP";
  
  private static final String TAOBAO_IP_REGION ="http://ip.taobao.com/service/getIpInfo.php?ip=";
  
  private static final String XINLANG_IP_REGION ="http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";
  
  /** 获取本机IP **/
  public static String getExterIp(HttpServletRequest request){
	 return request.getRemoteAddr();
  }

  /** 获取客户IP **/
  public static String getClientIp(HttpServletRequest request) {
	if(request == null){
		return null;
	}
	//获取真实的ip, nginx代理
	String ip =  request.getHeader(header_key_real_ip_2);
	//LogUtil.log.info("###IpUtils-nginx-ip:"+ip);
    if (LOG.isDebugEnabled()) {
      LOG.debug("x-forwarded-for = {}", ip);
    }
    
    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
    	ip = request.getHeader("x-forwarded-for");
        if (LOG.isDebugEnabled()) {
          LOG.debug("Proxy-Client-IP = {}", ip);
        }
     }
    
    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
      ip = request.getHeader("Proxy-Client-IP");
      if (LOG.isDebugEnabled()) {
        LOG.debug("Proxy-Client-IP = {}", ip);
      }
    }
    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
      ip = request.getHeader("WL-Proxy-Client-IP");
      if (LOG.isDebugEnabled()) {
        LOG.debug("WL-Proxy-Client-IP = {}", ip);
      }
    }
    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
      ip = request.getRemoteAddr();
      if (LOG.isDebugEnabled()) {
        LOG.debug("RemoteAddr-IP = {}", ip);
      }
    }
    if (StrUtil.isNullOrEmpty(ip)) {
      ip = ip.split(",")[0];
    }
    return ip;
  }
  
	public static void main(String[] args) {
		String url = "http://ip.taobao.com/service/getIpInfo.php?ip=119.129.211.108";
		System.err.println(getXinlangRegionByIp("202.106.212.226"));
	}
  
	/**
	 * 淘宝IP地址库获取归属地
	 * @param ip
	 * @return 省id
	 */
	public static String getTaobaoRegionByIp(String ip) {
		LogUtil.log.info(String.format("###IpUtils getTaobaoRegionByIp ip: %s,根据ip请求淘宝ip地址库 begin...",ip));
		if (StrUtil.isNullOrEmpty(ip)) {
			LogUtil.log.info("###IpUtils getTaobaoRegionByIp ip is null");
			return null;
		}
		String region_id = null;
		try {
			String content = HttpUtils.get(TAOBAO_IP_REGION+ip);
			LogUtil.log.info(convert(String.format("###IpUtils getTaobaoRegionByIp date=%s,ip=%s",content,ip)));
			JSONObject json = JsonUtil.strToJsonObject(content);
			if (json != null) {
				int code = json.getIntValue("code");
				if (code == 0) {
					json = JsonUtil.strToJsonObject(json.getString("data"));
					region_id = json.getString("city_id");//获取市city_id
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(String.format("###IpUtils getTaobaoRegionByIp error ip: %s,根据ip请求淘宝ip地址库产生错误...",ip));
			LogUtil.log.error(e.getMessage(), e);
		}
		LogUtil.log.info(String.format("###IpUtils getTaobaoRegionByIp region_id: %s,根据ip请求淘宝ip地址库 end...",region_id));
		return region_id;
	}
	
	/**
	 *新浪IP地址库获取归属地
	 * @param ip
	 * @return
	 */
	public static String getXinlangRegionByIp(String ip) {
		LogUtil.log.info(String.format("###IpUtils getXinlangRegionByIp ip: %s,根据ip请求新浪ip地址库 begin...",ip));
		if (StrUtil.isNullOrEmpty(ip)) {
			LogUtil.log.info("###IpUtils getXinlangRegionByIp ip is null");
			return null;
		}
		String region = null;
		try {
			String content = HttpUtils.get(XINLANG_IP_REGION+ip);
			LogUtil.log.info(convert(String.format("###IpUtils getXinlangRegionByIp date=%s,ip=%s",content,ip)));
			JSONObject json = JsonUtil.strToJsonObject(content);
			if (json != null) {
				region = json.getString("province");
			}
		} catch (Exception e) {
			LogUtil.log.error(String.format("###IpUtils getXinlangRegionByIp error ip: %s,根据ip请求新浪ip地址库产生错误...",ip));
			LogUtil.log.error(e.getMessage(), e);
		}
		LogUtil.log.info(String.format("###IpUtils getXinlangRegionByIp region: %s,根据ip请求新浪ip地址库 end...",region));
		return region;
	}
  
	public static String convert(String utfString) {
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;
		while ((i = utfString.indexOf("\\u", pos)) != -1) {
			sb.append(utfString.substring(pos, i));
			if (i + 5 < utfString.length()) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
			}
		}
		return sb.toString();
	} 
}