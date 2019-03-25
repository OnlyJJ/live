package com.lm.live.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public final class Helper {

	public static final Logger log = Logger.getLogger(Helper.class);

	/** 禁止实例化 **/
	private Helper() {
	}

	public static boolean verifyUUID(String uuid) {
		boolean flag = false;
		if (Helper.isNullOrEmpty(uuid))
			return flag;
		uuid = Helper.getCleanString(uuid);
		if (uuid.matches("\\d+"))
			flag = uuid.matches("^[0-9]{15}$");
		else
			flag = true;
		return flag;
	}

	public static boolean verifyMobile(String mobile) {
		return !isNullOrEmpty(mobile) && (mobile).matches("^1[0-9]{10}");
	}

	public static boolean verifyEmail(String email) {
		return !isNullOrEmpty(email) && (email).matches(".*?@.*?");
	}

	/**
	 * 检查字符串是否为null或者为空串<br>
	 * 为空串或者为null返回true，否则返回false
	 */
	public static boolean isNullOrEmpty(String str) {
		boolean result = true;
		str = getCleanString(str);
		if (str != null && !"".equals(str.trim())) {
			result = false;
		}
		return result;
	}

	/** 转换为float数据类型的方法 **/
	public static float _Float(Object str) {
		float f = 0.0f;
		try {
			f = Float.parseFloat(str.toString());
			return f;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return 0;
		}
	}

	/** 转换为int数据类型的方法 **/
	public static int _Integer(Object str) {
		int i = 0;
		try {
			i = Integer.parseInt(str.toString());
			return i;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return 0;
		}
	}

	/**
	 * <h1>获取时间到日期</h1> 说明：如果参数为null，则返回当天日期
	 * 
	 * @param date
	 * @return 格式：2012-01-05
	 */
	public static String getDateByDay(Date date) {
		if (date == null)
			date = new Date();
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	/**
	 * 字符串日期转换
	 * 
	 * @param dateStr
	 *            要转换的日期
	 * @param formatFrom
	 *            转前格式
	 * @param formatTo
	 *            转后格式
	 * @return
	 */
	public static String dateStrConvert(String dateStr, String formatFrom, String formatTo) {
		if (Helper.isNullOrEmpty(dateStr))
			return "";
		DateFormat sdf = new SimpleDateFormat(formatFrom);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return new SimpleDateFormat(formatTo).format(date);
	}

	/**
	 * 获取当前时间
	 * 
	 * @param type
	 *            格式,例如：yyyyMMddHHmmss，不传参数默认为：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDateTimeStr(String... type) {
		String ss = "yyyy-MM-dd HH:mm:ss";
		if (type.length > 0)
			ss = type[0];
		return new SimpleDateFormat(ss).format(new Date());
	}

	public static void main(String[] args) throws Exception {
//		Document doc = DocumentHelper.parseText(Helper.fileRead("D:\\workspace10-3\\common-service\\src\\AndroidManifest.xml","UTF-8"));
//		List<Attribute> l = doc.selectNodes("/manifest/@package");//@xx_game_id
//		for(Attribute attr:l)
//			attr.setValue("gggrrr");
//			System.out.println(attr.getValue());
//		l.get(0).setValue("gggddd");
//			System.out.println(attr.getName());
//		Attribute attr = ele.attribute("package");
//		attr.setValue("ssssssssss");
//		System.out.println(doc.asXML());
		int i=0;
		String[] packagesName = Helper.V("com.kkkd.tel").split(",");
		System.out.println(packagesName[i].lastIndexOf("."));
		System.out.println(packagesName[i].length()-1);
		String suffix = packagesName[i].substring(packagesName[i].lastIndexOf(".")+1, packagesName[i].length());
		System.out.println(suffix);
	}
	
	/**
	 * 字符串转换到日期时间格式
	 * 
	 * @param dateStr
	 *            需要转换的字符串
	 * @param formatStr
	 *            需要格式的目标字符串 举例 yyyy-MM-dd
	 * @return Date 返回转换后的时间
	 * @throws ParseException
	 *             转换异常
	 */
	public static Date strToDate(String dateStr, String formatStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return date;
	}

	/**
	 * 比较两个时间的相差值（d1与d2）
	 * 
	 * @param d1
	 *            时间一
	 * @param d2
	 * @param type
	 *            类型【d/h/m/s】
	 * @return d1 - d2
	 */
	public static long compareDate(Date d1, Date d2, char type) {
		long num = d1.getTime() - d2.getTime();
		num /= 1000;
		if ('m' == type) {
			num /= 60;
		} else if ('h' == type) {
			num /= 3600;
		} else if ('d' == type) {
			num /= 3600;
			num /= 24;
		}
		return num;
	}

	/**
	 * 根据参照时间获取相差天、小时数的新时间
	 * 
	 * @param date
	 *            参照时间
	 * @param type
	 *            天或小时[d/h]
	 * @param num
	 *            差值，例如：2表示之后2天或小时，-2表示之前2天或小时
	 * @return
	 */
	public static Date getNextDay(Date date, char type, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (type) {
		case 'd':
			calendar.add(Calendar.DAY_OF_MONTH, num);
			break;
		case 'h':
			calendar.add(Calendar.HOUR_OF_DAY, num);
			break;
		default:
			break;
		}
		date = calendar.getTime();
		return date;
	}

	/** 获取当前时间戳 **/
	public static String getTimeStamp() {
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		return timeStamp;
	}

	/** 获得本周第一天时间(yyyy-MM-dd) **/
	public static String getMonOfWeek() {
		// Calendar c = new GregorianCalendar();
		// c.setFirstDayOfWeek(Calendar.MONDAY);
		// c.setTime(new Date());
		// c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, c.getActualMinimum(Calendar.DAY_OF_WEEK));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(c.getTime());
		return str;
	}

	/** 获得本周的最后一天(yyyy-MM-dd) **/
	public static String getSunOfWeek() {
		// Calendar c = new GregorianCalendar();
		// c.setFirstDayOfWeek(Calendar.MONDAY);
		// c.setTime(new Date());
		// c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, c.getActualMaximum(Calendar.DAY_OF_WEEK));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(c.getTime());
		return str;
	}

	/** 获得当月的第一天(yyyy-MM-dd) **/
	public static String getFirstDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(cal.getTime());
		return str;
	}

	/** 获得当月的最后一天(yyyy-MM-dd) **/
	public static String getLastDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdfs.format(cal.getTime());
		return str;
	}

	/**
	 * 格式化时间，时间转字符串
	 * 
	 * @param date
	 *            null则为当前系统时间
	 * @param format
	 *            格式，null则默认为：'yyyy-MM-dd HH:mm:ss'
	 * @return 字符串格式的日期
	 */
	public static String getDateTimeByStr(Date date, String format) {
		if (date == null)
			date = new Date();
		if (format == null)
			format = "yyyy-MM-dd HH:mm:ss";
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 格式化时间，字符串转时间
	 * 
	 * @param dataStr
	 *            需要转换的字符串
	 * @param format
	 *            格式，null则默认为：'yyyy-MM-dd HH:mm:ss'
	 * @return 转换的Date
	 */
	public static Date getStrByDataTime(String dataStr, String format) {
		if (dataStr == null)
			return new Date();
		if (format == null)
			format = "yyyy-MM-dd HH:mm:ss";
		DateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(dataStr);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return date;
	}

	/**
	 * 限制字符串的最长长度，多余的截取
	 * 
	 * @param str
	 *            源字符串
	 * @param maxLen
	 *            最长长度
	 * @return
	 */
	public static String subStrmaxStr(String str, int maxLen) {
		if (!Helper.isNullOrEmpty(str)) {
			try {
				maxLen = (maxLen < 0) ? str.length() : maxLen;
				str = str.substring(0, str.length() >= maxLen ? maxLen : str.length());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				str = "";
			}
		} else {
			str = "";
		}
		return str;
	}

	/** 检查是否是数字 **/
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/** 保证将传入参数处理到一定是两位 **/
	public static String formatToTwoLength(Object o) {
		String str = "";
		try {
			str = String.valueOf(o);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if (str.length() > 2)
			return str.substring(0, 2);
		if (str.length() < 2)
			return "0" + str;
		return str;
	}

	/**
	 * 检查字符串是否为null或者为"null"<br>
	 * 为null或者为"null",返回""，否则返回字符串 <br>
	 * 用于处理数据库查询数据
	 */
	public static String getCleanString(Object obj) {
		if (obj == null) {
			return "";
		} else if (String.valueOf(obj).equals("null")) {
			return "";
		} else {
			return String.valueOf(obj);
		}
	}

	/**
	 * 检查int数据是否为null<br>
	 * 为null,返回0，否则返回原值 <br>
	 * 
	 * @param obj
	 * @return
	 */
	public static int getCleanInteger(Object obj) {
		if (obj != null) {
			return _Integer(obj);
		} else {
			return 0;
		}
	}

	/**
	 * 检查float数据是否为null<br>
	 * 为null,返回0f，否则返回原值 <br>
	 * 
	 * @param obj
	 * @param defaultValue
	 *            默认值，可以传入可以不传。传入，为null时返回本值
	 * @return
	 */
	public static float getCleanFloat(Object obj, float... defaultValue) {
		if (obj != null) {
			return _Float(obj);
		} else {
			if (defaultValue.length != 1) {
				return 0f;
			} else {
				return defaultValue[0];
			}
		}
	}

	/**
	 * 从number数值中随机抽取count个数字
	 * 
	 * @param count
	 *            要抽取的数值个数
	 * @param number
	 *            数字范围为 (0, number]
	 * @return
	 */
	public static int[] randomNumber(int count, int number) {
		int k = count, n = number;
		int[] numbers = new int[n];
		for (int i = 0; i < numbers.length; i++)
			numbers[i] = i + 1;
		int[] result = new int[k];
		for (int i = 0; i < result.length; i++) {
			int r = (int) (Math.random() * n);
			result[i] = numbers[r];
			numbers[r] = numbers[n - 1];
			n--;
		}

		// Arrays.sort(result);
		// for (int r : result)
		// System.out.println(r);
		return result;

	}

	/**
	* 取得一个随机数
	* @param minValue 下限
	* @param maxValue 上限
	* @return 下限<=随机数<上限
	*/
	public static int getOneRandom(int minValue,int maxValue)
	{
		int returnValue=minValue;
		new java.util.Random();
		returnValue=(int)((maxValue-minValue)*Math.random()+minValue);
		return returnValue;
	}
	   
	/**
	 * 返回随机字符串
	 * 
	 * @param toks
	 *            要随机操作的字符串
	 * @param len
	 *            长度
	 * @return
	 */
	public static String genRandoomString(String toks, int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++)
			sb.append(toks.charAt((int) (Math.random() * toks.length())));
		return sb.toString();
	}

	/**
	 * 高精度乘法运算
	 * 
	 * @param num
	 *            乘数
	 * @param num2
	 *            因子
	 * @return
	 */
	public static String getPrecision(String num, String num2) {
		return new BigDecimal(num).multiply(new BigDecimal(num2)).toString();
	}

	/**
	 * 截取小数后面的0
	 * 
	 * @param str
	 * @return
	 */
	public static String substringFloat(String str) {
		String ss = str;
		if (str.indexOf('.') > -1)
			ss = str.substring(0, str.indexOf('.'));
		if (Double.parseDouble(ss) == Double.parseDouble(str)) {
			return ss;
		}
		return str;
	}

	public static String paddingToFixedString(String src, char c, int fixLen, boolean isHead) {
		String s = src;
		if (s.length() > fixLen) {
			int begin = s.length() - fixLen;
			s = s.substring(begin);
			return s;
		} else if (s.length() == fixLen) {
			return src;
		} else {
			return fill(src, c, fixLen - src.length(), isHead);
		}
	}

	public static String fill(String src, char c, int num, boolean ishead) {

		StringBuilder sb = new StringBuilder(src);
		char[] cs = new char[num];
		Arrays.fill(cs, c);
		if (ishead)
			sb.insert(0, cs);
		else {
			sb.append(cs);
		}
		return sb.toString();
	}

	/**
	 * 判断版本号
	 * 
	 * @param version
	 *            v2.x.x
	 * @param ver
	 *            203
	 * @return version>ver 返回ture；否则返回false
	 */
	public static boolean judgeVersion(String version, int ver) {
		String[] arr = version.split("\\.");
		int vers = getCleanInteger(arrayTurnStr(arr, ""));
		if (vers > ver) {
			return true;
		}
		return false;
	}

	/** 数组转字符串 **/
	private static String arrayTurnStr(Object[] array, String split) {
		if (null == array)
			return "";
		StringBuilder ss = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			if (i > 0)
				ss.append(getCleanString(split));
			ss.append(getCleanString(array[i]));
		}
		return ss.toString();
	}

	public static String getFutureDay(int i, String format) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, i);
		if (format == null)
			format = "yyyyMMdd";
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(cal.getTime());
		return dateString;
	}

	public static String getFutureMonth(int i, String format) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, i);
		if (format == null)
			format = "yyyyMM";
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(cal.getTime());
		return dateString;
	}

	public static String getTel(String telInfo) {
		String ss = Helper.getCleanString(telInfo);
		StringBuffer tel = new StringBuffer();

		try {
			char[] cc = ss.toCharArray();
			for (char c : cc) {
				if (Character.isDigit(c)) {
					tel.append(c);
				}
			}
		} catch (Exception e) {
			try {
				Pattern p = Pattern.compile("\\d+");
				Matcher m = p.matcher(ss);
				while (m.find()) {
					tel.append(m.group());
				}
			} catch (Exception e1) {
				tel.setLength(0);
			}
		}

		return tel.toString();
	}

	/**
	 * 
	 * @param str1
	 * @param str2
	 *            空 则跟当前时间比较
	 * @return str1 < str2 <0 str1 > str2 >0
	 */
	public static int compareDate(String str1, String str2) {
		Date d1 = getStrByDataTime(str1, null);
		Date d2 = getStrByDataTime(str2, null);
		return d1.compareTo(d2);
	}

	public static void E(String msg) throws Exception {
		if (true)
			throw new Exception(msg);
	}

	public static void RE(String msg) {
		if (true)
			throw new RuntimeException(msg);
	}

	public static void addHashMapCount(HashMap<String, Integer> map, String name) {
		Integer value = map.get(name);
		if (value == null)
			value = new Integer(0);
		value = new Integer(value.intValue() + 1);
		map.put(name, value);
	}

	public static final String getCurrentThreadStackTrace() {
		StackTraceElement[] stes = Thread.currentThread().getStackTrace();

		StringBuffer sb = new StringBuffer("stack trace begin ===\r\n");
		for (int i = 0; i < stes.length; i++) {
			StackTraceElement ste = stes[i];
			sb.append("+" + ste.getClassName() + "," + ste.getMethodName() + ":" + ste.getFileName() + "[" + ste.getLineNumber() + "]\r\n");
		}
		sb.append("=====================\r\n");
		return sb.toString();
	}

	public static String V(String s) {
		if (s == null || "null".equals(s))
			return "";
		return s;
	}

	public static String randStringArray(String[] str, int[] factor) {
		return str[randIntArray(factor)];
	}

	public static int randIntArray(int[] factor) {
		int total = 0;
		int i = 0;
		for (i = 0; i < factor.length; i++)
			total += factor[i];
		int value = randSafeInt(total);
		int cp = 0;
		for (i = factor.length - 1; i >= 0; i--) {
			cp += factor[i];
			if (value < cp)
				break;
		}
		return i;
	}

	public static long last_seed = 0;
	public static Random rand = new Random();

	public static int randSafeInt(int range) {
		if (range <= 0)
			return 0;
		long seed = System.currentTimeMillis();
		if (seed != last_seed) {
			last_seed = seed;
			rand.setSeed(seed);
		}
		return rand.nextInt(range);
	}

	public static int randSafeInt() {
		rand.setSeed(System.currentTimeMillis());
		return rand.nextInt();
	}

	public static int randSafeInt(int min, int max) {
		return min + randSafeInt(max - min);
	}

	public static int randSafeInt2(int range) {
		if (range <= 0)
			return 0;
		long seed = System.currentTimeMillis();
		Random rd = new Random(seed);
		return rd.nextInt(range);
	}

	public static final int strToInt(String str, int radix, int def) {
		if (str == null || str.length() == 0)
			return def;
		try {
			return Integer.parseInt(str, radix);
		} catch (Exception e) {
			return def;
		}
	}

	public static final int strToInt(String str, int def) {
		if (str == null || str.length() == 0)
			return def;
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return def;
		}
	}

	public static void AssertNotEmpty(String str, String msg) throws Exception {
		if (str == null || str.length() == 0)
			E(msg);
	}

	public static void AssertNotEmpty(String str) throws Exception {
		if (str == null || str.length() == 0)
			E("assert not empty failed :" + str);
	}

	public static void AssertNotNull(Object obj, String msg) throws Exception {
		if (obj == null)
			E(msg);
	}

	public static void AssertNotNull(Object obj) throws Exception {
		if (obj == null)
			E("Assert not null failed");
	}

	public static boolean strIsProductionOf(String str, String toks) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (toks.indexOf(c) == -1)
				return false;
		}
		return true;
	}

	public static final boolean strIsInt(String str, boolean empty_str_return) {
		if (str == null || str.length() == 0)
			return empty_str_return;
		try {
			Integer.parseInt(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void Assert(boolean condition, String msg) throws Exception {
		if (condition == false)
			E(msg);
	}

	public static void Assert(boolean condition) throws Exception {
		if (condition == false)
			E("assert failed");
	}

	public static final int[] listIntToArray(List<Integer> list) {
		int[] res = new int[list.size()];
		int len = list.size();
		for (int i = 0; i < len; i++)
			res[i] = list.get(i).intValue();
		return res;
	}

	public static String getStringSafe(String value) {
		return V(value);
	}

	public static String getStringAssert(String value) throws Exception {
		if (value == null || value.length() == 0)
			Helper.E("getStringAssert Failed : " + value);
		return value;
	}

	public static String getStringAssert(String name, String value) throws Exception {
		if (value == null || value.length() == 0)
			Helper.E("getStringAssert Failed : " + name + "=" + value);
		return value;
	}

	public static String getStringDefault(String value, String def) {
		if (value == null || value.length() == 0)
			return def;
		return value;
	}

	public static int getIntSafe(String value) {
		if (value == null)
			return 0;
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static float getFloatSafe(String value) {
		if (value == null)
			return 0.0f;
		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
			return 0.0f;
		}
	}
	
	public static double getDoubleSafe(String value) {
		if (value == null)
			return 0.0;
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return 0.0;
		}
	}

	public static int getIntAssert(String value) throws Exception {
		if (value == null)
			Helper.E("getIntAssert Failed : " + value);
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			Helper.E("getIntAssert Failed : " + value);
			return 0;
		}
	}

	public static int getIntAssert(String name, String value) throws Exception {
		if (value == null)
			Helper.E("getIntAssert Failed : " + name + "=" + value);
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			Helper.E(" parse getIntAssert Failed : " + name + "=" + value);
			return 0;
		}
	}

	public static int getIntDefault(String value, int def) {
		if (value == null)
			return def;
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return def;
		}
	}

	public static boolean getBooleanSafe(String value) {
		if (value == null || value.length() == 0)
			return false;
		return Boolean.parseBoolean(value);
	}

	public static boolean getBooleanAssert(String value) throws Exception {
		if (value == null || (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")))
			Helper.E("getBooleanAssert Failed : " + value);
		return Boolean.parseBoolean(value);
	}

	public static boolean getBooleanAssert(String name, String value) throws Exception {
		if (value == null || (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")))
			Helper.E("getBooleanAssert Failed : " + name + "=" + value);
		return Boolean.parseBoolean(value);
	}

	public static boolean getBooleanDefault(String value, boolean def) {
		if (value == null || (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")))
			return def;
		return Boolean.parseBoolean(value);
	}

	public static final int PERDAY_SECS = 86400;
	public static final int[] DAYS_COUNT = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public static final long getTimeMillisByDatetimeStr(String datetime_str) {// ?
		int[] datetime = parseDateTimeStr(datetime_str);
		if (datetime == null)
			return -1;
		Calendar calendar = Calendar.getInstance();
		calendar.set(datetime[0], datetime[1] - 1, datetime[2], datetime[3], datetime[4], datetime[5]);
		return calendar.getTimeInMillis();
	}

	public static final int[] parseDateTimeStr(String date_str) {
		int[] res = new int[6];
		StringTokenizer st = new StringTokenizer(date_str, ": ,-");

		if (st.hasMoreTokens() == false)
			return null;
		res[0] = Helper.strToInt(st.nextToken(), -1);
		if (res[0] < 0 || res[0] > 3000)
			return null;

		if (st.hasMoreTokens() == false)
			return null;
		res[1] = Helper.strToInt(st.nextToken(), -1);
		if (res[1] < 1 || res[1] > 12)
			return null;

		int day_count = getDayCountByYearAndMonth(res[0], res[1]);
		if (day_count == 0)
			return null;

		if (st.hasMoreTokens() == false)
			return null;
		res[2] = Helper.strToInt(st.nextToken(), -1);
		if (res[2] < 1 || res[2] > day_count)
			return null;

		if (st.hasMoreTokens() == false)
			return null;
		res[3] = Helper.strToInt(st.nextToken(), -1);
		if (res[3] < 0 || res[3] > 23)
			return null;

		if (st.hasMoreTokens() == false)
			return null;
		res[4] = Helper.strToInt(st.nextToken(), -1);
		if (res[4] < 0 || res[4] > 59)
			return null;

		if (st.hasMoreTokens() == false)
			return null;
		res[5] = Helper.strToInt(st.nextToken(), -1);
		if (res[5] < 0 || res[5] > 59)
			return null;

		return res;
	}

	public static final int getDayCountByYearAndMonth(int year, int month) {
		if (month < 1 || month > 12 || year < 0)
			return 0;
		int res = DAYS_COUNT[month - 1];
		if (month == 2 && res % 4 == 0)
			res++;
		return res;
	}
	
	public static void fileWrite(String filePath, String encoding,String fileContent) throws Exception {
		FileOutputStream fos = new FileOutputStream(new File(filePath),false);
		fos.write(fileContent.getBytes("UTF-8"));
		fos.flush();
		fos.close();
	}

	public static String fileRead(String filePath, String encoding) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream is = null;
		try{
			is = new FileInputStream(new File(filePath));
			byte[] bytes = new byte[1024];
			int l = -1;
			while ((l = is.read(bytes)) > 0)baos.write(bytes, 0, l);
		}finally{
			if(is != null)is.close();
		}
		return baos.toString(encoding);
	}
	
	public static String streamRead(InputStream is, String encoding) throws Exception {
		StringBuffer sb = new StringBuffer();
		Reader rd = new InputStreamReader(is, encoding);
		char[] buf = new char[1024];
		while (true) {
			int size = rd.read(buf);
			if (size == -1)
				break;
			sb.append(buf, 0, size);
		}
		return sb.toString();
	}

	public static byte[] streamRead(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bt = 0;
		while ((bt = is.read()) != -1)
			baos.write(bt);
		baos.flush();
		return baos.toByteArray();
	}

	public static final Properties propsLoad(Class<?> cls, String path) throws Exception {
		InputStream is = cls.getResourceAsStream(path);
		if (is == null)
			Helper.E("can't get props file : " + path);
		Properties props = new Properties();
		props.load(is);
		is.close();
		return props;
	}

	public static Object classLoad(String class_name) {
		try {
			if (class_name == null)
				return null;
			return Class.forName(class_name).newInstance();
		} catch (NoClassDefFoundError e) {
			log.info("NoClassDefFoundError : " + class_name + " : ", e);
			return null;
		} catch (Exception e) {
			log.info("can't find class : " + class_name + " : " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 
	 * @param name
	 * @param file_name
	 * @param additivity
	 * @param pattern
	 *            log4j日志格式 默认%d %p %m%n
	 */
	public static final void Log4jConfigLoggerDaillyRollingFile(String name, String file_name, boolean additivity, String pattern) {
		if (Helper.isNullOrEmpty(pattern))
			pattern = "%d %p %m%n";
		Properties props = new Properties();
		if (name.equals("root"))
			props.put("log4j.rootLogger", "DEBUG," + name + "_appender");
		else {
			props.put("log4j.logger." + name, "DEBUG," + name + "_appender");
			props.put("log4j.additivity." + name, String.valueOf(additivity));
		}

		// String dir = System.getProperty("catalina.base");

		props.put("log4j.appender." + name + "_appender", "org.apache.log4j.DailyRollingFileAppender");
		props.put("log4j.appender." + name + "_appender.File", file_name);
		props.put("log4j.appender." + name + "_appender.layout", "org.apache.log4j.PatternLayout");
		props.put("log4j.appender." + name + "_appender.Append", "true");
		props.put("log4j.appender." + name + "_appender.layout.ConversionPattern", pattern);
		PropertyConfigurator.configure(props);
	}
	
	public static String XmlFormat(Document doc){
		try {
			OutputFormat os = OutputFormat.createPrettyPrint();
			os.setEncoding("UTF-8");
			ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
			XMLWriter writer = new XMLWriter(baos,os);
			writer.write(doc);
			return baos.toString();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	public static <T> T map2Object(Map<String, Object> map, Class<T> class1){
		T t = null;
		try {
			Field[] fields = class1.getDeclaredFields();
			if (fields.length > 0){
					t = class1.newInstance();
			}
			boolean flag;
			for (Field field : fields) {
				if (map.containsKey(field.getName())&& map.get(field.getName()) != null){
					flag = false;
					if (!field.isAccessible()){
						field.setAccessible(true);
						flag = true;
					}
					if ((field.getType() == java.util.Date.class || field.getType() == java.sql.Date.class)
							&& map.get(field.getName()).getClass() != field.getType()){
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						field.set(t,format.parse((String) map.get(field.getName())));
					} else if (field.getType() == java.sql.Timestamp.class
							&& map.get(field.getName()).getClass() != field.getType()){
						field.set(t, Timestamp.valueOf((String) map.get(field.getName())));
					} else if (field.getType() == java.lang.Long.class
							&& map.get(field.getName()).getClass() != field.getType()){
						field.set(t,Long.valueOf((String) map.get(field.getName())));
					}else if ((field.getType() == int.class || field.getType() == java.lang.Integer.class)
							&& map.get(field.getName()).getClass() != field.getType()){
						field.set(t,Integer.parseInt(map.get(field.getName()).toString()));
					}else if ((field.getType() == long.class || field.getType() == java.lang.Long.class)
							&& map.get(field.getName()).getClass() != field.getType()){
						field.set(t,Long.parseLong(map.get(field.getName()).toString()));
					}else if ((field.getType() == BigDecimal.class)
							&& map.get(field.getName()).getClass() != field.getType()){
						field.set(t,Long.parseLong(map.get(field.getName()).toString()));
					}else if(field.getType() == String.class && map.get(field.getName()).getClass() != field.getType()){
						field.set(t,map.get((String) field.getName()).toString());
					}else if(field.getType() == Double.class && map.get(field.getName()).getClass() == field.getType()){
						field.set(t,map.get((String) field.getName()).toString());
					}else{
						field.set(t,map.get((String) field.getName()));
					}
					if (flag){
						field.setAccessible(false);
					}
				}
			}
		} catch (InstantiationException e) {
			LogUtil.log.error(e,e);
		} catch (IllegalAccessException e) {
			LogUtil.log.error(e,e);
		} catch (IllegalArgumentException e) {
			LogUtil.log.error(e,e);
		} catch (ParseException e) {
			LogUtil.log.error(e,e);
		}
		return t;
	}
	
	public static String getRequestPath(HttpServletRequest request){
		String requestPath = request.getRequestURI();
		String contenxt = request.getContextPath();
		if(StringUtils.isNotBlank(contenxt)){
			requestPath = requestPath.replaceAll(contenxt, "");
		}
		return requestPath;
	}
	
}