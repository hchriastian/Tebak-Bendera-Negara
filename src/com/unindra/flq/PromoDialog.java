package com.unindra.flq;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuristicbear.americanquiz.R;
import com.unindra.flq.data.Stats;
import com.unindra.framework.Analytics;

public class PromoDialog extends Dialog implements android.view.View.OnClickListener {

	private TextView tvTitle;
	private TextView tvBody;
	private Button btnPositive;
	private Button btnNegative;
	private ImageButton ibClose;
	private ImageView ivImage;
	private int type;
	private Context context;

	public static final int TYPE_RATE = 0;
	public static final int TYPE_DOWNLOADFOOTBALL = 1;
	public static final int TYPE_COINS = 2;
	public static final int TYPE_DOWNLOADFLAGS = 3;

	public PromoDialog(Context context, int type) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.type = type;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		getWindow().setGravity(Gravity.CENTER);
		setContentView(R.layout.promo_dialog);
		setCanceledOnTouchOutside(true);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.6f;
		getWindow().setAttributes(lp);
		init();
		initData();
	}

	private void init() {
		tvBody = (TextView) findViewById(R.id.TV_body);
		tvTitle = (TextView) findViewById(R.id.TV_title);
		ivImage = (ImageView) findViewById(R.id.IV_body);
		btnNegative = (Button) findViewById(R.id.BTN_negative);
		btnPositive = (Button) findViewById(R.id.BTN_positive);
		ibClose = (ImageButton) findViewById(R.id.IV_closeView);
		btnNegative.setOnClickListener(this);
		btnPositive.setOnClickListener(this);
		ibClose.setOnClickListener(this);
	}

	private void initData() {
		btnNegative.setText(R.string.not_now);
		switch (type) {
		case TYPE_RATE:
			ivImage.setBackgroundResource(R.drawable.icon_rate);
			btnPositive.setText(R.string.rate_dialog);
			tvTitle.setText(R.string.rate_title_dialog);
			tvBody.setText(R.string.rate_body_dialog);
			break;
		case TYPE_COINS:
			ivImage.setBackgroundResource(R.drawable.icon_coins);
			btnPositive.setText(R.string.coins_dialog);
			tvTitle.setText(R.string.coins_title_dialog);
			tvBody.setText(R.string.coins_body_dialog);
			break;
		case TYPE_DOWNLOADFOOTBALL:
			ivImage.setBackgroundResource(R.drawable.icon_flq);
			btnPositive.setText(R.string.download_dialog);
			tvTitle.setText(R.string.download_title_dialog);
			tvBody.setText(R.string.download_body_dialog);
			break;
		case TYPE_DOWNLOADFLAGS:
			ivImage.setBackgroundResource(R.drawable.icon_wfq);
			btnPositive.setText(R.string.download_dialog);
			tvTitle.setText(R.string.download_title_dialog_world);
			tvBody.setText(R.string.download_body_dialog_world);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.IV_closeView || v.getId() == R.id.BTN_negative) {
			cancel();
		}
		if (v.getId() == R.id.BTN_positive) {
			switch (type) {
			case TYPE_COINS:
				Intent i = new Intent(context, ShopActivity.class);
				context.startActivity(i);
				break;
			case TYPE_RATE:
				Analytics.getInstance().sendPromoEvent(Analytics.BUTTON_RATE);
				if (X.isOnline()) {
					context.startActivity(ShopActivity.getMarket(context, context.getPackageName()));
					if (!X.getBooleanPreferences(Config.PREFS_IS_RATED, false, context)) {
						Stats.getInstance().setStat(Stats.KEY_COINS, 800, context);
						X.setBooleanPreferences(Config.PREFS_IS_RATED, true, context);
					}
				}
				break;
			case TYPE_DOWNLOADFOOTBALL:
				Analytics.getInstance().sendPromoEvent(Analytics.BUTTON_INSTALL);
				if (X.isOnline()) {
					context.startActivity(ShopActivity.getMarket(context, "com.futuristicbear.flq"));
					if (!X.getBooleanPreferences(Config.PREFS_IS_FOOTBALLQUIZ, false, context)) {
						Stats.getInstance().setStat(Stats.KEY_COINS, 1000, context);
						X.setBooleanPreferences(Config.PREFS_IS_FOOTBALLQUIZ, true, context);
					}
				}
				break;
			case TYPE_DOWNLOADFLAGS:
				Analytics.getInstance().sendPromoEvent(Analytics.BUTTON_INSTALL);
				if (X.isOnline()) {
					context.startActivity(ShopActivity.getMarket(context, "com.futuristicbear.flagquiz"));
					if (!X.getBooleanPreferences(Config.PREFS_IS_FLAGQUIZ, false, context)) {
						Stats.getInstance().setStat(Stats.KEY_COINS, 1000, context);
						X.setBooleanPreferences(Config.PREFS_IS_FLAGQUIZ, true, context);
					}
				}
				break;
			}
			cancel();
		}

	}

}
