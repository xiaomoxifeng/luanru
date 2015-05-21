package com.haoge.luanru.music.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonUtils {
	public static String getTimeString(long timemillis) {
		return new SimpleDateFormat("mm:ss", Locale.CHINA).format(new Date(
				timemillis));
	}
}
