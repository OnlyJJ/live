package com.jiun.shows.appclient.biz;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiun.shows.appclient.dao.AppCheckConfMapper;
import com.jiun.shows.appclient.domain.AppCheckConf;


/**
 * app配置相关业务
 * @author shao.xiang
 * @date 2017-09-15
 *
 */
@Service
public class AppCheckConfBiz {

	@Resource
	private AppCheckConfMapper appCheckConfMapper;
	
	public boolean appPackageCheck(DeviceProperties deviceProperties) {
		String packageName = deviceProperties.getPackageName();
		String appVersion = deviceProperties.getAppVersion();
		String signatures = deviceProperties.getSignatures();
		boolean flag = false;
		if(StringUtils.isEmpty(packageName)){
			return flag;
		}
		//获取所有配置
		List<AppCheckConf> list = appCheckConfMapper.getAppCheckConfList();
		if(list != null && list.size() >0){
			for (AppCheckConf appCheckConf : list) {
				String packageNameConf = appCheckConf.getPackageName();
				String version1 = appCheckConf.getVersion1();
				String version2 = appCheckConf.getVersion2();
				String signaturesConf = appCheckConf.getSignatures();
				
				if(StringUtils.isEmpty(packageNameConf)){
					continue;
				}
				
				//包名匹配
				if(StringUtils.isEmpty(version1) && StringUtils.isEmpty(version2) && StringUtils.isEmpty(signaturesConf)){
					if(packageName.equals(packageNameConf)){
						flag = true;
						break;
					}
				}//包名、版本号1匹配
				else if(StringUtils.isEmpty(version2) && StringUtils.isEmpty(signaturesConf) && StringUtils.isNotEmpty(version1) && StringUtils.isNotEmpty(appVersion)){
					if(packageName.equals(packageNameConf) && appVersion.equals(version1)){
						flag = true;
						break;
					}
				}//包名、版本号1、版本2匹配
				else if(StringUtils.isNotEmpty(version2) && StringUtils.isNotEmpty(version1) && StringUtils.isNotEmpty(appVersion) && StringUtils.isEmpty(signaturesConf)){
					if(packageName.equals(packageNameConf)){
						int dAppVersion = Integer.parseInt(appVersion.replace(".", ""));
						int dVersion1 = Integer.parseInt(version1.replace(".", ""));
						int dVersion2 = Integer.parseInt(version2.replace(".", ""));
						if(dAppVersion >= dVersion1 && dAppVersion <= dVersion2){
							flag = true;
							break;
						}
					}
				}//包名、签名匹配
				else if(StringUtils.isNotEmpty(signaturesConf) && StringUtils.isNotEmpty(signatures) && StringUtils.isEmpty(version1) && StringUtils.isEmpty(version2)){
					if(packageName.equals(packageNameConf) && signatures.equals(signaturesConf)){
						flag = true;
						break;
					}
				}//包名、版本号1、签名匹配
				else if(StringUtils.isEmpty(version2) && StringUtils.isNotEmpty(version1) && StringUtils.isNotEmpty(signaturesConf) && StringUtils.isNotEmpty(appVersion)){
					if(packageName.equals(packageNameConf) && signatures.equals(signaturesConf) && appVersion.equals(version1)){
						flag = true;
						break;
					}
				}//包名、版本号1、版本号2、签名匹配
				else if(StringUtils.isNotEmpty(version2) && StringUtils.isNotEmpty(version1) && StringUtils.isNotEmpty(appVersion) && StringUtils.isNotEmpty(signatures) &&  StringUtils.isNotEmpty(signaturesConf)){
					if(packageName.equals(packageNameConf) && signatures.equals(signaturesConf)){
						int dAppVersion = Integer.parseInt(appVersion.replace(".", ""));
						int dVersion1 = Integer.parseInt(version1.replace(".", ""));
						int dVersion2 = Integer.parseInt(version2.replace(".", ""));
						if(dAppVersion >= dVersion1 && dAppVersion <= dVersion2){
							flag = true;
							break;
						}
					}
				}
			}
		}
		return flag;
	}
	
	
}
