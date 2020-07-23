package com.unindra.framework;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;

import com.futuristicbear.americanquiz.R;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import com.purplebrain.adbuddiz.sdk.AdBuddizDelegate;
import com.unindra.flq.Config;
import com.unindra.flq.PromoDialog;
import com.unindra.flq.X;
import com.unindra.flq.data.Stats;


/**
 * class where all 3th party ad systems are integrated
 * 
 *
 */
public class AdController implements AdListener, AdBuddizDelegate {

	private InterstitialAd interstitial;
	private static final String ADMOB_ID = "";
	private boolean isAdmobLoaded = false;
	private boolean isAdbuddiezLoaded = false;
	private Context c;
	private AdView adView;

	private static final String KEY_SHOWAD = "showad";

	/**
	 * Type of ads:
	 *  1 banner ads
	 *  2 interstitial / fullscreen ads
	 *  3 promo dialog
	 */
	public static final int TYPE_BANNER = 0;
	public static final int TYPE_INTERSTITAL = 1;
	public static final int TYPE_PROMO = 2;

	/**
	 * When you want to show ad, simply create new object
	 * @param a activity
	 * @param c context
	 * @param type banner, interstitial or promo
	 */
	public AdController(final Activity a, Context c, int type) {

		if (X.getBooleanPreferences(Config.PREFS_SHOW_ADS, true, c)) {
			Logger.log("AD: ON CREATE");

			this.c = c;
			if (type == TYPE_INTERSTITAL && isShowAd()) {
				adBuddiezOnStart(a);
				AdBuddiz.getInstance().setDelegate(this);
				isAdbuddiezLoaded = true;
				initInterstitial(a);

			} else if (type == TYPE_BANNER) {

				a.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						adView = (AdView) a.findViewById(R.id.adView);
						adView.setAdListener(AdController.this);
						adView.loadAd(new AdRequest());

					}
				});

			} else if (type == TYPE_PROMO && isShowAd()) {

				int which = calculateType();
				Logger.log("PROMOTYPE: " + which);
				if (which != -1 && which != 4) {
					new PromoDialog(c, which).show();
				} else if (which == 4) {
					adBuddiezOnStart(a);
					isAdbuddiezLoaded = true;
					AdBuddiz.getInstance().showAd();
				} else {
					initInterstitial(a);
				}

			}
		} else {
			if (type == TYPE_BANNER) {
				AdView adView = (AdView) a.findViewById(R.id.adView);
				adView.setVisibility(View.GONE);
			}
		}
	}

	public static void cacheAdBuddiez(Activity c) {
		AdBuddiz.getInstance().cacheAds(c);
	}

	public static void adBuddiezOnStart(Activity c) {
		AdBuddiz.getInstance().onStart(c);
	}

	
	/**
	 * initialize interstitial ad
	 * @param a activity
	 */
	private void initInterstitial(Activity a) {

		interstitial = new InterstitialAd(a, ADMOB_ID);
		AdRequest adRequest = new AdRequest();
		interstitial.loadAd(adRequest);
		isAdmobLoaded = false;
		interstitial.setAdListener(this);
	}

	/**
	 * calculate what content of promo dialog should be created
	 * @return random value. which dialog should be initialized
	 */
	private int calculateType() {
		ArrayList<Integer> values = new ArrayList<Integer>();
		values.add(4); 
		if (!X.getBooleanPreferences(Config.PREFS_IS_RATED, false, c)) {
			values.add(0);
		}
		if (!X.getBooleanPreferences(Config.PREFS_IS_FLAGQUIZ, false, c)) {
			values.add(3);
		}
		if (!X.getBooleanPreferences(Config.PREFS_IS_FLAGQUIZ, false, c)) {
			values.add(1);
		}
		if (Stats.getInstance().getStat(Stats.KEY_COINS_PURCHASED, c) == 0) {
			values.add(2);
		}
		if (values.size() > 1) {
			return values.get(new Random().nextInt(values.size() - 1));
		} else if (values.size() == 1) {
			return values.get(0);
		} else {
			return -1;
		}

	}

	/**
	 * show interstitial ad
	 */
	public void showInterstiial() {
		if (isAdbuddiezLoaded) {
			AdBuddiz.getInstance().showAd();
			Logger.log("AD:ADBUDDIEZ SHOWING");
		} else if (isAdmobLoaded) {
			interstitial.show();
			Logger.log("AD: SHOWING INTERSTITAL ADMOB");
		}
	}

	/**
	 * check if should show ad
	 * @return true if should show ad false otherwise
	 */
	private boolean isShowAd() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		int prefsValue = sp.getInt(KEY_SHOWAD, 0);
		if (prefsValue % 5 == 4) {
			increaseAdCounter();
			return true;
		} else {
			increaseAdCounter();
			return false;
		}
	}

	/**
	 * adcounter for determining when to show ad
	 */
	private void increaseAdCounter() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		int oldValue = sp.getInt(KEY_SHOWAD, 0);
		Editor editor = sp.edit();
		editor.putInt(KEY_SHOWAD, oldValue + 1);
		editor.commit();
	}

	/****************************************
	 * ADMOB METHODS
	 ****************************************/

	@Override
	public void onDismissScreen(Ad arg0) {
		// TODO Auto-generated method stub
		Logger.log("AD: ON DISMISS SCREEN");
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		// TODO Auto-generated method stub
		Logger.log("AD: ON FAILED TO RECIEVE AD");
	}

	@Override
	public void onLeaveApplication(Ad arg0) {
		// TODO Auto-generated method stub
		Logger.log("AD: ON LEAVE APP");
	}

	@Override
	public void onPresentScreen(Ad arg0) {
		// TODO Auto-generated method stub
		Logger.log("AD: ON PRESENT SCREEN");
	}

	@Override
	public void onReceiveAd(Ad arg0) {
		// TODO Auto-generated method stub
		if (arg0.isReady()) {
			Logger.log("AD: ON RECIEVE AD");
			isAdmobLoaded = true;
		}
	}
	
	/********************************************
	 * END ADMOB METHODS
	 * 
	 * START ADBUDDIEZ
	 ********************************************/

	@Override
	public void failToLoadAd(String arg0, AdBuddizFailToDisplayCause arg1) {
		// TODO Auto-generated method stub
		isAdbuddiezLoaded = false;
		Logger.log("AD:ADBUDDIEZ ON FAILED");
	}

	

}
