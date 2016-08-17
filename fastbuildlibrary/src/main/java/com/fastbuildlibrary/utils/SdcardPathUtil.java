package com.fastbuildlibrary.utils;

import android.os.Environment;

import java.io.File;
import java.util.Map;

/**
 * SDCard 工具类
 */
public class SdcardPathUtil {
	public static String filepath = Environment.getExternalStorageDirectory().getPath();

	/**
	 * 判断SD是否挂载
	 * @return
     */
	private static boolean isSdCardMount(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取手机外置SD卡的根目录
	 * 可以先把env的值都打印出来打印出来看看
	 * @return
	 */
	public static String getExternalSDRoot() {

		Map<String, String> env = System.getenv();

		return env.get("SECONDARY_STORAGE");
	}

	/**
	 * 根据条件获取手机SD卡的根目录
	 * 
	 * @return
	 */
	public static String getPath() {
		if (getExternalSDRoot() != null) {
			if (new File(getExternalSDRoot()).length() != 0) {
				return getExternalSDRoot();
			}
		}
		return filepath;

	}
}
