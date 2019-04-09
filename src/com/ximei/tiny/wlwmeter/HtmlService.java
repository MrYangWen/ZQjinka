package com.ximei.tiny.wlwmeter;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlService {
	
	
	public static String gethtml(String urlpath) throws Exception{
		
		 URL url = new URL(urlpath);
		 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		 conn.setRequestMethod("GET");
		 conn.setConnectTimeout(6 * 1000);
		 if(conn.getResponseCode()==200){
			   InputStream inputStream=conn.getInputStream();
			   byte[] data = StreamTool.readStream(inputStream);
			   
			   return new String(data);
			  }
		
		
		
		
		return null;
	}

}
