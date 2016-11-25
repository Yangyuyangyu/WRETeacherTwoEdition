package com.teacherhelp.utils;

import android.util.Log;

public class LogHelper {

	public static boolean isDebug = true;
	private static final String TAG = "com.teacherhelp";

	public static int d(String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.d(TAG, msg);
		} else {
			return 0;
		}
	}

	public static int d(String subTAG, String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.d(TAG + "." + subTAG, msg);
		} else {
			return 0;
		}
	}

	public static int i(String subTAG, String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.i(TAG + "." + subTAG, msg);
		} else {
			return 0;
		}
	}

	public static int i(String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.i(TAG + ".", msg);
		} else {
			return 0;
		}
	}

	public static int e(String subTAG, String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.e(TAG + "." + subTAG, msg);
		} else {
			return 0;
		}
	}

	public static int e(String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.e(TAG, msg);
		} else {
			return 0;
		}
	}

	public static int w(String subTAG, String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.w(TAG + "." + subTAG, msg);
		} else {
			return 0;
		}
	}

	public static int v(String subTAG, String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.v(TAG + "." + subTAG, msg);
		} else {
			return 0;
		}
	}

	public static int w(String subTAG, Exception e) {
		if (null == e)
			return 0;
		if (isDebug) {
			return Log.w(TAG + "." + subTAG, e.getMessage());
		} else {
			return 0;
		}
	}

	public static int e(String subTAG, String msg, Exception e) {
		if (null == msg && null == e)
			return 0;
		if (isDebug) {
			return Log.e(TAG + "." + subTAG, msg + "." + e.getMessage());
		} else {
			return 0;
		}
	}
}
