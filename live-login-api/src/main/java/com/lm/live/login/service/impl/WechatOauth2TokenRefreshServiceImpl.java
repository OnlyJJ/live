package com.lm.live.login.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.JsonUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.login.constant.Constants;
import com.lm.live.login.dao.WechatOauth2TokenRefreshMapper;
import com.lm.live.login.domain.WechatOauth2TokenRefresh;
import com.lm.live.login.enums.ErrorCode;
import com.lm.live.login.exceptions.LoginBizException;
import com.lm.live.login.service.IWechatOauth2TokenRefreshService;
import com.lm.live.login.vo.AccessToken;

@Service
public class WechatOauth2TokenRefreshServiceImpl extends CommonServiceImpl<WechatOauth2TokenRefreshMapper,WechatOauth2TokenRefresh> implements IWechatOauth2TokenRefreshService {

	@Override
	public WechatOauth2TokenRefresh getByCode(String code) throws Exception {
		if(StringUtils.isEmpty(code)){
			throw new LoginBizException(ErrorCode.ERROR_101);
		}
		return this.dao.getByCode(code);
	}

	@Override
	public List<WechatOauth2TokenRefresh> findExpireSoonList(int soonSecond)
			throws Exception {
		LogUtil.log.info(String.format("###查询微信本地存储的即将过期的accessToken,soonSecond:%s", soonSecond)) ;
		List<WechatOauth2TokenRefresh> retList = new ArrayList<WechatOauth2TokenRefresh>();
		if(soonSecond <= 0){
			LogUtil.log.info(String.format("###查询微信本地存储的即将过期的accessToken,倒计时参数错误,不查db,soonSecond:%s", soonSecond)) ;
		}else{
			retList = this.dao.findExpireSoonList(soonSecond);
		}
		return retList;
	}

	@Override
	public void reFreshToken() {
		// 根据定时任务的间隔时间而定,因为微信用户有几十万,定时任务间隔时间越小,压力平分得越均衡,如定时任务间隔时间是30分钟,则设置soonSecond为40分钟
		int wechatAccessTokenExpireSoonSecond = 60*40;
		List<WechatOauth2TokenRefresh> list = null;
		try {
			list = this.findExpireSoonList(wechatAccessTokenExpireSoonSecond);
		} catch (Exception e) {
			LogUtil.log.error("###系统定时任务刷新微信accessToken时,查询本地(t_wechat_oauth2_token_refresh)accessToken发生异常");
			LogUtil.log.error(e.getMessage(),e);
		}
		
		try {
			if(list !=null &&list.size() >0){
				LogUtil.log.info(String.format("###系统定时任务刷新微信accessToken时,查询本地(t_wechat_oauth2_token_refresh)accessToken,发现需要刷新的数据共有%s行,设定倒计时:%s秒",list.size(),wechatAccessTokenExpireSoonSecond));
				for(WechatOauth2TokenRefresh w:list){
					//刷新每一行
					refreshAccesstokenRow(w);
				}
			}else{
				LogUtil.log.info(String.format("###系统定时任务刷新微信accessToken时,查询本地(t_wechat_oauth2_token_refresh)accessToken,没发现需要刷新的数据,设定倒计时:%s秒",wechatAccessTokenExpireSoonSecond));
			}
		} catch (Exception e) {
			LogUtil.log.error("###系统定时任务刷新微信accessToken时,刷新accessToken发生异常");
			LogUtil.log.error(e.getMessage(),e);
		}
		
	}

	private void refreshAccesstokenRow(WechatOauth2TokenRefresh w) {
		String grantType = "refresh_token";
		StringBuffer sbfUrl = new StringBuffer(Constants.WECHATREFRESHURL);
		int rowId = w.getId();
		String appId =  w.getAppid();
		String refreshToken = w.getRefreshToken();
		sbfUrl.append("?appid=").append(appId)
		.append("&grant_type=").append(grantType)
		.append("&refresh_token=").append(refreshToken) ;
		try {
			AccessToken accessToken = new AccessToken();
			String responseJsonStr = HttpUtils.get(sbfUrl.toString());
			LogUtil.log.info(String.format("###系统定时任务刷新微信accessToken时,根据rowId:%s的数据请求微信服务器拿到数据:%s",rowId,responseJsonStr));
			JSONObject responseJson = JsonUtil.strToJsonObject(responseJsonStr) ;
			BeanUtils.copyProperties(accessToken, responseJson);
			if(accessToken.isSuccess()){
				Date nowDate = new Date();
				w.setAccessToken(accessToken.getAccess_token());
				w.setExpiresinSecond(accessToken.getExpires_in());
				w.setRefreshToken(accessToken.getRefresh_token());
				w.setResultTime(nowDate);
				// 更新信息
				this.dao.update(w);
			}else{
				// 已失效,删掉
				this.dao.removeById(rowId);
				LogUtil.log.info(String.format("###系统定时任务刷新微信accessToken时,根据rowId:%s的数据请求微信服务器拿到数据为token已失效:%s",rowId,responseJsonStr));
			}
		} catch (Exception e) {
			LogUtil.log.error("##系统定时任务刷新微信,发生异常,t_wechat_oauth2_token_refresh-rowId:"+rowId);
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	@Override
	public void delExpireAccessToken() {
		this.dao.delExpireAccessToken();
	}

}
