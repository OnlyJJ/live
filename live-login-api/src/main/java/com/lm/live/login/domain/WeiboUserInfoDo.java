package com.lm.live.login.domain;

import java.io.Serializable;

import com.lm.live.common.vo.BaseVo;

/**
 * QQ互联用户信息对应的数据库对象 
 *
 */
public class WeiboUserInfoDo extends BaseVo implements Serializable{

	private static final long serialVersionUID = -4108030231784708317L;

	private  String id;
	private  String userId;
	private String idstr ;	
	private String screen_name ;	
	private String name ;	
	private int province ;	
	private int city;	
	private String location;	
	private String description ;	
	private String url;	
	private String profile_image_url ;	
	private String profile_url ;	
	private String	domain;
	private String	weihao;
	private String	gender;
	private int followers_count;	
	private int friends_count;	
	private int statuses_count;	
	private int favourites_count;	
	private String created_at;	
	private String following;	
	private String	allow_all_act_msg ;
	private String	 geo_enabled; 
	private String	verified;
	private int	verified_type ;
	private String remark;	
	private String	status ;
	private String	allow_all_comment;
	private String	avatar_large;
	private String avatar_hd;	
	private String verified_reason;	
	private String follow_me;	
	private int online_status;
	private int bi_followers_count	;
	private String lang;
	
	
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
	public String getIdstr() {
		return idstr;
	}
	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}
	public String getScreen_name() {
		return screen_name;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProvince() {
		return province;
	}
	public void setProvince(int province) {
		this.province = province;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getProfile_image_url() {
		return profile_image_url;
	}
	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}
	public String getProfile_url() {
		return profile_url;
	}
	public void setProfile_url(String profile_url) {
		this.profile_url = profile_url;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getWeihao() {
		return weihao;
	}
	public void setWeihao(String weihao) {
		this.weihao = weihao;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getFollowers_count() {
		return followers_count;
	}
	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}
	public int getFriends_count() {
		return friends_count;
	}
	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}
	public int getStatuses_count() {
		return statuses_count;
	}
	public void setStatuses_count(int statuses_count) {
		this.statuses_count = statuses_count;
	}
	public int getFavourites_count() {
		return favourites_count;
	}
	public void setFavourites_count(int favourites_count) {
		this.favourites_count = favourites_count;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getFollowing() {
		return following;
	}
	public void setFollowing(String following) {
		this.following = following;
	}
	public String getAllow_all_act_msg() {
		return allow_all_act_msg;
	}
	public void setAllow_all_act_msg(String allow_all_act_msg) {
		this.allow_all_act_msg = allow_all_act_msg;
	}
	public String getGeo_enabled() {
		return geo_enabled;
	}
	public void setGeo_enabled(String geo_enabled) {
		this.geo_enabled = geo_enabled;
	}
	public String getVerified() {
		return verified;
	}
	public void setVerified(String verified) {
		this.verified = verified;
	}
	public int getVerified_type() {
		return verified_type;
	}
	public void setVerified_type(int verified_type) {
		this.verified_type = verified_type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAllow_all_comment() {
		return allow_all_comment;
	}
	public void setAllow_all_comment(String allow_all_comment) {
		this.allow_all_comment = allow_all_comment;
	}
	public String getAvatar_large() {
		return avatar_large;
	}
	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}
	public String getAvatar_hd() {
		return avatar_hd;
	}
	public void setAvatar_hd(String avatar_hd) {
		this.avatar_hd = avatar_hd;
	}
	public String getVerified_reason() {
		return verified_reason;
	}
	public void setVerified_reason(String verified_reason) {
		this.verified_reason = verified_reason;
	}
	public String getFollow_me() {
		return follow_me;
	}
	public void setFollow_me(String follow_me) {
		this.follow_me = follow_me;
	}
	public int getOnline_status() {
		return online_status;
	}
	public void setOnline_status(int online_status) {
		this.online_status = online_status;
	}
	public int getBi_followers_count() {
		return bi_followers_count;
	}
	public void setBi_followers_count(int bi_followers_count) {
		this.bi_followers_count = bi_followers_count;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}	
	
}
