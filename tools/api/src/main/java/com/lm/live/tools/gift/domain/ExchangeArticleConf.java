package com.lm.live.tools.gift.domain;

import com.jiujun.shows.common.vo.BaseVo;

/**
 * @entity
 * @table t_exchange_article_conf
 * @author shao.xiang 
 * @date 2017-06-29
 */
public class ExchangeArticleConf extends BaseVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	/**
	 * 兑换物id
	 */
	private Integer articleId;
	/**
	 * 兑换物名称
	 */
	private String articleName;
	/**
	 * 礼物图片
	 */
	private String articleImg;
	/**
	 * 是否启用，默认1-启用，0-停用
	 */
	private int inUse;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getArticleid() {
		return this.articleId;
	}
	
	public void setArticleid(Integer articleId) {
		this.articleId = articleId;
	}
	public String getArticlename() {
		return this.articleName;
	}
	
	public void setArticlename(String articleName) {
		this.articleName = articleName;
	}
	public String getArticleimg() {
		return this.articleImg;
	}
	
	public void setArticleimg(String articleImg) {
		this.articleImg = articleImg;
	}
	public int getInuse() {
		return this.inUse;
	}
	
	public void setInuse(int inUse) {
		this.inUse = inUse;
	}
}
