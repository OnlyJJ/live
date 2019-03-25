package com.lm.live.login.vo;

import java.io.Serializable;



import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.JsonParseInterface;
import com.lm.live.common.utils.LogUtil;


/**
 * qq互联方式接入的用户个人信息
 * 参考url :　http://wiki.connect.qq.com/get_simple_userinfo
 */
public class QQConnectUserInfoVo extends JsonParseInterface implements Serializable{

	private static final long serialVersionUID = -35263031552026371L;
	
	private static String q_userId = "a";
	private static String q_nickname = "b";
	private static String q_figureurl = "c";
	private static String q_figureurl_1 ="d";
	private static String q_figureurl_2 = "e";
	private static String q_figureurl_qq_1 = "f";
	private static String q_figureurl_qq_2 ="g";
	private static String q_gender = "h";
	private static String q_is_yellow_vip = "i";
	private static String q_vip="j";
	private static String q_yellow_vip_level = "k";
	private static String q_level = "l";
	private static String q_is_yellow_year_vip ="m";
	private static String q_openid ="n";
	private static String q_province = "o";
	private static String q_city = "p";
	private static String q_flag = "q";
	
	
	private String userId;
	
	private String nickname;

	private String figureurl;

	private String figureurl_1;

	private String figureurl_2;

	private String figureurl_qq_1;

	private String figureurl_qq_2;

	private String gender;
	
	private int is_yellow_vip;
	
	private int vip;
	
	private String yellow_vip_level;
	
	private String level;
	
	private int is_yellow_year_vip;
	
	private String openid;
	
	private String province;
	
	private String city;
	
	private String flag;//用于判断从app端登录还是web端登录的标志位,1用于标示web端

	@Override
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			setString(json, q_userId, userId);
			setString(json, q_nickname, nickname);
			setString(json, q_figureurl, figureurl);
			setString(json, q_figureurl_1, figureurl_1);
			setString(json, q_figureurl_2, figureurl_2);
			setString(json, q_gender, gender);
			setInt(json, q_is_yellow_vip, is_yellow_vip);
			setInt(json, q_vip, vip);
			setString(json, q_yellow_vip_level, yellow_vip_level);
			setString(json, q_level, level);
			setInt(json, q_is_yellow_year_vip, is_yellow_year_vip);
			setString(json, q_openid, openid);
			setString(json, q_province, province);
			setString(json, q_city, city);
			setString(json,q_flag,flag);
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
			userId = getString(json, q_userId);
			nickname = getString(json, q_nickname);
			figureurl = getString(json, q_figureurl);
			figureurl_1 = getString(json, q_figureurl_1);
			figureurl_2 = getString(json, q_figureurl_2);
			figureurl_qq_1 = getString(json, q_figureurl_qq_1);
			figureurl_qq_2 = getString(json, q_figureurl_qq_2);
			gender = getString(json, q_gender);
			is_yellow_vip = getInt(json, q_is_yellow_vip);
			vip = getInt(json, q_vip);
			yellow_vip_level = getString(json, q_yellow_vip_level);
			level = getString(json, q_level);
			is_yellow_year_vip = getInt(json, q_is_yellow_year_vip);
			openid = getString(json, q_openid);
			province = getString(json, q_province);
			city = getString(json, q_city);
			flag = getString(json,q_flag);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(),e);
		}
		
	}
	
	@Override
	public String getShortName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFigureurl() {
		return figureurl;
	}

	public void setFigureurl(String figureurl) {
		this.figureurl = figureurl;
	}

	public String getFigureurl_1() {
		return figureurl_1;
	}

	public void setFigureurl_1(String figureurl_1) {
		this.figureurl_1 = figureurl_1;
	}

	public String getFigureurl_2() {
		return figureurl_2;
	}

	public void setFigureurl_2(String figureurl_2) {
		this.figureurl_2 = figureurl_2;
	}

	public String getFigureurl_qq_1() {
		return figureurl_qq_1;
	}

	public void setFigureurl_qq_1(String figureurl_qq_1) {
		this.figureurl_qq_1 = figureurl_qq_1;
	}

	public String getFigureurl_qq_2() {
		return figureurl_qq_2;
	}

	public void setFigureurl_qq_2(String figureurl_qq_2) {
		this.figureurl_qq_2 = figureurl_qq_2;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getIs_yellow_vip() {
		return is_yellow_vip;
	}

	public void setIs_yellow_vip(int is_yellow_vip) {
		this.is_yellow_vip = is_yellow_vip;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public String getYellow_vip_level() {
		return yellow_vip_level;
	}

	public void setYellow_vip_level(String yellow_vip_level) {
		this.yellow_vip_level = yellow_vip_level;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getIs_yellow_year_vip() {
		return is_yellow_year_vip;
	}

	public void setIs_yellow_year_vip(int is_yellow_year_vip) {
		this.is_yellow_year_vip = is_yellow_year_vip;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	

}
