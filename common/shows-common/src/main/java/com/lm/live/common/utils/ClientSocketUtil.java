package com.lm.live.common.utils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientSocketUtil {

	public static Socket getConnect(String url,int port) throws Exception{
		Socket server = null;
		try {
			server = new Socket(url,port);
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage() ,e);
			throw e;
		} 
		return server;
	}
	//	 向服务端程序发送数据
	public static void send(Socket server,String data,String charSet)  throws Exception{
		try {
			OutputStreamWriter osw = new OutputStreamWriter(server.getOutputStream(),charSet);
			BufferedWriter bw = new BufferedWriter(osw );
			bw.write(data+"\r\n");
			bw.flush();
		} catch (IOException e) {
			LogUtil.log.error(e.getMessage() ,e);
			throw e;
		}
	}
	
	
	// 向IM服务端程序发送数据(采用指定的压缩方法)
	public static void sendToIm(Socket server,byte[] data)  throws Exception{
		try {
			BufferedOutputStream bops = new BufferedOutputStream(server.getOutputStream(),1024);
			bops.write(data);
			bops.flush();
		} catch (IOException e) {
			LogUtil.log.error(e.getMessage() ,e);
			throw e;
		}
	}
	/**
	 * 从服务端程序接收数据,返回一个BufferedReader
	 * @return
	 */
	public static String recieve(Socket server,String charSet)  throws Exception{
		InputStreamReader isr=null;
		BufferedReader br=null;
		StringBuffer sbf = new StringBuffer();
		try {
			isr = new InputStreamReader(server.getInputStream(),charSet);
			br = new BufferedReader(isr);
			String s = br.readLine();
			while((s =br.readLine())!=null){
				sbf.append(s);
			}
		} catch (IOException e) {
			LogUtil.log.error(e.getMessage() ,e);
			throw e;
		}
		return sbf.toString();
	}
	
	/**
	 * 从IM服务端程序接收数据(采用指定的解压缩方法)
	 * @return
	 */
	public static String recieveFromIm(Socket server)  throws Exception{
		String str = null;
		if(server != null ){
			if(server.isConnected()){
				try {
					InputStream inputStream = server.getInputStream() ;
					if(inputStream != null){
						str = ByteUtil.getDataBody(inputStream);
					}else{
						LogUtil.log.info("###recieveFromIm,inputStream is null");
					}
				} catch (IOException e) {
					LogUtil.log.error(e.getMessage() ,e);
				}
			}else{
				LogUtil.log.info("###recieveFromIm,server not isConnected");
			}
			
		}else{
			LogUtil.log.info("###recieveFromIm,server not null");
		}
		
		return str;
	}

	public static void close(Socket server)  throws Exception{
		 try {
			 if(server!=null||server.isConnected()){
				// server.getInputStream().close();
				 server.close();
			 }
		} catch (IOException e) {
			LogUtil.log.error(e.getMessage() ,e);
			throw e;
		}
	}
}
