package com.lm.live.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteUtil {
	
	/**
	 * 将iSource转为长度为iArrayLen的byte数组，字节数组的低位是整型的低字节位
	 * @param iSource
	 * @param iArrayLen
	 * @return
	 */
	public static byte[] toByteArray(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);

		}
		return bLocalArr;
	}
	
	
	/**
	 * 将byte数组bRefArr转为一个整数,字节数组的低位是整型的低字节位
	 * @param bRefArr
	 * @return
	 */
	public static int toInt(byte[] bRefArr) {
		int iOutcome = 0;
		byte bLoop;

		for (int i = 0; i < 4; i++) {
			bLoop = bRefArr[i];
			iOutcome += (bLoop & 0xFF) << (8 * i);

		}
		return iOutcome;
	}
	
	 public static String getDataBody(InputStream is) throws IOException {
			String dataBody = null;
			// 获取头部
			byte[] head = getData(is, 4);
			//System.out.println("================================      "+bytesToHexString(head));
			int dataLength = ByteUtil.toInt(head);
			
			// 获取数据
			byte[] data = getData(is, dataLength);
			dataBody = GZipUtil.uncompressToString(data);

			return dataBody;
		}
	 
	  private static byte[] getData(InputStream is, int length) throws IOException {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[5120];
			int nIdx = 0; //累计读取了多少位
			int nReadLen = 0; //一次读取了多少位

			while (nIdx < length) { //循环读取足够长度的数据
				
				if(length - nIdx >= buffer.length){ //剩余数据大于缓存，则全部读取
					nReadLen = is.read(buffer);
				}else{ //剩余数据小于缓存，则注意拆分其他包，只取当前包剩余数据
					nReadLen = is.read(buffer, 0, length - nIdx);
				}
				
				if (nReadLen > 0) {
					baos.write(buffer, 0, nReadLen);
					nIdx = nIdx + nReadLen;
				} else {
					break;
				}
				
			}
			
			return baos.toByteArray();
		}

}
