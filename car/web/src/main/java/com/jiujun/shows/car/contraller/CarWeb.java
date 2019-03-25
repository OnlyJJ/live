package com.jiujun.shows.car.contraller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiujun.shows.car.enums.ErrorCode;
import com.jiujun.shows.car.service.ICarParkRecordService;
import com.jiujun.shows.car.service.ISysCarService;
import com.jiujun.shows.car.service.IUserCarPortService;
import com.jiujun.shows.car.vo.CarVo;
import com.jiujun.shows.car.vo.CarportVo;
import com.jiujun.shows.car.vo.DataRequest;
import com.jiujun.shows.common.utils.AppUtils;
import com.jiujun.shows.common.utils.LogUtil;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.common.vo.Result;
import com.jiujun.shows.framework.service.ServiceResult;

@Controller("CarWeb")
public class CarWeb {
	
	@Resource
	private ISysCarService sysCarService;
	
	@Resource
	private IUserCarPortService userCarPortService;
	
	@Resource
	private ICarParkRecordService carParkRecordService;
	
	
	/**
	 * U42 - 查看当前正在使用的座驾
	 * @param data
	 * @return
	 */
	public JSONObject getInUseUserCarPortDetailInfo(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null||data.getUserBaseInfo()==null){
				result.setResultCode(ErrorCode.ERROR_11001.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_11001.getResultDescr());
			}else{
				String userId = data.getUserBaseInfo().getUserId();
				ServiceResult<CarVo> srt = userCarPortService.getInUseUserCarPortDetailInfo(userId);
				if(srt.isSucceed()){
					jsonRes.put(srt.getData().getShortName(),srt.getData().buildJson());
				}
			}
		}catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_11000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * U43 - 查询系统商城的汽车
	 * @param data
	 * @return
	 */
	public JSONObject pageFindSysCar(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null||data.getPage()==null){
				result.setResultCode(ErrorCode.ERROR_11001.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_11001.getResultDescr());
			}else{
				String userId ="";
				if(data.getUserBaseInfo()!=null){
					userId = data.getUserBaseInfo().getUserId();
				}
				CarVo carVo = data.getCarVo();
				Page reqPageVo = data.getPage();
				ServiceResult<Page> srt = sysCarService.pageFind(carVo, reqPageVo,userId);
				if(srt.isSucceed()) {
					jsonRes.put(srt.getData().getShortName(), srt.getData().buildJson());
				} else {
					result.setResultCode(srt.getResultCode());
					result.setResultDescr(srt.getResultMsg());
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_11000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}

	
	/**
	 * U44 - 购买座驾
	 * @param data
	 * @return
	 */
	public JSONObject buyCar(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null||data.getUserBaseInfo()==null||data.getCarVo()==null){
				result.setResultCode(ErrorCode.ERROR_11001.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_11001.getResultDescr());
			}else{
				String userId = data.getUserBaseInfo().getUserId(); 
				int carId = data.getCarVo().getCarId(); 
//				int num = data.getCarVo().getNum();
				ServiceResult<Boolean> srt = userCarPortService.buyCar(userId, carId);
				if(!srt.isSucceed()) {
					result.setResultCode(srt.getResultCode());
					result.setResultDescr(srt.getResultMsg());
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_11000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * U45 - 赠送座驾
	 * @param data
	 * @return
	 */
	public JSONObject giveCar(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null||data.getUserBaseInfo()==null||data.getCarVo()==null||data.getSession()==null){
				result.setResultCode(ErrorCode.ERROR_11001.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_11001.getResultDescr());
			}else{
				String sendUserId = data.getUserBaseInfo().getUserId(); 
				String receiveUserId = data.getCarVo().getReceiveUserId();
				int carId = data.getCarVo().getCarId(); 
				String sendComment = data.getCarVo().getSendComment();
				ServiceResult<Boolean> srt = 
						userCarPortService.giveCar(sendUserId, receiveUserId, carId,sendComment);
				if(!srt.isSucceed()) {
					result.setResultCode(srt.getResultCode());
					result.setResultDescr(srt.getResultMsg());
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_11000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * U46 - 设置座驾为当前使用
	 * @param data
	 * @return
	 */
	public JSONObject inUse(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null||data.getUserBaseInfo()==null||data.getCarVo()==null){
				result.setResultCode(ErrorCode.ERROR_11001.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_11001.getResultDescr());
			}else{
				String userId = data.getUserBaseInfo().getUserId(); 
				int carId = data.getCarVo().getCarId();
				ServiceResult<Boolean> srt = userCarPortService.inUse(userId, carId);
				if(!srt.isSucceed()) {
					result.setResultCode(srt.getResultCode());
					result.setResultDescr(srt.getResultMsg());
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_11000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}

	/**
	 * U47 - 分页查询拥有的座驾
	 * @param data
	 * @return
	 */
	public JSONObject pageFindUserCarPort(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null||data.getUserBaseInfo()==null||data.getPage()==null){
				result.setResultCode(ErrorCode.ERROR_11001.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_11001.getResultDescr());
			}else{
				String userId = data.getUserBaseInfo().getUserId(); 
				Page reqPageVo = data.getPage();
				ServiceResult<Page> srt = userCarPortService.pageFindUserCarPort(userId, reqPageVo);
				if(srt.isSucceed()) {
					jsonRes.put(srt.getData().getShortName(), srt.getData().buildJson());
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_11000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}
	
	/**
	 * U56 - 取消使用
	 * @param data
	 * @return
	 */
	public JSONObject cancelInUse(DataRequest data){
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null||data.getUserBaseInfo()==null){
				result.setResultCode(ErrorCode.ERROR_11001.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_11001.getResultDescr());
			}else{
				String userId = data.getUserBaseInfo().getUserId(); 
				ServiceResult<Boolean> srt = userCarPortService.cancelInUse(userId);
				if(!srt.isSucceed()) {
					result.setResultCode(srt.getResultCode());
					result.setResultDescr(srt.getResultMsg());
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_11000.getResultDescr());
		}
		jsonRes.put("result",result.buildJson());
		return jsonRes; 
	}

	/**
	 * U57 查询车位使用情况，返回正在使用的车位和车id
	 * @param data
	 * @return
	 */
	public JSONObject findCarportStatus(DataRequest data) {
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject json = new JSONObject();
		JSONArray jarry = new JSONArray();
		try {
			if(data == null || data.getAnchorInfo() == null) {
				result.setResultCode(ErrorCode.ERROR_11001.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_11001.getResultDescr());
			} else {
				String appVersion = "old";
				if(data.getDeviceProperties() != null && data.getDeviceProperties().getAppVersion() != null) {
					appVersion = data.getDeviceProperties().getAppVersion();
				}
				String roomId = data.getAnchorInfo().getRoomId();
				ServiceResult<List<CarportVo>> srt = carParkRecordService.findCarportByRoomId(roomId);
				if(srt.isSucceed()) {
					List<CarportVo> list = srt.getData();
					if(list != null) {
						if("old".equals(appVersion) || !AppUtils.checkAppVersion("3.0.1",appVersion)) {
							int showSize = 3;
							for(int i=0;i<list.size();i++) {
								if(showSize <= 0){
									break;
								}
								CarportVo c = list.get(i);
								int carPort = c.getCarport();
								if(carPort <= 3){
									jarry.add(c.buildJson());
									showSize --;
								}
							}
						} else {
							for(CarportVo c : list) {
								jarry.add(c.buildJson());
							}
						}
						json.put("list", jarry.toString());
					}
				} else {
					result.setResultCode(srt.getResultCode());
					result.setResultDescr(srt.getResultMsg());
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_11000.getResultDescr());
		}
		json.put("result", result.buildJson());
		return json;
	}
	
	/**
	 * U58  抢车位
	 * 	
	 */
	public JSONObject grapCarport(DataRequest data) {
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());
		JSONObject json = new JSONObject();
		try{
			if(data == null || data.getAnchorInfo() == null ||data.getUserBaseInfo() == null
					|| data.getCarportVo() == null) {
				result.setResultCode(ErrorCode.ERROR_11001.getResultCode());
				result.setResultDescr(ErrorCode.ERROR_11001.getResultDescr());
			} else {
				//主播房间
				String roomId = data.getAnchorInfo().getRoomId();
				//主播id
				String anchorId = data.getAnchorInfo().getUserId();
				//用户id
				String userId = data.getUserBaseInfo().getUserId();
				//主播等级
				String anchorLev = data.getAnchorInfo().getAnchorLevel();
				int carport = data.getCarportVo().getCarport();
				ServiceResult<Boolean> srt = 
						carParkRecordService.grapCarport(roomId,anchorId,userId,anchorLev,carport);
				if(!srt.isSucceed()) {
					result.setResultCode(srt.getResultCode());
					result.setResultDescr(srt.getResultMsg());
				}
			}
		} catch(Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			result.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_11000.getResultDescr());
		}
		json.put("result", result.buildJson());
		return json;
	}
	
}
