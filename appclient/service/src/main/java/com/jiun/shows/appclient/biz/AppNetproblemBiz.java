package com.jiun.shows.appclient.biz;


import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jiujun.shows.common.constant.ErrorCode;
import com.jiujun.shows.common.exception.SystemDefinitionDbNotRollBackException;
import com.jiujun.shows.common.utils.DateUntil;
import com.jiujun.shows.common.utils.MemcachedUtil;
import com.jiun.shows.appclient.dao.AppNetproblemMapper;
import com.jiun.shows.appclient.domain.AppNetproblem;
import com.jiun.shows.appclient.vo.AppSubmitPb;


/**
 * app用户网络诊断数据业务
 * @author shao.xiang
 * @date 2017-09-15
 */
@Service("appNetproblemBiz")
public class AppNetproblemBiz {

	@Resource
	private AppNetproblemMapper appNetproblemMapper;
	
	/** 用户每天可提交诊断次数，3次 */
	private static final int INIT_COUNT = 3;

	public void handleAppSubmitProblem(String userId,AppSubmitPb vo) throws Exception {
		String contact = vo.getContact();
		String content = vo.getContent();
		String description = vo.getDescription();
		String diagnosisResult = vo.getDiagnosisResult();
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(contact)) {
			Exception e = new SystemDefinitionDbNotRollBackException(ErrorCode.ERROR_2017);
			throw e;
		}
		Date now = new Date();
		String nowStr = DateUntil.format2Str(now, "yyyyMMdd");
		AppNetproblem anp = this.findAppNetproblem(userId);
		boolean isInsert = true;
		if(anp != null) {
			Date addTime = anp.getAddtime();
			String timeStr = DateUntil.format2Str(addTime, "yyyyMMdd");
			if(timeStr.equals(nowStr)) {
				isInsert = false;
				String key = "netpb_" + userId + "_" + nowStr;
				Object obj = MemcachedUtil.get(key);
				int count = 1;
				if(obj != null) {
					count =Integer.parseInt(obj.toString());
					if(count > INIT_COUNT) {
						Exception e = new SystemDefinitionDbNotRollBackException(ErrorCode.ERROR_3192);
						throw e;
					}
				}
				count++;
				MemcachedUtil.set(key, count, 60*60*24); // 缓存一天
			}
		}
		// 超长则截取
		if(diagnosisResult.length() > 500) {
			diagnosisResult = diagnosisResult.substring(0, 500);
		}
		if(content.length() > 200) {
			content = content.substring(0, 200);
		}
		// 没有记录则插入
		if(isInsert) {
			AppNetproblem po = new AppNetproblem();
			po.setUserid(userId);
			po.setContact(contact);
			po.setContent(content);
			po.setDescription(description);
			po.setDiagnosisresult(diagnosisResult);
			po.setAddtime(now);
			appNetproblemMapper.insert(po);
		} else {
			// 有则更新
			anp.setContact(contact);
			anp.setContent(content);
			anp.setDescription(description);
			anp.setDiagnosisresult(diagnosisResult);
			anp.setAddtime(now);
			appNetproblemMapper.update(anp);
		}
		
	}

	public AppNetproblem findAppNetproblem(String userId) throws Exception {
		if(StringUtils.isEmpty(userId)) {
			Exception e = new SystemDefinitionDbNotRollBackException(ErrorCode.ERROR_2017);
			throw e;
		}
		return appNetproblemMapper.getAppNetproblem(userId);
	}
	
	/**验证手机号码*/
	private boolean matchMobile(String mobile) {
		Pattern pattern = Pattern.compile("^[1][34578][0-9]{9}$");
		if( null != mobile ){
			Matcher match = pattern.matcher(mobile);
			return match.matches();
		}
		return false;
	}
}
