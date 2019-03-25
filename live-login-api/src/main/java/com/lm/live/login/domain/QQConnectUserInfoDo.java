package com.lm.live.login.domain;

import java.io.Serializable;

import com.lm.live.common.vo.BaseVo;

/**
 * QQ互联用户信息对应的数据库对象 
 *
 */
public class QQConnectUserInfoDo extends BaseVo implements Serializable{

	private static final long serialVersionUID = 2221526576544942841L;

	private String id;
	
	private String userId;
	
	private String openid;
	
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
	
	private String province;
	
	private String city;

	private String clientid;
	/** 多客户端登录，产生同一个unionid */
	private String unionid;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
	
	
}
