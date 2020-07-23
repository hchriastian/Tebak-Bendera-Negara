package com.unindra.framework;

import android.app.Activity;
import android.content.Context;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class Analytics {

	/**
	 * tags for google analytics
	 */
	
	private static final String TAG_UI = "UI ACTION";
	private static final String TAG_BUTTON_PRESS = "BUTTON PRESS";
	private static final String TAG_LEVEL_SOLVE = "LEVEL SOLVE";
	private static final String TAG_CHALLENGE = "CHALLENGE";
	private static final String TAG_PROMO = "PROMO";
	
	public static final String BUTTON_WIKI = "BUTTON WIKIPEDIA";
	public static final String BUTTON_PLAY = "BUTTON PLAY";
	public static final String BUTTON_CHALLENGE = "BUTTON CHALLENGE";
	public static final String BUTTON_COINS = "BUTTON GET COINS";
	public static final String BUTTON_LEADERBOARDS = "BUTTON LEADERBOARDS";
	public static final String BUTTON_EP_LOCKED = "BUTTON EPISODE LOCKED";
	public static final String BUTTON_SETTINGS = "BUTTON ACHIVEMENTS";
	public static final String BUTTON_BACK_HARDWARE = "BUTTON HARDWARE BACK";
	public static final String BUTTON_BACK = "BUTTON BACK";
	
	public static final String BUTTON_INSTALL = "BUTTON INSTALL";
	public static final String BUTTON_BUY1 = "BUTTON BUY1";
	public static final String BUTTON_BUY2 = "BUTTON BUY2";
	public static final String BUTTON_BUY3 = "BUTTON BUY3";
	public static final String BUTTON_AD = "BUTTON AD";
	public static final String BUTTON_RATE = "BUTTON RATE";
	public static final String BUTTON_FB = "BUTTON FACEBOOK";
	public static final String BUTTON_TWITTER = "BUTTON TWITTER";
	
	private static Analytics instance = null;
	
	public Analytics(){
		//EMPTY CONSTRUCTOR
	}
	
	/**
	 * Singleton pattern
	 * @return instance
	 */
	public static Analytics getInstance(){
		if(instance == null){
			instance = new Analytics();
		}
		
		return instance;
	}
	
	
	public void setContext(Context c){
		EasyTracker.getInstance().setContext(c);
	}
	
	public void activityStart(Activity a){
		EasyTracker.getInstance().activityStart(a);
	}
	
	public void activityStop(Activity a){
		EasyTracker.getInstance().activityStop(a);
	}
	
	public void sendException(String message, boolean fatal){
		 Tracker tracker = EasyTracker.getTracker();
		 tracker.sendException(message, fatal);
	}
	
	public void sendUiClickEvent(String button){
		EasyTracker.getTracker().sendEvent(TAG_UI, TAG_BUTTON_PRESS, button, 0L);
	}
	
	public void sendPromoEvent(String button){
		EasyTracker.getTracker().sendEvent(TAG_PROMO, TAG_BUTTON_PRESS, button, 0L);
	}
	
	public void sendLevelSolveEvent(String button, String value){
		EasyTracker.getTracker().sendEvent(TAG_LEVEL_SOLVE, button, value, 0L);
	}
	
	public void sendChallengeEvent(String what, String value){
		EasyTracker.getTracker().sendEvent(TAG_CHALLENGE, what, value, 0L);
	}
	
	
}
