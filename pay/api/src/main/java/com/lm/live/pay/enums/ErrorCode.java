package com.lm.live.pay.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础服务错误码<10000><br>
 * 	<说明：错误码应由各自模块维护，不应夸服务使用>
 * @author shao.xiang
 * @date 2017-06-11
 *
 */
public enum ErrorCode {
	//5000 支付的错误码
	/** 订单处理失败 */
	ERROR_5022(5022,"订单处理失败"),
	/** 下订单通讯失 */
	ERROR_5021(5021,"下订单失败，请重试!"),
	/** 被充值用户账号不存在,请确认后重新再来 */
	ERROR_5020(5020,"被充值用户账号不存在,请确认后重新再来"),
	/** 支付已下架 */
	ERROR_5019(5019,"支付已下架"),
	/** app端微信为他人充值暂未开放,敬请期待 */
	ERROR_5018(5018,"app端微信为他人充值暂未开放,敬请期待"),
	/** 公众号支付暂未开启，请稍后 */
	ERROR_5017(5017,"公众号支付暂未开启，请稍后"),
	/** 支付通知包含的商户号与本地的商户号不匹配 */
	ERROR_5016(5016,"支付通知包含的商户号与本地的商户号不匹配"),
	/** 支付宝返回的通知seller_id校验不通过 */
	ERROR_5015(5015,"支付宝返回的通知seller_id校验不通过"),
	/** 充值金额至少5元 */
	ERROR_5014(5014,"充值金额至少5元"),
	/** 通知返回的金币与本地订单的金币不相等 */
	ERROR_5013(5013,"通知返回的金币与本地订单的金币不相等"),
	/** 支付宝返回的通知签名校验不通过 */
	ERROR_5012(5012,"支付宝返回的通知签名校验不通过"),
	/** 支付宝返回的通知状态未知 */
	ERROR_5011(5011,"支付宝返回的通知状态未知"),
	/** 订单同步错误,本地未曾成功处理 */
	ERROR_5010(5010,"订单同步错误,本地未曾成功处理"),
	/** 支付订单不存在 */
	ERROR_5009(5009,"支付订单不存在"),
	/** 此订单已处理过 */
	ERROR_5008(5008,"此订单已处理过"),
	/** 微信支付,根据订单号找不到相应的\"充值记录\" */
	ERROR_5007(5007,"微信支付,根据订单号找不到相应的\"充值记录\""),
	/** 微信支付,查询订单返回错误提示 */
	ERROR_5006(5006,"微信支付,查询订单返回错误提示"),
	/** 微信支付,微信通知返回错误提示 */
	ERROR_5005(5005,"微信支付,微信通知返回错误提示"),
	/** 微信支付,验证签名失败 */
	ERROR_5004(5004,"微信支付,验证签名失败"),
	/** 微信支付,统一下单失败 */
	ERROR_5003(5003,"微信支付,统一下单失败"),
	/** 加密过程出错 */
	ERROR_5002(5002,"加密过程出错"),
	/** 支付请求失败 */
	ERROR_5001(5001,"支付请求失败，请联系客服"),
	/** 网络不给力，请稍后重试 */
	ERROR_5000(5000,"网络不给力，请稍后重试"),
	/** 参数错误 */
	ERROR_101(101, "参数错误"),
	/** 不需要重新请求，只提示错误（未知的异常应该统一使用此信息返回客户端）  */
	ERROR_100(100, "网络繁忙，请稍后重试"),
	/** 需要重新请求的错误码 */
	ERROR_1(-1, "网络繁忙，请稍后重试"),
	/** 成功 */
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
