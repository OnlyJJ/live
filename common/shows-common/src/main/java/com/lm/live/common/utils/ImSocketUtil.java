package com.lm.live.common.utils;

public class ImSocketUtil {
	
	public static byte[] generateImReqByteArrParam(String reqDataStr,String charSet){
		byte[] dataByteArr = GZipUtil.compressToByte(reqDataStr,charSet);
		int dataLen = dataByteArr.length;
		byte[] lenArr = ByteUtil.toByteArray(dataLen, 4);
		byte[] packetAllArr = new byte[lenArr.length+dataByteArr.length];
		for(int i=0;i<lenArr.length;i++){
			packetAllArr[i] = lenArr[i];
		}
		for(int i=0;i<dataByteArr.length;i++){
			packetAllArr[lenArr.length+i] = dataByteArr[i];
		}
		return packetAllArr;
	}

}
