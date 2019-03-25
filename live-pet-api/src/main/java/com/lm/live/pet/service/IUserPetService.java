package com.lm.live.pet.service;

import java.util.List;

import com.lm.live.pet.domain.UserPet;
import com.lm.live.pet.vo.PetVo;
import com.lm.live.common.service.ICommonService;

/**
 * Service - 用户宠物表
 */
public interface IUserPetService extends ICommonService<UserPet>{
	
	/**
	 * 获取用户正在使用的宠物
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月15日
	 */
	PetVo getUsePet(String userId) throws Exception;
	
	/**
	 * 获取用户所有的宠物
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author shao.xiang
	 * @date 2018年3月15日
	 */
	List<PetVo> listUserPet(String userId) throws Exception;
}
