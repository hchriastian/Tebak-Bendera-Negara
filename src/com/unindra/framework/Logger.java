package com.unindra.framework;

import android.util.Log;

/**
 * Class for logs
 * 
 *
 */
public class Logger {

	/**
	 * tag
	 */
	public static final String tag = "QUIZGAME";
	
	/**
	 * log
	 * @param msg log message
	 */
	public static void log(String msg){
		Log.d(tag, "GAME: " + msg);
	}
	
}
