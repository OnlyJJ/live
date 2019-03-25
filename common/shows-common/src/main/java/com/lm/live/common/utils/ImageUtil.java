package com.lm.live.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 图片验证码工具类
 *
 */
public class ImageUtil {
	/**
	 * 验证码的长度
	 */
	private static final int NUM = 5;
	/**
	 * 干扰线的条数
	 */
	private static final int LINE = 10;
	/**
	 * 图片宽度
	 */
	private static final int WIDTH = 200;
	/**
	 * 图片高度
	 */
	private static final int HEIGHT = 80;
	
	/*private static String[] chars = {"0","1","2","3","4","5","6","7","8","9"};*/
	
	private static final String VERIFY_CODES = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
	
	/**
	 * 返回map  <br />
	 * key: code(5位验证码) 图片对应验证码 <br />
	 * key: img  图片
	 * @return
	 */
	public static Map<String,Object> createImage(){
		Map<String,Object> m = new HashMap<String,Object>();
		//构造内存中的图片
		BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		Graphics grap = image.getGraphics();
		grap.setColor(new Color(150,150,150));
		grap.fillRect(0, 0, WIDTH, HEIGHT);
		//保存随机生成的验证码字符串
		StringBuffer codeString = new StringBuffer();
		for(int i=0;i<NUM;i++){
			String c = getRandomChar();
			codeString.append(c);
			grap.setColor(getRandomColor());
			grap.setFont(new Font(null,Font.BOLD,50));
			grap.drawString(c, 10+i*WIDTH/NUM, HEIGHT/2+20);
		}
		
		//添加干扰线
		for(int i=0;i<NUM;i++){
			Random ran = new Random();
			grap.setColor(getRandomColor());
			grap.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT), ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
		}
		m.put("img", image);
		m.put("code", codeString);
		return m;
	}
	
	private static String getRandomChar(){
		Random ran = new Random();
		//int dex = ran.nextInt(chars.length);
		//return chars[dex];
		int index = ran.nextInt(VERIFY_CODES.length());
		return VERIFY_CODES.substring(index,index+1);
	}
	
	private static Color getRandomColor(){
		Random ran = new Random();
		return new Color(ran.nextInt(256),ran.nextInt(256),ran.nextInt(256));
	}

}
