package com.lm.live.common.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;

public class Code extends JsonParseInterface implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// 字段key
	private static final String u_code = "a";
	private static final String u_from = "b";
	private static final String u_uuid = "c";
	private static final String u_account = "d";
	private static final String u_codeFromNewBindAcc = "e";
	
	private String code;
	private int from;//1:email 2:mobile
	private String uuid;
	
	/** 手机号或邮箱号 */
	private String account;
	
	/** 新绑定的号收到的验证码  */
	private String codeFromNewBindAcc;

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, u_code, code);
			setInt(json, u_from, from);
			setString(json, u_uuid, uuid);
			setString(json, u_account, account);
			return json;
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		return json;
	}

	@Override
	public void parseJson(JSONObject json) {
		if (json == null) 
			return ;
		try {
			code = getString(json, u_code);
			from = getInt(json, u_from);
			uuid = getString(json, u_uuid);
			account = getString(json, u_account);
			codeFromNewBindAcc = getString(json, u_codeFromNewBindAcc);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
	}

	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCodeFromNewBindAcc() {
		return codeFromNewBindAcc;
	}

	public void setCodeFromNewBindAcc(String codeFromNewBindAcc) {
		this.codeFromNewBindAcc = codeFromNewBindAcc;
	}
	
}
