package com.haoge.luanru.music.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.haoge.luanru.music.entity.Music;

/** json解析的工具类 */
public class JSONParser {
	/**
	 * 解析ary 获取List<Music>
	 * 
	 * @param ary
	 * @return 
	 *         [{"album":"君生今世","albumpic":"images/junshengjinshi.jpg","author":"小虫"
	 *         ,"composer":"小虫","downcount":"1896","durationtime":"4:32",
	 *         "favcount"
	 *         :"1896","id":1,"musicpath":"musics/yelaixiang.mp3","name"
	 *         :"夜来香","singer":"邓丽君"}
	 */
	public static List<Music> parse(JSONArray ary) throws Exception {
		List<Music> musics = new ArrayList<Music>();
		for (int i = 0; i < ary.length(); i++) {
			JSONObject obj = ary.getJSONObject(i);
			Music music = new Music();
		//	music.setId(obj.getInt("id"));
			music.setName(obj.getString("name"));
			music.setSinger(obj.getString("singer"));
			music.setAuthor(obj.getString("author"));
			music.setComposer(obj.getString("composer"));
			music.setAlbum(obj.getString("album"));
			//music.setAlbumpic(obj.getString("albumpic"));
			//music.setMusicpath(obj.getString("musicpath"));
			music.setDurationtime(obj.getString("durationtime"));
			musics.add(music);
		}
		return musics;
	}
}
