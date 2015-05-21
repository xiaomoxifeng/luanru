package com.haoge.luanru.music.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.datatype.BmobFile;

import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.util.DatabaseConst;
import com.haoge.luanru.music.util.MusicGlobalConsts;
import com.haoge.luanru.music.util.SQLiteDBOpenHelper;

public class MusicDaoImp implements MusicDao, DatabaseConst {
	private SQLiteDBOpenHelper helper = null;
	private Context context;

	public MusicDaoImp(Context context) {
		super();
		this.context = context;
		// 实例化helper
		helper = new SQLiteDBOpenHelper(context);
	}

	@Override
	public List<Music> findAllMusic() {
		List<Music> list = new ArrayList<Music>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		int colums = cursor.getColumnCount();
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			Map<String, Object > map = new HashMap<String, Object>();
			for (int i = 0; i < colums; i++) {
				String cols_name = cursor.getColumnName(i);
				String cols_value = cursor.getString(cursor
						.getColumnIndex(cols_name));
				if (cols_value == null) {
					cols_value = "";
				}
				map.put(cols_name, cols_value);
			}
//			map.get(FIELD_NAME), map.get(FIELD_SINGER),
//			map.get(FIELD_AUTHOR), map.get(FIELD_COMPOSER),
//			map.get(FIELD_ALBUM), map.get(FIELD_ALBUMPIC).toString(),
//			map.get(FIELD_MUSICPATH), map.get(FIELD_DURATIONTIME)
//			public Music( String name, String singer, String author,
//					String composer, String album, BmobFile albumpic, BmobFile musicpath,
//					String durationtime) 
		//	BmobFile f =	(BmobFile) map.get(FIELD_ALBUMPIC);
			Music m = new Music(map.get(FIELD_NAME).toString(), 
					map.get(FIELD_SINGER).toString(),map.get(FIELD_AUTHOR).toString(), 
					map.get(FIELD_COMPOSER).toString(),map.get(FIELD_ALBUM).toString(), 
					map.get(FIELD_ALBUMPIC).toString(),
					map.get(FIELD_MUSICPATH).toString(),
					map.get(FIELD_DURATIONTIME).toString());
			list.add(m);
		}
		return list;
	}

	@Override
	public int deleteMusic(Music music) {
		// TODO Auto-generated method stub
		// 执行数据库操作的对象
		SQLiteDatabase db = helper.getWritableDatabase();
		// 结果
		int result = -1;
		// where子句，不需要where关键字
		String whereClause = FIELD_NAME + "=?";
		// where子句中?匹配的值把?鬟^?淼?id??成String
		String[] whereArgs = new String[] { music.getName() + "" };
		// 执行删除
		result = db.delete(TABLE_NAME, whereClause, whereArgs);
		// 关闭
		db.close();
		// 返回

		return result;
	}

	@Override
	public long addMusic(Music music) {
		// 执行数据库操作的对象
		long result = -1;
		Music m = findOneMusic(music);
		if (m.toString() == null) {
			SQLiteDatabase db = new SQLiteDBOpenHelper(context)
					.getWritableDatabase();
//			db.beginTransaction();
			// 插入数据时SQL语句需要的参数，即字段列表和值列表
			ContentValues values = new ContentValues();
			values.put(FIELD_ALBUM, music.getAlbum());
			values.put(FIELD_ALBUMPIC, MusicGlobalConsts.EXTERNALSTORAGE + "/图片/"
					+ music.getAlbumpic().getFilename());
			values.put(FIELD_AUTHOR, music.getAuthor());
			values.put(FIELD_COMPOSER, music.getComposer());
			values.put(FIELD_DURATIONTIME, music.getDurationtime());
			values.put(FIELD_MUSICPATH, MusicGlobalConsts.EXTERNALSTORAGE + "/音乐/"
					+ music.getMusicpath().getFilename());
			values.put(FIELD_NAME, music.getName());
			values.put(FIELD_SINGER, music.getSinger());
			// 执行
			result = db.insert(TABLE_NAME, null, values);
//			db.setTransactionSuccessful();
//			db.endTransaction();
			// 关闭
			db.close();
			// 返回结果
			return result;
		}
		return result;
	}

	@Override
	public Music findOneMusic(Music music) {
		Music m = new Music();
		// 执行数据库操作的对象
		SQLiteDatabase db = helper.getReadableDatabase();
		// 查询条件
		String selection;
		// 结果
		String[] selectionArgs = null;
		selection = FIELD_ID + "=?";
		//selectionArgs = new String[] { music.getId() + "" };
		Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs,
				null, null, null);
		int colums = cursor.getColumnCount();
		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < colums; i++) {
				String cols_name = cursor.getColumnName(i);
				String cols_value = cursor.getString(cursor
						.getColumnIndex(cols_name));
				if (cols_value == null) {
					cols_value = "";
				}
				map.put(cols_name, cols_value);
			}
			m = new Music(map.get(FIELD_NAME).toString(), 
					map.get(FIELD_SINGER).toString(),map.get(FIELD_AUTHOR).toString(), 
					map.get(FIELD_COMPOSER).toString(),map.get(FIELD_ALBUM).toString(), 
					map.get(FIELD_ALBUMPIC).toString(),
					map.get(FIELD_MUSICPATH).toString(),
					map.get(FIELD_DURATIONTIME).toString());
		}
		// 关闭
		db.close();
		// 返回
		return m;
	}

}
