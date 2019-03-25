package com.lm.live.common.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.util.StringUtils;

/**
 * 字符工具类
 * @author huangzp
 * @date 2015-4-7
 */
public class StrUtil {
	public final static String UTF8 = "utf-8";

	public static boolean isNullOrEmpty(String str){
		boolean result = false;
		if(null == str || "".equals(str)){
			result = true;
		}
		return result;
	}
	
	public static String getOrderId(){
		String orderId = DateUntil.getFormatDate("yyyyMMddHHmmssSSS",new Date());
		for(int i=0 ; i<4 ; i++ ){
			orderId+=RandomUtils.nextInt(10);
		}
		return orderId;
	}

	/** 禁止实例化 **/
	private StrUtil() {
	}
	
	/** 转换为float数据类型的方法 **/
	public static float _Float(Object str) {
		float f = 0.0f;
		try {
			f = Float.parseFloat(str.toString());
			return f;
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
			return 0;
		}
	}
	
	/** 转换为int数据类型的方法 **/
	public static double _Double(Object str) {
		double i = 0;
		try {
			i = Double.parseDouble(str.toString());
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * <h1>获取时间到日期</h1>
	 * 说明：如果参数为null，则返回当天日期 
	 * @param date
	 * @return 格式：2012-01-05
	 */
	public static String getDateByDay(Date date) {
		if(date == null)
			date = new Date();
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	
	/**
	 * 字符串日期转换 
	 * @param dateStr 要转换的日期
	 * @param formatFrom 转前格式
	 * @param formatTo 转后格式
	 * @return
	 */
	public static String dateStrConvert(String dateStr,String formatFrom,String formatTo){
		if(StrUtil.isNullOrEmpty(dateStr))
			return "";
		DateFormat sdf= new SimpleDateFormat(formatFrom);
		Date date=null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new SimpleDateFormat(formatTo).format(date);
	}
	
	/**
	 * 获取当前时间
	 * @param type 格式,例如：yyyyMMddHHmmss，不传参数默认为：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDateTimeStr(String... type) {
		String ss = "yyyy-MM-dd HH:mm:ss";
		if(type.length > 0) ss = type[0];
		return new SimpleDateFormat(ss).format(new Date());
	}
	
	
	/**
	 * 字符串转换到日期时间格式
	 * @param dateStr 需要转换的字符串
	 * @param formatStr 需要格式的目标字符串  举例 yyyy-MM-dd
	 * @return Date 返回转换后的时间
	 * @throws ParseException 转换异常
	 */
	public static Date strToDate(String dateStr, String formatStr){
		DateFormat sdf= new SimpleDateFormat(formatStr);
		Date date=null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	
	/**
	 * 比较两个时间的相差值（d1与d2）
	 * @param d1 时间一
	 * @param d2
	 * @param type 类型【d/h/m/s】
	 * @return d1 - d2
	 */
	public static long compareDate(Date d1, Date d2, char type) {
		long num = d1.getTime() - d2.getTime();
		num/=1000;
		if('m' == type){
			num/=60;
		}else if ('h' == type) {
			num/=3600;
		}else if ('d' == type) {
			num/=3600;
			num/=24;
		}
		return num;
	}
	
	
	/**
	 * 根据参照时间获取相差天、小时数的新时间
	 * @param date		参照时间
	 * @param type		天或小时[d/h]
	 * @param num		差值，例如：2表示之后2天或小时，-2表示之前2天或小时
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
	

	/** 获取当前时间戳  **/
	public static String getTimeStamp() {
		  String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	      return timeStamp;
	}
	
	
	/** 获得本周第一天时间(yyyy-MM-dd) **/
	public static String getMonOfWeek(){
//		Calendar c = new GregorianCalendar();
//		c.setFirstDayOfWeek(Calendar.MONDAY);
//		c.setTime(new Date());
//		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, c.getActualMinimum(Calendar.DAY_OF_WEEK));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(c.getTime());
		return str;
	}
	
	
	/** 获得本周的最后一天(yyyy-MM-dd) **/
	public static String getSunOfWeek(){
//		Calendar c = new GregorianCalendar();
//		c.setFirstDayOfWeek(Calendar.MONDAY);
//		c.setTime(new Date());
//		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, c.getActualMaximum(Calendar.DAY_OF_WEEK));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(c.getTime());
		return str;
	}
	
	
	/** 获得当月的第一天(yyyy-MM-dd) **/
	public static String getFirstDayOfMonth(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String str=sdf.format(cal.getTime());
		return str;
	}
	
	
	/** 获得当月的最后一天(yyyy-MM-dd) **/
	public static String getLastDayOfMonth(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdfs=new SimpleDateFormat("yyyy-MM-dd");
        String str=sdfs.format(cal.getTime());
		return str;
	}
	
	
	/**
	 * 格式化时间，时间转字符串
	 * @param date	null则为当前系统时间
	 * @param format 格式，null则默认为：'yyyy-MM-dd HH:mm:ss'
	 * @return 字符串格式的日期
	 */
	public static String getDateTimeByStr(Date date, String format) {
		if(date == null)
			date = new Date();
		if(format == null)
			format = "yyyy-MM-dd HH:mm:ss";
		return new SimpleDateFormat(format).format(date);
	}
	
	
	/**
	 * 格式化时间，字符串转时间
	 * @param dataStr	需要转换的字符串
	 * @param format 	格式，null则默认为：'yyyy-MM-dd HH:mm:ss'
	 * @return	转换的Date
	 */
	public static Date getStrByDataTime(String dataStr, String format) {
		if(dataStr == null)
			return new Date();
		if (format == null)
			format = "yyyy-MM-dd HH:mm:ss";
		DateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(dataStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	
	public static void main(String[] args) {
//		long nn = compareDate((new Date()), getNextDay(new Date(), 'd', -3), 'd');
//		System.out.println(nn);
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH").format(getNextDay(new Date(), 'h', -2)));
//		Integer aa = 11;
//		System.out.println(getCleanInteger(aa));
//		System.out.println(getPrecision("100", "200454561656565668594919619256255.2"));
//		System.out.println(subStrmaxStr("1234567890", 20));
		
		/*System.out.println(isNum("023940324"));
		System.out.println(isNum("02394039-24"));
		System.out.println(isNum("0239403b24"));*/
		
//		StrUtil u = new StrUtil();
		//System.out.println(u.dateStrConvert("2013-01-30 02:03:04.3","yyyy-MM-dd HH:mm:ss","yyyyMMddHHmmss"));
		//u.getCleanInteger(335);
//		System.out.println(StrUtil.dateStrConvert("2013-05-21 02:02:03.3","yyy-MM-dd HH:mm:ss","yyy-MM-dd HH:mm:ss"));
		
		String amount = "sdfasdfasdf、";
		removeSymbol(amount); 
		System.err.println(removeSymbol(amount));
	}
	
	
	/**
	 * 限制字符串的最长长度，多余的截取
	 * @param str	源字符串
	 * @param maxLen 最长长度	
	 * @return
	 */
	public static String subStrmaxStr(String str, int maxLen){
		if(!StrUtil.isNullOrEmpty(str)){
			try {
				maxLen = (maxLen < 0) ? str.length() : maxLen;
				str = str.substring(0,str.length() >= maxLen ? maxLen : str.length());
			} catch (Exception e) {
				e.printStackTrace();
				str = "";
			}
		}else{
			str = "";
		}
		return str;
	}
	
	/**检查是否是数字 **/
	public static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	
	
	 /** 保证将传入参数处理到一定是两位 **/
	public static String formatToTwoLength(Object o){
		String str = "";
		try {
			str = String.valueOf(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    if(str.length() > 2)
	       return str.substring(0, 2);
	    if(str.length() < 2)
	       return "0"+str;
	    return str;
	}
	
	/**
	 * 检查字符串是否为null或者为"null"<br>
	 * 为null或者为"null",返回""，否则返回字符串 <br>
	 * 用于处理数据库查询数据
	 */
	public static String getCleanString(Object obj){
		if( obj == null ){
			return "";
		}else if(String.valueOf(obj).equals("null")){
			return "";
		}else{
			return String.valueOf(obj);
		}
	}
	

	/**
	 * 检查int数据是否为null<br>
	 * 为null,返回0，否则返回原值 <br>
	 * @param obj
	 * @return
	 */
	public static int getCleanInteger(Object obj){
		if( obj != null ){
			return _Integer(obj);
		}else{
			return 0;
		}
	}
	
	

	/**
	 * 检查float数据是否为null<br>
	 * 为null,返回0f，否则返回原值 <br>
	 * @param obj
	 * @param defaultValue	默认值，可以传入可以不传。传入，为null时返回本值
	 * @return
	 */
	public static float getCleanFloat(Object obj, float... defaultValue){
		if( obj != null ){
			return _Float(obj);
		}else{
			if(defaultValue.length != 1){
				return 0f;
			}else{
				return defaultValue[0];
			}
		}
	}
	
	
	/**
	 * 从number数值中随机抽取count个数字
	 * @param count	  要抽取的数值个数
	 * @param number 数字范围为 (0, number]
	 * @return
	 */
	public static int[] randomNumber(int count, int number) {
	    int k=count, n=number; 
		int[] numbers = new int[n];
		for (int i = 0; i < numbers.length; i++)
			numbers[i] = i + 1;
		int[] result = new int[k];
		for (int i = 0; i < result.length; i++){  
			int r = (int) (Math.random() * n);
			result[i] = numbers[r];
			numbers[r] = numbers[n - 1];
			n--;
		}
	      
//	    Arrays.sort(result);
//	    for (int r : result)
//	       System.out.println(r);
		return result;
		
	}
	
	/**
	 * 返回随机字符串
	 * @param toks 要随机操作的字符串
	 * @param len 长度
	 * @return
	 */
	public static String genRandoomString(String toks,int len)
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<len;i++) sb.append(toks.charAt((int)(Math.random() * len)));
		return sb.toString();
	}
	
	/**
	 * 高精度乘法运算
	 * @param num	乘数
	 * @param num2	因子
	 * @return
	 */
	public static String getPrecision(String num, String num2){
		return new BigDecimal(num).multiply(new BigDecimal(num2)).toString();
	}


	/**
	 * 截取小数后面的0
	 * @param str
	 * @return
	 */
	public static String substringFloat(String str){
		String ss = str.substring(0, str.indexOf('.'));
		if(Double.parseDouble(ss) == Double.parseDouble(str)){
			return ss;
		}
		return str;
	}
	
	/**
	 * 生成随机字符串
	 * @param length length表示生成字符串的长度
	 * @return
	 */
	public static String getRandomString(int length) {
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";  
	    Random random = new Random();  
	    StringBuffer sb = new StringBuffer();  
	    for (int i = 0; i < length; i++) {  
	        int number = random.nextInt(base.length());  
	        sb.append(base.charAt(number));  
	    }  
	    return sb.toString();  
	 }  
	
	/**
	 * 去掉所有空格字符
	 * @param str
	 * @return
	 */
	public static String trimStr(String str) {
		StringBuffer sbf = new StringBuffer();
		String[] trims = str.trim().split(" ");
			if(trims != null && trims.length>0) {
				for(int i=0;i<trims.length;i++) {
					sbf.append(trims[i]);
				}
			} 
			return sbf.toString();
	}
	
	/**
	 * 替换如下字符 <br />
	 * \n 回车(\u000a)  <br />
    	\t 水平制表符(\u0009)  <br />
    	\s 空格(\u0008) <br />
    	\r 换行(\u000d) <br />
	 * @param str
	 * @return
	 */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    
    /**
     * 替换回车(\n)、换行符(\r)
     * @param str
     * @return
     */
    public static String replaceNewline(String str) {
        String dest = "";
        if (!StringUtils.isEmpty(str)) {
            Pattern p = Pattern.compile("\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    
    /**
     * 格式化最后的标点符号，句号结尾
     * @param str
     * @return
     */
    public static String removeSymbol(String str) {
    	String ret = "";
    	if(!StringUtils.isEmpty(str)) {
    		ret = str.substring(0,str.length()-1) + "。";
    	}
    	return ret;
    }
    
    
    /**
	 * 校验版本
	 * @param oldVersion 老版本
	 * @param newVersion 新版本
	 * @return 是否较之前版本为新版本
	 */
	public static boolean checkAppVersion(String oldVersion, String newVersion) {
		boolean flag = false;
		if(StringUtils.isEmpty(newVersion)) {
			return flag;
		}
        int intOldVersion = Integer.parseInt(oldVersion.replace(".", "0"));
        int intNewVersion = Integer.parseInt(newVersion.replace(".", "0"));
        if(intNewVersion > intOldVersion) {
        	flag = true;
        }
		return flag;
	}
	
	/**
	 * 字符串中的特殊字符trim掉(如:控制字符),以免
	 * @param str
	 * @return
	 */
	public static String trimControlCharacter(String str) {
		if(str!=null && str.indexOf("\\p{Cntrl}" )!= -1){
			LogUtil.log.warn("####contains trimControlCharacter:"+str);
		}
		return str == null ? null : str.replaceAll("\\p{Cntrl}", "");
	}
	/**
	 * 字符截取
	 * @param s
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public static String jiequStr(String s,int num)throws Exception{
        int changdu = s.getBytes("GBK").length;
        if(changdu > num){
            s = s.substring(0, s.length() - 1);
            s = jiequStr(s,num);
        }
        return s;
    }
	
	/**
	 * 求百分比
	 * @param currentVal 目前值
	 * @param totalVal 总值
	 * @param minimumFractionDigits 百分比后保留的小数位
	 * @return 返回百分比的字符串,如:33%
	 */
	public static String convert2Percent(int currentVal,int totalVal,int minimumFractionDigits){
		 	NumberFormat numberFormat = NumberFormat.getInstance();  
	        // 设置精确到小数点后2位  
	        numberFormat.setMaximumFractionDigits(minimumFractionDigits);  
	        String result = numberFormat.format((float) currentVal / (float) totalVal * 100);
	        return result+"%";
	}
}
