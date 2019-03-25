package com.lm.live.common.utils;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 不同概率抽奖工具包
 *
 * @author long.bin
 */
public class LotteryUtil {
   
	/**
	 * 随机抽奖,返回序号 (对应奖品的序号)<br />
	 * 1.参数为List<Double>,里面的元素为开奖百分比转成的double小数， <br />
	 * 2.不要设置概率相等的奖品
	 * @param paramListRate 
	 * @return
	 */
    public static int lottery(List<Double> paramListRate) {
    	LogUtil.log.info(String.format("###begin-lottery,listRateInAscSort:", JsonUtil.arrayToJsonString(paramListRate)));
        if (paramListRate == null || paramListRate.isEmpty()) {
            return -1;
        }
        String strListRateInAscSort = JsonUtil.arrayToJsonString(paramListRate);
        LogUtil.log.info("###lottery-orignalRatesInsort:"+strListRateInAscSort);
        int size = paramListRate.size();
 
        // 计算总份额，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (double rate : paramListRate) {
            sumRate += rate;
        }
 
        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<Double>(size);
        Double tempSumRate = 0d;
        for (double rate : paramListRate) {
            tempSumRate += rate;
            sortOrignalRates.add(tempSumRate / sumRate);
        }
 
        //获取一个0-1直接的小数
        double randomDouble = Math.random();
        sortOrignalRates.add(randomDouble);
        Collections.sort(sortOrignalRates);
        String strSortOrignalRates = JsonUtil.arrayToJsonString(sortOrignalRates) ;
        LogUtil.log.info("###lottery-sortOrignalRates:"+strSortOrignalRates);
     // 根据区块值来获取抽取到的物品序号
        int index = sortOrignalRates.indexOf(randomDouble);
        LogUtil.log.info("###lottery-sortOrignalRates:"+strSortOrignalRates+",return_index:"+index+",randomDouble:"+randomDouble);
    	LogUtil.log.info(String.format("###end-lottery,return_index:%s,randomDouble:%s,paramListRateInAscSort:",index, randomDouble,JsonUtil.arrayToJsonString(paramListRate)));
        return index;
    }
    
    /**
     * BigDecimal 此类型计算更加精确 <br />
	 * @param paramListRate 每个选项的概率份额 
	 * @return
	 */
    public static int lotteryWithBigDecimal(List<BigDecimal> paramListRate) {
    	LogUtil.log.info(String.format("###begin-lotteryWithBigDecimal,listRateInAscSort:", JsonUtil.arrayToJsonString(paramListRate)));
        if (paramListRate == null || paramListRate.isEmpty()) {
            return -1;
        }
        String strListRateInAscSort = JsonUtil.arrayToJsonString(paramListRate);
        LogUtil.log.info("###lotteryWithBigDecimal-orignalRatesInsort:"+strListRateInAscSort);
        int size = paramListRate.size();
 
        // 计算总份额，这样可以保证不一定总概率是1
        BigDecimal sumRate = new BigDecimal(0);
        for (BigDecimal itemRate : paramListRate) {
        	sumRate = sumRate.add(itemRate);
        }
 
        // 计算每个物品在总概率的基础下的概率情况
        List<BigDecimal> sortOrignalRates = new ArrayList<BigDecimal>(size);
        BigDecimal tempSumRate =new BigDecimal(0);
        for (BigDecimal itemRate : paramListRate) {
        	tempSumRate = tempSumRate.add(itemRate);
        	//取5位小数,向上取整,避免产生无限小数而报错
            sortOrignalRates.add(tempSumRate.divide(sumRate,5,BigDecimal.ROUND_CEILING));
        }
 
        //获取一个0-1直接的小数
        double randomDouble = Math.random();
        BigDecimal randomBigDecimal = new BigDecimal(randomDouble);
        sortOrignalRates.add(randomBigDecimal);
        Collections.sort(sortOrignalRates);
        String strSortOrignalRates = JsonUtil.arrayToJsonString(sortOrignalRates) ;
      //  LogUtil.log.info("###lotteryWithBigDecimal-sortOrignalRates:"+strSortOrignalRates);
     // 根据区块值来获取抽取到的物品序号
        int index = sortOrignalRates.indexOf(randomBigDecimal);
       // LogUtil.log.info("###v-sortOrignalRates:"+strSortOrignalRates+",return_index:"+index+",randomDouble:"+randomDouble);
    	LogUtil.log.info(String.format("###end-lotteryWithBigDecimal,return_index:%s,randomDouble:%s,sortOrignalRates:%s,paramListRate:%s",index, randomDouble,strSortOrignalRates,JsonUtil.arrayToJsonString(paramListRate)));
        return index;
    }
    
    private static void testUnit1(int total) {
        //参数设置
        int times = total;
        List<Double>  possibility = new ArrayList();
    	possibility.add(0.01);
    	possibility.add(0.02);
    	possibility.add(0.02);
    	possibility.add(0.05);
    	possibility.add(0.1);
    	possibility.add(0.8);
        int length = possibility.size();
        int[] sum = new int[length];
       
        //测试
        System.out.println("一共测试" + times + "次，正在进行中。。");
        double loading = 0.1;
        for (int i = 0; i < times; i++) {
             if(Math.abs((double)i / times - loading) < 0.00000000001){
                  System.out.println("已完成"+ Math.round(loading * 100) + "%");
                  loading += 0.1;
             }
             if (i == times - 1) {
                  System.out.println("已完成100%");
             }
             //调用随机函数
             int index = lottery(possibility);
             sum[index]++;
        }
       
        //输出
        for (int i = 0; i < length; i++) {
             double percent = (double)possibility.get(i) / times;
             DecimalFormat df = new DecimalFormat("0.0000000");
             System.out.println("事件" + i + "发生概率：" + df.format(percent * 100) + "%" + "     " + "发生次数：" + sum[i] + "次");
        }
   }
	
    
    public static void main(String[] args) {
    	testUnit1(10000000);
	}
}