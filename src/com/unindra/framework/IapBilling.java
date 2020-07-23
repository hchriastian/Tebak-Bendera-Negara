package com.unindra.framework;

import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.unindra.billing.util.IabHelper;
import com.unindra.billing.util.IabResult;
import com.unindra.billing.util.Inventory;
import com.unindra.billing.util.Purchase;
import com.unindra.flq.Config;
import com.unindra.flq.X;
import com.unindra.flq.data.Stats;

/**
 * This class handles google inapp purchases
 * 
 *
 */
public class IapBilling {

	public static String item1Price = "";
	public static String item3Price = "";
	public static String item2Price = "";
	public static String removeAdsPrice = "";

	/**
	 * SKU's defined in google play account
	 */
	public static final String SKU_ITEM1 = "item1";
	public static final String SKU_ITEM2 = "item2";
	public static final String SKU_ITEM3 = "item3";
	public static final String SKU_REMOVEADS = "remove_ads";

	public static final int RC_REQUEST = 898;

	public static IabHelper mHelper;

	/**
	 * Key from google play developer console divided into two strings
	 */
	private static final String base64EncodedPublicKey1 = "REPLACE WITH YOUR KEY";
	private static final String base64EncodedPublicKey2 = "REPLACE WITH YOUR KEY";

	
	/**
	 * initialize billing. init helper variable, query inventory (get data from google server)
	 */
	public static void initializeBilling() {
		Logger.log("IAP: INITIALIZE");
		mHelper = new IabHelper(X.context, base64EncodedPublicKey1 + base64EncodedPublicKey2);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					Logger.log("IAB: error setup");
				} else {
					String[] skus = {SKU_ITEM1, SKU_ITEM2, SKU_ITEM3, SKU_REMOVEADS};
					mHelper.queryInventoryAsync(true, Arrays.asList(skus), mGotInventoryListener);
				}
			}
		});

	}
	
	
	/**
	 * reward player with coins (consumable purchases)
	 */
	static IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		
		@Override
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			if (purchase.getSku().equals(SKU_ITEM1)) {
				Stats.getInstance().setStat(Stats.KEY_COINS_PURCHASED, 5000, X.context);
				Stats.getInstance().setStat(Stats.KEY_COINS, 5000, X.context);
			} else if (purchase.getSku().equals(SKU_ITEM2)) {
				Stats.getInstance().setStat(Stats.KEY_COINS_PURCHASED, 13000, X.context);
				Stats.getInstance().setStat(Stats.KEY_COINS, 13000, X.context);
			} else if (purchase.getSku().equals(SKU_ITEM3)) {
				Stats.getInstance().setStat(Stats.KEY_COINS_PURCHASED, 30000, X.context);
				Stats.getInstance().setStat(Stats.KEY_COINS, 30000, X.context);
			}
		}
	};

	/**
	 * query google server for purchases. Check if bought remove ads item. Add price tags to item's strings.
	 */
	static IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {

		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inv) {

			if (result.isFailure()) {
				Logger.log(result.getMessage());
			} else {
				
				
				if (inv.hasPurchase(SKU_REMOVEADS)) {
					Logger.log("HAS PURCHASE REMOVE ADS");
					X.setBooleanPreferences(Config.PREFS_SHOW_ADS, false, X.context);
				}
				try {
					item1Price = inv.getSkuDetails(SKU_ITEM1).getPrice();
					item2Price = inv.getSkuDetails(SKU_ITEM2).getPrice();
					item3Price = inv.getSkuDetails(SKU_ITEM3).getPrice();
					removeAdsPrice = inv.getSkuDetails(SKU_REMOVEADS).getPrice();	
				} catch (NullPointerException e) {
					Logger.log("IAP: query skuS error");
				}	
			}
		}
	};

	/**
	 * purchase finished. Check if payment successful. If yes, set not to show ads or consume consumable items.
	 */
	static IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {

		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Logger.log(result.getMessage());
			if (result.isFailure()) {
				Logger.log(result.getMessage());
			} else {
				if (purchase.getSku().equals(SKU_REMOVEADS)) {
					X.setBooleanPreferences(Config.PREFS_SHOW_ADS, false, X.context);
				} else if (purchase.getSku().equals(SKU_ITEM1)) {
					mHelper.consumeAsync(purchase, mConsumeFinishedListener);
				} else if (purchase.getSku().equals(SKU_ITEM2)) {
					mHelper.consumeAsync(purchase, mConsumeFinishedListener);
				} else if (purchase.getSku().equals(SKU_ITEM3)) {
					mHelper.consumeAsync(purchase, mConsumeFinishedListener);
				}
			}
		}
	};

	/**
	 * free helper variable
	 */
	public static void onDestroy() {
		Logger.log("IAP: onDestroy");
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;
	}

	/**
	 * start purchase
	 * @param a activity
 	 * @param sku SKU defined in google developer console
	 * @param developerPayload tag
	 */
	public static void startPurchaseIntent(Activity a, String sku, String developerPayload) {
		try {
			mHelper.flagEndAsync();	
			mHelper.launchPurchaseFlow(a, sku, RC_REQUEST, mPurchaseFinishedListener, developerPayload);
		} catch (NullPointerException e) {
			Logger.log("IAP: startPurchase intetn exeption");
			//mHelper.launchPurchaseFlow(a, sku, RC_REQUEST, mPurchaseFinishedListener, developerPayload);
		} catch (IllegalStateException e){
			
		}
		
	}

	public static boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		return mHelper.handleActivityResult(requestCode, resultCode, data);
	}

}
