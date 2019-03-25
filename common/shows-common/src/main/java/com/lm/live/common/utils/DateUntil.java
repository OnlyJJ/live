package com.lm.live.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUntil {
	
	/**
	 * 使用默认的格式("yyyy-MM-dd HH:mm:ss")转换成date
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static Date parse2DefaultFormat(String dateStr) throws ParseException{
		String format = "yyyy-MM-dd HH:mm:ss" ;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}
	
	public static Date parse(String dateStr,String format) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}
	
	/**
	 * 获取默认格式日期(yyyy-MM-dd HH:mm:ss)
	 * @param date 日期
	 * @return
	 */
	public static String getDefaultFormatDate(Date date){
		String format = "yyyy-MM-dd HH:mm:ss" ;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 获取自定格式日期
	 * @param format 格式
	 * @param date 日期
	 * @return
	 */
	public static String getFormatDate(String format,Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static void main(String[] args) throws ParseException {
		//System.out.println(getFormatDate("yyyyMMddHHmm",new Date()));
		/*String date = "2015-05-01 12:45:35.0";
		Date addTime = DateUntil.getDateByFormat("yyyy-MM-dd HH:mm:ss.SS", date);
		System.out.println(addTime.getYear());
		System.out.println(addTime.getMonth());
		System.out.println(addTime.getDate());
		System.out.println(addTime.getHours());
		System.out.println(addTime.getMinutes());
		System.out.println(addTime.getSeconds());
		System.out.println(addTime.toLocaleString());*/
//		String brithday = "1996-2-10 00:00:00.0";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");  
//	    Date date = sdf.parse(brithday);
//	    System.err.println(date.getMonth());
//		getAstro(1,21);
//		getAstro(date.getMonth()+1 , date.getDate());
//		System.out.println(getFormatDate("yyyy-MM-dd HH:mm:ss", new Date()));
		
//		Calendar ca = Calendar.getInstance();
//		long l = 1472785526000l;
//		SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");  
//		  
//		String sd = sdf.format(new Date(l));  
//		
//		DateUntil.daysBetween(new Date(l), new Date());
//		 System.err.println(sd);
//		System.out.println(DateUntil.getDay(new Date()));
//		
		 System.err.println(getLastWeek());
	}
	
	public static Date getDateByFormat(String format,String date){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateD = null;
		try {
			dateD = sdf.parse(date);
		} catch (ParseException e) {
			LogUtil.log.info("时间格式转换错误", e);
		}
		return dateD;
	}
	
	/**
	 * 比较连个时间至差是否小于 nDay 天
	 * @param date1 被减事物日期
	 * @param date
	 * @param nDay
	 * @return
	 */
	public static boolean isTrueDate(Date date1,Date date,int nDay){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		cal.add(Calendar.DAY_OF_YEAR, -nDay);
		date1 = cal.getTime();
		if(date1.before(date)){
			return true;
		}
		return false;
	}
	
	/**
	 * 日期增添天数的方法
	 * @param date 
	 * @param n 增加|减少的天数
	 * @return
	 */
	public static Date addDay(Date date,int n){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, n);
		return cal.getTime();
	}
	
	/**
	 * 日期增添天数的方法
	 * @param date 
	 * @param n 增加|减少的天数
	 * @return
	 */
	public static String addDayByDate(Date date,int n){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, n);
		return getFormatDate("yyyyMMdd", cal.getTime());
	}
	
	/**
	 * 日期增添月数的方法
	 * @param date 
	 * @param n 增加|减少的月数
	 * @return
	 */
	public static String addMonthByDate(Date date,int n){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return getFormatDate("yyyyMMdd", cal.getTime());
	}
	
	/**
	 * 获取当上周日
	 * @param date
	 * @param n
	 * @return
	 */
	public static String getWeek(){
		Calendar cal = Calendar.getInstance();
		int w = cal.get(Calendar.DAY_OF_WEEK)-1 ;
		return addDayByDate(new Date(), -w);
	}
	
	/**
	 * 日期增添月数的方法
	 * @param date 
	 * @param n 增加|减少的月数
	 * @return date
	 */
	public static Date addMonthDatetime(Date date,int n){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}
	
	/**
	 * 日期增添天数的方法
	 * @param date 
	 * @param n 增加|减少的月数
	 * @return date
	 */
	public static Date addDatyDatetime(Date date,int n){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, n);
		return cal.getTime();
	}
	
	/**
	 * 日期增添天数的方法
	 * @param date 
	 * @param n 增加|减少的月数
	 * @return date
	 */
	public static Date addMinute(Date date,int n){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, n);
		return cal.getTime();
	}
	
	/**
	 * 增加时间(秒)
	 * @param date 
	 * @param n 增加|减少的月数
	 * @return date
	 */
	public static Date addSecond(Date date,int n){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, n);
		return cal.getTime();
	}
	
	/**
	 * 获取当前月第一天
	 * @param date
	 * @param n
	 * @return
	 */
	public static String getMonthDatetime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH,1);
		return format.format(cal.getTime());
	}
	
	/**
	 * 获取当前时间的yyyyMMddHHmmss
	 * @return
	 */
	public static String getNowYyyyMMddHHmmss(){
		Date now = new Date();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(now);
	}
	
	/**
	 * 获取当天日期的yyyyMMdd
	 * @return
	 */
	public static String getNowYyyyMMdd(){
		Date now = new Date();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd");
		return sdf.format(now);
	}
	
	/**
	 * 获取时间
	 * @param str “yyyyMMddHHmmss”
	 * @return	Date
	 * */
	public static Date getDateByStr(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = null ;
		try{
			date = sdf.parse(str);
		}catch(Exception e){
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取两个时间差的天数,
	 * @param date1，date2，时间字符串类型，格式为"yyyy-MM-dd HH:mm:ss"
	 * @return long 返回date1 - date2的差
	 * 
	 */
	public static long getCountByDate(String date1) {
		long count = 0;
		Date d1 = new Date();
		Date d2 = getDateByFormat("yyyy-MM-dd HH:mm:ss",date1);
		count = (d1.getTime() - d2.getTime()) / (24*60*60*1000);
		return count;
	}
	
	/**
	 * 获取数据库存储时间字段列的日期中的日
	 * @param dateStr 格式需为"yyyy-MM-dd HH:mm:ss"
	 * @return day 日
	 */
	public static int getDayByMysqlTime(String dateStr) {
		String d1[] = dateStr.split(" ");
		String d2 = d1[0];
		String d3[] = d2.split("-");
		int day = Integer.parseInt(d3[2]);
		return day;
	}
	
	/**
	 * 获取当前时间的日
	 * @return
	 */
	public static int getNowDateDay(){
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
	
	/**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();  
        long timeSub = time2-time1;
        long between_days=(timeSub)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }    
    
    /**  
     * 计算两个日期之间相差的天数  (向下取整)
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetweenFloor(Date smdate,Date bdate) throws ParseException    
    {    
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();  
        long timeSub = time2-time1;
        long between_days=(timeSub)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }    
    
    /**  
     * 计算两个日期之间相差的天数  (向下取整)
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
  /*  public static int daysBetweenFloor(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();    
        Double 
        long between_days=(time2-time1)/(1000*3600*24);  
        //向下取整
       // between_days =  Math.floor(between_days);
       return between_days.;           
    }   */ 
    
    /**
     * 返回两个之间剩余的倒计时
     * @param beginTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static String getTimeRemains(Date beginTime,Date endTime) throws ParseException    
    {    
    	long between=(endTime.getTime()-beginTime.getTime())/1000;//除以1000是为了转换成秒   
        long day1=between/(24*3600);   
        long hour1=between%(24*3600)/3600;   
        long minute1=between%3600/60;   
        long second1=between%60/60;   
       return day1+"天"+hour1+"小时"+minute1+"分"+second1+"秒";           
    }    
    
    public static int getMinute(Date date) {
    	Date now = new Date();
    	long def = (now.getTime() - date.getTime())/1000; // second
    	long minute = def%3600/60; // min
    	return (int)def;
    }
    
    public static long getTimeTamp(Date date) {
    	Date now = new Date();
    	long timeTamp = now.getTime() - date.getTime();
    	return timeTamp;
    }
    
    /**
     * 返回两个时间之间的间隔秒数(向下取整)
     * @param beginTime 起点时间
     * @param endTime 结束时间
     * @return
     */
    public static int getTimeIntervalSecond(Date beginTime,Date endTime){
    	Long beginTimeLong = beginTime.getTime(); 
    	Long endTimeLong = endTime.getTime();
    	Long timeInterval = ((endTimeLong - beginTimeLong) / 1000);
    	return timeInterval.intValue();
    }
    
    /**
     * 返回两个时间之间的间隔分钟数(向下取整)
     * @param beginTime 起点时间
     * @param endTime 结束时间
     * @return
     */
    public static int getTimeIntervalMinute(Date beginTime,Date endTime){
    	Long beginTimeLong = beginTime.getTime(); 
    	Long endTimeLong = endTime.getTime();
    	Long timeInterval = ((endTimeLong - beginTimeLong) / 1000/60);
    	return timeInterval.intValue();
    }
    
    public static long getCurrentTime(Date date,long park) {
    	Date now = new Date();
    	long timeTamp = (date.getTime() /1000 +park) - now.getTime()/1000;
    	return timeTamp;
    }
    /**
     * 根据月 ，日 获取星座
     * @param month  月	
     * @param day  日
     * @return String
     * */
    public static String getAstro(int month, int day) {
	    String[] starArr = {"魔羯座","水瓶座", "双鱼座", "牡羊座",
	        "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座" };
	    int[] DayArr = {22, 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22};  // 两个星座分割日
	    int index = month-1;
	    // 所查询日期在分割日之前，索引-1，否则不变
//	    if (day < DayArr[month - 1]) {
//	            index = index - 1;
//	    }
	    if(day > DayArr[index]) {
	    	if(month == 12) {
	    		index = 0;
	    	} else {
	    		index = index + 1;
	    	}
	    }
	    // 返回索引指向的星座string
	    return starArr[index];
	}
    
    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                      calendar.getFirstDayOfWeek()); // Sunday
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取日期特定格式的字符
     * @param nowDate
     * @param format
     * @return
     */
	public static String format2Str(Date nowDate, String format) {
		SimpleDateFormat sdf =new SimpleDateFormat(format);
		return sdf.format(nowDate);
	}
	
	/**
	 * 格式化自定义日期格式的日期，返回date
	 * @param date
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date paser(Date date, String format) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat(format);  
		date=sdf.parse(sdf.format(date));  
        return date;
	}
	
	/**
	 * 获取当前月的第一天，返回日期date
	 * @return
	 */
	public static Date getFirstDayOfMounth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH,1);
		return calendar.getTime();
	}
	
	/**
	 * 日期转化为时间戳
	 * @param date
	 * @return
	 */
	public static long getTime(Date date) {
	    	long timeTamp = date.getTime();
	    	return timeTamp;
	    }
	
	/**
	 * 获取时间的月
	 * @param d
	 * @return
	 */
	public static int getMounth(Date d) {
		int m = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		m = calendar.get(Calendar.MONTH) + 1;
		return m;
	}
	
	public static int getDay(Date d) {
		int day = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	
    /**
     * 返回两个之间剩余的倒计时
     * @param beginTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static String getTimeRemains2(Date beginTime,Date endTime) throws ParseException    
    {    
    	long between=(endTime.getTime()-beginTime.getTime())/1000;//除以1000是为了转换成秒   
        long day1=between/(24*3600);   
        long hour1=between%(24*3600)/3600;   
        long minute1=between%3600/60;   
       return day1+"天"+hour1+"小时"+minute1+"分";           
    } 
    
    /**
	 * 获取当上周的开始日期
	 * @return yyyy-MM-dd
	 */
	public static String getLastWeekBeginDay(){
		Date nowDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDate);
		int w = cal.get(Calendar.DAY_OF_WEEK)-1 ;
		w= w+7;
		cal.add(Calendar.DAY_OF_YEAR, -w);
		return getFormatDate("yyyy-MM-dd", cal.getTime());
	}
	
	/**
	 * 获取当上周的结束日期
	 * @return yyyy-MM-dd
	 */
	public static String getLastWeekEndDay(){
		Date nowDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDate);
		int w = cal.get(Calendar.DAY_OF_WEEK)-1 ;
		w= w+1;
		cal.add(Calendar.DAY_OF_YEAR, -w);
		return getFormatDate("yyyy-MM-dd", cal.getTime());
	}
	
	
	/**获取下周第一天*/
	public static Date getFirstDayOfNextWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.add(Calendar.DATE,7);//加7天
        calendar.set(Calendar.DAY_OF_WEEK,
                      calendar.getFirstDayOfWeek()); //SUNDAY
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
	
	/**获取结开始时间*/
	public static String getStartTime(Date date) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = DateUntil.getFirstDayOfWeek(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DATE, -7);
		return sd.format(calendar.getTime());
    }
	
	/**获取结束时间*/
	public static String getEndTime(Date date) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = DateUntil.getFirstDayOfWeek(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
		return sd.format(calendar.getTime());
    }
	
	/**
	 * 两个时间的差的时间戳
	 * @param beginTime
	 * @param engTime
	 * @return
	 */
	public static long getDateIncCurrentTime(Date beginTime,Date engTime) {
	    	long timeTamp = beginTime.getTime() - engTime.getTime();
	    	return timeTamp;
	    }
	
	
	/**获取第二天的某个整点时间*/
	public static Date getNextDayTime(Date date,int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);//加1天
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
	
	/**
	 * 获取上周第一天
	 * @return
	 */
	public static String getLastWeek() {
		Calendar cal = Calendar.getInstance();
		int w = cal.get(Calendar.DAY_OF_WEEK)-1 ;
		int lw = w + 7;
		return addDayByDate(new Date(), -lw);
	} 
	
	/**
	 * 比较两个时间是否为同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
		return isSameDate;
	}

}
