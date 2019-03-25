package com.lm.live.login.vo;

/**
 * 微信授权登录时拿到的token
 * @author Administrator
 *
 */
public class AccessToken extends BaseResult{
	/*
		参数	      是否必须	说明
		appid	是	应用唯一标识，在微信开放平台提交应用审核通过后获得
		secret	是	应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
		code	是	填写第一步获取的code参数
		grant_type	是	填authorization_code
	 */

	private String access_token;

	private Integer expires_in;

	private String refresh_token;

	private String openid;

	private String scope;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String accessToken) {
		access_token = accessToken;
	}

	public Integer getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Integer expiresIn) {
		expires_in = expiresIn;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refreshToken) {
		refresh_token = refreshToken;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}


}
