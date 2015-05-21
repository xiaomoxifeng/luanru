package com.haoge.luanru.music.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDBOpenHelper extends SQLiteOpenHelper implements
		DatabaseConst {

	public SQLiteDBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public SQLiteDBOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table " + TABLE_NAME + "(" + FIELD_ID
				+ " integer primary key autoincrement," + FIELD_NAME
				+ " varchar(30) not null," + FIELD_SINGER
				+ " varchar(30) not null ," + FIELD_AUTHOR
				+ " varchar(30) not null," + FIELD_COMPOSER
				+ " varchar(30) not null," + FIELD_ALBUM
				+ " varchar(100) not null," + FIELD_ALBUMPIC
				+ " varchar(100) not null," + FIELD_MUSICPATH
				+ " varchar(30) not null," + FIELD_DURATIONTIME
				+ " varchar(30) not null)";
		Log.d("", "sql -> " + sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
