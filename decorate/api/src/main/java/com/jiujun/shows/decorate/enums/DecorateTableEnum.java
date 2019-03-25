package com.jiujun.shows.decorate.enums;


/** 勋章表(t_decorate)对应的字段枚举  */
public class DecorateTableEnum {

	/**
	 * 勋章id
	 *
	 */
	public static enum Id{
		/**
		 * 射手勋章
		 */
		SHESHOU(1),
		
		/**
		 * 女神经勋章
		 */
		NVSHENJING(2),
		
		/**
		 * 男神经勋章
		 */
		NANSHENJING(3),
		
		/**
		 * 歌神勋章
		 */
		GESHEN(4),
		
		/**
		 * id=9:女神勋章
		 */
		Nvshen(9),
		
		/**
		 * 用户七夕徽章(男)
		 */
		UserQixiNan(10),
		
		/**
		 * 用户七夕徽章(女)
		 */
		UserQixiNv(11),
		
		/**
		 * 主播七夕徽章(男)
		 */
		AnchorQixiNan(12),
		
		/**
		 * 主播七夕徽章(女)
		 */
		AnchorQixiNv(13),

		/**
		 * 砸蛋达人勋章
		 */
		UserZadandaren(15),
		
		/**
		 * 奥运铜牌
		 */
		UserAoyunTongpai(17),
		
		/**
		 * 奥运银牌
		 */
		UserAoyunYinpai(18),
		
		/**
		 * 奥运金牌
		 */
		UserAoyunJinpai(19),
		
		/**
		 * 用户特权勋章
		 */
		UserSpecialDer(20),
		
		/** 用户恋爱勋章 */
		UserLoveSeptemberDer(21),
		
		/** 主播恋爱勋章 */
		AnchorLoveSeptemberDer(22),
		
		/** 守护1级勋章 */
		ShouhuOneDec(23),
		/** 守护2级勋章 */
		ShouhuTwoDec(24),
		
		/** 中秋勋章 */
		ZhongQiuDec(25),
		
		/** 南瓜勋章 */
		DaoDanWang(30),
		
		/** 31:光棍勋章 */
		Decorate1111(31),
		
		/** 33:蜜桃万人迷 */
		Mitaowanrenmi(33),
		
		/** 37:蜜桃奢华女神勋章 */
		Mitaoshehuanvshen(37),
		
		/** 新人铜 */
		xinrenTong(38),
		/** 新人银 */
		xinrenYin(39),
		/** 新人金 */
		xinrenJin(40),
		
		/** 圣诞节勋章 */
		Christmas(47),
		
		/** 圣诞用户摘礼物勋章 */
		ChristmasUser(48),
		
		/** 围巾男神 */
		Weijin_nanshen(66),
		
		/** 围巾女神 */
		Weijin_nvshen(67),
		
		/** 秋裤男神 */
		Qiuku_nanshen(68),
		
		/** 秋裤女神 */
		Qiuku_nvshen(69),
		
		/** 火 */
		Huo(70),
		
		/** 炎 */
		Yan_2(71),
		
		/** 焱 */
		Yan_3(72),
		
		/** 运气王七彩 */
		YunqiWang1(73),
		/** 运气王钻石 */
		YunqiWang2(74),
		/** 运气王金 */
		YunqiWang3(75),
		
		/** 猎手勋章  */
		Lieshou(79),
		/** 觅心猎手勋章  */
		Mixinlieshou(80),
		/** 宝贝勋章  */
		Baobei(81),
		/** 甜心宝贝勋章  */
		Tianxinbaobei(82),
		/** 园丁勋章 */
		Yuanding(84),
		/** 桃花勋章 */
		Taohua(85),
		
		/** 护花使者勋章 */
		Huhuashizhe(86),
		/** 银蛋达人勋章  */
		Yindandaren(87),
		
		/** 金蛋达人勋章  */
		Jindandaren(88),
		
		/** 冠军勋章（为爱奔跑活动） */
		LoveForBP1(89),
		/** 冠军勋章（为爱奔跑活动） */
		LoveForBP2(90),
		/** 冠军勋章（为爱奔跑活动） */
		LoveForBP3(91),
		/** 助跑达人勋章 */
		LovuForUser(92),
		
		/** 环球勋章(用户)  */
		Huanqiu(94),
		
		/** 奇幻赤道之旅勋章(主播)  */
		Qihuanchidaozhilv(95),
		/** 睿智王子勋章 */
		Rzwangzi(96),
		/**勇武王子勋章 */
		Ywwangzi(97),
		/**公主勋章 */
		Princess(98),
		/**自由女神勋章 */
		FreeNs(99),
		/** 感恩同行-主播封面勋章 */
		ZNTongxing(101),
		/** 周年纪念 */
		Znjn(102),
		/** 周年狂欢 */
		Znkh(103),
		/**蒲剑 */
		Pujian(104),
		/** 沐兰 */
		Mulan(105),
		/** 仲夏女神 */
		Zxns(106),
		/** 角黍 */
		Jiaoshu(107),
		/** 爱的勋章 */
		Love520(108),
		/** 慧眼勋章-用户 */
		Huiyan(109),
		/** 粉红女郎 */
		PinkLady(110),
		/** 婴儿勋章 */
		childrenDayAnchor(111),
		/** 宠爱勋章 */
		ChildrenDayUser(112),
		/** 幸运四叶草勋章 */
		Luckyclover(113),
		/** 爆铜 */
		LuckyCopper(114),
		/** 爆银 */
		LuckySilver(115),
		/** 爆金 */
		LuckyGold(116),
		
		/** 万众瞩目 */
		EyesForYou(118),
		/** 派对女王*/
		PartyQueen(119),
		/** 魅力四射*/
		Glamorous(120),
		/** 派对男神*/
		PartyMan(121),
		
		/** 点金手铜勋章 */
		MidasCopper(122),
		/** 点金手银勋章*/
		MidasSilver(123),
		/** 点金手金勋章 */
		MidasGold(124),
		/** 守护者勋章*/
		Guardian(125),
		/** 白马王子勋章*/
		PrinceCharming(126),
		/** 灰姑娘勋章*/
		GreyGirl(127),
		/** 白雪公主勋章*/
		SnowWhite(128),
		/** 小活力勋章*/
		LittleEnergy(129),
		/** 碧海细沙勋章*/
		BlueSeaSand(130),
		/** 小清新勋章*/
		Partysu(131),
		/** 皓月星光勋章*/
		BrightMoonStar(132),
		
		/** 七夕牛郎勋章*/
		QiXiNiuLang(133),
		/** 盖世英雄勋章*/
		GaiShiYingXiong(134),
		/** 七夕织女勋章*/
		QiXiZhiNv(135),
		/** 九天仙女勋章*/
		JiuTianXianNv(136),
		/** 森之爱恋勋章勋章*/
		SenLove(138),
		/** 森之爱恋首页勋章*/
		SenLoveIndex(139),
		/** 统帅一方勋章*/
		TONGSYF(140),
		/** 雄霸天下勋章*/
		XIONGBTX(141),
		/** 一笑倾城勋章*/
		YIXIAOQC(142),
		/** 国色天香勋章*/
		GUOSTX(143),
		/** 呼朋唤友勋章*/
		HUPENGHY(144),
		/** 吴刚伐桂勋章*/
		WUGANGFG(145),
		/** 寒宫殿主勋章*/
		HANGDZ(146),
		/** 玉兔捣药勋章*/
		YUTUDY(147),
		/** 广寒仙子勋章*/
		GUANGHXZ(148),
		/** 用户长情勋章*/
		CHANGQUSER(149),
		/** 主播长情勋章*/
		CHANGQANCHOR(150);
		
		
		
		
		private final int value;
		
		Id(int value) {
			this.value = value;
		}

		
		public int getValue() {
			return value;
		}
	}
	
	/**
	 * category
	 * 类型,0:主播勋章(所有主播通用),1:普通用户勋章(所有用户通用),2:主播勋章(针对单个主播)';
	 */
	public static enum Category{
		/**
		 * 0:主播勋章(所有主播通用)
		 */
		ANCHOR(0),
		
		/**
		 * 1:普通用户勋章(所有用户通用)
		 */
		USER(1),
		
		/**
		 * 2:主播勋章(针对单个主播)
		 */
		ANCHOR_SINGLE(2);
		
		private final int value;
		
		Category(int value) {
			this.value = value;
		}

		
		public int getValue() {
			return value;
		}
	}
	
	public static enum Type{
		//0:主播,1:普通用户
		/**
		 * 普通类型勋章
		 */
		CommonUser(0),
		
		/**
		 * 守护类型勋章
		 */
		GuardUser(1),
		
		/**
		 * 首页主播勋章
		 */
		HomeDecorate(2);
		
		private final int value;
		
		Type(int value) {
			this.value = value;
		}

		
		public int getValue() {
			return value;
		}
	}
}
