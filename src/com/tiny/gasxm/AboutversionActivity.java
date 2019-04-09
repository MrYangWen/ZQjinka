package com.tiny.gasxm;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class AboutversionActivity extends Activity {
    private TextView TxtVersion;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutversion);
		TxtVersion=(TextView)findViewById(R.id.version);
		TxtVersion.setText("版本号修改记录：\r\n"
				+"V3.2:当前最新中继器版本\r\n"
				+"V4.0:屏蔽快速集抄\r\n"
				+"V4.0:修复南岸区表册第0行地址报错问题\r\n"
				+"V4.0:修改采集数据功能存储时间格式不正确\r\n"
				+"V4.0:增加采集数据LOG\r\n"
				+"V4.0:修改设置表地址自动识别新旧表16-10-27\r\n"
				+"V4.0:修改操作中继器程序崩溃16-10-31\r\n"
				+"V4.0:修改不能打开表册(表册中包含幢关键字)20161215\r\n"
				+"V4.1:增加状态提示，优化普通快速集抄数据在写入数据库前进行CRC校验，增加判断第5位为9做为新表20161216\r\n"
				+"V4.1:修复抄收新旧表返回数据长度不一致导致程序崩溃(新80旧56更旧32)20161219"
				+"V4.1:1、普通集抄间隔由原来的15s修改到现在的9秒20161221\r\n"
				+"V4.1:2、修改发送数据时不再采集电压值20161221\r\n"
				+"V4.2:兼容7位和14位表号20170117\r\n"
				+"V4.2:兼容假14位20170121\r\n"
				+"V4.2修改普通集抄的超时机制20170204\r\n"
				+"V4.2貌似解决偶尔死机问题20170204\r\n"
				+"V4.2解决蓝牙断开还能数据通信（需测试）20170207\r\n"
				+"V4.2解决表册不能打开、报错问题20170215\r\n"
				+"V4.2增加搜索 号 关键字做为楼栋进行搜索20170315\r\n"
				+"V4.2过滤表号中的null20170406\r\n"
				+"V4.2去掉LOG日志,去掉自动读取电压,打开蓝牙连接断开提示框,增加手动读取电压20170526\r\n"
				+"V4.2解决抄表表号和数据库表号不对应问题20170607\r\n"
				+"V4.2增加080、090,100,110,120,010强制为新表20170620\r\n");			
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aboutversion, menu);
		return true;
	}

}
