package com.ximei.tiny.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/*
 * 生成usertable表的数据库类
 * 
 * 
 */
public class UserBDhelper extends SQLiteOpenHelper
{
  private static final int VERSION = 1;

	public UserBDhelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public UserBDhelper(Context context,String name){
		this(context,name,VERSION);
	}
	public UserBDhelper(Context context,String name,int version){
		this(context, name,null,version);
	}
  public void onCreate(SQLiteDatabase db)
  {
	  db.execSQL("create table usertable(username varchar(20),password varchar(20),power varchar(20),qydm varchar(20),macdz varchar(20),obligate1 varchar(20),obligate2 varchar(20))");
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}