package com.ximei.tiny.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * 生成ximeitable表的数据库类
 * 数据库名和传递的表名相同
 * 
 */


public class BDhelper extends SQLiteOpenHelper{
	private static final int VERSION = 1;

	

	public BDhelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public BDhelper(Context context,String name){
		this(context,name,VERSION);
	}
	public BDhelper(Context context,String name,int version){
		this(context, name,null,version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL("create table ximeitable(qbbh varchar(20),cbjlid varchar(20),dzms varchar(20),qbztbh varchar(20),yhxm varchar(20),qbql varchar(20),state varchar(20),flag varchar(20),jzqflag varchar(20))");
		
	}
	
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}