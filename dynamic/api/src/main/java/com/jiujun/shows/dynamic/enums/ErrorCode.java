package com.jiujun.shows.dynamic.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态服务错误码<8000><br>
 * 	<说明：错误码应由各自模块维护，不应夸服务使用>
 * @author shao.xiang
 * @date 2017-06-21
 *
 */
public enum ErrorCode {
	// 8000 动态模块相关
	/** 动态账户不存在 */
	ERROR_8017(8017,"账号不存在"),
	/** 获取信息失败，请重试 */
	ERROR_8016(8016,"获取信息失败，请重试"),
	/** 参数错误 */
	ERROR_8015(8015,"参数错误"),
	/** 一富以上才能评论哦 */
	ERROR_8014(8014,"一富以上才能评论哦"),
	/** 无法删除他人动态 */
	ERROR_8013(8013,"无法删除他人动态"),
	/** 无法删除他人评论 */
	ERROR_8012(8012,"无法删除他人评论"),
	/** 不能举报自己 */
	ERROR_8011(8011,"不能举报自己"),
	/** 不能回复自己 */ 
	ERROR_8010(8010,"不能回复自己"),
	/** 已赞 */
	ERROR_8009(8009,"已赞"),
	/** 评论不存在 */
	ERROR_8008(8008,"评论不存在"),
	/** 动态不存在 */
	ERROR_8007(8007,"动态不存在"),
	/** 内容不能为空 */
	ERROR_8006(8006,"内容不能为空"),
	/** 请填写内容或者发布图片 */
	ERROR_8005(8005,"请填写内容或者发布图片"),
	/** 内测中，十级以上主播才可以发布 */
	ERROR_8004(8004,"内测中，十级以上主播才可以发布"),
	/** 很抱歉，动态评论功能暂未全面开放，敬请期待  */
	ERROR_8003(8003,"很抱歉，动态评论功能暂未全面开放，敬请期待"),
	/** 动态发布暂未全面开放，敬请期待  */
	ERROR_8002(8002,"动态发布暂未全面开放，敬请期待"),
	/** 正在升级中，敬请期待 */
	ERROR_8001(8001,"正在升级中，敬请期待"),
	/** 网咯不给力，正在努力中... */
	ERROR_8000(8000,"网咯不给力，正在努力中..."),
	
	SUCCESS_0(0,"SUCCESS");
	private int resultCode;
	private String resultDescr;
	
	private ErrorCode(int resultCode,String resultDescr){
		this.resultCode = resultCode;
		this.resultDescr = resultDescr;
	}
	
	public int getResultCode() {
		return resultCode;
	}
	public String getResultDescr() {
		return resultDescr;
	}
	
	public void setResultDescr(String resultDescr) {
		this.resultDescr = resultDescr;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> r = new HashMap<String, Object>();
		r.put("a", this.resultCode);
		r.put("b", this.resultDescr);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("r", r);
		return map;
	}
}
