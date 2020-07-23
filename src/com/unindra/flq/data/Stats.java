package com.unindra.flq.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;

/**
 * Statistics and coins are stored in shared preferences defined in this class.
 * 
 *
 */
public class Stats {

	public static final String KEY_SCORE = "S_SCORE";
	public static final String KEY_COINS = "S_COINS";
	public static final String KEY_COINS_PURCHASED = "S_COINSPURCHASED";
	public static final String KEY_USEDHINTS = "S_USEDHINTS";
	public static final String KEY_3X = "S_3X";
	public static final String KEY_2X = "S_2X";
	public static final String KEY_1X = "S_1X";
	public static final String KEY_RESOLVED = "S_RESOLVED";
	public static final String KEY_WRONG = "S_WRONG";
	
	private static Stats instance = null;

    protected Stats() {

    }

    public static Stats getInstance() {
    	if(instance == null) {
    		instance = new Stats();
		}
    	return instance;
    }

    /**
     * @param c context
     * @return coins in game.
     */
    public int getCoins(Context c){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
    	return sp.getInt(KEY_COINS, getStat(KEY_COINS_PURCHASED, c));
    }
    
    /**
     * get stat
     * @param key which stat
     * @param c context
     * @return value of stat
     */
    public int getStat(String key, Context c){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
    	return sp.getInt(key, 0);
    }
        
    
    /**
     * set statistic
     * @param key which stat
     * @param value what value
     * @param c context
     */
    public void setStat(String key, int value, Context c){
    	int oldValue = getStat(key, c);
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
    	Editor editor = sp.edit();
    	editor.putInt(key, oldValue + (value));
    	editor.commit();
    }
    
    /**
     * reset all stats
     * @param c context
     */
    public void resetStats(Context c){
    	int purchased = getStat(KEY_COINS_PURCHASED, c);
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
    	Editor editor = sp.edit();
    	editor.putInt(KEY_1X, 0);
    	editor.putInt(KEY_2X, 0);
    	editor.putInt(KEY_3X, 0);
    	editor.putInt(KEY_COINS, purchased);
    	editor.putInt(KEY_RESOLVED, 0);
    	editor.putInt(KEY_SCORE, 0);
    	editor.putInt(KEY_USEDHINTS, 0);
    	editor.putInt(KEY_WRONG, 0);
    	editor.commit();
    }
}
