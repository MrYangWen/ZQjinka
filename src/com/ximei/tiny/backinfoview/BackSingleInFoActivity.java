package com.ximei.tiny.backinfoview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.tiny.gasxm.R;
import com.ximei.tiny.tools.FileOpertion;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.ToInverted;
import com.ximei.tiny.tools.TypeConvert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/*
 * 单个抄表信息显示activity
 * 
 * 只是显示抄表信息
 * 
 */
public class BackSingleInFoActivity extends Activity {
	private int chongzhi;
	private String info1 = "";
	private String info10 = "";
	private String info11 = "";
	private String info12 = "";
	private String info13 = "";
	private String info14 = "";
	private String info15 = "";
	private String info16 = "";
	private String info17 = "";
	private String info18 = "";
	private String info2 = "";
	private String info3 = "";
	private String info4 = "";
	private String info5 = "";
	private String info6 = "";
	private String info7 = "";
	private String info8 = "";
	private String info9 = "";
	private int jidian;
	private int leiji;
	private long shenyuqiliang;
	private ToInverted toinver;
	private TextView value10info;
	private TextView value10name;
	private TextView value11info;
	private TextView value11name;
	private TextView value12info;
	private TextView value12name;
	private TextView value1info;
	private TextView value1name;
	private TextView value2info;
	private TextView value2name;
	private TextView value3info;
	private TextView value3name;
	private TextView value4info;
	private TextView value4name;
	private TextView value5info;
	private TextView value5name;
	private TextView value6info;
	private TextView value6name;
	private TextView value7info;
	private TextView value7name;
	private TextView value8info;
	private TextView value8name;
	private TextView value9info;
	private TextView value9name;
	private TextView value13info;
	private TextView value13name;
	private TextView value14info;
	private TextView value14name;
	private TextView value15info;
	private TextView value15name;
	private TextView value16info;
	private TextView value16name;
	private TextView value17info;
	private TextView value17name;
	private TextView value18info;
	private TextView value18name;
	private TextView value19info;
	private TextView value19name;
	private TextView value20info;
	private TextView value20name;
	private TextView value21info;
	private TextView value21name;
	private TextView value22info;
	private TextView value22name;
	private TextView value23info;
	private TextView value23name;
	private TextView value24info;
	private TextView value24name;
	private TextView value25info;
	private TextView value25name;
	private TextView value26info;
	private TextView value26name;
	private TextView value27info;
	private TextView value27name;
	private TextView value28info;
	private TextView value28name;	
	private TextView value29info;
	private TextView value29name;		
	private int zhuangtai;
	private String zhuangtaiinfo = "";
	GetmsgID getmsg;
    Intent intent ;
	FileOpertion fileopertion=new FileOpertion();
	@SuppressWarnings("unused")
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.resvalue);
		getmsg = new GetmsgID();
		intent=getIntent();
		this.toinver = new ToInverted();
		value1name = (TextView) findViewById(R.id.value1name);
		value1info = (TextView) findViewById(R.id.value1info);
		value2name = (TextView) findViewById(R.id.value2name);
		value2info = (TextView) findViewById(R.id.value2info);
		value3name = (TextView) findViewById(R.id.value3name);
		value3info = (TextView) findViewById(R.id.value3info);
		value4name = (TextView) findViewById(R.id.value4name);
		value4info = (TextView) findViewById(R.id.value4info);
		value5name = (TextView) findViewById(R.id.value5name);
		value5info = (TextView) findViewById(R.id.value5info);
		value6name = (TextView) findViewById(R.id.value6name);
		value6info = (TextView) findViewById(R.id.value6info);
		value7name = (TextView) findViewById(R.id.value7name);
		value7info = (TextView) findViewById(R.id.value7info);
		value8name = (TextView) findViewById(R.id.value8name);
		value8info = (TextView) findViewById(R.id.value8info);
		value9name = (TextView) findViewById(R.id.value9name);
		value9info = (TextView) findViewById(R.id.value9info);

		value10name = (TextView) findViewById(R.id.value10name);
		value10info = (TextView) findViewById(R.id.value10info);
		value11name = ((TextView) findViewById(R.id.value11name));
		value11info = ((TextView) findViewById(R.id.value11info));
		value12name = ((TextView) findViewById(R.id.value12name));
		this.value12info = ((TextView) findViewById(R.id.value12info));

		value13name = (TextView) findViewById(R.id.value13name);
		value13info = (TextView) findViewById(R.id.value13info);
		value14name = (TextView) findViewById(R.id.value14name);
		value14info = (TextView) findViewById(R.id.value14info);
		value15name = (TextView) findViewById(R.id.value15name);
		value15info = (TextView) findViewById(R.id.value15info);
		value16name = (TextView) findViewById(R.id.value16name);
		value16info = (TextView) findViewById(R.id.value16info);
		value17name = (TextView) findViewById(R.id.value17name);
		value17info = (TextView) findViewById(R.id.value17info);
		value18name = (TextView) findViewById(R.id.value18name);
		value18info = (TextView) findViewById(R.id.value18info);
		value19name = (TextView) findViewById(R.id.value19name);
		value19info = (TextView) findViewById(R.id.value19info);
		value20name = (TextView) findViewById(R.id.value20name);
		value20info = (TextView) findViewById(R.id.value20info);
		value21name = (TextView) findViewById(R.id.value21name);
		value21info = (TextView) findViewById(R.id.value21info);
		value22name = (TextView) findViewById(R.id.value22name);
		value22info = (TextView) findViewById(R.id.value22info);
		value23name = (TextView) findViewById(R.id.value23name);
		value23info = (TextView) findViewById(R.id.value23info);
		value24name = (TextView) findViewById(R.id.value24name);
		value24info = (TextView) findViewById(R.id.value24info);
		value25name = (TextView) findViewById(R.id.value25name);
		value25info = (TextView) findViewById(R.id.value25info);
		value26name = (TextView) findViewById(R.id.value26name);
		value26info = (TextView) findViewById(R.id.value26info);
		value27name = (TextView) findViewById(R.id.value27name);
		value27info = (TextView) findViewById(R.id.value27info);
		value28name = (TextView) findViewById(R.id.value28name);
		value28info = (TextView) findViewById(R.id.value28info);
		value29name = (TextView) findViewById(R.id.value29name);
		value29info = (TextView) findViewById(R.id.value29info);		
		String msg =  intent.getStringExtra("resmsg");//获取返回数据
		String SendOrder=intent.getStringExtra("sendorder");//获取值域标签
		if(msg == null) {
			int oknum = intent.getIntExtra("oknum", 0);
			int count = intent.getIntExtra("count", 0);
			value8name.setText("通讯失败");
			//value8info.setText(oknum+"/"+count);
			return;
		}
		String qbbh = msg.substring(16, 30);//获取表号
		if (msg.length() == 0) {
			this.value1name.setText("区域代码错误，");
			this.value1info.setText("操作失败");
		}
		try{
			//抄表
			if(SendOrder.equals("06")) {
				String datamsg = msg.substring(36, msg.length()-6);//获取数据域
				String state = datamsg.substring(0, 6);//状态字
				String power = datamsg.substring(6, 8);//电池电压
				       power = TypeConvert.hexString2Int(power)+"";//转换成int
				String reading = datamsg.substring(10, 18);//基表读数
					   reading = TypeConvert.hexString2Int(reading)+"";//转换成int
				String used = datamsg.substring(18, 26);// 总用气量/金额
				String buynum = datamsg.substring(26, 34);//总购气量/金额
				String amount = datamsg.substring(34, 38);//当期用量
				String unit = datamsg.substring(38, 42);//当期单价
				String allused = datamsg.substring(42, 46);//总用/总购系数位
				String highestused = datamsg.substring(46, 50);//当期用量高位
				String higheststate = datamsg.substring(50, 54);//状态字高位
				String st1 = TypeConvert.hexStrTo2Str(state.substring(0, 2));
				String st2 = TypeConvert.hexStrTo2Str(state.substring(2, 4));
				String st3 = TypeConvert.hexStrTo2Str(state.substring(4, 6));
				
				int oknum = intent.getIntExtra("oknum", 0);
				int count = intent.getIntExtra("count", 0);
				
				value1name.setText("表号:");
    			value1info.setText(qbbh);
    			
    			value2name.setText("阀门状态:");
    			String valvestate = "";
    			if(st1.substring(7, 8).equals("0")) {
    				valvestate+="开 ";
    			}else if(st1.substring(7, 8).equals("1")) {
    				valvestate+="关 ";
    			}
    			if(st1.substring(6, 7).equals("1")) {
    				valvestate+="故障 ";
    			}
    			if(st2.substring(5, 6).equals("1")) {
    				valvestate+="强制关阀 ";
    			}
    			if(st2.substring(6, 7).equals("1")) {
    				valvestate+="强磁干扰 ";
    			}
    			
    			value2info.setText(valvestate);

    			value3name.setText("电池电压:");
    			String powerstate = (Float.parseFloat(power)/10)+"V ";
    			String v1 = st1.substring(5, 6);
				String v2 = st3.substring(2, 3);
				if(v1.equals("0") && v2.equals("0")) {
					powerstate+="电压正常";
				}else if(v1.equals("0") && v2.equals("1")) {
					powerstate+="电量不足";
				}else if(v1.equals("1") && v2.equals("0")) {
					powerstate+="久压";
				}else if(v1.equals("1") && v2.equals("1")) {
					powerstate+="拔电池";
				}
    			value3info.setText(powerstate);
    			
    			value4name.setText("基表读数:");
    			value4info.setText(Float.parseFloat(reading)/10+"m³");
    			
    			value5name.setText("状态字1:");
    			value5info.setText(st1);
    			
    			value6name.setText("状态字2:");
    			value6info.setText(st2);
    			
    			value7name.setText("状态字3:");
    			value7info.setText(st3);
    			if(st3.substring(4, 5).equals("1")) {
    				value7name.setText("传感器:");
        			value7info.setText("坏");
    			}
    			value8name.setText("抄表成功次数:");
    			value8info.setText(oknum+"/"+count);
			}
			//开阀
			if(SendOrder.equals("00")) {
				value1name.setText("表号:");
    			value1info.setText(qbbh);
    			value2name.setText("开阀:");
    			value2info.setText("指令成功");
			}
			//关阀
			if(SendOrder.equals("01")) {
				value1name.setText("表号:");
    			value1info.setText(qbbh);
    			value2name.setText("关阀:");
    			value2info.setText("指令成功");
			}
			//写RTC
			if(SendOrder.equals("02")) {
				value1name.setText("表号:");
    			value1info.setText(qbbh);
    			value2name.setText("写RTC:");
    			value2info.setText("指令成功");
			}
			//读RTC
			if(SendOrder.equals("03")) {
				String datamsg = msg.substring(36, msg.length()-6);//获取数据域
				String datetime = datamsg.substring(0, 12);//日期时间
				value1name.setText("表号:");
    			value1info.setText(qbbh);
    			value2name.setText("读RTC:");
    			value2info.setText(datetime);
			}
			//读历史记录
			if(SendOrder.equals("07") || SendOrder.equals("08")) {
				String datamsg = msg.substring(42, msg.length()-6);//获取数据域
				int dlen = datamsg.length()/16;
				String numdata = msg.substring(36, 42);
				value1name.setText("表号:");
    			value1info.setText(qbbh);//+"   序号+条数："+numdata
    			String[] dataq = new String[dlen];
    			for(int i=0,j=0;i<datamsg.length();i+=16,j++) {
    				dataq[j]=datamsg.substring(i, i+16);
				}
    			value2name.setText("记录:");
				value2info.setText(dataq[0].substring(4, 6)+"年"+dataq[0].substring(6, 8)+"月"+dataq[0].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[0].substring(10))+"")/10+"m³");
				if(dlen>1) {
					value3name.setText("记录:");
    				value3info.setText(dataq[1].substring(4, 6)+"年"+dataq[1].substring(6, 8)+"月"+dataq[1].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[1].substring(10))+"")/10+"m³");
				}
				if(dlen>2) {
					value4name.setText("记录:");
    				value4info.setText(dataq[2].substring(4, 6)+"年"+dataq[2].substring(6, 8)+"月"+dataq[2].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[2].substring(10))+"")/10+"m³");
				}
				if(dlen>3) {
					value5name.setText("记录:");
    				value5info.setText(dataq[3].substring(4, 6)+"年"+dataq[3].substring(6, 8)+"月"+dataq[3].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[3].substring(10))+"")/10+"m³");
				}
				if(dlen>4) {
					value6name.setText("记录:");
    				value6info.setText(dataq[4].substring(4, 6)+"年"+dataq[4].substring(6, 8)+"月"+dataq[4].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[4].substring(10))+"")/10+"m³");
    			}
				if(dlen>5) {
					value7name.setText("记录:");
    				value7info.setText(dataq[5].substring(4, 6)+"年"+dataq[5].substring(6, 8)+"月"+dataq[5].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[5].substring(10))+"")/10+"m³");
    			}
				if(dlen>6) {
					value8name.setText("记录:");
    				value8info.setText(dataq[6].substring(4, 6)+"年"+dataq[6].substring(6, 8)+"月"+dataq[6].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[6].substring(10))+"")/10+"m³");
    			}
				if(dlen>7) {
					value9name.setText("记录:");
    				value9info.setText(dataq[7].substring(4, 6)+"年"+dataq[7].substring(6, 8)+"月"+dataq[7].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[7].substring(10))+"")/10+"m³");
    			}
				if(dlen>8) {
					value10name.setText("记录:");
    				value10info.setText(dataq[8].substring(4, 6)+"年"+dataq[8].substring(6, 8)+"月"+dataq[8].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[8].substring(10))+"")/10+"m³");
    			}
				if(dlen>9) {
					value11name.setText("记录:");
    				value11info.setText(dataq[9].substring(4, 6)+"年"+dataq[9].substring(6, 8)+"月"+dataq[9].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[9].substring(10))+"")/10+"m³");
    			}
				if(dlen>10) {
					value12name.setText("记录:");
    				value12info.setText(dataq[10].substring(4, 6)+"年"+dataq[10].substring(6, 8)+"月"+dataq[10].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[10].substring(10))+"")/10+"m³");
    			}
				if(dlen>11) {
					value12name.setText("记录:");
    				value12info.setText(dataq[11].substring(4, 6)+"年"+dataq[11].substring(6, 8)+"月"+dataq[11].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[11].substring(10))+"")/10+"m³");
    			}
				if(dlen>12) {
					value12name.setText("记录:");
    				value12info.setText(dataq[12].substring(4, 6)+"年"+dataq[12].substring(6, 8)+"月"+dataq[12].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[12].substring(10))+"")/10+"m³");
    			}
				if(dlen>13) {
					value12name.setText("记录:");
    				value12info.setText(dataq[13].substring(4, 6)+"年"+dataq[13].substring(6, 8)+"月"+dataq[13].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[13].substring(10))+"")/10+"m³");
    			}
				if(dlen>14) {
					value12name.setText("记录:");
    				value12info.setText(dataq[14].substring(4, 6)+"年"+dataq[14].substring(6, 8)+"月"+dataq[14].substring(8, 10)+"日      "+Float.parseFloat(TypeConvert.hexString2Int(dataq[14].substring(10))+"")/10+"m³");
    			}
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			/*
		if (msg.length() == 12) {
			long l2 = Long.parseLong(this.toinver.toinverted(msg
					.substring(0, 6)));
			long l3 = Long.parseLong(this.toinver.toinverted(msg.substring(8,
					12)));
			this.value1name.setText("用户ID:");
			this.value1info.setText(String.valueOf(l2));
			this.value2name.setText("当月输差:");
			this.value2info.setText(String.valueOf(l3));
		}
		if (msg.length() == 20) {
			long l1 = Long.parseLong(
					this.toinver.toinverted(msg.substring(4, 8)), 16);
			int i30 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(8, 12)), 16);
			int i31 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(12, 16)), 16);
			int i32 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(16, 20)), 16);
			this.value2name.setText("表内最大存量:");
			this.value2info.setText(Long.toString(l1));
			this.value3name.setText("表内关阀值:");
			this.value3info.setText(Integer.toString(i30));
			this.value4name.setText("过流保护值:");
			this.value4info.setText(Integer.toString(i31));
			this.value5name.setText("表内透支额度:");
			this.value5info.setText(Integer.toString(i32));
		}
		// 返回气量计费没有输差统计
		if (msg.length() == 32) {

			String leijistr = toinver.toinverted(msg.substring(0, 8));
			leiji = Integer.parseInt(leijistr, 16);

			String shenyuqiliangstr = toinver.toinverted(msg.substring(8, 16));
			shenyuqiliang = Long.parseLong(shenyuqiliangstr, 16);

			if (shenyuqiliang > 2147483647) {
				shenyuqiliang = shenyuqiliang - Long.parseLong("FFFFFFFF", 16);
			}
			// int shenyuqiliang = Integer.parseInt(shenyuqiliangstr,16);

			String jidianstr = toinver.toinverted(msg.substring(16, 24));
			jidian = Integer.parseInt(jidianstr, 16);

			String zhuangtaistr = toinver.toinverted(msg.substring(24, 28));
			zhuangtai = Integer.parseInt(zhuangtaistr, 16);

			String chongzhistr = toinver.toinverted(msg.substring(28, 32));
			chongzhi = Integer.parseInt(chongzhistr, 16);

			value1name.setText("累计气量:");
			value1info.setText(Float.toString((float) leiji / 10));

			value2name.setText("剩余气量:");
			value2info.setText(Double.toString((double) shenyuqiliang / 10));

			value3name.setText("机电同步气量:");
			value3info.setText(Float.toString((float) jidian / 10));

			value4name.setText("表状态:");

			// if((zhuangtai&0x0001)==0x0001)
			// {
			// info1 = "状态是否变化";
			// }
			// if((zhuangtai&0x0002)==0x0002)
			// {
			// info2 = "RF通信异常.";
			// }
			if ((zhuangtai & 0x0004) == 0x0000) {
				info3 = "机电未同步.";
			}
			if ((zhuangtai & 0x0008) == 0x0008) {
				info4 = "燃气泄漏.";
			}
			if ((zhuangtai & 0x0010) == 0x0010) {
				info5 = "干簧管坏.";
			}
			if ((zhuangtai & 0x0020) == 0x0020) {
				info6 = "强行关阀.";
			}

			// if((zhuangtai&0x0040)==0x0040)
			// {
			// info7 = "阀门关闭.";
			// }
			// else if((zhuangtai&0x0040)==0x0000)
			// {
			// info8 = "阀门开启.";
			// }

			if ((zhuangtai & 0x0080) == 0x0080) {
				info9 = "表未注册.";
			}
			if ((zhuangtai & 0x0100) == 0x0100) {
				info10 = "存储器坏.";
			}
			if ((zhuangtai & 0x0200) == 0x0200) {
				info11 = "电池电压低于4.8V.";
			}
			if ((zhuangtai & 0x0400) == 0x0400) {
				info12 = "电池电压低于4.2.";
			}
			if ((zhuangtai & 0x0800) == 0x0800) {
				info13 = "强磁攻击.";
			}
			if ((zhuangtai & 0x1000) == 0x1000) {
				info14 = "阀门有问题.";
			}
			if ((zhuangtai & 0x2000) == 0x2000) {
				info15 = "死表状态.";
			}
			if ((zhuangtai & 0x4000) == 0x4000) {
				info16 = "阀门漏气.";
			}
			if ((zhuangtai & 0x8000) == 0x8000) {
				info17 = "阀门关闭.";
			} else if ((zhuangtai & 0x8000) == 0x0000) {
				info18 = "阀门开启.";
			}
			zhuangtaiinfo = info2 + info3 + info4 + info5 + info6 + info10
					+ info11 + info12 + info13 + info15 + info16 + info17
					+ info18;
			value4info.setText(zhuangtaiinfo);

			value5name.setText("充值次数:");
			value5info.setText(Integer.toString(chongzhi));

			value6name.setText("电子读数:");
			value6info.setText(Float.toString((float) (leiji + jidian) / 10)
					+ "方");

		}
		// 返回集中器时间信息
		if (msg.length() == 16) {

			String nowtime = msg.substring(0, 12);
			this.value1name.setText("中继器当前时间:");
			this.value1info.setText(nowtime);

			// 当前电池电压

			String batterystr = msg.substring(12, 16);
			double battery = Integer.parseInt(batterystr, 16) / 100.0;
			this.value2name.setText("当前电池电压:");
			this.value2info.setText(String.valueOf(battery));

		}
		// 返回新中继器信息
		if ((msg.length() == 112)&&(msg.substring(14, 22).equals("AA40F903"))) {

			
			value1name.setText("中继器版本号:");
			value1info.setText(String.valueOf(Integer.parseInt(toinver.toinverted(msg.substring(0, 2)), 16)));			
			value2name.setText("中继器本机地址:");
			value2info.setText(String.valueOf(Integer.parseInt(toinver.toinverted(msg.substring(2, 8)), 16)));			
			value3name.setText("中继器上级地址:");
			value3info.setText(String.valueOf(Integer.parseInt(toinver.toinverted(msg.substring(8, 14)), 16)));			
			value4name.setText("表区域码:");
			value4info.setText(String.valueOf(Integer.parseInt(toinver.toinverted(msg.substring(14, 22)), 16)));			
			value5name.setText("PIC-1101快速集抄次数:");
			value5info.setText(String.valueOf(Integer.parseInt((msg.substring(22, 24)), 16)));			
			value6name.setText("PIC-1101普通集抄次数:");
			value6info.setText(String.valueOf(Integer.parseInt((msg.substring(24, 26)), 16)));			
			value7name.setText("PIC-1278快速集抄次数:");
			value7info.setText(String.valueOf(Integer.parseInt((msg.substring(26, 28)), 16)));			
			value8name.setText("PIC-1278普通集抄次数:");
			value8info.setText(String.valueOf(Integer.parseInt((msg.substring(28, 30)), 16)));			
			value9name.setText("MSP-1278快速集抄次数:");
			value9info.setText(String.valueOf(Integer.parseInt((msg.substring(30, 32)), 16)));			
			value10name.setText("MSP-1278普通集抄次数:");
			value10info.setText(String.valueOf(Integer.parseInt((msg.substring(32, 34)), 16)));						
			value12name.setText("中继集抄时间:");
			value12info.setText(msg.substring(36, 46));			
			value13name.setText("集抄频度:");
			value13info.setText(msg.substring(46, 48));			
			value14name.setText("PIC-1101集抄硬件状态:");
			value14info.setText(String.valueOf(Integer.parseInt((msg.substring(48, 50)), 16)));			
			value15name.setText("PIC-1101集抄后中继电池电压:");
			value15info.setText(String.valueOf(Integer.parseInt((msg.substring(50, 54)), 16)));			
			value16name.setText("PIC-1101集抄数量:");
			value16info.setText(String.valueOf(Integer.parseInt((msg.substring(54, 58)), 16)));			
			value17name.setText("PIC-1101集抄成功数量:");
			value17info.setText(String.valueOf(Integer.parseInt((msg.substring(58, 62)), 16)));			
			value18name.setText("PIC-1278集抄硬件状态:");
			value18info.setText(String.valueOf(Integer.parseInt((msg.substring(62, 64)), 16)));			
			value19name.setText("PIC-1278集抄后中继电池电压:");
			value19info.setText(String.valueOf(Integer.parseInt((msg.substring(64, 68)), 16)));			
			value20name.setText("PIC-1278集抄数量:");
			value20info.setText(String.valueOf(Integer.parseInt((msg.substring(68, 72)), 16)));			
			value21name.setText("PIC-1278集抄成功数量:");
			value21info.setText(String.valueOf(Integer.parseInt((msg.substring(72, 76)), 16)));			
			value22name.setText("MSP-1278集抄硬件状态:");
			value22info.setText(String.valueOf(Integer.parseInt((msg.substring(76, 78)), 16)));			
			value23name.setText("MSP-1278集抄后中继电池电压:");
			value23info.setText(String.valueOf(Integer.parseInt((msg.substring(78, 82)), 16)));			
			value24name.setText("MSP-1278集抄数量:");
			value24info.setText(String.valueOf(Integer.parseInt((msg.substring(82, 86)), 16)));			
			value25name.setText("MSP-1278集抄成功数量:");
			value25info.setText(String.valueOf(Integer.parseInt((msg.substring(86, 90)), 16)));
			
			value26name.setText("PIC-1101表总数:");
			value26info.setText(String.valueOf(Integer.parseInt((msg.substring(96, 100)), 16)));			
			value27name.setText("PIC-1278表总数:");
			value27info.setText(String.valueOf(Integer.parseInt((msg.substring(100, 104)), 16)));			
			value28name.setText("MSP-1278表总数:");
			value28info.setText(String.valueOf(Integer.parseInt((msg.substring(104, 108)), 16)));		
			value29name.setText("当前电池电压:");
			value29info.setText(String.valueOf(Integer.parseInt((msg.substring(108, 112)), 16)));			

		}else if((msg.length()==112)){
			
			// 返回气表表号
			String qbbhhex = msg.substring(8, 14);
			String qbbhstr = String.valueOf(Integer.parseInt(
					getmsg.getMsgID(qbbhhex), 16));
			Log.e("test", "表号"+qbbhstr);
			
			
			//返回抄表日期
			String cbdate=msg.substring(14, 20);
			Log.e("test", cbdate);
			
			//返回抄表时间
			String cbtime=msg.substring(22, 28);
			Log.e("test", cbtime);
			
			// 返回累计气量
			int leiji = Integer.parseInt(toinver.toinverted(msg.substring(28, 36)), 16);
			Log.e("test", "累计气量"+String.valueOf(leiji));
			// 返回剩余气量
			long shengyu = Long.parseLong(toinver.toinverted(msg.substring(36, 44)), 16);
			if (shengyu > 0x7fffffffL) {
				shengyu = shengyu - Long.parseLong("FFFFFFFF", 16);
			}
		
			// 返回机电同步气量
			int jidian = Integer.parseInt(toinver.toinverted(msg.substring(44, 52)), 16);
			Log.e("test", "机电同步气量"+String.valueOf(jidian));
			// 返回气表状态
			int state = Integer.parseInt(toinver.toinverted(msg.substring(52, 56)), 16);
			// 返回充值次数
			int czcs =Integer.parseInt(toinver.toinverted(msg.substring(56, 60)), 16);
			// 得到最后抄表数据写入数据库
			String qbql = Float.toString((float) (leiji + jidian) / 10F);
			Log.e("test", "气量"+qbql);			
			value1name.setText("气表表号：");
			value1info.setText(qbbhstr);
			value2name.setText("气表气量：");
			value2info.setText(qbql);
			
			
		}
		
		// 返回新集中器信息
		if (msg.length() == 76) {

			String bjadd = String.valueOf(Integer.parseInt(
					toinver.toinverted(msg.substring(2, 8)), 16));
			this.value1name.setText("集中器本机地址:");
			this.value1info.setText(bjadd);
			String sjadd = String.valueOf(Integer.parseInt(
					toinver.toinverted(msg.substring(8, 14)), 16));
			this.value2name.setText("集中器上级地址:");
			this.value2info.setText(sjadd);
			String xjadd = String.valueOf(Integer.parseInt(
					toinver.toinverted(msg.substring(14, 20)), 16));
			this.value3name.setText("集中器下级地址:");
			this.value3info.setText(xjadd);
			String cbtime = msg.substring(20, 32);
			this.value3name.setText("集抄时间:");
			this.value3info.setText(cbtime);

			String errorflag1101 = msg.substring(32, 34);
			Log.e("test", "错误" + errorflag1101);
			this.value11name.setText("1101错误信息:");
			this.value11info.setText(errorflag1101);

			String ccallnub = String.valueOf(Integer.parseInt(msg.substring(38, 42), 16));
			if (ccallnub.equals("65535")) {
				ccallnub = "0";
			}
			this.value4name.setText("CC1101集抄数量:");
			this.value4info.setText(ccallnub);

			String ccjc = String.valueOf(Integer.parseInt(msg.substring(42, 46), 16));
			if (ccjc.equals("65535")) {
				ccjc = "0";
			}
			this.value5name.setText("CC1101集抄成功数量:");
			this.value5info.setText(ccjc);

			String errorflag1278 = msg.substring(46, 48);
			Log.e("test", "错误" + errorflag1278);
			this.value12name.setText("1278错误信息:");
			this.value12info.setText(errorflag1278);

			String sxallnub = String.valueOf(Integer.parseInt(msg.substring(52, 56), 16));
			if (sxallnub.equals("65535")) {
				sxallnub = "0";
			}
			this.value6name.setText("SX1278集抄数量:");
			this.value6info.setText(sxallnub);

			String sxjc = String.valueOf(Integer.parseInt(
					msg.substring(56, 60), 16));
			if (sxjc.equals("65535")) {
				sxjc = "0";
			}
			this.value7name.setText("SX1278集抄成功数量:");
			this.value7info.setText(sxjc);

			String cctotal = String.valueOf(Integer.parseInt(
					msg.substring(64, 68), 16));
			this.value8name.setText("CC1101表总数:");
			this.value8info.setText(cctotal);

			String sxtotal = String.valueOf(Integer.parseInt(
					msg.substring(68, 72), 16));
			this.value9name.setText("SX1278表总数:");
			this.value9info.setText(sxtotal);

			// 当前电池电压

			String batterystr = msg.substring(72, 76);
			double battery = Integer.parseInt(batterystr, 16) / 100.0;
			this.value10name.setText("当前电池电压:");
			this.value10info.setText(String.valueOf(battery));

		}

		// 返回抄表测试数据
		if ((msg.length() == 56)&&((SendOrder!=null)&&SendOrder.equals("0C"))) {
			this.value1name.setText("抄表成功日期:");
			this.value1info.setText(msg.substring(6, 12)+msg.substring(14, 20));
			
			String leijistr = toinver.toinverted(msg.substring(20, 28));
			leiji = Integer.parseInt(leijistr, 16);
			this.value2name.setText("累计气量:");
			this.value2info.setText(Float.toString(this.leiji / 10.0F));	
			
			this.shenyuqiliang = Long.parseLong(this.toinver.toinverted(msg.substring(28, 36)), 16);
			if (shenyuqiliang > 2147483647)
				shenyuqiliang = shenyuqiliang - Long.parseLong("FFFFFFFF", 16)- 1;
			this.value3name.setText("剩余气量:");
			this.value3info.setText(Double.toString(this.shenyuqiliang / 10.0D));
			
			this.jidian = Integer.parseInt(this.toinver.toinverted(msg.substring(36, 44)), 16);
			this.value4name.setText("机电同步气量:");
			this.value4info.setText(Float.toString(this.jidian / 10.0F));	
			
			this.zhuangtai = Integer.parseInt(this.toinver.toinverted(msg.substring(44, 48)), 16);
			if ((0x4 & this.zhuangtai) == 0)
				this.info3 = "机电未同步.";
			if ((0x8 & this.zhuangtai) == 8)
				this.info4 = "燃气泄漏.";
			if ((0x10 & this.zhuangtai) == 16)
				this.info5 = "干簧管坏.";
			if ((0x20 & this.zhuangtai) == 32)
				this.info6 = "强行关阀.";
			if ((0x80 & this.zhuangtai) == 128)
				this.info9 = "表未注册.";
			if ((0x100 & this.zhuangtai) == 256)
				this.info10 = "存储器坏.";
			if ((0x200 & this.zhuangtai) == 512)
				this.info11 = "电池电压低于4.8V.";
			if ((0x400 & this.zhuangtai) == 1024)
				this.info12 = "电池电压低于4.2.";
			if ((0x800 & this.zhuangtai) == 2048)
				this.info13 = "强磁攻击.";
			if ((0x1000 & this.zhuangtai) == 4096)
				this.info14 = "阀门有问题.";
			if ((0x2000 & this.zhuangtai) == 8192)
				this.info15 = "死表状态.";
			if ((0x4000 & this.zhuangtai) == 16384)
				this.info16 = "阀门漏气.";
			if ((0x8000 & this.zhuangtai) == 32768)
				this.info17 = "阀门关闭.";
			if ((0x8000 & this.zhuangtai) == 0)
				this.info18 = "阀门开启.";
			this.zhuangtaiinfo = (this.info2 + this.info3 + this.info4
					+ this.info5 + this.info6 + this.info10 + this.info11
					+ this.info12 + this.info13 + this.info15 + this.info16
					+ this.info17 + this.info18);
			this.value5name.setText("表状态:");
			this.value5info.setText(this.zhuangtaiinfo);
			this.chongzhi = Integer.parseInt(this.toinver.toinverted(msg.substring(48, 52)), 16);
			this.value6name.setText("充值次数:");
			this.value6info.setText(Integer.toString(this.chongzhi));
			this.value7name.setText("电子读数:");
			this.value7info.setText((leiji+jidian)/10.0F+"");			
			//this.value8name.setText("中继器电池电压:");
			//this.value8info.setText(msg.substring(52, 56));	
		}
		if ((msg.length() == 56)&&(SendOrder.equals("6D"))) {
			this.leiji = Integer.parseInt(
					this.toinver.toinverted(msg.substring(0, 8)), 16);
			this.shenyuqiliang = Long.parseLong(
					this.toinver.toinverted(msg.substring(8, 16)), 16);
			if (shenyuqiliang > 2147483647)
				shenyuqiliang = shenyuqiliang - Long.parseLong("FFFFFFFF", 16)- 1;
			this.jidian = Integer.parseInt(
					this.toinver.toinverted(msg.substring(16, 24)), 16);
			this.zhuangtai = Integer.parseInt(
					this.toinver.toinverted(msg.substring(24, 28)), 16);
			this.chongzhi = Integer.parseInt(
					this.toinver.toinverted(msg.substring(28, 32)), 16);
			int i24 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(32, 40)), 16);
			int i25 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(40, 48)), 16);
			int i26 = Integer.parseInt(msg.substring(48, 50), 16);
			int i27 = Integer.parseInt(msg.substring(52, 54), 16);
			int i28 = Integer.parseInt(msg.substring(50, 52), 16);
			int i29 = Integer.parseInt(msg.substring(54, 56), 16);
			this.value1name.setText("累计气量:");
			this.value1info.setText(Float.toString(this.leiji / 10.0F));
			this.value2name.setText("剩余气量:");
			this.value2info.setText(Double.toString(this.shenyuqiliang / 10.0D));
			this.value3name.setText("机电同步气量:");
			this.value3info.setText(Float.toString(this.jidian / 10.0F));
			this.value4name.setText("表状态:");
			if ((0x4 & this.zhuangtai) == 0)
				this.info3 = "机电未同步.";
			if ((0x8 & this.zhuangtai) == 8)
				this.info4 = "燃气泄漏.";
			if ((0x10 & this.zhuangtai) == 16)
				this.info5 = "干簧管坏.";
			if ((0x20 & this.zhuangtai) == 32)
				this.info6 = "强行关阀.";
			if ((0x80 & this.zhuangtai) == 128)
				this.info9 = "表未注册.";
			if ((0x100 & this.zhuangtai) == 256)
				this.info10 = "存储器坏.";
			if ((0x200 & this.zhuangtai) == 512)
				this.info11 = "电池电压低于4.8V.";
			if ((0x400 & this.zhuangtai) == 1024)
				this.info12 = "电池电压低于4.2.";
			if ((0x800 & this.zhuangtai) == 2048)
				this.info13 = "强磁攻击.";
			if ((0x1000 & this.zhuangtai) == 4096)
				this.info14 = "阀门有问题.";
			if ((0x2000 & this.zhuangtai) == 8192)
				this.info15 = "死表状态.";
			if ((0x4000 & this.zhuangtai) == 16384)
				this.info16 = "阀门漏气.";
			if ((0x8000 & this.zhuangtai) == 32768)
				this.info17 = "阀门关闭.";
			if ((0x8000 & this.zhuangtai) == 0)
				this.info18 = "阀门开启.";
			this.zhuangtaiinfo = (this.info2 + this.info3 + this.info4
					+ this.info5 + this.info6 + this.info10 + this.info11
					+ this.info12 + this.info13 + this.info15 + this.info16
					+ this.info17 + this.info18);
			this.value4info.setText(this.zhuangtaiinfo);
			this.value5name.setText("充值次数:");
			this.value5info.setText(Integer.toString(this.chongzhi));
			this.value6name.setText("电子读数:");
			this.value6info.setText(Float
					.toString((this.leiji + this.jidian) / 10.0F) + "方");
			this.value8name.setText(Integer.toString(i26) + "年"
					+ Integer.toString(i28) + "月" + "为:");
			this.value8info.setText(Float.toString(i24 / 10.0F) + "方");
			this.value9name.setText(Integer.toString(i27) + "年"
					+ Integer.toString(i29) + "月" + "为:");
			this.value9info.setText(Float.toString(i25 / 10.0F) + "方");
		}
		
		// 解析返回GPM表停用
		if (msg.length() == 46) {

			String leijistr = toinver.toinverted(msg.substring(0, 8));
			leiji = Integer.parseInt(leijistr, 16);
			String shenyuqiliangstr = toinver.toinverted(msg.substring(8, 16));
			shenyuqiliang = Long.parseLong(shenyuqiliangstr, 16);

			if (shenyuqiliang > 2147483647) {
				shenyuqiliang = shenyuqiliang - Long.parseLong("FFFFFFFF", 16)
						- 1;
			}
			String jidianstr = toinver.toinverted(msg.substring(16, 24));
			jidian = Integer.parseInt(jidianstr, 16);

			String zhuangtaistr = toinver.toinverted(msg.substring(24, 28));
			zhuangtai = Integer.parseInt(zhuangtaistr, 16);

			String chongzhistr = toinver.toinverted(msg.substring(28, 32));
			chongzhi = Integer.parseInt(chongzhistr, 16);
			String backgpmqbbh = msg.substring(32, 46);

			// int i24 =
			// Integer.parseInt(this.toinver.toinverted(msg.substring(48, 52)),
			// 16);
			// int i25 =
			// Integer.parseInt(this.toinver.toinverted(msg.substring(52, 56)),
			// 16);
			// int i26 = Integer.parseInt(msg.substring(56, 58), 16);
			// int i27 = Integer.parseInt(msg.substring(58, 60), 16);
			// int i28 = Integer.parseInt(msg.substring(60, 62), 16);
			// int i29 = Integer.parseInt(msg.substring(62, 64), 16);

			value1name.setText("累计气量:");
			value1info.setText(Float.toString((float) leiji / 100) + "方");

			value2name.setText("剩余气量:");
			value2info.setText(Double.toString((double) shenyuqiliang / 100)
					+ "方");

			value3name.setText("总购入气量:");
			value3info.setText(Float.toString((float) jidian) + "方");

			value4name.setText("表状态:");

			if ((zhuangtai & 0x0004) == 0x0004) {
				info3 = "机电未同步.";
			}
			if ((zhuangtai & 0x0008) == 0x0008) {
				info4 = "燃气泄漏.";
			}
			if ((zhuangtai & 0x0010) == 0x0010) {
				info5 = "干簧管坏.";
			}
			if ((zhuangtai & 0x0020) == 0x0020) {
				info6 = "强行关阀.";
			}

			if ((zhuangtai & 0x0080) == 0x0080) {
				info9 = "表未注册.";
			}
			if ((zhuangtai & 0x0100) == 0x0100) {
				info10 = "存储器坏.";
			}
			if ((zhuangtai & 0x0200) == 0x0200) {
				info11 = "电池电压低于4.8V.";
			}
			if ((zhuangtai & 0x0400) == 0x0400) {
				info12 = "电池电压低于4.2.";
			}
			if ((zhuangtai & 0x0800) == 0x0800) {
				info13 = "强磁攻击.";
			}
			if ((zhuangtai & 0x1000) == 0x1000) {
				info14 = "阀门有问题.";
			}
			if ((zhuangtai & 0x2000) == 0x2000) {
				info15 = "死表状态.";
			}
			if ((zhuangtai & 0x4000) == 0x4000) {
				info16 = "阀门漏气.";
			}
			if ((zhuangtai & 0x8000) == 0x8000) {
				info17 = "阀门关闭.";
			}
			if ((zhuangtai & 0x8000) == 0x0000) {
				info18 = "阀门开启.";
			}
			zhuangtaiinfo = info2 + info3 + info4 + info5 + info6 + info10
					+ info11 + info12 + info13 + info15 + info16 + info17
					+ info18;
			value4info.setText(zhuangtaiinfo);

			value5name.setText("充值次数:");
			value5info.setText(Integer.toString(chongzhi) + "次");

			// value6name.setText("电子读数:");
			// value6info.setText(Float.toString((float) (leiji + jidian) / 10)
			// + "方");
			value7name.setText("IC卡号");
			value7info.setText(backgpmqbbh);
			// value8name.setText(Integer.toString(i26) + "年" +
			// Integer.toString(i28) + "月" + "为:");
			// value8info.setText(Float.toString(i24 / 10.0F) + "方");
			// value9name.setText(Integer.toString(i27) + "年" +
			// Integer.toString(i29) + "月" + "为:");
			// value9info.setText(Float.toString(i25 / 10.0F) + "方");

		}

		// 解析返回GPM表
		if (msg.length() == 64) {

			String leijistr = toinver.toinverted(msg.substring(0, 8));
			leiji = Integer.parseInt(leijistr, 16);
			String shenyuqiliangstr = toinver.toinverted(msg.substring(8, 16));
			shenyuqiliang = Long.parseLong(shenyuqiliangstr, 16);

			if (shenyuqiliang > 2147483647) {
				shenyuqiliang = shenyuqiliang - Long.parseLong("FFFFFFFF", 16)
						- 1;
			}
			String jidianstr = toinver.toinverted(msg.substring(16, 24));
			jidian = Integer.parseInt(jidianstr, 16);

			String zhuangtaistr = toinver.toinverted(msg.substring(24, 28));
			zhuangtai = Integer.parseInt(zhuangtaistr, 16);

			String chongzhistr = toinver.toinverted(msg.substring(28, 32));
			chongzhi = Integer.parseInt(chongzhistr, 16);
			String backgpmqbbh = msg.substring(32, 48);

			int i24 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(48, 52)), 16);
			int i25 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(52, 56)), 16);
			int i26 = Integer.parseInt(msg.substring(56, 58), 16);
			int i27 = Integer.parseInt(msg.substring(58, 60), 16);
			int i28 = Integer.parseInt(msg.substring(60, 62), 16);
			int i29 = Integer.parseInt(msg.substring(62, 64), 16);

			value1name.setText("累计气量:");
			value1info.setText(Float.toString((float) leiji / 100) + "方");

			value2name.setText("剩余气量:");
			value2info.setText(Float.toString((float) shenyuqiliang / 100)
					+ "方");

			value3name.setText("总购入气量:");
			value3info.setText(Float.toString((float) jidian) + "方");

			value4name.setText("表状态:");

			if ((zhuangtai & 0x0004) == 0x0004) {
				info3 = "机电未同步.";
			}
			if ((zhuangtai & 0x0008) == 0x0008) {
				info4 = "燃气泄漏.";
			}
			if ((zhuangtai & 0x0010) == 0x0010) {
				info5 = "干簧管坏.";
			}
			if ((zhuangtai & 0x0020) == 0x0020) {
				info6 = "强行关阀.";
			}

			if ((zhuangtai & 0x0080) == 0x0080) {
				info9 = "表未注册.";
			}
			if ((zhuangtai & 0x0100) == 0x0100) {
				info10 = "存储器坏.";
			}
			if ((zhuangtai & 0x0200) == 0x0200) {
				info11 = "电池电压低于4.8V.";
			}
			if ((zhuangtai & 0x0400) == 0x0400) {
				info12 = "电池电压低于4.2.";
			}
			if ((zhuangtai & 0x0800) == 0x0800) {
				info13 = "强磁攻击.";
			}
			if ((zhuangtai & 0x1000) == 0x1000) {
				info14 = "阀门有问题.";
			}
			if ((zhuangtai & 0x2000) == 0x2000) {
				info15 = "死表状态.";
			}
			if ((zhuangtai & 0x4000) == 0x4000) {
				info16 = "阀门漏气.";
			}
			if ((zhuangtai & 0x8000) == 0x8000) {
				info17 = "阀门关闭.";
			}
			if ((zhuangtai & 0x8000) == 0x0000) {
				info18 = "阀门开启.";
			}
			zhuangtaiinfo = info2 + info3 + info4 + info5 + info6 + info10
					+ info11 + info12 + info13 + info15 + info16 + info17
					+ info18;
			value4info.setText(zhuangtaiinfo);

			value5name.setText("充值次数:");
			value5info.setText(Integer.toString(chongzhi) + "次");

			// value6name.setText("电子读数:");
			// value6info.setText(Float.toString((float) (leiji + jidian) / 10)
			// + "方");
			value7name.setText("IC卡号");
			value7info.setText(backgpmqbbh);
			value8name.setText(Integer.toString(i26) + "年"
					+ Integer.toString(i27) + "月" + "为:");
			value8info.setText(Float.toString(i24 / 10.0F) + "方");
			value9name.setText(Integer.toString(i28) + "年"
					+ Integer.toString(i29) + "月" + "为:");
			value9info.setText(Float.toString(i25 / 10.0F) + "方");

		}

		// 返回金额计费2个月输差的
		if (msg.length() == 70) {
			int i14 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(0, 8)), 16);
			int i15 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(8, 16)), 16);
			this.zhuangtai = Integer.parseInt(
					this.toinver.toinverted(msg.substring(16, 20)), 16);
			int i16 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(20, 24)), 16);
			int i17 = Integer.parseInt(msg.substring(24, 26), 16);
			int i18 = Integer.parseInt(msg.substring(28, 30), 16);
			int i19 = Integer.parseInt(msg.substring(26, 28), 16);
			int i20 = Integer.parseInt(msg.substring(30, 32), 16);
			this.leiji = Integer.parseInt(
					this.toinver.toinverted(msg.substring(32, 40)), 16);
			this.jidian = Integer.parseInt(
					this.toinver.toinverted(msg.substring(40, 48)), 16);
			int i21 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(48, 56)), 16);
			int i22 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(56, 64)), 16);
			this.chongzhi = Integer.parseInt(
					this.toinver.toinverted(msg.substring(64, 68)), 16);
			int i23 = Integer.parseInt(
					this.toinver.toinverted(msg.substring(68, 70)), 16);
			this.value1name.setText("剩余气量:");
			this.value1info.setText(Float.toString(i14 / 10.0F) + "方");
			this.value2name.setText("剩余金额:");
			this.value2info.setText(Float.toString(i15 / 1000.0F) + "圆");
			this.value3name.setText("燃气单价:");
			this.value3info.setText(Float.toString(i16 / 100.0F) + "圆/方");
			this.value4name.setText("表状态:");
			if ((0x4 & this.zhuangtai) == 4)
				this.info3 = "机电未同步.";
			if ((0x8 & this.zhuangtai) == 8)
				this.info4 = "燃气泄漏.";
			if ((0x10 & this.zhuangtai) == 16)
				this.info5 = "干簧管坏.";
			if ((0x20 & this.zhuangtai) == 32)
				this.info6 = "强行关阀.";
			if ((0x80 & this.zhuangtai) == 128)
				this.info9 = "表未注册.";
			if ((0x100 & this.zhuangtai) == 256)
				this.info10 = "存储器坏.";
			if ((0x200 & this.zhuangtai) == 512)
				this.info11 = "电池电压低于4.8V.";
			if ((0x400 & this.zhuangtai) == 1024)
				this.info12 = "电池电压低于4.2.";
			if ((0x800 & this.zhuangtai) == 2048)
				this.info13 = "强磁攻击.";
			if ((0x1000 & this.zhuangtai) == 4096)
				this.info14 = "阀门有问题.";
			if ((0x2000 & this.zhuangtai) == 8192)
				this.info15 = "死表状态.";
			if ((0x4000 & this.zhuangtai) == 16384)
				this.info16 = "阀门漏气.";
			if ((0x8000 & this.zhuangtai) == 32768)
				this.info17 = "阀门关闭.";
			if ((0x8000 & this.zhuangtai) == 0)
				this.info18 = "阀门开启.";
			this.zhuangtaiinfo = (this.info2 + this.info3 + this.info4
					+ this.info5 + this.info6 + this.info9 + this.info10
					+ this.info11 + this.info12 + this.info13 + this.info15
					+ this.info16 + this.info17 + this.info18);
			this.value4info.setText(this.zhuangtaiinfo);
			this.value5name.setText("充值次数:");
			this.value5info.setText(Integer.toString(this.chongzhi));
			this.value6name.setText("调价次数:");
			this.value6info.setText(Integer.toString(i23));
			this.value7name.setText("读数:");
			this.value7info.setText(Float
					.toString((this.leiji + this.jidian) / 10.0F) + "方");
			this.value8name.setText(Integer.toString(i17) + "年"
					+ Integer.toString(i19) + "月" + "为:");
			this.value8info.setText(Float.toString(i21 / 10.0F) + "方");
			this.value9name.setText(Integer.toString(i18) + "年"
					+ Integer.toString(i20) + "月" + "为:");
			this.value9info.setText(Float.toString(i22 / 10.0F) + "方");
		}

		// 单抄表返回数据，////////////////////////////////////////////////////////////////////返回6个月输差的
         if((SendOrder!=null)&&(SendOrder.equals("6D"))){   //气量表 7字节表号数据长度42字，3字节表号40字节
    			String leijistr = toinver.toinverted(msg.substring(0, 8));
    			leiji = Integer.parseInt(leijistr, 16);
    			String shenyuqiliangstr = toinver.toinverted(msg.substring(8, 16));
    			shenyuqiliang = Long.parseLong(shenyuqiliangstr, 16);

    			if (shenyuqiliang > 2147483647) {
    				shenyuqiliang = shenyuqiliang - Long.parseLong("FFFFFFFF", 16)- 1;
    			}
    			// int shenyuqiliang = Integer.parseInt(shenyuqiliangstr,16);

    			String jidianstr = toinver.toinverted(msg.substring(16, 24));
    			jidian = Integer.parseInt(jidianstr, 16);

    			String zhuangtaistr = toinver.toinverted(msg.substring(24, 28));
    			zhuangtai = Integer.parseInt(zhuangtaistr, 16);

    			String chongzhistr = toinver.toinverted(msg.substring(28, 32));
    			chongzhi = Integer.parseInt(chongzhistr, 16);
    	
    			// 前6个月的用气总量
    			String sixstr = toinver.toinverted(msg.substring(32, 36));
    			int six = Integer.parseInt(sixstr, 16);
    			// 前5个月的用气总量
    			String fivestr = toinver.toinverted(msg.substring(36, 40));
    			int five = Integer.parseInt(fivestr, 16);
    			// 前4个月的用气总量
    			String fourstr = toinver.toinverted(msg.substring(40, 44));
    			int four = Integer.parseInt(fourstr, 16);

    			// 前3个月的用气总量
    			String threestr = toinver.toinverted(msg.substring(44, 48));
    			int three = Integer.parseInt(threestr, 16);

    			// 前2个月的用气总量
    			String twostr = toinver.toinverted(msg.substring(48, 52));
    			int two = Integer.parseInt(twostr, 16);

    			// 前1个月的用气总量
    			String onestr = toinver.toinverted(msg.substring(52, 56));
    			int one = Integer.parseInt(onestr, 16);

    			// 前6月年份
    			String sixyearstr = toinver.toinverted(msg.substring(56, 58));
    			int sixyear = Integer.parseInt(sixyearstr, 16);
    			// 前6月月份
    			String sixmothstr = toinver.toinverted(msg.substring(58, 60));
    			int sixmoth = Integer.parseInt(sixmothstr, 16);
    			// 前5月年份
    			String fiveyearstr = toinver.toinverted(msg.substring(60, 62));
    			int fiveyear = Integer.parseInt(fiveyearstr, 16);
    			// 前5月月份
    			String fivemothstr = toinver.toinverted(msg.substring(62, 64));
    			int fivemoth = Integer.parseInt(fivemothstr, 16);

    			// 前4年份
    			String fouryearstr = toinver.toinverted(msg.substring(64, 66));
    			int fouryear = Integer.parseInt(fouryearstr, 16);
    			// 前4月份
    			String fourmothstr = toinver.toinverted(msg.substring(66, 68));
    			int fourmoth = Integer.parseInt(fourmothstr, 16);

    			// 前3年份
    			String threeyearstr = toinver.toinverted(msg.substring(68, 70));
    			int threeyear = Integer.parseInt(threeyearstr, 16);
    			// 前3月份
    			String threemothstr = toinver.toinverted(msg.substring(70, 72));
    			int threemoth = Integer.parseInt(threemothstr, 16);

    			// 前2年份
    			String twoyearstr = toinver.toinverted(msg.substring(72, 74));
    			int twoyear = Integer.parseInt(twoyearstr, 16);
    			// 前2月份
    			String twomothstr = toinver.toinverted(msg.substring(74, 76));
    			int twomoth = Integer.parseInt(twomothstr, 16);

    			// 前1年份
    			String oneyearstr = toinver.toinverted(msg.substring(76, 78));
    			int oneyear = Integer.parseInt(oneyearstr, 16);
    			// 前1月份
    			String onemothstr = toinver.toinverted(msg.substring(78, 80));
    			int onemoth = Integer.parseInt(onemothstr, 16);

    			value1name.setText("累计气量:");
    			value1info.setText(Float.toString((float) leiji / 10));

    			value2name.setText("剩余气量:");
    			value2info.setText(Double.toString((double) shenyuqiliang / 10));

    			value3name.setText("机电同步气量:");
    			value3info.setText(Float.toString((float) jidian / 10));

    			value4name.setText("表状态:");
    			String log="单抄："+msg+"\r\n"+"累计气量："+leiji+"\r\n";
    			fileopertion.writeTxtToFile(log);
    			// if((zhuangtai&0x0001)==0x0001)
    			// {
    			// info1 = "状态是否变化";
    			// }
    			// if((zhuangtai&0x0002)==0x0002)
    			// {
    			// info2 = "RF通信异常.";
    			// }
    			if ((zhuangtai & 0x0004) == 0x0000) {
    				info3 = "机电未同步.";
    			}
    			if ((zhuangtai & 0x0008) == 0x0008) {
    				info4 = "燃气泄漏.";
    			}
    			if ((zhuangtai & 0x0010) == 0x0010) {
    				info5 = "干簧管坏.";
    			}
    			if ((zhuangtai & 0x0020) == 0x0020) {
    				info6 = "强行关阀.";
    			}

    			// if((zhuangtai&0x0040)==0x0040)
    			// {
    			// info7 = "阀门关闭.";
    			// }
    			// else if((zhuangtai&0x0040)==0x0000)
    			// {
    			// info8 = "阀门开启.";
    			// }

    			if ((zhuangtai & 0x0080) == 0x0080) {
    				info9 = "表未注册.";
    			}
    			if ((zhuangtai & 0x0100) == 0x0100) {
    				info10 = "存储器坏.";
    			}
    			if ((zhuangtai & 0x0200) == 0x0200) {
    				info11 = "电池电压低于4.8V.";
    			}
    			if ((zhuangtai & 0x0400) == 0x0400) {
    				info12 = "电池电压低于4.2.";
    			}
    			if ((zhuangtai & 0x0800) == 0x0800) {
    				info13 = "强磁攻击.";
    			}
    			if ((zhuangtai & 0x1000) == 0x1000) {
    				info14 = "阀门有问题.";
    			}
    			if ((zhuangtai & 0x2000) == 0x2000) {
    				info15 = "死表状态.";
    			}
    			if ((zhuangtai & 0x4000) == 0x4000) {
    				info16 = "阀门漏气.";
    			}
    			if ((zhuangtai & 0x8000) == 0x8000) {
    				info17 = "阀门关闭.";
    			}
    			if ((zhuangtai & 0x8000) == 0x0000) {
    				info18 = "阀门开启.";
    			}
    			zhuangtaiinfo = info2 + info3 + info4 + info5 + info6 + info10+ info11 + info12 + info13 + info15 + info16 + info17+ info18;
    			value4info.setText(zhuangtaiinfo);
    			value5name.setText("充值次数:");
    			value5info.setText(Integer.toString(chongzhi));
    			value6name.setText("电子读数:");
    			value6info.setText(Float.toString((float) (leiji + jidian) / 10)+"方");
    			if (sixmoth - 1 == 0) {
    				value7name.setText(String.valueOf(sixyear - 1) + "年"+ String.valueOf(12) + "月:");
    				value7info.setText(Float.toString((float) six / 10) + "方");
    			} else {
    				value7name.setText(String.valueOf(sixyear) + "年"+ String.valueOf(sixmoth - 1) + "月:");
    				value7info.setText(Float.toString((float) six / 10) + "方");
    			}
    			if (fivemoth - 1 == 0) {
    				value8name.setText(String.valueOf(fiveyear - 1) + "年"+ String.valueOf(12) + "月:");
    				value8info.setText(Float.toString((float) fiveyear / 10) + "方");
    			} else {
    				value8name.setText(String.valueOf(fiveyear) + "年"+ String.valueOf(fivemoth - 1) + "月:");
    				value8info.setText(Float.toString((float) five / 10) + "方");
    			}
    			if (fourmoth - 1 == 0) {
    				value9name.setText(String.valueOf(fouryear - 1) + "年"+ String.valueOf(12) + "月:");
    				value9info.setText(Float.toString((float) four / 10) + "方");
    			} else {
    				value9name.setText(String.valueOf(fouryear) + "年"+ String.valueOf(fourmoth - 1) + "月:");
    				value9info.setText(Float.toString((float) four / 10) + "方");
    			}
    			if (threemoth - 1 == 0) {
    				value10name.setText(String.valueOf(threeyear - 1) + "年"+ String.valueOf(12) + "月:");
    				value10info.setText(Float.toString((float) three / 10) + "方");
    			} else {
    				value10name.setText(String.valueOf(threeyear) + "年"+ String.valueOf(threemoth - 1) + "月:");
    				value10info.setText(Float.toString((float) three / 10) + "方");
    			}
    			if (twomoth - 1 == 0) {
    				value11name.setText(String.valueOf(twoyear - 1) + "年"+ String.valueOf(12) + "月:");
    				value11info.setText(Float.toString((float) two / 10) + "方");

    			} else {
    				value11name.setText(String.valueOf(twoyear) + "年"+ String.valueOf(twomoth - 1) + "月:");
    				value11info.setText(Float.toString((float) two / 10) + "方");
    			}
    			if (onemoth - 1 == 0) {
    				value11name.setText(String.valueOf(oneyear - 1) + "年"+ String.valueOf(12) + "月:");
    				value11info.setText(Float.toString((float) one / 10) + "方");
    			} else {
    				value12name.setText(String.valueOf(oneyear) + "年"+ String.valueOf(onemoth - 1) + "月:");
    				value12info.setText(Float.toString((float) one / 10) + "方");
    			} 
    			value12name.setText("信号强度值：");
    			value12info.setText("-"+(137-Integer.parseInt(msg.substring(82, 84),16))+"dBm");
        	 	value1name.setText("返回数据:");
 			    value1info.setText(msg);
            	
            }else if((SendOrder!=null)&&SendOrder.equals("6C")){  //金额表
    			String leijistr = toinver.toinverted(msg.substring(0, 8));
    			leiji = Integer.parseInt(leijistr, 16);
    			String shenyuqiliangstr = toinver.toinverted(msg.substring(8, 16));
    			shenyuqiliang = Long.parseLong(shenyuqiliangstr, 16);

    			if (shenyuqiliang > 2147483647) {
    				shenyuqiliang = shenyuqiliang - Long.parseLong("FFFFFFFF", 16)- 1;
    			}
    			// int shenyuqiliang = Integer.parseInt(shenyuqiliangstr,16);
    			long shenyuMoney=Long.parseLong(toinver.toinverted(msg.substring(16, 24)), 16);
    			String jidianstr = toinver.toinverted(msg.substring(16+8, 24+8));
    			jidian = Integer.parseInt(jidianstr, 16);

    			String zhuangtaistr = toinver.toinverted(msg.substring(24+8, 28+8));
    			zhuangtai = Integer.parseInt(zhuangtaistr, 16);

    			String chongzhistr = toinver.toinverted(msg.substring(28+8, 32+8));
    			chongzhi = Integer.parseInt(chongzhistr, 16);
    	
    			// 前6个月的用气总量
    			String sixstr = toinver.toinverted(msg.substring(32+8, 36+8));
    			int six = Integer.parseInt(sixstr, 16);
    			// 前5个月的用气总量
    			String fivestr = toinver.toinverted(msg.substring(36+8, 40+8));
    			int five = Integer.parseInt(fivestr, 16);
    			// 前4个月的用气总量
    			String fourstr = toinver.toinverted(msg.substring(40+8, 44+8));
    			int four = Integer.parseInt(fourstr, 16);

    			// 前3个月的用气总量
    			String threestr = toinver.toinverted(msg.substring(44+8, 48+8));
    			int three = Integer.parseInt(threestr, 16);

    			// 前2个月的用气总量
    			String twostr = toinver.toinverted(msg.substring(48+8, 52+8));
    			int two = Integer.parseInt(twostr, 16);

    			// 前1个月的用气总量
    			String onestr = toinver.toinverted(msg.substring(52+8, 56+8));
    			int one = Integer.parseInt(onestr, 16);

    			// 前6月年份
    			String sixyearstr = toinver.toinverted(msg.substring(56+8, 58+8));
    			int sixyear = Integer.parseInt(sixyearstr, 16);
    			// 前6月月份
    			String sixmothstr = toinver.toinverted(msg.substring(58+8, 60+8));
    			int sixmoth = Integer.parseInt(sixmothstr, 16);
    			// 前5月年份
    			String fiveyearstr = toinver.toinverted(msg.substring(60+8, 62+8));
    			int fiveyear = Integer.parseInt(fiveyearstr, 16);
    			// 前5月月份
    			String fivemothstr = toinver.toinverted(msg.substring(62+8, 64+8));
    			int fivemoth = Integer.parseInt(fivemothstr, 16);

    			// 前4年份
    			String fouryearstr = toinver.toinverted(msg.substring(64+8, 66+8));
    			int fouryear = Integer.parseInt(fouryearstr, 16);
    			// 前4月份
    			String fourmothstr = toinver.toinverted(msg.substring(66+8, 68+8));
    			int fourmoth = Integer.parseInt(fourmothstr, 16);

    			// 前3年份
    			String threeyearstr = toinver.toinverted(msg.substring(68+8, 70+8));
    			int threeyear = Integer.parseInt(threeyearstr, 16);
    			// 前3月份
    			String threemothstr = toinver.toinverted(msg.substring(70+8, 72+8));
    			int threemoth = Integer.parseInt(threemothstr, 16);

    			// 前2年份
    			String twoyearstr = toinver.toinverted(msg.substring(72+8, 74+8));
    			int twoyear = Integer.parseInt(twoyearstr, 16);
    			// 前2月份
    			String twomothstr = toinver.toinverted(msg.substring(74+8, 76+8));
    			int twomoth = Integer.parseInt(twomothstr, 16);

    			// 前1年份
    			String oneyearstr = toinver.toinverted(msg.substring(76+8, 78+8));
    			int oneyear = Integer.parseInt(oneyearstr, 16);
    			// 前1月份
    			String onemothstr = toinver.toinverted(msg.substring(78+8, 80+8));
    			int onemoth = Integer.parseInt(onemothstr, 16);

    			value1name.setText("累计气量:");
    			value1info.setText(Float.toString((float) leiji / 10));

    			value2name.setText("剩余金额//购气金额:");
    			value2info.setText(Double.toString((double) shenyuqiliang / 1000)+"//"+Double.toString((double) shenyuMoney / 1000));

    			value3name.setText("机电同步气量:");
    			value3info.setText(Float.toString((float) jidian / 10));

    			value4name.setText("表状态:");
    			String log="单抄："+msg+"\r\n"+"累计气量："+leiji+"\r\n";
    			fileopertion.writeTxtToFile(log);
    			// if((zhuangtai&0x0001)==0x0001)
    			// {
    			// info1 = "状态是否变化";
    			// }
    			// if((zhuangtai&0x0002)==0x0002)
    			// {
    			// info2 = "RF通信异常.";
    			// }
    			if ((zhuangtai & 0x0004) == 0x0000) {
    				info3 = "机电未同步.";
    			}
    			if ((zhuangtai & 0x0008) == 0x0008) {
    				info4 = "燃气泄漏.";
    			}
    			if ((zhuangtai & 0x0010) == 0x0010) {
    				info5 = "干簧管坏.";
    			}
    			if ((zhuangtai & 0x0020) == 0x0020) {
    				info6 = "强行关阀.";
    			}

    			// if((zhuangtai&0x0040)==0x0040)
    			// {
    			// info7 = "阀门关闭.";
    			// }
    			// else if((zhuangtai&0x0040)==0x0000)
    			// {
    			// info8 = "阀门开启.";
    			// }

    			if ((zhuangtai & 0x0080) == 0x0080) {
    				info9 = "表未注册.";
    			}
    			if ((zhuangtai & 0x0100) == 0x0100) {
    				info10 = "存储器坏.";
    			}
    			if ((zhuangtai & 0x0200) == 0x0200) {
    				info11 = "电池电压低于4.8V.";
    			}
    			if ((zhuangtai & 0x0400) == 0x0400) {
    				info12 = "电池电压低于4.2.";
    			}
    			if ((zhuangtai & 0x0800) == 0x0800) {
    				info13 = "强磁攻击.";
    			}
    			if ((zhuangtai & 0x1000) == 0x1000) {
    				info14 = "阀门有问题.";
    			}
    			if ((zhuangtai & 0x2000) == 0x2000) {
    				info15 = "死表状态.";
    			}
    			if ((zhuangtai & 0x4000) == 0x4000) {
    				info16 = "阀门漏气.";
    			}
    			if ((zhuangtai & 0x8000) == 0x8000) {
    				info17 = "阀门关闭.";
    			}
    			if ((zhuangtai & 0x8000) == 0x0000) {
    				info18 = "阀门开启.";
    			}
    			zhuangtaiinfo = info2 + info3 + info4 + info5 + info6 + info10+ info11 + info12 + info13 + info15 + info16 + info17+ info18;
    			value4info.setText(zhuangtaiinfo);
    			value5name.setText("充值次数:");
    			value5info.setText(Integer.toString(chongzhi));
    			value6name.setText("电子读数:");
    			value6info.setText(Float.toString((float) (leiji + jidian) / 10)+"方");
    			if (sixmoth - 1 == 0) {
    				value7name.setText(String.valueOf(sixyear - 1) + "年"+ String.valueOf(12) + "月:");
    				value7info.setText(Float.toString((float) six / 10) + "方");
    			} else {
    				value7name.setText(String.valueOf(sixyear) + "年"+ String.valueOf(sixmoth - 1) + "月:");
    				value7info.setText(Float.toString((float) six / 10) + "方");
    			}
    			if (fivemoth - 1 == 0) {
    				value8name.setText(String.valueOf(fiveyear - 1) + "年"+ String.valueOf(12) + "月:");
    				value8info.setText(Float.toString((float) fiveyear / 10) + "方");
    			} else {
    				value8name.setText(String.valueOf(fiveyear) + "年"+ String.valueOf(fivemoth - 1) + "月:");
    				value8info.setText(Float.toString((float) five / 10) + "方");
    			}
    			if (fourmoth - 1 == 0) {
    				value9name.setText(String.valueOf(fouryear - 1) + "年"+ String.valueOf(12) + "月:");
    				value9info.setText(Float.toString((float) four / 10) + "方");
    			} else {
    				value9name.setText(String.valueOf(fouryear) + "年"+ String.valueOf(fourmoth - 1) + "月:");
    				value9info.setText(Float.toString((float) four / 10) + "方");
    			}
    			if (threemoth - 1 == 0) {
    				value10name.setText(String.valueOf(threeyear - 1) + "年"+ String.valueOf(12) + "月:");
    				value10info.setText(Float.toString((float) three / 10) + "方");
    			} else {
    				value10name.setText(String.valueOf(threeyear) + "年"+ String.valueOf(threemoth - 1) + "月:");
    				value10info.setText(Float.toString((float) three / 10) + "方");
    			}
    			if (twomoth - 1 == 0) {
    				value11name.setText(String.valueOf(twoyear - 1) + "年"+ String.valueOf(12) + "月:");
    				value11info.setText(Float.toString((float) two / 10) + "方");

    			} else {
    				value11name.setText(String.valueOf(twoyear) + "年"+ String.valueOf(twomoth - 1) + "月:");
    				value11info.setText(Float.toString((float) two / 10) + "方");
    			}
    			if (onemoth - 1 == 0) {
    				value11name.setText(String.valueOf(oneyear - 1) + "年"+ String.valueOf(12) + "月:");
    				value11info.setText(Float.toString((float) one / 10) + "方");
    			} else {
    				value12name.setText(String.valueOf(oneyear) + "年"+ String.valueOf(onemoth - 1) + "月:");
    				value12info.setText(Float.toString((float) one / 10) + "方");
    			}                 	
            }
		*/}catch(Exception e){
			Log.e("BackSingleInFoActivity-1560", e.toString());
			
		}
	}

}
