package com.jiun.shows.others.report.biz;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiujun.shows.common.constant.ErrorCode;
import com.jiujun.shows.common.exception.SystemDefinitionException;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.vo.DeviceProperties;
import com.jiujun.shows.framework.service.ServiceResult;
import com.jiujun.shows.user.domain.UserInfo;
import com.jiujun.shows.user.service.IUserInfoService;
import com.jiun.shows.appclient.domain.AppInstallChannelDo;
import com.jiun.shows.appclient.service.IAppInstallChannelService;
import com.jiun.shows.others.report.dao.DayStatMapper;
import com.jiun.shows.others.report.domain.DayStat;
import com.jiun.shows.others.report.vo.ReportVo;


/**
 * 每日统计数据表
 * @author shao.xiang
 * @date 2017-06-15
 */
@Service("dayStatBiz")
public class DayStatBiz {

	@Resource
	private DayStatMapper dayStatMapper;
	
	/** 打开应用 */
	private static final int TYPE_OPEN = 0;
	/** 登录 */
	private static final int TYPE_LOGIN = 1;
	/** 注册  */
	private static final int TYPE_REGRET = 2;
	
	@Resource
	private IAppInstallChannelService appInstallChannelService;
	
	@Resource
	private IUserInfoService userInfoService;
	
	/** 新建表名前缀 */
	private static final String TABLE_ = "t_day_stat_";
	
	public void handleReport(ReportVo vo, DeviceProperties dp) throws Exception {
		if(vo == null || dp == null) {
			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3017);
			throw e;
		}
		// app当前操作，0-打开应用（app调用此类型时，需要在打开应用并且调用完C27接口之后，才调用本接口本类型），1-登录，2-注册
		int clientType = dp.getAppType();
		int type = vo.getType();
		int isAdd = 0;
		int isLogin = 0;
		int isReg = 0;
		int isAct = 1;
		int isVaild = 0; // 是否有效用户（注册、登录时为1）
		int retentionVaild = 0; // 注册后到现在为止的天数
		int retention = 0; // 安装后到现在为止的天数
		Date now = new Date();
		if(type == TYPE_OPEN) {
			// 打开应用，判断当前设备是否首次打开
			boolean addFlag = true;
			if(null != dp.getImei() && null != dp.getMac()) { // 安卓已经将原来的imei改为uuid
				ServiceResult<Boolean> srt = appInstallChannelService.testIfExistsAndCache(dp.getMac(), dp.getImei());
				addFlag = srt.isSucceed();
				// 校验
				if(!addFlag) {
					isAdd = 1;
				} else {
					ServiceResult<AppInstallChannelDo> asrt = appInstallChannelService.getByImei(dp.getImei());
					if(srt.isSucceed()) {
						AppInstallChannelDo d = asrt.getData();
						if(d != null) {
							int time = DateUntil.getTimeIntervalMinute(d.getRecordTime(), now);
							retention = time / 60 / 24;
						}
					}
				}
			} else {
				isAdd = 1;
			}
		} else if(type == TYPE_LOGIN) {
			isLogin = 1;
			isVaild = 1;
		} else if(type == TYPE_REGRET) {
			isReg = 1;
			isVaild = 1;
		}
		
		String addtime = DateUntil.format2Str(now, "yyyyMMddHH");
		String timeStr = DateUntil.format2Str(now, "yyyyMMdd");
		String tableName = TABLE_ + timeStr;
		DayStat ds = new DayStat();
		String uuid = "";
		if(null != vo.getUuid()) {
			String userId = vo.getUserId();
			UserInfo user = userInfoService.getUserInfoFromCache(userId);
			if(user != null) {
				int time = DateUntil.getTimeIntervalMinute(user.getAddTime(), now);
				retentionVaild = time / 60 / 24;
			}
			uuid = vo.getUuid();
		}
		String douId = "";
		String channelId = "";
		if(null != vo.getChannelId()) {
			channelId = vo.getChannelId();
			douId = channelId;
		}
		if(null != dp.getPackageName()) {
			douId += "_" + dp.getPackageName();
		}
		String userId = "";
		if(null != vo.getUserId()) {
			userId = vo.getUserId();
		}
		String version = "";
		if(null != dp.getAppVersion()) {
			version = dp.getAppVersion();
		}
		ds.setUseraccount(userId);
		ds.setVersion(version);
		ds.setChannelid(channelId);
		ds.setUuid(uuid);
		ds.setIsvaild(isVaild);
		ds.setClienttype(clientType);
		ds.setAddtime(addtime);
		ds.setDouid(douId);
		ds.setIsact(isAct);
		ds.setIsadd(isAdd);
		ds.setIslogin(isLogin);
		ds.setIsreg(isReg);
		ds.setRetention(retention);
		ds.setRetentionvaild(retentionVaild);
		
		ds.setTableName(tableName);
		dayStatMapper.insertBath(ds);
	}
}
