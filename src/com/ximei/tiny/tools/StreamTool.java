package com.ximei.tiny.tools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {
	
	public static byte[] readStream(InputStream inStream) throws Exception {
		  ByteArrayOutputStream outstream=new ByteArrayOutputStream();
		  byte[] buffer=new byte[1024];
		  int len=-1;
		  while((len=inStream.read(buffer)) !=-1){
		   outstream.write(buffer, 0, len);
		  }
		  outstream.close();
		  inStream.close();
		  
		return outstream.toByteArray();
		}
	
	

}
