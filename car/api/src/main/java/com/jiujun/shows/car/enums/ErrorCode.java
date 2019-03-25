package com.jiujun.shows.car.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 座驾模块使用
 * @author shao.xiang
 * @date 2017年6月25日
 *
 */
public enum ErrorCode {
	
	/** 停车收益未配置 */
	ERROR_11015(11015,"停车收益未配置"),
	/** 车位已经被占领 */
	ERROR_11014(11014,"车位已经被占领"),
	/** 每天只能抢3次哦 */
	ERROR_11013(11013,"每天只能抢3次哦"),
	/** 您的豪车已经抢到位啦 */
	ERROR_11012(11012,"您的豪车已经抢到位啦"),
	/** 关注主播才能抢占车位哦 */
	ERROR_11011(11011,"关注主播才能抢占车位哦"),
	/** 主播等级在lv3开启 */
	ERROR_11010(11010,"主播等级在lv3开启"),
	/** 座驾已取消使用 */
	ERROR_11009(11009,"座驾已取消使用"),
	/** 座驾已过期 */
	ERROR_11008(11008,"座驾已过期"),
	/** 您未购买此座驾 */
	ERROR_11007(11007,"您未购买此座驾"),
	/** 正在抢车位中,不能切换 */
	ERROR_11006(11006,"正在抢车位中,不能切换"),
	/** 账户余额不足 */
	ERROR_11005(11005,"账户余额不足"),
	/** 开通守护可立即获得该座驾 */
	ERROR_11004(11004,"开通守护可立即获得该座驾"),
	/** 此类座驾不能通过购买获取 */
	ERROR_11003(11003, "此类座驾不能通过购买获取"),
	/** 座驾不存在 */
	ERROR_11002(11002, "座驾不存在 "),
	/** 参数错误 */
	ERROR_11001(11001, "参数错误"),
	/** 网络不给力，请稍后重试 */
	ERROR_11000(11000, "网络不给力，请稍后重试"),
	
	SUCCESS_0(0, "SUCCESS");
	
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
