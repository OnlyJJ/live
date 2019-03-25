package com.jiujun.shows.car.biz;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.jiujun.shows.car.constant.Constants;
import com.jiujun.shows.car.dao.SysCarMapper;
import com.jiujun.shows.car.domain.SysCarDo;
import com.jiujun.shows.car.enums.ErrorCode;
import com.jiujun.shows.car.vo.CarVo;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.vo.Page;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * 座驾业务
 * @author shao.xiang
 * @date 2017-09-21
 */
@Service("sysCarBiz")
public class SysCarBiz {

	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	
	@Resource
	private SysCarMapper sysCarMapper;

	/**
	 * 查询商城座驾信息
	 * @return
	 * @author shao.xiang
	 * @date 2017年9月21日
	 */
	public ServiceResult<Page> pageFind(CarVo reqCarVo,Page reqPageVo,String userId) {
		log.info("### SysCarBiz-pageFind:userId=" + userId + ",begin...");
		ServiceResult<Page> srt = new ServiceResult<Page>();
		srt.setSucceed(false);
		if(StringUtils.isEmpty(reqPageVo.getPageNum())
				|| StringUtils.isEmpty(reqPageVo.getPagelimit())) {
			srt.setResultCode(ErrorCode.ERROR_11001.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11001.getResultDescr());
			return srt;
		}
		String pageNum = reqPageVo.getPageNum();
		String pagelimit = reqPageVo.getPagelimit();
		int type = 0; // 默认只查询购买型的
		String carName = "";
		if(reqCarVo != null) {
			carName = reqCarVo.getCarName();
			type = reqCarVo.getCarType();
		}
		try {
			//根据id,名称、类型分页查询
			SysCarDo sysCarDo = new SysCarDo();
			sysCarDo.setUserId(userId);
			sysCarDo.setType(type);
			sysCarDo.setCarName(carName);
			int count = sysCarMapper.countForGetListByPage(sysCarDo);
			List<SysCarDo> list = sysCarMapper.getListByPage(sysCarDo);
			Page page = new Page();
			page.setCount(String.valueOf(count));
			page.setPageNum(pageNum);
			page.setPagelimit(pagelimit);
			
			JSONArray jsonArray = new JSONArray();
			if(list != null) {
				for(SysCarDo sysCar:list){
					CarVo car = new CarVo();
					int status = sysCar.getStatus();
					car.setCarId(sysCar.getId());
					car.setCarName(sysCar.getCarName());
					car.setCarType(sysCar.getType());
					car.setCarSpendGolds(sysCar.getGold());
					//返回的是金币(为了跟客户端容错)
					car.setCarSpendMoney(sysCar.getGold());
					car.setCarEffectiveDays(sysCar.getEffectiveDays());
					car.setCarComment(sysCar.getComment());
					car.setStatus(status);
					car.setInUse(sysCar.getInUse());
					car.setCarImg(Constants.cdnPath + Constants.CAR_IMG_FILE_URI  + File.separator+ sysCar.getImage());
					car.setNum(sysCar.getNum());
					car.setShowStatus(sysCar.getShowStatus());
					if(status==1){
						Date nowDate = new Date();
						Date endTime = sysCar.getEndTime();
						// 剩余天数
						int remainDays = DateUntil.daysBetween(nowDate, endTime);
						// 剩余的时间倒计时
						String remainTimes = DateUntil.getTimeRemains(nowDate, endTime);
						car.setRemainDays(remainDays);
						car.setRemainTimes(remainTimes);
						car.setEndTime(DateUntil.getFormatDate("yyyy-MM-dd HH:mm:ss", endTime));
					}
					jsonArray.add(car.buildJson());
				}
			}
			page.setDataJsonStr(jsonArray.toString());
			srt.setSucceed(true);
			srt.setData(page);
		} catch(Exception e) {
			log.error(e.getMessage(),e);
			srt.setResultCode(ErrorCode.ERROR_11000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_11000.getResultDescr());
		}
		log.info("### SysCarBiz-pageFind:userId=" + userId + ",end!");
		return srt;
	}


	
}
