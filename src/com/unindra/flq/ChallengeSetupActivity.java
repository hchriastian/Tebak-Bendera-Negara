package com.unindra.flq;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuristicbear.americanquiz.R;
import com.unindra.flq.data.Stats;
import com.unindra.flq.states.Difficulty;
import com.unindra.framework.AdController;
import com.unindra.framework.SoundHandler;

public class ChallengeSetupActivity extends X implements OnClickListener {

	private Button BTN_easy;
	private Button BTN_medium;
	private Button BTN_hard;
	private String diff;
	private int gamePoints;
	private EditText ET_playerName;
	private Button BTN_setName;
	private Context context;
	private AdController ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.challenge_setup);
		init();
	}

	@Override
	protected void onStart() {
		super.onStart();
		ad = new AdController(this, this, AdController.TYPE_PROMO);
	}

	private void init() {
		initHeaderWithScorePanel(R.string.head_chall_setup, R.drawable.icon_challenge, Stats.getInstance().getStat(Stats.KEY_SCORE, this), Stats.getInstance().getCoins(this), this);
		BTN_easy = (Button) findViewById(R.id.btn_diff_easy);
		BTN_medium = (Button) findViewById(R.id.btn_diff_medium);
		BTN_hard = (Button) findViewById(R.id.btn_diff_hard);
		ET_playerName = (EditText) findViewById(R.id.ET_playerName);
		BTN_setName = (Button) findViewById(R.id.BTN_set_name);
	}

	private void initData() {

		gamePoints = Stats.getInstance().getStat(Stats.KEY_COINS, this);
		initHeaderWithScorePanel(R.string.head_chall_setup, R.drawable.icon_challenge, Stats.getInstance().getStat(Stats.KEY_SCORE, this), gamePoints, this);
		final Drawable drawableLock = getResources().getDrawable(R.drawable.icon_chall_lock);
		final Drawable drawableStars = getResources().getDrawable(R.drawable.icon_chall_unl);

		if (getBooleanPreferences(Config.PREFS_EASY_UNLOCKED, false, this)) {
			BTN_easy.setCompoundDrawablesWithIntrinsicBounds(drawableStars, null, null, null);
			BTN_easy.setText(R.string.easy);
		} else {
			BTN_easy.setCompoundDrawablesWithIntrinsicBounds(drawableLock, null, null, null);
			BTN_easy.setText(R.string.easy_locked);
		}

		if (getBooleanPreferences(Config.PREFS_MEDIUM_UNLOCKED, false, this)) {
			BTN_medium.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableStars, null);
			BTN_medium.setText(R.string.medium);
		} else {
			BTN_medium.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableLock, null);
			BTN_medium.setText(R.string.medium_locked);
		}

		if (getBooleanPreferences(Config.PREFS_HARD_UNLOCKED, false, this)) {
			BTN_hard.setCompoundDrawablesWithIntrinsicBounds(drawableStars, null, null, null);
			BTN_hard.setText(R.string.hard);
		} else {
			BTN_hard.setCompoundDrawablesWithIntrinsicBounds(drawableLock, null, null, null);
			BTN_hard.setText(R.string.hard_locked);
		}
		
		ET_playerName.setText(getPlayerNameFromPreferences(this));
		
		BTN_setName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				savePlayerName(ET_playerName.getText().toString(), ChallengeSetupActivity.this);
			}
		});

		BTN_easy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
				// TODO Auto-generated method stub
				if (getBooleanPreferences(Config.PREFS_EASY_UNLOCKED, false, context)) {
					diff = Difficulty.EASY;
					startChallenge(diff);
				} else {
					if (gamePoints < Config.COINS_EASY_NEEDED) {
						new ChallSetupDialog(context, ChallSetupDialog.TYPE_NOT_ENOUGH, Difficulty.EASY).show();
					} else {
						new ChallSetupDialog(context, ChallSetupDialog.TYPE_CONFIRM, Difficulty.EASY).show();
					}
				}
			}
		});

		BTN_medium.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
				// TODO Auto-generated method stub
				if (getBooleanPreferences(Config.PREFS_MEDIUM_UNLOCKED, false, context)) {
					diff = Difficulty.MEDIUM;
					startChallenge(diff);
				} else {
					if (gamePoints < Config.COINS_MEDIUM_NEEDED) {
						new ChallSetupDialog(context, ChallSetupDialog.TYPE_NOT_ENOUGH, Difficulty.MEDIUM).show();
					} else {
						new ChallSetupDialog(context, ChallSetupDialog.TYPE_CONFIRM, Difficulty.MEDIUM).show();
					}
				}
			}

		});

		BTN_hard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
				// TODO Auto-generated method stub
				if (getBooleanPreferences(Config.PREFS_HARD_UNLOCKED, false, context)) {
					diff = Difficulty.HARD;
					startChallenge(diff);
				} else {
					if (gamePoints < Config.COINS_HARD_NEEDED) {
						new ChallSetupDialog(context, ChallSetupDialog.TYPE_NOT_ENOUGH, Difficulty.HARD).show();
					} else {
						new ChallSetupDialog(context, ChallSetupDialog.TYPE_CONFIRM, Difficulty.HARD).show();
					}
				}
			}

		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void startChallenge(String diff) {
		ad.showInterstiial();
		savePlayerName(ET_playerName.getText().toString(), ChallengeSetupActivity.this);
		Intent i = new Intent(this, ChallengeLoopActivity.class);
		i.putExtra("DIFF", diff);
		startActivity(i);
		finish();
	}

	@Override
	public void onClick(View v) {

		SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
		if (v.getId() == R.id.BTN_back) {
			ad.showInterstiial();
			savePlayerName(ET_playerName.getText().toString(), ChallengeSetupActivity.this);
			finish();
		} else if (v.getId() == R.id.IV_headerb_button) {
			ad.showInterstiial();
			savePlayerName(ET_playerName.getText().toString(), ChallengeSetupActivity.this);
			Intent i = new Intent(this, ShopActivity.class);
			startActivity(i);
		}
	}

	public class ChallSetupDialog extends Dialog implements android.view.View.OnClickListener {

		private TextView tvTitle;
		private TextView tvBody;
		private Button btnPositive;
		private Button btnNegative;
		private ImageButton ibClose;
		private ImageView ivImage;
		private int type;
		private Context context;
		private String diff;
		private String config;
		private int coins = 0;

		public static final int TYPE_CONFIRM = 0;
		public static final int TYPE_NOT_ENOUGH = 1;

		public ChallSetupDialog(Context context, int type, String diff) {
			super(context, android.R.style.Theme_Translucent_NoTitleBar);
			this.type = type;
			this.diff = diff;
			this.context = context;
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
			initializeData();
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

		private void initializeData() {

			switch (type) {
			case TYPE_NOT_ENOUGH:
				ivImage.setBackgroundResource(R.drawable.icon_coins);
				btnPositive.setText(R.string.visit_shop);
				tvTitle.setText(R.string.not_enough);
				tvBody.setText(R.string.buy_coins_message);
				btnNegative.setText(R.string.cancel);
				break;
			case TYPE_CONFIRM:

				if (diff == Difficulty.EASY) {
					coins = Config.COINS_EASY_NEEDED;
					config = Config.PREFS_EASY_UNLOCKED;
				} else if (diff == Difficulty.MEDIUM) {
					coins = Config.COINS_MEDIUM_NEEDED;
					config = Config.PREFS_MEDIUM_UNLOCKED;
				} else if (diff == Difficulty.HARD) {
					coins = Config.COINS_HARD_NEEDED;
					config = Config.PREFS_HARD_UNLOCKED;
				}

				String strBody = getResources().getString(R.string.really_want_unlock);
				String strBodyData = String.format(strBody, diff, Math.abs(coins));

				btnNegative.setText(R.string.no);
				ivImage.setImageResource(R.drawable.icon_lock);
				btnPositive.setText(R.string.yes);
				tvTitle.setText(R.string.are_you_sure);
				tvBody.setText(strBodyData);
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
				case TYPE_NOT_ENOUGH:
					context.startActivity(new Intent(ChallengeSetupActivity.this, ShopActivity.class));
					break;
				case TYPE_CONFIRM:
					setBooleanPreferences(config, true, ChallengeSetupActivity.this);
					Stats.getInstance().setStat(Stats.KEY_COINS, coins - 2 * coins, ChallengeSetupActivity.this);
					initData();
					break;
				}
				cancel();
			}

		}

	}

}
