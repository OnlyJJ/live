package com.jiujun.shows.dynamic.constant;


/**
 * 存放mc key前缀相关的常量
 * @author huangzp
 * @date 2015-4-7
 */
public class MCPrefix {
	
	/** 用户登录后，服务端派发给客户端用于数据加密token的key前缀 */
	public static final String MC_TOKEN_PREFIX = "mc_data_token_";
	public static final String MC_USERINFO_PREFIX = "mc_userinfo_";
	
	/** msgid消息资源映射关系的key前缀 */
	public static final String MC_MSGID_MAP_PREFIX = "mc_msgid_map_";
	
	/** 聊天消息中的url地址缓存的key前缀 */
	public static final String MC_MSG_URL_PREFIX = "mc_msg_url_";
	
	/** im系统中存储token */
	public static final String IM_MC_SESSION_ = "mc_session_";
	
	/**
	 * 保存用户信息到cache的key前缀
	 */
	public final static String USERCACHEINFO_PREKEY = "service_user_";
	
	
	/**
	 * 保存房间-角色信息到cache的key前缀
	 */
	public final static String ROOMCHEINFO_PREKEY = "service_room_role_";
	
	/**
	 * 房间
	 */
	public final static String ROOM_ONLINE_MEMBERS_PREKEY = "roomonlinemembers_";
	
	/** 找回密码时,发送邮箱code的cache key前缀 */
	public final static String RETRIEVE_PWD_CACHEINFO_PREKEY = "retrievePwd_";
	
	/** 系统自动注册时,保存信息的cache key前缀 */
	public final static String USERINFO_AUTO_REGIST_PREKEY = "userinfoautoregist_";
	
	/** 抢红包,保存当前已关注主播的用户id的cache key前缀 */
	public final static String REDPACKET_ATTENTION_USERIDS_PREKEY = "redpacket_fans_";
	
	/** 抢红包,保存已拆分好的子红包tableId的cache key前缀 */
	public final static String REDPACKET_RECEIVE_TABLEIDS_PREKEY = "redpacket_receiveTableIds_";
	
	/** 抢红包,保存已拆分好的子红包tableId的cache key前缀 */
	public final static String REDPACKET_ENDTIME_PREKEY = "redpacket_endtime_";
	
	/** 抢红包,保存已拆分好的子红包tableId的cache key前缀 */
	public final static String REDPACKET_RECEIVE_USERID = "redpacket_receive_userid_";
	
	/** 热门主播列表放到缓存 */
	public final static String HOT_ANCHOR_LIST_IN_CACHE = "hot_anchor_list_in_cache"; 
	
	/** 系统已用的机器人userId*/
	public final static String SYS_ALL_LOGIN_ROBOT_USERID_IN_CACHE = "AllLoginRobotUserId";
	
	/** 记录房间机器人*/ 
	public final static String RECORD_ROOM_ROBOTS_IN_CACHE = "recordRoomRobotS_";
	
	/** 已安装并记录了渠道号的设备 */ 
	public final static String APP_HAVE_INSTALL_DEVICE_IN_CACHE = "appHaveInstallDevice_";
	
	/** 房间虚拟的在线人数  */ 
	public final static String VIRTURALONLINEMEMBERNUM_IN_CACHE = "virtualRoomOnlineMembersNum_";
	
	/** 已经停播的主播roomId  */ 
	public final static String ANCHOR_PLAYOFF_RECORD_IN_CACHE = "anchorPlayOff_";
	
	/** 自动加免费礼物   */
	public final static String AUTO_ADD_FREE_GIFT_USEID_IN_CACHE = "auto_add_free_gift_userId_";
	
	/** 免费礼物对象   */
	public final static String AUTO_FREE_GIFT_USEID_IN_CACHE = "auto_free_gift_userId_";
	
	/** 保存用户进入房间时间*/
	public final static String INROOM_TIME_KEY = "inRoomTime_";
	
	/** 保存用户访问房间时长*/
	public final static String VISIT_TIME_KEY =  "visitTime";
	
	/** 保存每个房间的蜜桃经验 */
	public final static String ROOM_DAILY_PEACH_ = "roomDailyPeach_";
	
	/** 保存礼物的cahce的key前缀 */
	public static final String GIFT_INFO_KEY = "gift__";
	

	/** 保存请求生成图片验证码时的code */
	public static final String IMG_CODE = "imgCode_";
	
	/** 根据等级缓存蜜桃 */
	public static final String PEACH_LEVEL = "peachLevel_";
	
	/** service端请求生成的用于发送系统消息的imtoken,不宜保存过长，因为这个没有客户端保持心跳  */
	public static final String IM_TOKEN_FOR_SYSTEMMSG = "imTokenForSystemMsg";
	
	/** 用户登录错误次数记录前缀  */
	public static final String LOGIN_ERROR_DAILY_NUM = "loginErrorDailyNum_";
	
	/** ip访问频率记录前缀  */
	public static final String IP_REQ_FREQUENCY = "ip_req_frequency_";
	
	/** 保存游客昵称  */
	public static final String PESUDOUSERNAME = "pesudoUserName_";
	
	/** 猜你喜欢主播列表放到缓存 */
	public static final String LOVE_FIRST_PAGE_ANCHOR_LIST_IN_CACHE = "love_first_page_anchor_list_in_cache";
	
	/** 猜你喜欢主播page信息放到缓存 */
	public static final String LOVE_FIRST_PAGE_IN_CACHE = "love_first_page_list_in_cache";
	
	/** 查询全部主播列表放到缓存 */
	public static final String ALL_FIRST_PAGE_ANCHOR_LIST_IN_CACHE = "all_first_page_pageInfo_anchor_list_in_cache";
	
	/** 查询全部主播page放到缓存 */
	public static final String ALL_FIRST_PAGE__IN_CACHE = "all_first_list_in_cache";
	
	/** 主播实体(db信息)缓存 */
	public static final String ANCHOR_DB_IN_CACHE = "anchor_db_info_cahce";
	
	/** 保存停播状态的房间号 */
	public static final String ROOMIDS_PLAYOFF_IN_CACHE = "roomids_playoff";
	
	/** 缓存最近一段时间有开播记录的主播 */
	public static final String LATELY_PALY_ANCHOR_LIST_PLAY_CACHE = "lately_play_anchor_list_cache";
	
	/** 缓存所有开播记录的主播 */
	public static final String ALL_PALY_ANCHOR_LIST_PLAY_CACHE = "all_play_anchor_list_in_cache";
	
	/** 发送手机验证码时,缓存code */
	public static final String VERIFICATIONCODE_MOBILE_ = "verificationCode_mobile_";
	
	
	/** 缓存砸金蛋的奖品 */
	public static final String EGGS_LOTTERY_DRAW_WITH_FIXEDGIFT = "eggs_lottery_draw_with_fixedgift";
	
	/** 缓存砸银蛋的奖品 */
	public static final String EGGS_LOTTERY_DRAW_WITH_SILVER = "silver_eggs_fixedgift";
	
	/** 缓存幸运夺宝的奖品 */
	public static final String TREASURE_LOTTERY_DRAW_WITH_FIXEDGIFT = "treasure_lottery_draw_with_fixedgift";
	
	
	/** 主播实体(db信息)缓存 */
	public static final String ALL_ANCHOR_DB_IN_CACHE = "all_anchor_db_info_cahce";
	
	/** 首页主播分页缓存 */
	public static final String ALL_MAIN_PAGE_ANCHOR_DB_IN_CACHE = "all_main_page_anchor_db_info_cahce";
	
	/** 老的首页主播分页缓存 */
	public static final String ALL_MAIN_PAGE_ANCHOR_DB_IN_CACHE_OLD = "all_main_page_anchor_db_old_info_cahce";
	
	/** 新的首页主播分页，缓存 */
	public static final String ALL_MAIN_PAGE_ANCHOR_DB_IN_CACHE_NEW = "all_main_page_anchor_db_new_info_cahce";
	
	/** 热门推荐的主播缓存 */
	public static final String MAIN_RECOMMEND_ANCHOR_DB_IN_CACHE = "main_recommend_anchor_db_info_cahce";
	
	/** 充值送礼活动配置缓存 */
	public static final String ACTIVITIES_PAY_CONF_DB_CACHE = "activities_pay_conf_db_cache";
	
	/** 消费送礼活动配置缓存 */
	public static final String ACTIVITIES_CONSUME_CONF_CACHE = "activities_consume_conf_db_cache";
	
	/** 运营活动奖励配置缓存 */
	public static final String ACTIVITIES_PRIZES_CONF_CACHE = "activities_prize_conf_db_cache";
	
	/** 用户对某房间的当天打卡记录缓存前缀 */
	public static final String USER_FOR_ROOM_PUNCHCARD_TODAY_CACHE = "user_for_room_punchcard_today_";
	
	/** 用户对某房间的当天打卡记录缓存前缀 */
	public static final String USER_FOR_ROOM_PUNCHCARD_TODAY_CACHE_NEW = "upunchcard_";
	
	/** 用户对某房间的所有打卡记录缓存前缀 */
	public static final String USER_FOR_ROOM_PUNCHCARD_ALL_CACHE = "user_for_room_punchcard_all_";
	
	/** app公告信息缓存 */
	public static final String APP_ARTICLE_INFO_DB_CACHE = "app_article_info_db_cache";
	

	/** 房间非守护信息缓存 */
	public static final String USER_WORK_INFO_DB_CACHE_FOR_ROOM = "user_work_info_db_cache_room_";
	
	/** 用户所有房间守护信息缓存 */
	public static final String USER_GUARD_INFO_IN_ALL_ROOM_CACHE = "user_guard_info_in_all_room_cache_";
	
	/** 用户个人房间个人守护信息缓存 */
	public static final String USER_GUARD_INFO_IN_ROOM_CACHE = "userself_guard_info_in_room_cache_";
	
	/** 房间守护信息缓存 */
	public static final String GUARD_WORK_INFO_DB_CACHE_FOR_ROOM = "guard_work_info_db_cache_room_";
	
	/** 蜜桃成熟时，房间守护信息缓存，用于固定摘蜜桃机会，缓存24h，蜜桃摘完后，删除该缓存 */
	public static final String ROOM_GUARD_FOR_PEACH_CACHE = "room_guard_for_peach_in_db_cache_";
	
	/** 守护配置信息缓存，时间2h */
	public static final String GUARD_CONF_ALL_CACHE = "guard_conf_all_data_cache";
	
	/** 用户、主播礼物排行榜 */
	public static final String STAT_SPECIAL_GIFT_CACHE = "stat_special_gift_cache";
	
	/** 游戏礼物缓存前缀,加上游戏id构成 key*/
	public static final String GAME_DRAW_WITH_FIXEDGIFT = "game_lottery_draw_with_fixedgift_";
	
	/** 排首页、房间(魅力、财富)排行(总、月、周、日)榜的缓存 key前缀*/
	public static final String RANK = "rank_";
	
	/** 幸运礼物-公布的奖金池 */
	public static final String LUCKYGIFT_PUBLICGOLD = "luckygift_publicgold";
	
	/** 缓存纪录已经不能玩摇钱树的用户(3天之后),因为用户每次进入房间都要查 */
	public static final String CannotGameMoneytree = "cannotGameMoneytree_";
	
	/** 房间守护配置缓存，5main */
	public static final String GUARD_WORK_CONF_INFO_DB_CACHE = "guard_work_conf_info_db_cache";
	
	/** 主播总数缓存，12h */
	public static final String ANCHOR_SUM_DATA_INFO_DB_CACHE = "anchor_sum_data_info_db_cace";
	
	/** 2016111活动:蜜桃万人迷,纪录收到礼物的数量到缓存 */
	public static final String RecordReceiveNum20161111ActivieyWanrenmiGiftNum_DB_CACHE= "recordReceiveNum20161111ActivieyWanrenmiGiftNum_";
	
	/** 不存在的机器人id  */ 
	public static final String ROBOT_DB_NOT_IDS_CACHE = "robotDbNotIds_";
	
	/** 正在直播的房间Obj   */ 
	public static final String ONLNE_ROOM_CACHE = "onlive_room_cahce_";
	
	/** 房间录像   */ 
	public static final String 	ROOM_DEFAULTVIDEO_CACHE = "room_defaultvedio_cahce_";
	

	/** 房间红包雨(停止标志)   */ 
	public static final String 	ROOM_HONGBAOYU_STOP_CACHE = "room_hongbaoyu_stop_cahce_";
	
	/** 每日任务配置信息缓存 */
	public static final String USER_DAILY_TASK_CONF_CACHE = "user_daily_task_conf_cache";
	
	/** 用户每日任务缓存 */
	public static final String USER_DAILY_TASK_CACHE = "user_daily_task_cache_";
	
	/** 用户每日任务奖励领取缓存 */
	public static final String USER_DAILY_TASK_RECEIVE_CACHE = "user_daily_task_receive_cache_";
	
	/** 用户每日送礼记录缓存 -次数 */
	public static final String USER_SENDGIFT_COUNT_CACHE = "user_sendgift_count_cache_";
	
	/** 用户信息缓存 t_user_info*/
	public static final String USER_INFO_CACHE = "service_user_info_cache_";
	
	/** 游戏信息缓存 t_activity_game_conf*/
	public static final String GAME_CONF_CACHE = "service_game_conf_cache_";

	/** 守护配置信息缓存 */
	public static final String GUARD_CONF_FOR_CACHE = "guard_conf_for_cache";
	
	/** 签到查询列表缓存 */
	public static final String USER_SIGN_FINDLIST_CACHE = "user_sign_findlist_cache_";
	
	/** 用户房间禁言、被踢信息缓存 */
	public static final String USER_ROOMBANNEDOPREATION_CACHE = "user_roombannedoperation_cache_";
	
	/** 主播设置 @ 我内容列表*/
	public static final String USER_SET_RDC_LIST = "user_set_rdc_list_";
	
	/** 系统默认设置@我内容*/
	public static final String SYSTEM_SET_RDC_LIST = "sys_set_rdc_list_000000";
	
	/** 用户关注主播缓存 */
	public static final String USER_ATTENTION_ANCHOR_CACHE = "user_attention_anchor_cache_";
	
	/** 摇钱树缓存 */
	public static final String USER_GAME_FOR_YAOQIANSHU_CACHE = "user_game_yqs_cache_";
	
	/** 主播首页缓存 */
	public static final String ANCHOR_C1_DECORATE_CACHE = "anchor_first_decorate_Cache_";
	
	/** 主播收礼缓存 */
	public static final String ANCHOR_RECEIVES_GIFT_CACHE = "anchor_receviesgifts_cache_";
	
	/** 首页数据缓存 */
	public static final String ANCHOR_C1_DATA_CACHE = "anchor_c1_data_cache";
	
	/** 首页数据缓存 (旧版本)*/
	public static final String ANCHOR_C1_OV_DATA_CACHE = "anchor_c1_ov_data_cache";
	
	/**用户送礼缓存 */
	public static final String USER_SEDNGIFT_FORND_CACHE = "user_send_ndgifts_cache_";
	/** 主播年度活动收礼缓存 */
	public static final String ANCHOR_RECEIVES_NDGIFT_CACHE = "anchor_recevies_ndgifts_cache_";

	/** 用户在某房间具有勋章礼物权限 */
	public static final String USER_ATROOM_CAN_SEND_DECOREATE_GIFT_CACHE = "user_atRoom_can_send_decorate_gift_";
	
	/** 房间蜜桃每天成熟的纪录 */
	public static final String ROOM_PEACH_DAILY_RIPE_CACHE = "room_peach_daily_ripe_cache_";
	
	/** 圣诞节活动，成熟后的树可摘次数缓存 */
	public static final String CHRISTMAS_REMAINPLAYNUM_LEVEL_CACHE = "christmas_cache_";
	
	/** 201612月活动:雪山之豹 ,用户送礼每日数据缓存  */
	public static final String ACTIVITY_201612_XUESHAN_SENDGIFT_DAILY_CACHE = "activity_201612_xueshan_sendgift_daily_cache_";
	
	/** 201612月活动:雪山之豹 ,用户送礼日榜数据缓存  */
	public static final String ACTIVITY_201612_XUESHAN_SENDGIFT_DAILY_RANK_CACHE = "activity_201612_xueshan_sendgift_daily_rank_cache_";
	
	/** 活动配置缓存 ，拼接配置表的NO */
	public static final String ACTIVITY_CONF_DB_CACHE = "activity_conf_db_cache_";
	
	/** 活动送礼缓存，缓存为用户送礼物的总数量 */
	public static final String ACTIVITY_USER_GIFT_TOTAL_CACHE = "user_sendgift_total_";
	
	/** 201612月活动:雪山之豹 ,登上雪山之巅的历史数据缓存  */
	public static final String ACTIVITY_201612_XUESHAN_TOP_HIS_CACHE = "activity_201612_xueshan_top_his_cache_";
	
	/** 用户ip归属地缓存*/
	public static final String USER_IP_REGION_CACHE = "user_ip_region_";
	
	/** 用户分享缓存 */
	public static final String USER_SHARE_CACHE = "user_share_cache_";
	
	/** 签到用户ip缓存 */
	public static final String USER_SIGN_IP_CACHE = "user_sign_ip_";
	
	/** 省市缓存key*/
	public static final String PROVINCE_CODE_CACHE = "province_code_";
	
	/** 记录每个ip每日摘蜜桃的次数  */
	public static final String PEACH_EVERY_IP_DAILY_PLUCK_TOTAL_CACHE = "peach_every_ip_daily_pluck_total_";
	
	/** 记录每个ip上次请求摘蜜桃的时间  */
	public static final String PEACH_IP_PRE_REQ_PLUCK_TIME_CACHE = "peach_ip_pre_req_pluck_time_";
	
	/** 记录每个ip抢每个红包的个数  */
	public static final String REDPACK_EVERY_IP_GRAB_TOTAL_CACHE = "redpack_every_ip_grab_total_";
	
	/** 记录上一次缓存省市记录时间  */
	public static final String PROVINCE_TIME_CACHE = "province_time_";
	
	/** 受惩罚的主播 */
	public static final String PUNISMENT_FOR_ANCHORS_CACHE = "punisment_anchors";
	
	/** 苹果用户每日充值次数  */
	public static final String APPLE_USER_24HOUR_CHARGE_TIMES_CACHE = "apple_user_24hour_charge_times_";
	
	/** @缓存内容 */
	public static final String ROOM_CHATMSG_CACHE = "room_chat_all_msg";
	/** 首页热门推荐主播缓存 */
	public static final String RECOMMOND_ANCHOR_FOR_C1 = "recommond_anchor_cache";
	/** 首页新秀推荐主播缓存 */
	public static final String NEW_ANCHOR_FOR_C1 = "new_anchor_cache";
	
	/** 2017暖冬计划,主播收到秋裤数量的缓存(Map) */
	public static final String ACTIVITY_201701_NUANDONG_RECEIVEQIUKUGIFT_CACHE = "nuandong_receiveQiukuGift";
	
	/** 2017暖冬计划,主播收到围巾数量的缓存(Map) */
	public static final String ACTIVITY_201701_NUANDONG_RECEIVEWEIJINGIFT_CACHE = "nuandong_receiveWeijinGift";
	
	/** 2017暖冬计划,送出暖冬礼物数量的缓存(Map) */
	public static final String ACTIVITY_201701_NUANDONG_SENDGIFT_CACHE = "nuandong_sendGift";
	
	/** 2017暖冬计划,已经下发勋章奖励的用户string */
	public static final String ACTIVITY_201701_NUANDONG_PRIZE_USER_CACHE = "nuandong_prize_user_";
	
	/** 2017暖冬计划,已经下发勋章奖励的主播string */
	public static final String ACTIVITY_201701_NUANDONG_PRIZE_ANCHOR_CACHE = "nuandong_prize_anchor_";
	
	/** 2017暖冬计划,过冬成功,已经发送im全站通知的主播list<userId> */
	public static final String ACTIVITY_201701_NUANDONG_GUODONGSUCCESS_IMMSG_CACHE = "nuandong_guodongSuccess_imMsg";
	/** 2017新首页缓存 ,ONE*/
	public static final String ANCHOR_MAIN_NEW_ONE_DATAS_CACHE = "anchor_main_new_one_datas_cache";
	/** 2017新首页缓存 ,TWO*/
	public static final String ANCHOR_MAIN_NEW_TWO_DATAS_CACHE = "anchor_main_new_two_datas_cache";
	
	/** 2017暖冬计划,进入云端城堡的主播list<userId> */
	public static final String ACTIVITY_201701_YUNDUANCHENGBAO_ANCHOR_CACHE = "nuandong_yunduanchengbao_anchor";
	
	/** 大喇叭间隔缓存 */
	public static final String DLB_MSG_SENDTIME_CACHE = "dlb_msg_sendtime_";
	
	/** 2017暖冬计划, 榜单(凛冬)数据缓存List*/
	public static final String ACTIVITY_201701_NUANDONG_RANK_LINDONG_RESULT_CACHE_TIME = "nuandong_rank_lindong";
	
	/** 2017暖冬计划, 榜单(暖冬)数据缓存List*/
	public static final String ACTIVITY_201701_NUANDONG_RANK_NUANDONG_RESULT_CACHE_TIME = "nuandong_rank_nuandong";
	
	/** 2017暖冬计划, 榜单(云端城堡)数据缓存List*/
	public static final String ACTIVITY_201701_NUANDONG_RANK_YUNDUANCHENGBAO_RESULT_CACHE_TIME = "nuandong_rank_yunduanchengbao";
	
	/** 2017暖冬计划,进入凛冬榜的主播list<userId> */
	public static final String ACTIVITY_201701_LINDONGBANG_ANCHOR_CACHE = "nuandong_lindongbang_anchor";
	
	/** 2017新年活动，主播收礼缓存 */
	public static final String ACTIVITY_2017_NEWYEAR_ANCHOR_CACHE = "newyear_anchor_gifts_";
	
	/** 新年活动今日财富榜缓存 */
	public static final String FUHAO_SORT_TODAY_CACHE = "fuhao_sort_today_cache_";
	/** 新年活动昨日财富榜缓存 */
	public static final String FUHAO_SORT_YESDAY_CACHE = "fuhao_sort_yesday_cache_";
	/** 新年活动，财富榜相关配置缓存 */
	public static final String FUHAO_PRIZE_CONF_CACHE = "fuhao_prize_cache";
	/** 新年活动，发红包时缓存uuid */
	public static final String NEWYEAR_ACTIVITY_REDPACKET_CACHE = "newyear_activity_redpacket_";
	/** 新年活动，缓存发红包的特殊礼物数 */
	public static final String NEWYEAR_ACTIVITY_SPECIALNUM_CACHE = "newyear_activity_specialnum";
	/** 主播开播时长缓存 */
	public static final String ANCHOR_LIVECDN_RECORD_CACHE = "anchor_livecdn_record_";

	/** 2017情人节-用户全站总共送出礼物总量  */
	public static String CACHEKEYOF201702QINGRENJIEUSERWHOLESITETOTALSENDNUM = "s_201702qrj_u_ws_t_s_num";
	
	/** 2017情人节-用户全站总共送出礼物总量排行榜  */
	public static String CACHEKEYOF201702QINGRENJIEUSERWHOLESITETOTALSENDNUMRANK = "s_201702qrj_u_ws_t_s_num_rank";
	
	/** 2017情人节-主播奖励纪录 */
	public static String CACHEKEYOF201702QINGRENJIEANCHORPRIZE = "s_201702qrj_a_prize";
	
	/** 2017情人节-用户表白成功纪录 */
	public static final String CACHEKEYOF201702QINGRENJIEUSERBIAOBAISUCCESS =  "s_201702qrj_u_bb_success";
	
	/** 2017情人节- 用户表白成功排行 */
	public static String CACHEKEYOF201702QINGRENJIEBIAOBAISUCCESSRANKDATA = "s_201702qrj_bb_success_rank";
	
	/** 2017情人节- 用户在每个房间送出的礼物数量 */
	public static String CACHEKEYOF201702QINGRENJIEUSEREACHROOMSENDNUM = "s_201702qrj_u_each_r_numm";
	
	/**  2017情人节-主播总共收到礼物数量  */
	public static String CACHEKEYOF201702QINGRENJIEANCHORTOTALRECEIVENUM = "s_201702qrj_a_t_r_num";
	
	/**  2017情人节-主播总共收到礼物数量  */
	public static String CACHEKEYOF201702QINGRENJIEANCHORTOTALRECEIVENUMRANK = "s_201702qrj_a_t_r_num_rank";
	
	/**  2017情人节-开启守护精灵通知  */
	public static String CACHEKEYOF201702QINGRENJIESTARTJINGLINGIMMSG = "s_201702qrj_foom_im_start_sh";
	
	/** 缓存用户砸蛋次数 */
	public static final String USER_OPENEGGS_TIMES_CACHE = "s_user_openeggs_times_cache_";
	
	/** 缓存用户夺宝次数 */
	public static final String USER_TREASURE_TIMES_CACHE = "s_user_treasure_times_cache_";
	
	/** 缓存用户(真实用户的)包裹数 */
	public static final String ROOM_USER_GIFT_PACKAGE_CACHE = "s_room_user_gift_package_";
	

	/** 缓存房间内显示的礼物列表 */
	public static final String ROOM__GIFT_SHOW_CACHE = "s_room__gift_show_cache";
	
	/** 统计主播收礼数量,给勋章 */
	public static final String STATISTICS_AHCHOR_RECEIVE_GIFT_NUM_CACHE = "s_statistics_ahchor_receive_gift_num_";
	
	/** 缓存用户勋章 */
	public static final String USER_DECORATE_CACHE = "s_user_decorate_cache_";
	
	/** 缓存用户已佩戴勋章 */
	public static final String USER_DECORATE_HASADORN_CACHE = "s_user_decorate_hasAdorn_cache_";
	
	/** 缓存主播勋章 */
	public static final String AHCHOR_DECORATE_CACHE = "s_ahchor_decorate_cache_";

	/** 缓存房间-勋章墙显示的主播勋章 */
	public static final String ROOM_DECORATE_WALLS_CACHE = "s_room_decorate_walls_cache_";

	/** 桃花活动，主播收礼总数缓存 */
	public static final String TAOHUA_ANCHOR_ALLTOTALGIFT_CACHE = "taohua_anchor_recevies_gifts_";
	/** 主播摘桃花勋章缓存 */
	public static final String TAOHUA_ANCHOR_PLUCK_DECORATE_CACHE = "anchor_pluck_decorate_";
	/**  萌芽榜 */
	public static final String TAOHUA_ANCHOR_BANGDAN_MENGYA_CAHCE = "taohua_mengya";
	/**  青春榜 */
	public static final String TAOHUA_ANCHOR_BANGDAN_QINGCHUN_CAHCE = "taohua_qingchun";
	/**  绽放榜 */
	public static final String TAOHUA_ANCHOR_BANGDAN_ZHANFANG_CAHCE = "taohua_zhanfang";
	/** 桃花活动，用户送礼总数缓存 */
	public static final String TAOHUA_USER_ALLTOTALGIFT_CACHE = "taohua_user_send_gifts_";
	/** 桃花活动，用户送礼单个房间数量缓存 */
	public static final String TAOHUA_USER_TOTALGIFTBYROOM_CACHE = "taohua_user_send_gifts_byroom_";
	/** 桃花活动，单个房间所有用户送礼缓存 */
	public static final String TAOHUA_USER_SEND_GIFTBYROOM_CACHE = "taohua_sendgifts_alluser_byroom_";
	/** 用户送雨露缓存 */
	public static final String TAOHUA_USER_SENDYULU_CACHE = "taohua_user_sendyulu_";
	/** 给用户发勋章缓存，每个用户只发一次 */
	public static final String TAOHUA_USER_DECORATE_CACHE = "taohua_user_decorate_";
	/** 桃花活动特效缓存 */
	public static final String TAOHUA_TEXIAO_CACHE = "taohua_texiao_";
	/** 桃花精灵 */
	public static final String TAOHUA_JINGLING_CACHE = "taohua_jingling_";
	/** 捕捉桃花精灵次数 */
	public static final String TAOHUA_CATCH_JINGLING_CACHE = "taohua_catch_jingling_";
	/** 桃花精灵王 */
	public static final String TAOHUA_JINGLINGWANG_CACHE = "taohua_jingling_wang_";
	/** 捕捉桃花精灵王次数 */
	public static final String TAOHUA_CATCH_JINGLINGWANG_CACHE = "taohua_catch_jingling_wang_";
	
	/** 缓存用户工具背包数据 */
	public static final String USER_TOOL_PACKAGE_CACHE = "s_user_tool_package_cache_";
	
	/** 缓存房间默认录制的视屏url数据 */
	public static final String ROOM_DEFAULT_VIDEO_URL_CACHE = "s_room_default_video_url_cache_";
	
	/** 缓存砸蛋勋章奖励历史 */
	public static final String DECORATE_PRIZE_HIS_ZADAN_CACHE = "s_decorate_prize_his_zadan_cache_";
	
	/** 缓存夺宝勋章奖励历史 */
	public static final String DECORATE_PRIZE_HIS_TREASURE_CACHE = "s_decorate_prize_his_treasure_cache_";
	
	/** 缓存用户浏览历史 */
	public static final String USER_VISIT_HIS_CACHE = "s_user_visit_his_cache_";
	
	/** 缓存用户浏览的房间(单行纪录) */
	public static final String USER_VISITROOM_HIS_CACHE = "s_user_visitroom_his_cache_";
	/** 为爱奔跑活动，所有主播每日收礼缓存 */
	public static final String LOVE_ANCHOR_RECEIVES_ALL_TOTAL = "love_anchor_all_num_";
	/** 为爱奔跑活动，主播每日收到礼物缓存 */
	public static final String LOVE_ANCHOR_RECEIVES_TODAY_TOTAL = "love_anchor_today_num_";
	/** 为爱奔跑活动，主播上冠军跑道的次数 */
	public static final String LOVE_ANCHOR_GETFIRST_TOTAL = "love_anchor_first_total_";
	/** 为爱奔跑活动，主播可开启百米竞速次数 */
	public static final String LOVE_ANCHOR_OPENRUNWAY_TOTAL = "love_anchor_openrunway_count_";
	/** 为爱奔跑活动，每日冠军跑道 */
	public static final String LOVE_ANCHOR_FIRST = "love_anchor_first_";
	/** 为爱奔跑活动，主播每日上冠军跑道 */
	public static final String LOVE_ANCHOR_DAY_FIRST = "love_anchor_day_first_";
	/** 为爱奔跑活动，每日亚军跑道 */
	public static final String LOVE_ANCHOR_SECOND = "love_anchor_second_";
	/** 为爱奔跑活动，每日季军跑道 */
	public static final String LOVE_ANCHOR_THIRD = "love_anchor_third_";
	/** 为爱奔跑活动，每日上跑道消息 */
	public static final String LOVE_ANCHOR_RUNWAY_CACHE = "love_anchor_runway_";
	/** 为爱奔跑活动，用户送出活动礼物总数*/
	public static final String LOVE_USER_SEND_ACGIFT_ALLNUM = "love_user_total_";
	/** 为爱奔跑活动，用户每天在房间内送礼数*/
	public static final String LOVE_USER_SEND_ACGIFT_ROOM_DAYNUM = "love_user_room_day_";
	/** 为爱奔跑活动，房间内所有用户每天送礼数*/
	public static final String LOVE_ROOM_ALLUSER_SEND_ACGIFT_DAYNUM = "love_room_alluser_day_";
	/** 用户获得勋章缓存 */
	public static final String LOVE_USER_DECORATE_CACHE = "love_user_decorate_";
	
	/** 用户是否首次进入房间   */
	public final static String USER_IF_FIRST_INTO_ROOM_ = "s_user_if_first_into_room_";
	
	/** 用户是否每日首次进入房间   */
	public final static String USER_IF_DAILY_FIRST_INTO_ROOM_ = "s_user_if_daily_first_into_room_";
	
	/** 免费礼物数量   */
	public final static String USER_FREE_GIFT_NUM_ = "s_user_free_gift_num_";
	
	/** 免费礼物数量(anchorSum)   */
	public final static String USER_FREE_GIFT_ANCHOR_NUM_ = "s_user_free_gift_anchorNum_";
	
	/** 系统帮未注册用户自动生成一个预登录帐号, 记录某ip对应的数量*/
	public static final String SYSTEM_AUTO_REGIST_LIMIT_USERNUM = "s_system_auto_regist_limit_usernum_";
	
	/** 每日执行禁言次数 */
	public static final String DAILY_FORBITSPEAKTIMES_CACHE = "s_daily_forbitspeaktimes_cache_";
	
	/** 四圣兽守护在一个有效期内可以设置管理员 */
	public static final String WEEKLY_SET_ROOM_MGR_CACHE = "s_weekly_set_room_mgr_cache_";
	
	/** 201703奇幻赤道之旅活动-用户整站金币送出情况 */ 
	public static final String ACTIVITIES_201703QHCDZL_SEND_WHOLESITE_GOLD_INFO_CACHE = "activities_201703qhcdzl_send_wholesite_gold_info";
	
	/** 201703奇幻赤道之旅活动-主播金币收入情况 */ 
	public static final String ACTIVITIES_201703QHCDZL_RECEIVE_GOLD_INFO_CACHE = "activities_201703qhcdzl_receive_gold_info";
	
	/** 201703奇幻赤道之旅活动-每个房间内用户金币消费情况 */
	public static final String ACTIVITIES_201703QHCDZL_EACHROOM_GOLD_CHANGE_INFO_CACHE = "activities_201703qhcdzl_eachroom_gold_change_info";
	
	/** 201703奇幻赤道之旅活动-主播启动颁奖次数 */
	public static final String ACTIVITIES_201703QHCDZL_ANCHORPRESENTATION_CACHE = "activities_201703qhcdzl_anchorPresentation_";
	
	/** 201703奇幻赤道之旅活动-记录系统已下发完成任务的奖励情况(用户) */
	public static final String ACTIVITIES_201703QHCDZL_SYSPRIZE_FINISHTASK_USER_CACHE = "activities_201703qhcdzl_sysprize_finishtask_user_";

	/** 201703奇幻赤道之旅活动-记录系统已下发完成任务的奖励情况(主播) */
	public static final String ACTIVITIES_201703QHCDZL_SYSPRIZE_FINISHTASK_ANCHOR_CACHE = "activities_201703qhcdzl_sysprize_finishtask_anchor_";
	
	/** 201703奇幻赤道之旅活动-排行榜数据缓存 */
	public static final String ACTIVITIES_201703QHCDZL_RANK_LIST_CACHE = "activities_201703qhcdzl_rank_list_";

	/** 用户关注用户缓存 */
	public static final String USER_ATTENTION_ALL_CACHE = "user_attention_all_";
	/** 用户粉丝缓存 */
	public static final String USER_FANS_ALL_CACHE = "user_fans_all_";
	
	/** 普通用户摘蜜桃缓存 */
	public static final String GENERAL_USER_PEACH_CAHCE = "general_pluck_";
	
	/** 房间守护摘蜜桃缓存 */
	public static final String GUARD_USER_PEACH_CAHCE = "guard_pluck_";
	
	/** 房间守护摘蜜桃缓存 */
	public static final String USER_PLUCK_PEACH_DAILY_CAHCE = "pluck_peach_";
	
	/** 房间内的主播缓存 */
	public static final String ROOM_ANCHOR_CACHE = "room_anchor_";
	
	/** 201704活动房间收到所有活动礼物总数缓存,格式：key + anchorId  */
	public static final String ACTIVITY_FOR_201704_ROOM_ALL_CACHE = "saveanchor_room_all_";
	
	/** 201704活动房间收到智者礼物总数缓存,格式：key + anchorId  */
	public static final String ACTIVITY_FOR_201704_ROOM_ZZ_CACHE = "room_zz_";
	
	/** 201704活动房间收到智者礼物总数缓存,格式：key + anchorId  */
	public static final String ACTIVITY_FOR_201704_ROOM_YZ_CACHE = "room_yz_";
	
	/** 201704活动用户智者礼物缓存,格式：key + userId  */
	public static final String ACTIVITY_FOR_201704ZZ_USER_CACHE = "saveanchor_zz_";
	
	/** 201704活动用户勇者礼物缓存,格式：key + userId  */
	public static final String ACTIVITY_FOR_201704YZ_USER_CACHE = "saveanchor_yz_";
	
	/** 201704活动用户所有活动礼物缓存,格式：key + userId  */
	public static final String ACTIVITY_FOR_201704TOTAL_USER_CACHE = "saveanchor_all_user_";
	
	/** 201704拯救主播活动，发放智者勋章缓存 */
	public static final String ACTIVITY_FOR_201704_USER_ZZDECORATE_CACHE = "saveanchor_zzdec_";
	
	/** 201704拯救主播活动，发放智者勋章缓存 */
	public static final String ACTIVITY_FOR_201704_USER_YZDECORATE_CACHE = "saveanchor_yzdec_";
	
	/**手机注册验证码缓存 */
	public static final String REGISTER_MOBILE_CACHE = "register_mobile_";
	
	/** 注册的ip缓存 */
	public static final String REGISTER_IP_CACHE = "register_ip_";
	
	/** 201704拯救主播活动，主播勋章缓存 */
	public static final String ACTIVITY_FOR_201704_ANCHOR_DECORATE_CACHE = "saveanchor_anchor_";
	
	/** 201704拯救主播活动，主播开启奖励缓存 */
	public static final String ACTIVITY_FOR_201704_ANCHOR_OPENCHANCE_CACHE = "saveanchor_open_";
	
	/** 首页数据缓存，二次缓存，用于在缓存失效时，返回给客户端使用 */
	public static final String ANCHOR_C1_OLD_DATA_CACHE = "c1_olddata_";
	
	/** 首页数据缓存，二次缓存，用于在缓存失效时，返回给客户端使用 (旧版本)*/
	public static final String ANCHOR_C1_OLD_ONE_DATA_CACHE = "c1_olddata_oldversion_";
	
	/** 201704拯救主播活动，主播开启发奖时，奖励等级缓存 */
	public static final String ACTIVITY_FOR_201704_ANCHOR_OPENLEVEL_CACHE = "saveanchor_open_level_";
	
	/** 201704拯救主播活动，全站送智者礼物用户缓存 */
	public static final String ACTIVITY_FOR_201704_ALL_SENDZZGIFT_CACHE = "saveanchor_all_zz";
	
	/** 201704拯救主播活动，全站送勇者礼物用户缓存 */
	public static final String ACTIVITY_FOR_201704_ALL_SENDYZGIFT_CACHE = "saveanchor_all_yz";
	
	/** 201704拯救主播活动，全站主播收到礼物缓存 */
	public static final String ACTIVITY_FOR_201704_ALL_ANCHOR_CACHE = "saveanchor_allstation";
	
	/** 201704拯救主播活动，房间内所有用户送礼缓存，不区分礼物类别 ，缓存map*/
	public static final String ACTIVITY_FOR_201704_ALL_ROOM_CACHE = "saveanchor_allroom_";
	
	/** 201704拯救主播活动，房间内所有用户送智者礼缓存，，缓存map*/
	public static final String ACTIVITY_FOR_201704_ZZALL_ROOM_CACHE = "zz_allroom_";
	
	/** 201704拯救主播活动，房间内所有用户送勇者礼缓存 ，缓存map*/
	public static final String ACTIVITY_FOR_201704_YZALL_ROOM_CACHE = "yz_allroom_";
	
	/** 周年庆活动，主播房间收礼总金币 */
	public static final String ACTIVITY_FOR_ANNIVERSARY_ANCHOR_ALLGOLDS_CACHE = "anniversary_anchor_";
	/** 周年庆活动，全站主播活动期收礼缓存，map */
	public static final String ACTIVITY_FOR_ANNIVERSARY_ALLTIME_ALLSTATTION_CACHE = "anniversary_anchorstation";
	/** 周年庆活动，全站主播每日收礼缓存，map,格式： key + 日期 */
	public static final String ACTIVITY_FOR_ANNIVERSARY_DAY_ALLSTATTION_CACHE = "anniversary_allstation_daily_";
	/** 周年庆活动，活动期内用户总共送礼缓存 ，格式：key + userId + "_" + anchorId*/
	public static final String ACTIVITY_FOR_ANNIVERSARY_USER_ALLGOLDS_CACHE ="anniversary_user_";
	/** 周年庆活动，活动期内用户房间内送礼缓存 */
	public static final String ACTIVITY_FOR_ANNIVERSARY_USER_ROOM_GOLDS_CACHE = "anniversary_room_";
	/** 周年庆活动，活动期内房间内所有用户送礼缓存，map,格式：key + anchorId */
	public static final String ACTIVITY_FOR_ANNIVERSARY_USER_ROOM_ALL_CACHE = "anniversary_roommap_";
	/** 周年庆活动，活动期内房间内所有用户送礼缓存，用于房间精灵榜单显示数据，map,格式：key + anchorId */
	public static final String ACTIVITY_FOR_ANNIVERSARY_USER_ROOM_SORTALL_CACHE = "anniversary_roomsort_map_";
	/** 周年庆活动，活动期内全站所有用户送礼缓存，map */
	public static final String ACTIVITY_FOR_ANNIVERSARY_USER_STATION_CACHE = "anniversary_stationmap";
	/** 周年庆活动，每日全站所有用户送礼缓存，map,格式： key + 日期*/
	public static final String ACTIVITY_FOR_ANNIVERSARY_USER_STATION_DAILY_CACHE = "anniversary_stationmap_daily_";
	/** 周年庆活动，用户勋章缓存，格式：key + userId + "_" + 等级数量 */
	public static final String ACTIVITY_FOR_ANNIVERSARY_USER_DECORATE_CACHE = "anniversary_userdeco_";
	/** 周年庆活动，主播勋章缓存，格式：key + anchorId + "_" + 等级数量 */
	public static final String ACTIVITY_FOR_ANNIVERSARY_ANCHOR_DECORATE_CACHE = "anniversary_anchordeco_";
	/** 周年庆活动，主播开奖次数缓存，格式：key + anchorId*/
	public static final String ACTIVITY_FOR_ANNIVERSARY_ANCHOR_CHANCE_CACHE = "anniversary_chance_";
	/** 周年庆活动，主播开奖等级缓存，格式：key + anchorId*/
	public static final String ACTIVITY_FOR_ANNIVERSARY_ANCHOR_LEVEL_CACHE = "anniversary_level_";
	
	/** 送礼额外加经验配置缓存 */
	public static final String EXTRA_POINT_CACHE = "extra_point";
	/** 2017520活动缓存 */
	public static final String SAYTOYOU_2017520_CACHE = "love17520_";
	/** 2017520活动勋章缓存 */
	public static final String SAYTOYOU_2017520_DEC_CACHE = "love17520_dec_";
	
	// 主播pk相关缓存，begin
	/** 所有有效的pk配置缓存，格式：key */
	public static final String ANCHOR_PK_ALL_CONF_CACHE = "pk_all_conf";
	/** 主播pk，发起者pk实体缓存，格式：key + anchorId */
	public static final String ANCHOR_PK_CHALLENGER_CACHE = "pk_cher_";
	/** 主播pk，接受者pk实体缓存，格式：key + anchorId */
	public static final String ANCHOR_PK_BECHALLENGER_CACHE = "pk_becher_";
	/** 主播pk，发起者pk收到的礼物缓存，格式：key + anchorId */
	public static final String ANCHOR_PK_CHALLENGER_RECGOLDS_CACHE = "pk_cher_golds_";
	/** 主播pk，发起者pk收到的礼物缓存，格式：key + anchorId */
	public static final String ANCHOR_PK_BECHALLENGER_RECGOLDS_CACHE = "pk_becher_golds_";
	// 主播pk相关缓存，end
	
	// 2017-端午活动缓存-begin
	/** 端午活动，用户在房间内送古风游轮的个数 ,key+userId+"_"+anchorId*/
	public static final String DUANWU_USER_SENDGUFENG_NUM_CACHE = "duanwu_gufeng_";
	/** 端午活动，用户触发开奖次数,key+userId*/
	public static final String DUANWU_USER_OPEN_NUM_CACHE = "duanwu_opennum_";
	/** 端午活动，用户全站送礼总数，key+userId */
	public static final String DUANWU_USER_ALL_STATION_TOTAL_CACHE = "duanwu_user_allnum_";
	/** 端午活动，用户房间送礼总数，key + userId + "_" + anchorId */
	public static final String DUANWU_USER_ROOM_TOTAL_CACHE = "duanwu_user_roomnum_";
	/** 端午活动，房间所有送礼用户-map，key+anchorId */
	public static final String DUANWU_USER_ROOM_ALL_CACHE = "duanwu_roommap_";
	/** 端午活动，房间精灵排行送礼用户-map，key+anchorId */
	public static final String DUANWU_USER_ROOM_SORT_CACHE = "duanwu_room_sortmap_";
	/** 端午活动，用户获取勋章后缓存，key+userId + "_" + totalNum */
	public static final String DUANWU_USER_DECORAGE_CACHE = "duanwu_userdec_";
	/** 端午活动，主播收礼总数，key + anchorId */
	public static final String DUANWU_ANCHOR_RECEIVES_TOTAL_CACHE = "duwanwu_anchor_";
	/** 端午活动，全站所有主播收礼，map */
	public static final String DUANWU_ANCHOR_ALL_STATION_CACHE = "duanwu_anchor_allstation";
	/** 主播勋章缓存，key + anchorId+ "_" + total(收礼等级，如99999) */
	public static final String DUANWU_ANCHOR_DECORATE_CACHE = "duanwu_anchordec_";
	// 2017-端午活动缓存-end
	
	/**黑名单*/
	public static final String USER_BLACK = "userblack";
	/**成长树缓存*/
	public static final String USER_GROWTH = "service_growth";

	/***当前靓号等级缓存*/
	public static final String CUR_GOOD_CODE_LEVEL = "service_cur_goodcode_level";
	
	// 6.1活动 begin
	public static final String CHILDRENDAY_USER_CHCHE = "childeday_user_";
	public static final String CHILDRENDAY_ANCHOR_CHCHE = "childeday_anchor_";
	// 6.1 活动end

	/** 抽奖奖品缓存 */
	public static final String LOTTERY_DRAW_TOTAL_COUNT_CACHE = "lottery_prize";
	
	/** 灰姑娘变身记积分抽奖奖品缓存 */
	public static final String GRAYGIRL_LOTTERY_DRAW_TOTAL_COUNT_CACHE = "graygirl_lottery_prize";
	
	/** 2017七夕活动积分抽奖奖品缓存 */
	public static final String MAGPIE_FESTIVAL_DRAW_TOTAL_COUNT_CACHE = "mf_lottery_prize";

	/** 靓号抽奖机会 :key + userId*/
	public static final String LOTTERY_GOODCODE_OPTINITY = "lottery_gc_";
	
	// 粉红女郎活动-begin
	/** 用户送出总金币:key + userId */
	public static final String PINK_USER_TOTAL = "pink_u_all_";
	/** 用户在房间内送出金币数：key + userId+ "_" + anchorId */
	public static final String PINK_USER_ROOM_TOTAL = "pink_u_r_";
	/** 用户勋章缓存:key + userId */
	public static final String PINK_USER_DECORATE = "pink_u_dec_";
	/** 房间内送礼用户集缓存,map: key+anchorId */
	public static final String PINK_ROOM_USER_MAP = "pink_r_map_";
	/** 全站用户送礼集合-map:key */
	public static final String PINK_USER_ALL_STATION = "pink_u_station";
	/** 主播收礼金币总数：key + anchorId */
	public static final String PINK_ANCHOR_TOTAL = "pink_a_all_";
	/** 主播勋章缓存:key+anchorId */
	public static final String PINK_ANCHOR_DECORATE = "pink_a_dec_";
	/** 全站主播收礼金币集,map：key */
	public static final String PINK_ALL_ANCHOR_TOTAL = "pink_a_station";
	// 粉红女郎end！
	
	/**幸运四叶草勋章*/
	public static final String  LUCKY_CLOVER_MEDAL = "lucky_clover_medal_";
	/**幸运礼物触发特殊动画*/
	public static final String  LUCKY_GIFT_SPECIAL_ANIMATION = "lucky_gift_special_animation_";
	/**幸运礼物勋章*/
	public static final String  LUCKY_GIFT_DECORATE = "lucky_gift_decorate_";

	
	/**万众瞩目勋章*/
	public static final String DECORATE_EYES_FOR_YOU = "decorate_eyes_for_you_";
	/**魅力四射勋章*/
	public static final String DECORATE_GLAMOROUS = "decorate_glamorous_";
	
	//泳池派对 start
	/**泳池派对全站主播收礼总数-map*/
	public static final String SWIMMING_PARTY_ANCHOR_TOTAL = "swimming_party_anchor_total";
	/**泳池派对全站用户收礼总数-map*/
	public static final String SWIMMING_PARTY_USER_TOTAL = "swimming_party_user_total_";
	/**泳池派对主播房间用户收礼总数：key + userId */
	public static final String SWIMMING_PARTY_USER_ROOM_TOTAL = "swimming_party_user_room_total_";
	/**泳池派对主播房间主播收礼总数：key + anchorId */
	public static final String SWIMMING_PARTY_ANCHOR_ROOM_TOTAL = "swimming_party_anchor_room_total_";
	/**泳池派对主播房间6种派对礼物收礼总数map：key + map */
	public static final String SWIMMING_PARTY_ANCHOR_ROOM_TOTAL_MAP = "swimming_party_anchor_room_total_map_";
	/**泳池派对房间内送礼用户集缓存,map: key+anchorId */
	public static final String SWIMMING_PARTY_USER_ROOM_MAP = "swimming_party_user_room_map_";
	/**泳池派对主播勋章缓存:key+anchorId */
	public static final String SWIMMING_PARTY_ANCHOR_DECORATE = "swimming_party_anchor_decorate_";
	/**泳池派对用户勋章缓存:key+userId */
	public static final String SWIMMING_PARTY_USER_DECORATE = "swimming_party_user_decorate_";
	/**泳池派对全站6种派对礼物主播收礼总数－榜单*/
	public static final String SWIMMING_PARTY_ANCHOR_GIFT_TOTAL = "swimming_party_anchor_gift_total_";
	/**泳池派对全站6种派对礼物主主播收礼总数－房间精灵*/
	public static final String SWIMMING_PARTY_ANCHOR_GIFT_ROOM_TOTAL = "swimming_party_anchor_gift_room_total_";
	/**泳池派对全站6种派对礼物主播房间用户收礼总数－榜单*/
	public static final String SWIMMING_PARTY_USER_GIFT_ROOM_TOTAL = "swimming_party_user_gift_room_total_";

	
	/** 泳池派对缓存，key+ sendUserId + "_" + anchorId */
	public static final String SWIMMING_PARTY_GIFT_CACHE = "party_gift_";
	/** 泳池派对活动，用户触发开奖次数,key+userId*/
	public static final String SWIMMING_PARTY_USER_OPEN_NUM_CACHE = "party_opennum_";
	//泳池派对 end

	/** 可冠名礼物的主播集合缓存，map，key */
	public static final String RENAME_GIFT_ALL_CHACHE = "rename_all";
	/** 可冠名某个礼物的缓存，key+giftId */
	public static final String RENAME_GIFT_CHACHE = "rename_id_";
	/** 礼物名称缓存 */
	public static final String GIFT_SETNAME_CACHE = "giftname_";
	
	//灰姑娘变身记 start
	/**全站用户收礼金币总数-map*/
	public static final String GREY_GIRL_USER_TOTAL = "grey_girl_user_total_";
	/**主播房间用户收礼金币总数：key + userId */
	public static final String GREY_GIRL_USER_ROOM_TOTAL = "grey_girl_user_room_total_";
	/**主播房间内送礼用户集缓存,map: 房间精灵*/
	public static final String GREY_GIRL_USER_ROOM_MAP = "grey_girl_user_room_map_";
	/**主播房间内送礼用户集缓存,map: 榜单 */
	public static final String GREY_GIRL_USER_ROOM_ALL = "grey_girl_user_room_all_";
	/**用户勋章缓存:key+userId */
	public static final String GREY_GIRL_USER_DECORATE = "grey_girl_user_decorate_";
	/**主播房间主播收礼总数：key + anchorId */
	public static final String GREY_GIRL_ANCHOR_ROOM_TOTAL = "grey_girl_anchor_room_total_";
	/**全站主播收礼总数-map*/
	public static final String GREY_GIRL_ANCHOR_TOTAL = "grey_girl_anchor_total";
	/**主播勋章缓存:key+anchorId */
	public static final String GREY_GIRL_ANCHOR_DECORATE = "grey_girl_anchor_decorate_";
	/**用户可留言次数*/
	public static final String GREY_GIRL_CONTENT_COUNT = "grey_girl_content_count_";
	/**用户已留言次数*/
	public static final String GREY_GIRL_CONTENT_COUNT_HAVE = "grey_girl_content_count_have_";
	/**用户留言 key+userId*/
	public static final String GREY_GIRL_CONTENT = "grey_girl_content_";
	//灰姑娘变身记 end
	
	/** 首页主播分类换成，新秀 */
	public static final String HOME_ANCHOR_STYLE_NEWSTAR_CACHE = "style_nerstar";
	/** 首页主播分类换成，新秀一级缓存 */
	public static final String HOME_ANCHOR_STYLE_NEWSTAR_ONE_CACHE = "style_nt_one_";
	/** 首页主播分类换成，新秀二级缓存 */
	public static final String HOME_ANCHOR_STYLE_NEWSTAR_TWO_CACHE = "style_nt_two_";
	/** 首页主播分类换成，新秀主播总数缓存 */
	public static final String HOME_ANCHOR_STYLE_NEWSTAR_SUM_CACHE = "style_nt_sum";
	/** 首页主播分类换成，女神 */
	public static final String HOME_ANCHOR_STYLE_NVSHEN_CACHE = "style_nvshen";
	/** 首页主播分类换成，女神一级缓存*/
	public static final String HOME_ANCHOR_STYLE_NVSHEN_ONE_CACHE = "style_ns_one_";
	/** 首页主播分类换成，女神二级缓存 */
	public static final String HOME_ANCHOR_STYLE_NVSHEN_TWO_CACHE = "style_ns_two_";
	/** 首页主播分类换成，女神主播总数缓存 */
	public static final String HOME_ANCHOR_STYLE_NVSHEN_SUM_CACHE = "style_ns_sum";
	/** 首页主播分类换成，好声音 */
	public static final String HOME_ANCHOR_STYLE_GOODVOICE_CACHE = "style_goodvoice";
	/** 首页主播分类换成，好声音一级缓存 */
	public static final String HOME_ANCHOR_STYLE_GOODVOICE_ONE_CACHE = "style_gv_one_";
	/** 首页主播分类换成，好声音二级缓存 */
	public static final String HOME_ANCHOR_STYLE_GOODVOICE_TWO_CACHE = "style_gv_two_";
	/** 首页主播分类换成，好声音主播总数缓存 */
	public static final String HOME_ANCHOR_STYLE_GOODVOICE_SUM_CACHE = "style_gv_sum";
	
	
	/** 守护个数区间缓存 */
	public static final String GUARD_NUM_REGION_CACHE = "guard_region_";
	/** 守护榜单总缓存 */
	public static final String GUARD_RANK_CACHE = "guard_rank";
	
	/** 其他包使用的首页主播换成，key + type  */
	public static final String HOME_ANCHOR_OTHER_CACHE = "anchor_other_";
	/** 其他包使用的首页主播换成，key + type + pageNum  */
	public static final String HOME_ANCHOR_OTHER_ONE_CACHE = "other_one_";
	/** 其他包使用的首页主播换成，key + type + pageNum  */
	public static final String HOME_ANCHOR_OTHER_TWO_CACHE = "other_two_";
	/** 其他包使用的首页主播，总数换成  */
	public static final String HOME_ANCHOR_OTHER_SUM_CACHE = "other_sum";
	
	//仲夏之夜活动 start
	/**仲夏之夜用户送礼总数:key+userId*/
	public static final String MIDSUMMER_NIGHT_USER_TOTAL = "mnl_user_total_";
	/**仲夏之夜主播房间用户送礼总数：key + userId + anchorId*/
	public static final String MIDSUMMER_NIGHT_USER_ROOM_TOTAL = "mnl_user_room_total_";
	/**仲夏之夜用户在各主播房间送礼总数集缓存,map: key+userId */
	public static final String MIDSUMMER_NIGHT_USER_MAP_BY = "mnl_user_map_";
	/**仲夏之夜房间内送礼用户集缓存,map: key+anchorId */
	public static final String MIDSUMMER_NIGHT_USER_ROOM_MAP = "mnl_user_room_map_";
	/**仲夏之夜全站用户送礼总数,map*/
	public static final String MIDSUMMER_NIGHT_USER_MAP = "mnl_user_map_total";
	
	/**仲夏之夜主播房间主播收礼总数：key + anchorId */
	public static final String MIDSUMMER_NIGHT_ANCHOR_ROOM_TOTAL = "mnl_aroom_total_";
	/**仲夏之夜全站主播收礼总数-map*/
	public static final String MIDSUMMER_NIGHT_ANCHOR_TOTAL = "mnl_anchor_total";
	
	/**仲夏之夜主播勋章缓存:key+anchorId */
	public static final String MIDSUMMER_NIGHT_ANCHOR_DECORATE = "mnl_adecorate_";
	/**仲夏之夜用户勋章缓存:key+userId */
	public static final String MIDSUMMER_NIGHT_USER_DECORATE = "mnl_udecorate_";
	
	/**仲夏之夜活动，主播开奖次数缓存，格式：key + anchorId*/
	public static final String MIDSUMMER_NIGHTY_ANCHOR_CHANCE_CACHE = "mnl_chance_";
	/**仲夏之夜活动，主播开奖等级缓存，格式：key + anchorId*/
	public static final String MIDSUMMER_NIGHT_ANCHOR_LEVEL_CACHE = "mnl_level_";
	/**仲夏之夜活动，用户进场特效处理次数缓存，格式：key + userId + roomId*/
	public static final String MIDSUMMER_NIGHT_ANCHOR_NUM_CACHE = "mnl_num_";
	//仲夏之夜活动  end
	
	/**真爱粉图标*/
	public static final String LOVE_FANS = "love_fans";
	/**打卡亲密度排行榜*/
	public static final String LOVE_FANS_RANK = "love_fans_rank";
	
	/**徽章*/
	public static final String MEDAL = "md";
	/**用户佩戴徽章*/
	public static final String USER_MEDAL = "umd";
	
	/**用户徽章列表*/
	public static final String USER_MEDAL_LIST = "umdlist";
	
	/**定制徽章信息*/
	public static final String MEDAL_INFO = "mdinfo";
	
	/**定制徽章信息*/
	public static final String MEDAL_GUARD_USER = "md_g_u";
	
	
	//七夕活动 start
	/**七夕活动用户送礼总数:key+userId*/
	public static final String MAGPIE_FESTIVAL_USER_TOTAL = "mf_ut_";
	/**七夕活动主播房间用户送礼总数：key + userId + anchorId*/
	public static final String MAGPIE_FESTIVAL_USER_ROOM_TOTAL = "mf_urt_";
	/**七夕活动房间内送礼用户集缓存,map: key+anchorId */
	public static final String MAGPIE_FESTIVAL_USER_ROOM_MAP = "mf_ur_map_";
	/**七夕活动全站用户送礼总数,map*/
	public static final String MAGPIE_FESTIVAL_USER_TOTAL_MAP = "mf_ut_map";
	
	/**七夕活动主播房间主播收礼总数：key + anchorId */
	public static final String MAGPIE_FESTIVAL_ANCHOR_ROOM_TOTAL = "mf_art_";
	/**七夕活动全站主播收礼总数-map*/
	public static final String MAGPIE_FESTIVAL_ANCHOR_TOTAL_MAP = "mf_at_map";
	
	/**七夕活动用户勋章缓存:key+userId */
	public static final String MAGPIE_FESTIVAL_USER_DECORATE = "mf_ud_";
	/**七夕活动主播勋章缓存:key+anchorId */
	public static final String MAGPIE_FESTIVAL_ANCHOR_DECORATE = "mf_ad_";
	/**七夕活动绝版情侣勋章缓存:key+userId */
	public static final String MAGPIE_FESTIVAL_USER_LOVESDECORATE = "mf_uld_";
	/**七夕活动绝版情侣勋章缓存:key+anchorId */
	public static final String MAGPIE_FESTIVAL_ANCHOR_LOVESDECORATE = "mf_ald_";
	//七夕活动  end
	
	
	/** 全站头条缓存 */
	public static final String ALL_ROOM_HEADLINE_INFO_CACHE = "headline_info";
	/** 首页滚动头条缓存 */
	public static final String HOME_HEADLINE_CACHE = "headline_list";
	
	/** 宝箱奖品配置缓存：key+roomId+boxrecordId */
	public static final String GRABBOX_ROOM_PRICE_CACHE = "grab_box_";
	/** 房间宝箱缓存，key+roomId */
	public static final String GRABBOX_ROOM_BOX_CAHCE = "grab_room_box_";
	
	/**森之爱恋勋章*/
	public static final String  SEN_LOVE = "sen_love_";
	
	//0905活动 start
	/**用户送礼总数:key+userId*/
	public static final String  FESTIVAL0905_USER_TOTAL = "ft0905_ut_";
	/**房间用户送礼总数：key + userId + anchorId*/
	public static final String FESTIVAL0905_USER_ROOM_TOTAL = "ft0905_urt_";
	/**房间送礼用户缓存,map: key+anchorId */
	public static final String FESTIVAL0905_USER_ROOM_MAP = "ft0905_ur_map_";
	/**用户送礼总数,map*/
	public static final String FESTIVAL0905_USER_TOTAL_MAP = "ft0905_ut_map";
	/**用户勋章缓存:key+userId */
	public static final String FESTIVAL0905_USER_DECORATE = "ft0905_ud_";
	/**房间主播收礼总数：key + anchorId */
	public static final String FESTIVAL0905_ANCHOR_ROOM_TOTAL = "ft0905_art_";
	/**全站主播收礼总数-map*/
	public static final String FESTIVAL0905_ANCHOR_TOTAL_MAP = "ft0905_at_map";
	/**主播勋章缓存:key+anchorId */
	public static final String FESTIVAL0905_ANCHOR_DECORATE = "ft0905_ad_";
	//0905活动 start
	
}
