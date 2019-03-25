package com.lm.live.user.domain;

import com.lm.live.common.vo.BaseVo;
/**
 * @entity
 * @table t_level
 * @author shao.xiang
 * @date 2017-06-14
 */
public class Level extends BaseVo {
	private static final long serialVersionUID = 1L;
	/** 主键自增ID */
	private Integer id;
	/** 用户类型：1-普通用户，2-主播 */
	private Integer levelType;
	/** 主播：歌手等级:新主播。普通用户空值 */
	private String levelName;
	/** 主播：子等级:S1。普通用户: V1 */
	private String level;
	/** 主播：称号:新生代。普通用户:银翼少侠V1 */
	private String title;
	/** 对应的等级图标 */
	private String image;
	/** 对应的等级所需积分 */
	private String point;

	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setLevelType(Integer levelType){
		this.levelType = levelType;
	}
	
	public Integer getLevelType() {
		return this.levelType;
	}
	
	public void setLevelName(String levelName){
		this.levelName = levelName;
	}
	
	public String getLevelName() {
		return this.levelName;
	}
	
	public void setLevel(String level){
		this.level = level;
	}
	
	public String getLevel() {
		return this.level;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setImage(String image){
		this.image = image;
	}
	
	public String getImage() {
		return this.image;
	}
	
	public void setPoint(String point){
		this.point = point;
	}
	
	public String getPoint() {
		return this.point;
	}
	

}