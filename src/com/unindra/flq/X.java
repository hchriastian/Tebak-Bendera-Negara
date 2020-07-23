package com.unindra.flq;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.futuristicbear.americanquiz.R;
import com.unindra.framework.Analytics;
import com.unindra.framework.IapBilling;
import com.unindra.framework.Logger;
import com.unindra.framework.NameValidator;
import com.unindra.framework.SoundHandler;

public abstract class X extends Activity implements OnClickListener {

	private Vibrator vibrator;
	private TextView TV_score;
	private TextView TV_hints;
	

	public static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.log("onCreate " + this.getClass().getName());
		vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		context = getBaseContext();
		SoundHandler.getInstance().initSounds(context);

	}

	@Override
	protected void onStart() {
		super.onStart();
		Analytics.getInstance().activityStart(this);
		Logger.log("onStart " + this.getClass().getName());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		IapBilling.onDestroy();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Logger.log("onPause " + this.getClass().getName());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Logger.log("onResume " + this.getClass().getName());
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Logger.log("onStop " + this.getClass().getName());
		Analytics.getInstance().activityStop(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.IV_headerb_button) {
			SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
			Intent i = new Intent(this, ShopActivity.class);
			startActivity(i);
		}

	}
	
	public static boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public void animate(int animation, View target) {
		Animation anim = AnimationUtils.loadAnimation(this, animation);
		target.startAnimation(anim);
	}

	public void vibrate(Context c, long milis) {
		if (getBooleanPreferences(Config.PREFS_VIBRATE, true, this)) {
			vibrator.vibrate(milis);
		}
	}

	public int getDeviceAPILevel() {
		return Build.VERSION.SDK_INT;
	}

	public void initHeader(String headerText, int icon, OnClickListener listener) {
		Button BTN_back = (Button) findViewById(R.id.BTN_back);
		BTN_back.setOnClickListener(listener);
		TextView TV_header = (TextView) findViewById(R.id.TV_header_text);
		TV_header.setText(headerText);
		ImageView IV_icon = (ImageView) findViewById(R.id.IV_header_icon);
		IV_icon.setImageResource(icon);
	}

	public void initHeader(int headerText, int icon, OnClickListener listener) {
		Button BTN_back = (Button) findViewById(R.id.BTN_back);
		BTN_back.setOnClickListener(listener);
		TextView TV_header = (TextView) findViewById(R.id.TV_header_text);
		TV_header.setText(headerText);
		ImageView IV_icon = (ImageView) findViewById(R.id.IV_header_icon);
		IV_icon.setImageResource(icon);
	}

	public void initHeaderWithScorePanel(String headerText, int icon, int score, int hints, OnClickListener listener) {
		Button BTN_back = (Button) findViewById(R.id.BTN_back);
		BTN_back.setOnClickListener(listener);
		TextView TV_header = (TextView) findViewById(R.id.TV_header_text);
		TV_header.setText(headerText);
		ImageView IV_icon = (ImageView) findViewById(R.id.IV_header_icon);
		IV_icon.setImageResource(icon);
		LinearLayout LL_score = (LinearLayout) findViewById(R.id.IV_headerb_button);
		LL_score.setOnClickListener(listener);

		TV_score = (TextView) findViewById(R.id.TV_score);
		TV_hints = (TextView) findViewById(R.id.TV_hints_available);
		TV_score.setText(Integer.toString(score));
		TV_hints.setText(Integer.toString(hints));
	}

	public void initHeaderWithScorePanel(int headerText, int icon, int score, int hints, OnClickListener listener) {
		Button BTN_back = (Button) findViewById(R.id.BTN_back);
		BTN_back.setOnClickListener(listener);
		TextView TV_header = (TextView) findViewById(R.id.TV_header_text);
		TV_header.setText(headerText);
		ImageView IV_icon = (ImageView) findViewById(R.id.IV_header_icon);
		IV_icon.setImageResource(icon);
		LinearLayout LL_score = (LinearLayout) findViewById(R.id.IV_headerb_button);
		LL_score.setOnClickListener(listener);

		TV_score = (TextView) findViewById(R.id.TV_score);
		TV_hints = (TextView) findViewById(R.id.TV_hints_available);
		TV_score.setText(Integer.toString(score));
		TV_hints.setText(Integer.toString(hints));
	}

	public void updateHeaderData(int score, int hints) {
		TV_score.setText(Integer.toString(score));
		TV_hints.setText(Integer.toString(hints));
	}

	public void updateHeaderData(int hints) {
		TV_hints.setText(Integer.toString(hints));
	}

	
	public static boolean getBooleanPreferences(String key, boolean defaultVal, Context c) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		return sp.getBoolean(key, defaultVal);
	}

	public static void setBooleanPreferences(String key, boolean value, Context c) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public int getIntegerPreferences(String key, int defaultVal, Context c) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		return sp.getInt(key, defaultVal);
	}

	public void setIntegerPreferences(String key, int value, Context c) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public int getScreenWidth() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		return metrics.widthPixels;
	}

	public int getScreenHeight() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		return metrics.heightPixels;

	}

	/**
	 * 
	 * @return player name from preferences
	 */
	public String getPlayerNameFromPreferences(Context c) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		String devId2 = "player-" + getDeviceId().substring(getDeviceId().length() - 5);
		String player = sp.getString("PLAYER", devId2);
		return player;
	}

	/**
	 * save player name to preferences
	 * 
	 * @param name
	 *            player name
	 * @param c
	 *            context
	 */
	public boolean savePlayerName(String name, Context c) {

		NameValidator validator = new NameValidator(name);

		if (validator.isValid()) {
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
			Editor editor = sp.edit();
			editor.putString("PLAYER", validator.getValidatedName());
			editor.commit();
			return true;
		} else {
			Toast.makeText(c, R.string.name_length_dialog, Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	/**
	 * 
	 * @return 64 bit unique number
	 */
	public String getDeviceId() {
		final String androidId;

		try {
			androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			return androidId;
		} catch (NullPointerException e) {
			Analytics.getInstance().sendException("GET DEVICE ID: " + e.getMessage(), false);
		}
		Random rand = new Random();
		String number = Integer.toString(rand.nextInt(10000));
		if (number.length() < 5) {
			number = "00000" + number;
		}
		return number;
	}

	public static String sha1(String password) {
		String sha1 = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(password.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sha1;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
}
