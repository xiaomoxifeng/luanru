package com.haoge.luanru.music.util;

import java.io.File;

public class DeleteFileUtil {
	public static int delete(String path) {

		int i = -1;
		boolean flag = new File(path).delete();
		return i;
	}
}
