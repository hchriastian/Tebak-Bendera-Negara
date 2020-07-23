package com.unindra.flq;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;

import com.futuristicbear.americanquiz.R;
import com.unindra.flq.data.DbAdapter;
import com.unindra.flq.data.HRArrayAdapter;
import com.unindra.flq.data.Level;
import com.unindra.flq.data.Stats;
import com.unindra.flq.states.LevelState;
import com.unindra.framework.AdController;
import com.unindra.framework.Analytics;
import com.unindra.framework.Logger;
import com.unindra.framework.SoundHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LevelViewActivity extends X implements TextWatcher, OnClickListener, OnItemClickListener {

	private ImageView IV_image;
	private Button BTN_done;
	private ImageButton BTN_hint;
	private ImageButton BTN_nameList;
	private ImageButton BTN_resolve;
	private AutoCompleteTextView ACTV_input;
	private DbAdapter db;
	private Level level;
	private int hintsUsed;
	private String[] hintsData;
	private int wrongTry = 0;
	private ArrayList<String> clubNames = new ArrayList<String>();
	private Context context;
	private AdController ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.level_view);
		db = new DbAdapter(this);
		ad = new AdController(this, this, AdController.TYPE_INTERSTITAL);
		Bundle data = getIntent().getExtras();
		level = data.getParcelable("LEVEL");
		hintsUsed = getIntegerPreferences("LEVEL" + level.getId(), 0, this);
		init();
		

	}

	private void init() {
		initHeaderWithScorePanel("LEVEL " + level.getEpisodeId(), R.drawable.icon_question, Stats.getInstance().getStat(Stats.KEY_SCORE, this), Stats.getInstance().getCoins(this), this);

		IV_image = (ImageView) findViewById(R.id.IV_image);
		IV_image.setImageBitmap(Resources.loadBitmap(Integer.toString(level.getImage()) + ".png"));

		BTN_done = (Button) findViewById(R.id.BTN_done);
		BTN_hint = (ImageButton) findViewById(R.id.BTN_hint);
		BTN_nameList = (ImageButton) findViewById(R.id.BTN_clubList);
		BTN_resolve = (ImageButton) findViewById(R.id.BTN_resolve);

		ACTV_input = (AutoCompleteTextView) findViewById(R.id.ACTV_insertTeamName);
		ACTV_input.addTextChangedListener(this);
		ACTV_input.setOnItemClickListener(this);

		clubNames = db.getAllClubNames();
		ACTV_input.setAdapter(new HRArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, clubNames));

		BTN_done.setOnClickListener(this);
		BTN_resolve.setOnClickListener(this);
		BTN_hint.setOnClickListener(this);
		BTN_nameList.setOnClickListener(this);
		hintsData = getResources().getStringArray(level.getHints());
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.BTN_hint) {
			SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
			Analytics.getInstance().sendLevelSolveEvent("HINT BUTTON", level.getName());
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			new HintsDialog(this, android.R.style.Theme_Translucent_NoTitleBar, inflater.inflate(R.layout.hints_dialog, null), hintsData).show();
		}
		if (v.getId() == R.id.BTN_done) {
			boolean isGoodAnswer = false;
			String inputText = removeDiacriticalMarks(ACTV_input.getText().toString());
			for (int i = 0; i < level.getNames().length; i++) {
				if (inputText.equalsIgnoreCase(removeDiacriticalMarks(level.getName(i)))) {
					SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_SUCCESS);
					isGoodAnswer = true;
					int[] resState = getResultState();
					level.setState(resState[0]);
					Stats.getInstance().setStat(Stats.KEY_WRONG, wrongTry, this);
					db.updateLevelState(level);
					Analytics.getInstance().sendLevelSolveEvent("DONE GOOD BUTTON", level.getName());
					Intent intent = new Intent();
					intent.putExtra("LEVEL", level);
					intent.putExtra("COINS", resState[1]);
					setResult(1, intent);
					ad.showInterstiial();
					finish();
				}
			}
			if (!isGoodAnswer) {
				SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_WRONG);
				vibrate(this, 300);
				Analytics.getInstance().sendLevelSolveEvent("DONE WRONG BUTTON", level.getName());
				animate(R.anim.left_to_right, IV_image);
				Toast.makeText(this, R.string.try_again, Toast.LENGTH_SHORT).show();
				wrongTry++;
			}
		}

		if (v.getId() == R.id.BTN_clubList) {
			SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
			Analytics.getInstance().sendLevelSolveEvent("LEVEL LIST CLICK", level.getName());
			closeSoftwareKeyboard();
			AlertDialog dialog = clubNamesDialog();
			dialog.show();
		}

		if (v.getId() == R.id.BTN_resolve) {
			SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
			Analytics.getInstance().sendLevelSolveEvent("RESOLVE", level.getName());
			if (Stats.getInstance().getCoins(this) < 2000) {
				Toast.makeText(this, R.string.no_resolve, Toast.LENGTH_SHORT).show();
			} else {
				resolveDialog().show();
			}
		}

		if (v.getId() == R.id.BTN_back) {
			SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
			Analytics.getInstance().sendUiClickEvent(Analytics.BUTTON_BACK);
			Stats.getInstance().setStat(Stats.KEY_WRONG, 1, this);
			ad.showInterstiial();
			finish();
		}

	}

	@Override
	public void onBackPressed() {
		Analytics.getInstance().sendUiClickEvent(Analytics.BUTTON_BACK_HARDWARE);
		super.onBackPressed();
		Stats.getInstance().setStat(Stats.KEY_WRONG, 1, this);
		ad.showInterstiial();
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	// add points to stats and return level state
	private int[] getResultState() {
		if (wrongTry == 0) {
			Stats.getInstance().setStat(Stats.KEY_COINS, 100, this);
			Stats.getInstance().setStat(Stats.KEY_SCORE, 3, this);
			Stats.getInstance().setStat(Stats.KEY_3X, 1, this);
			return new int[] { LevelState.SOLVED_THREE, 100 };
		} else if (wrongTry == 1) {
			Stats.getInstance().setStat(Stats.KEY_SCORE, 2, this);
			Stats.getInstance().setStat(Stats.KEY_COINS, 75, this);
			Stats.getInstance().setStat(Stats.KEY_2X, 1, this);
			return new int[] { LevelState.SOLVED_TWO, 75 };
		} else if (wrongTry < 6) {
			int helper = 5 - wrongTry + 2;
			Stats.getInstance().setStat(Stats.KEY_COINS, 10 * helper, this);
			Stats.getInstance().setStat(Stats.KEY_1X, 1, this);
			Stats.getInstance().setStat(Stats.KEY_SCORE, 1, this);
			return new int[] { LevelState.SOLVED_ONE, 10 * helper };
		} else if (wrongTry < 15) {
			int helper = 9 - wrongTry + 6;
			Stats.getInstance().setStat(Stats.KEY_COINS, 2 * helper, this);
			Stats.getInstance().setStat(Stats.KEY_1X, 1, this);
			Stats.getInstance().setStat(Stats.KEY_SCORE, 1, this);
			return new int[] { LevelState.SOLVED_ONE, 2 * helper };
		} else {
			Stats.getInstance().setStat(Stats.KEY_COINS, 2, this);
			Stats.getInstance().setStat(Stats.KEY_1X, 1, this);
			Stats.getInstance().setStat(Stats.KEY_SCORE, 1, this);
			return new int[] { LevelState.SOLVED_ONE, 2 };
		}
	}

	public static String removeDiacriticalMarks(String string) {
		return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	private AlertDialog clubNamesDialog() {

		AlertDialog listDialog = new AlertDialog.Builder(this).setSingleChoiceItems(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, clubNames), 1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				ACTV_input.setText(clubNames.get(which));
				dialog.dismiss();
				ACTV_input.dismissDropDown();
			}
		}).create();

		return listDialog;
	}

	// po kliku na hint zavri softverovu klavesnicu
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		closeSoftwareKeyboard();
	}

	private void closeSoftwareKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private AlertDialog resolveDialog() {
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(R.string.are_you_sure).setMessage(R.string.resolve_message_body).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				level.setState(LevelState.RESOLVED);
				Stats.getInstance().setStat(Stats.KEY_COINS, -2000, context);
				Stats.getInstance().setStat(Stats.KEY_RESOLVED, 1, context);
				db.updateLevelState(level);
				Intent intent = new Intent();
				intent.putExtra("LEVEL", level);
				setResult(1, intent);
				finish();
			}
		}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		return dialog.create();
	}

	private class HintsDialog extends Dialog implements android.view.View.OnClickListener {

		private TextView hint1;
		private TextView hint2;
		private TextView hint3;
		private Button BTN_getHint;

		public HintsDialog(Context context, int theme, View view, String[] hints) {
			super(context, theme);
			requestWindowFeature(Window.FEATURE_NO_TITLE);

			getWindow().setLayout(getScreenWidth() - getScreenWidth() / 5, LayoutParams.WRAP_CONTENT);
			getWindow().setGravity(Gravity.CENTER);
			setContentView(view);
			setCanceledOnTouchOutside(true);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.dimAmount = 0.6f;
			getWindow().setAttributes(lp);
			hint1 = (TextView) view.findViewById(R.id.TV_hint_1);
			hint2 = (TextView) view.findViewById(R.id.TV_hint_2);
			hint3 = (TextView) view.findViewById(R.id.TV_hint_3);
			hint1.setVisibility(View.INVISIBLE);
			hint2.setVisibility(View.INVISIBLE);
			hint3.setVisibility(View.INVISIBLE);
			ImageButton BTN_close = (ImageButton) view.findViewById(R.id.IV_closeView);
			BTN_close.setOnClickListener(this);
			BTN_getHint = (Button) view.findViewById(R.id.BTN_getHint);
			BTN_getHint.setOnClickListener(this);
			switch (hintsUsed) {
			case 0:
				break;
			case 1:
				hint1.setVisibility(View.VISIBLE);
				hint1.setText(hintsData[0]);
				break;
			case 2:
				hint1.setVisibility(View.VISIBLE);
				hint1.setText(hintsData[0]);
				hint2.setVisibility(View.VISIBLE);
				hint2.setText(hintsData[1]);
				break;
			case 3:
				hint1.setVisibility(View.VISIBLE);
				hint1.setText(hintsData[0]);
				hint2.setVisibility(View.VISIBLE);
				hint2.setText(hintsData[1]);
				hint3.setVisibility(View.VISIBLE);
				hint3.setText(hintsData[2]);
				BTN_getHint.setVisibility(View.INVISIBLE);
				break;
			}

		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.IV_closeView) {
				cancel();
			}
			if (v.getId() == R.id.BTN_getHint) {
				SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
				if (hintsUsed < 3 && Stats.getInstance().getCoins(context) >= 400) {
					if (hintsUsed == 0) {
						hint1.setVisibility(View.VISIBLE);
						hint1.setText(hintsData[hintsUsed]);
						hintsUsed++;
						Stats.getInstance().setStat(Stats.KEY_COINS, -400, context);
						Stats.getInstance().setStat(Stats.KEY_USEDHINTS, 1, context);
						updateHeaderData(Stats.getInstance().getCoins(context));
					} else if (hintsUsed == 1) {
						hint2.setVisibility(View.VISIBLE);
						hint2.setText(hintsData[hintsUsed]);
						hintsUsed++;
						Stats.getInstance().setStat(Stats.KEY_COINS, -400, context);
						Stats.getInstance().setStat(Stats.KEY_USEDHINTS, 1, context);
						updateHeaderData(Stats.getInstance().getCoins(context));
					} else if (hintsUsed == 2) {
						hint3.setVisibility(View.VISIBLE);
						hint3.setText(hintsData[hintsUsed]);
						hintsUsed++;
						Stats.getInstance().setStat(Stats.KEY_COINS, -400, context);
						Stats.getInstance().setStat(Stats.KEY_USEDHINTS, 1, context);
						updateHeaderData(Stats.getInstance().getCoins(context));
						BTN_getHint.setVisibility(View.INVISIBLE);
					}
					LevelViewActivity.this.setIntegerPreferences("LEVEL" + level.getId(), hintsUsed, LevelViewActivity.this);
				} else {
					SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_WRONG);
					Toast.makeText(context, R.string.no_hints, Toast.LENGTH_SHORT).show();
				}
			}

		}
	}

}
