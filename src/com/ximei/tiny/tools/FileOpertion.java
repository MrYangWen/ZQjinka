package com.ximei.tiny.tools;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;

import android.os.Environment;
import android.util.Log;

public class FileOpertion {
	
	private String filePath;
    private static String fileName;
	public String getFilePath() {
		return filePath;
	}
	public FileOpertion() {
		//得到当前外部存储设备的目录
		filePath = Environment.getExternalStorageDirectory()  + "/"+"西美抄表LOG文件"+"/";
	}
	public String getCurTime(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());	
		return date;
	}
	// 将字符串写入到文本文件中
	public void writeTxtToFile(String strcontent) {
	    //生成文件夹之后，再生成文件，不然会出错
//	    makeFilePath(filePath, fileName);   
//	    String strFilePath = filePath+fileName;
//	    // 每次写入时，都换行写
//	    String strContent = strcontent + "\r\n";
//	    try {
//	        File file = new File(strFilePath);
//	        if (!file.exists()) {
//	            Log.d("TestFile", "Create the file:" + strFilePath);
//	            file.getParentFile().mkdirs();
//	            file.createNewFile();
//	        }
//	        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
//	        raf.seek(file.length());
//	        raf.write(strContent.getBytes());
//	        raf.close();
//	    } catch (Exception e) {
//	        Log.e("TestFile", "Error on write File:" + e);
//	    }
	}
	 
	// 生成文件
	public File makeFilePath(String filePath, String filename) {
	    File file = null;
	    fileName =filename;
	    makeRootDirectory(filePath);
	    try {
	        file = new File(filePath + filename);
	        if (!file.exists()) {
	            file.createNewFile();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return file;
	}
	 
	// 生成文件夹
	public static void makeRootDirectory(String filePath) {
	    File file = null;
	    try {
	        file = new File(filePath);
	        if (!file.exists()) {
	            file.mkdir();
	        }
	    } catch (Exception e) {
	        Log.i("error:", e+"");
	    }
	}

}
