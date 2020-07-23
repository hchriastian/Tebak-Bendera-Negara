package com.unindra.flq;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.futuristicbear.americanquiz.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.unindra.framework.AdController;
import com.unindra.framework.Analytics;
import com.unindra.framework.IapBilling;
import com.unindra.framework.SoundHandler;

public class MainMenuActivity extends X implements OnClickListener {

	private Button BTN_playgame;
	private Button BTN_challenge;
	private Button BTN_leaderboard;
	private Button BTN_credits;
	private ImageButton IB_sound;
	private ImageButton IB_vibrate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		init();
		Analytics.getInstance().setContext(this);
		configImageLoader();
		AdController.cacheAdBuddiez(this);
		if (isOnline()) {
			IapBilling.initializeBilling();
		}

	}

	@Override
	public void onBackPressed() {
		exitDialog().show();
	}

	@Override
	public void onClick(View v) {
		SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
		if (v.getId() == R.id.BTN_playgame) {
			Analytics.getInstance().sendUiClickEvent(Analytics.BUTTON_PLAY);
			startActivity(new Intent(this, SelectEpisodeActivity.class));
		}
		if (v.getId() == R.id.BTN_challenge) {
			Analytics.getInstance().sendUiClickEvent(Analytics.BUTTON_CHALLENGE);
			startActivity(new Intent(this, ChallengeSetupActivity.class));
		}
		if (v.getId() == R.id.BTN_leaderboard) {
			Analytics.getInstance().sendUiClickEvent(Analytics.BUTTON_LEADERBOARDS);
			if (isOnline()) {
				startActivity(new Intent(this, LeaderboardActivity.class));
			} else {
				Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
			}
		}
		if (v.getId() == R.id.BTN_credits) {
			Analytics.getInstance().sendUiClickEvent(Analytics.BUTTON_COINS);
			startActivity(new Intent(this, ShopActivity.class));
		}

		if (v.getId() == R.id.IB_sound) {
			if (getBooleanPreferences(Config.PREFS_SOUND, true, this)) {
				setBooleanPreferences(Config.PREFS_SOUND, false, this);
				IB_sound.setBackgroundResource(R.drawable.speaker_off);
			} else {
				setBooleanPreferences(Config.PREFS_SOUND, true, this);
				IB_sound.setBackgroundResource(R.drawable.speaker_on);
			}
		}

		if (v.getId() == R.id.IB_vibrate) {
			if (getBooleanPreferences(Config.PREFS_VIBRATE, true, this)) {
				setBooleanPreferences(Config.PREFS_VIBRATE, false, this);
				IB_vibrate.setBackgroundResource(R.drawable.vibrate_off);
			} else {
				setBooleanPreferences(Config.PREFS_VIBRATE, true, this);
				IB_vibrate.setBackgroundResource(R.drawable.vibrate_on);
			}
		}

	}

	private void init() {
		BTN_playgame = (Button) findViewById(R.id.BTN_playgame);
		BTN_challenge = (Button) findViewById(R.id.BTN_challenge);
		BTN_leaderboard = (Button) findViewById(R.id.BTN_leaderboard);
		BTN_credits = (Button) findViewById(R.id.BTN_credits);
		IB_sound = (ImageButton) findViewById(R.id.IB_sound);
		IB_vibrate = (ImageButton) findViewById(R.id.IB_vibrate);


		if (getBooleanPreferences(Config.PREFS_SOUND, true, this)) {
			IB_sound.setBackgroundResource(R.drawable.speaker_on);
		} else {
			IB_sound.setBackgroundResource(R.drawable.speaker_off);
		}

		if (getBooleanPreferences(Config.PREFS_VIBRATE, true, this)) {
			IB_vibrate.setBackgroundResource(R.drawable.vibrate_on);
		} else {
			IB_vibrate.setBackgroundResource(R.drawable.vibrate_off);
		}

		IB_sound.setOnClickListener(this);
		IB_vibrate.setOnClickListener(this);
		BTN_playgame.setOnClickListener(this);
		BTN_challenge.setOnClickListener(this);
		BTN_leaderboard.setOnClickListener(this);
		BTN_credits.setOnClickListener(this);
	}

	private AlertDialog exitDialog() {

		Builder dialog = new AlertDialog.Builder(this);

		dialog.setTitle(R.string.are_you_sure).setIcon(R.drawable.ic_launcher).setMessage(R.string.quit_message_body).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		return dialog.create();
	}

	public static void configImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(X.context.getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoader.getInstance().init(config);
	}

}
