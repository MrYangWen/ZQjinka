package com.tiny.gasxm;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.tiny.gasxm.LoginActivity.stopappthread;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.service.CaijiService;
import com.ximei.tiny.service.DataBaseService;
import com.ximei.tiny.tools.ExitApplication;
import com.ximei.tiny.tools.FileOpertion;
import com.ximei.tiny.tools.FileUtils;

public class IndexActivity extends Activity {
	
	FileUtils fileutil;
	String  pathsdcard;
	private BluetoothAdapter mBluetoothAdapter; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 取消状态标题栏
		getWindow().setFlags(1024, 1024);
        setContentView(R.layout.index);
       // ExitApplication.getInstance().addActivity(this);
        
        fileutil = new FileUtils();
		pathsdcard = fileutil.getSDPATH();
//        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);  
//		 mBluetoothAdapter = bluetoothManager.getAdapter(); 
//		 if(mBluetoothAdapter!=null)
//			 mBluetoothAdapter.enable();		
		
		new Thread(new welcomethread()).start();
    }
    
    
    public class welcomethread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			try {
				// 如果不存在就创建ximei
				if (!fileutil.isFileExist("ximei")) {
					fileutil.creatSDDir("ximei");
				}
				// 如果不存在就创建newximei
				if (!fileutil.isFileExist("newximei")) {
					fileutil.creatSDDir("newximei");
				}
				// 如果不存在就创建alldata
				if (!fileutil.isFileExist("alldata")) {
					fileutil.creatSDDir("alldata");
				}
				FileOpertion fileopertion=new FileOpertion();   
				fileopertion.makeFilePath(fileopertion.getFilePath(),fileopertion.getCurTime()+".txt");//生成log文件				

				
				Intent intent3 = new Intent();
				intent3.setClass(IndexActivity.this, BtXiMeiService.class);
				stopService(intent3);
				
				Thread.sleep(2000);//启动加载2.5秒后进去主界面
				Intent intent = new Intent();
				//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("startFlag", "OK");
				intent.setClass(IndexActivity.this, MainActivity.class);
				IndexActivity.this.startActivity(intent);
				finish();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
 
    }

   
}
