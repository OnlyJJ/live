package com.jiujun.shows.dynamic.biz.home;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiujun.shows.common.redis.RedisUtil;
import com.jiujun.shows.dynamic.constant.CacheKey;
import com.jiujun.shows.dynamic.constant.Constants;
import com.jiujun.shows.dynamic.dao.DiaryInfoImgMapper;
import com.jiujun.shows.dynamic.dao.DiaryInfoImgSubMapper;
import com.jiujun.shows.dynamic.domain.home.DiaryInfoImg;
import com.jiujun.shows.dynamic.domain.home.DiaryInfoImgSub;
import com.jiujun.shows.dynamic.enums.ErrorCode;
import com.jiujun.shows.dynamic.vo.ImagesVo;
import com.jiujun.shows.framework.service.ServiceResult;


/**
 * Service -动态图片表
 * @author shao.xiang
 * @Date 2017-06-04
 */
@Service("diaryInfoImgBiz")
public class DiaryInfoImgBiz {
	private static final Logger log = Logger.getLogger(Constants.LOG_CAR_SERVICE);
	
	@Resource
	private DiaryInfoImgMapper diaryInfoImgMapper;
	
	@Resource
	private DiaryInfoImgSubMapper diaryInfoImgSubMapper;
	
	/**
	 * 根据动态id获取动态图片
	 * @param dirayInfoId
	 * @return
	 * @throws Exception
	 */
	public ServiceResult<List<ImagesVo>> getImagesVoFromCache(long diaryInfoId) {
		ServiceResult<List<ImagesVo>> srt = new ServiceResult<List<ImagesVo>>();
		srt.setSucceed(false);
		List<ImagesVo> ret = null;
		String imageKey = CacheKey.DIARY_IMAGES_CACHE + diaryInfoId;
		try {
			Object imageCache = RedisUtil.get(imageKey);
			if(imageCache != null) {
				ret = (List<ImagesVo>) imageCache;
			} else {
				// 取大图
				List<DiaryInfoImg> imgs = diaryInfoImgMapper.getImgsByDiaryInfoId(diaryInfoId);
				if(imgs != null && imgs.size() > 0) {
					ret = new ArrayList<ImagesVo>();
					for(DiaryInfoImg img : imgs) {
						ImagesVo vo = new ImagesVo();
						long id = img.getId();
						String bigUrl = img.getUrl();
						vo.setBigImage(bigUrl);
						// 取小图
						List<DiaryInfoImgSub> imgsubs = diaryInfoImgSubMapper.getImgByDiaryInfoImageId(id);
						if(imgsubs != null && imgsubs.size() >0) {
							for(DiaryInfoImgSub imgsub : imgsubs) {
								String smallUrl = imgsub.getUrl();
								vo.setSmallImage(smallUrl);
							}
						}
						ret.add(vo);
					}
					RedisUtil.set(imageKey, ret);
				}
			}
			srt.setSucceed(true);
			srt.setData(ret);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			srt.setResultCode(ErrorCode.ERROR_8000.getResultCode());
			srt.setResultMsg(ErrorCode.ERROR_8000.getResultDescr());
		}
		return srt;
	}
}
