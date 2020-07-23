package com.unindra.flq;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.vending.billing.IInAppBillingService;
import com.futuristicbear.americanquiz.R;
import com.unindra.billing.util.IabHelper;
import com.unindra.billing.util.IabResult;
import com.unindra.billing.util.Inventory;
import com.unindra.billing.util.Purchase;
import com.unindra.flq.data.DbAdapter;
import com.unindra.flq.data.Stats;
import com.unindra.framework.Analytics;
import com.unindra.framework.IapBilling;
import com.unindra.framework.Logger;
import com.unindra.framework.SoundHandler;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ShopActivity extends X implements OnClickListener {

	private Button BTN_facebook;
	private Button BTN_twitter;
	private Button BTN_rate;
	private Button BTN_flq;
	private Button BTN_removeAds;
	private Button BTN_buyItem1;
	private Button BTN_buyItem2;
	private Button BTN_buyItem3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		init();
		
		if(isOnline())
			if(IapBilling.mHelper == null)
				IapBilling.initializeBilling();

	}

	private void init() {
		initHeader(R.string.head_credits, R.drawable.icon_coins_header, this);
		BTN_facebook = (Button) findViewById(R.id.BTN_facebook);
		BTN_twitter = (Button) findViewById(R.id.BTN_twitter);
		BTN_rate = (Button) findViewById(R.id.BTN_rate);
		BTN_flq = (Button) findViewById(R.id.BTN_flq);
		BTN_removeAds = (Button) findViewById(R.id.BTN_removeAds);
		BTN_buyItem1 = (Button) findViewById(R.id.BTN_buy_item1);
		BTN_buyItem2 = (Button) findViewById(R.id.BTN_buy_item2);
		BTN_buyItem3 = (Button) findViewById(R.id.BTN_buy_item3);
		BTN_buyItem1.setOnClickListener(this);
		BTN_buyItem2.setOnClickListener(this);
		BTN_buyItem3.setOnClickListener(this);
		BTN_removeAds.setOnClickListener(this);
		BTN_flq.setOnClickListener(this);
		BTN_facebook.setOnClickListener(this);
		BTN_twitter.setOnClickListener(this);
		BTN_rate.setOnClickListener(this);

		BTN_buyItem1.setText(getResources().getString(R.string.buy_item1) + "\n" + IapBilling.item1Price);
		BTN_buyItem2.setText(getResources().getString(R.string.buy_item2) + "\n" + IapBilling.item2Price);
		BTN_buyItem3.setText(getResources().getString(R.string.buy_item3) + "\n" + IapBilling.item3Price);
		BTN_removeAds.setText(getResources().getString(R.string.remove_ads) + "\n" + IapBilling.removeAdsPrice);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.BTN_back) {
			SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
			finish();
		}
		if (v.getId() == R.id.BTN_facebook) {
			Analytics.getInstance().sendPromoEvent(Analytics.BUTTON_FB);
			if (isOnline()) {
				startActivity(getOpenFacebookIntent(this));
				if (!getBooleanPreferences(Config.PREFS_IS_FACEBOOK_LIKED, false, this)) {
					Stats.getInstance().setStat(Stats.KEY_COINS, 500, this);
					setBooleanPreferences(Config.PREFS_IS_FACEBOOK_LIKED, true, this);
				}

			} else {
				Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
			}
		}
		if (v.getId() == R.id.BTN_twitter) {
			Analytics.getInstance().sendPromoEvent(Analytics.BUTTON_TWITTER);
			if (isOnline()) {
				startActivity(getTwitter(this));
				if (!getBooleanPreferences(Config.PREFS_IS_TWITTER_FOLLOWED, false, this)) {
					Stats.getInstance().setStat(Stats.KEY_COINS, 500, this);
					setBooleanPreferences(Config.PREFS_IS_TWITTER_FOLLOWED, true, this);
				}
			} else {
				Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
			}
		}

		if (v.getId() == R.id.BTN_rate) {
			Analytics.getInstance().sendPromoEvent(Analytics.BUTTON_RATE);
			if (isOnline()) {
				startActivity(getMarket(this, this.getPackageName()));
				if (!getBooleanPreferences(Config.PREFS_IS_RATED, false, this)) {
					Stats.getInstance().setStat(Stats.KEY_COINS, 800, this);
					setBooleanPreferences(Config.PREFS_IS_RATED, true, this);
				}
			} else {
				Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
			}
		}

		if (v.getId() == R.id.BTN_flq) {
			Analytics.getInstance().sendPromoEvent(Analytics.BUTTON_INSTALL);
			if (isOnline()) {
				startActivity(getMarket(this, "com.futuristicbear.flq"));
				if (!getBooleanPreferences(Config.PREFS_IS_FOOTBALLQUIZ, false, this)) {
					Stats.getInstance().setStat(Stats.KEY_COINS, 1000, this);
					setBooleanPreferences(Config.PREFS_IS_FOOTBALLQUIZ, true, this);
				}
			} else {
				Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
			}
		}

		if (v.getId() == R.id.BTN_buy_item1) {
			Analytics.getInstance().sendPromoEvent(Analytics.BUTTON_BUY1);
			if (isOnline()) {
				IapBilling.startPurchaseIntent(this, IapBilling.SKU_ITEM1, "bear0001");
			} else {
				Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
			}
		}
		if (v.getId() == R.id.BTN_buy_item2) {
			Analytics.getInstance().sendPromoEvent(Analytics.BUTTON_BUY2);
			if (isOnline()) {
				IapBilling.startPurchaseIntent(this, IapBilling.SKU_ITEM2, "bear0002");
			} else {
				Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
			}
		}
		if (v.getId() == R.id.BTN_buy_item3) {
			Analytics.getInstance().sendPromoEvent(Analytics.BUTTON_BUY3);
			if (isOnline()) {
				IapBilling.startPurchaseIntent(this, IapBilling.SKU_ITEM3, "bear0003");
			} else {
				Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
			}
		}
		if (v.getId() == R.id.BTN_removeAds) {
			Analytics.getInstance().sendPromoEvent(Analytics.BUTTON_AD);
			if (isOnline()) {
				IapBilling.startPurchaseIntent(this, IapBilling.SKU_REMOVEADS, "bear0");
			} else {
				Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
			}
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!IapBilling.onActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}

	};

	public static Intent getOpenFacebookIntent(Context context) {

		try {
			context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
			Intent intent;
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/265211256948935"));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			return intent;
		} catch (Exception e) {
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/kondespace"));
		}
	}

	private static Intent getTwitter(Context context) {
		try {
			context.getPackageManager().getPackageInfo("com.twitter.android", 0);
			Intent intent;
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=1284092940"));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			return intent;
		} catch (Exception e) {
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://mobile.twitter.com/kondespace"));
		}
	}

	public static Intent getMarket(Context context, String packageName) {
		Uri uri = Uri.parse("market://details?id=" + packageName);
		return new Intent(Intent.ACTION_VIEW, uri);
	}

}
