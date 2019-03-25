package com.lm.live.login.service.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.login.constant.Constants;
import com.lm.live.login.dao.WeiboUserInfoDoMapper;
import com.lm.live.login.domain.WeiboUserInfoDo;
import com.lm.live.login.enums.ErrorCode;
import com.lm.live.login.exceptions.LoginBizException;
import com.lm.live.login.service.WeiboAccessService;
import com.lm.live.login.vo.WeiboUserInfo;

/**
 * 
 * 新浪微博接入相关业务
 *
 */
@Service
public class WeiboAccessServiceImpl extends CommonServiceImpl<WeiboUserInfoDoMapper, WeiboUserInfoDo>
	implements WeiboAccessService{

	@Override
	public WeiboUserInfo getInfo(String accessToken, String uid)throws Exception {
		LogUtil.log.info(String.format("###begin-请求微博服务器获取用户信息,accessToken:%s,uid:%s", accessToken,uid));
		WeiboUserInfo userInfo = null ;
		try {
			/**
			 *  新浪安全发现第三方应用的部分开发者直接使用返回uid值来作为的第三方应用身份凭证，导致指定任意授权第三方应用的帐号uid登陆第三方应用。
				因为此uid值可以被攻击者代理拦截篡改其他用户的uid值，攻击者就可以直接控制受害人的第三方应用账户。 请开发人员一定要检验accesstoken和uid的一致性再进行授权。
				如有其它问题可以发邮件反馈给我们，邮件地址:weibo_app@vip.sina.com。
			 */
			
			StringBuffer sbfUrl = new StringBuffer();
			sbfUrl.append(Constants.WEIBO_URL).append("?")
			.append("access_token=").append(accessToken)
			.append("&uid=").append(uid) ;
			//请求微信服务器，获取到json字符串
			String responseJsonString = HttpUtils.get(sbfUrl.toString(), Constants.DEFAULT_UNICODE);
			LogUtil.log.info(String.format("###微博登录-请求微博服务器获取用户信息,accessToken:%s,uid:%s,响应信息:%s", accessToken,uid,responseJsonString));
			// 字符串中的特殊字符trim掉
			responseJsonString = StrUtil.trimControlCharacter(responseJsonString);
			JSONObject responseJson = JsonUtil.strToJsonObject(responseJsonString);
			responseJson.remove("status");
			String idstrResponse = responseJson.getString("idstr");
			
			if(!uid.equals(idstrResponse)){
				throw new LoginBizException(ErrorCode.ERROR_100);
			}
			userInfo = new WeiboUserInfo();
			BeanUtils.copyProperties(userInfo, responseJson);
		} catch (LoginBizException e) {
			throw e;
		} catch (Exception e) {
			throw new LoginBizException(ErrorCode.ERROR_100);
		}
		return userInfo;
	}

}
