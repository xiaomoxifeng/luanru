package com.haoge.luanru.music.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.util.Log;

public class MusicScanner {
	private static final String TAG = null;
	/**
	 * 扫描到的歌曲的List集合
	 */
	private static List<String> musics;

	public static List<String> getMusicList(File rootDir) {
		musics = new ArrayList<String>();
		scan(rootDir);
		return musics;
	}

	/**
	 * 扫描文件夹，并将*.mp3和*.wma格式的文件的路径添加到List集合中
	 * 
	 * @param dir
	 *            需要扫描的文件夹
	 */
	private static void scan(File dir) {
		File[] files = dir.listFiles();
		if (dir != null) {
			Log.d(TAG, "scan dir -> " + dir.getAbsolutePath());
			for (File f : files) {
				if (f.isDirectory()) {
					scan(f);
				} else {
					String fileName = f.getName().toLowerCase(Locale.CHINA);
					if (fileName.endsWith(".mp3") || fileName.endsWith(".wma")) {
						musics.add(f.getAbsolutePath());
						Log.d(TAG, "add -> " + f.getAbsolutePath());
					}
				}

			}
		}
	}
}
