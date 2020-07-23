package com.unindra.flq;

import com.futuristicbear.americanquiz.R;
import com.unindra.flq.data.DbAdapter;
import com.unindra.flq.data.Stats;
import com.unindra.framework.AdController;
import com.unindra.framework.SoundHandler;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StatsActivity extends X implements OnClickListener {

	private DbAdapter db;
	private TextView TV_score;
	private TextView TV_hintsUsed;
	private TextView TV_hints;
	private TextView TV_wrong;
	private TextView TV_perfect;
	private TextView TV_resolved;
	private TextView TV_levels;
	private TextView TV_one;
	private TextView TV_two;
	private Button BTN_reset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);
		db = new DbAdapter(this);
		init();
		AdController ad = new AdController(this, this, AdController.TYPE_BANNER);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Bundle data = getIntent().getExtras();
		initData();
	}

	private void init() {
		initHeader(R.string.head_stats, R.drawable.icon_stats, this);

		TV_score = (TextView) findViewById(R.id.TV_score);
		TV_one = (TextView) findViewById(R.id.TV_one);
		TV_two = (TextView) findViewById(R.id.TV_two);
		TV_levels = (TextView) findViewById(R.id.TV_levels);
		TV_hintsUsed = (TextView) findViewById(R.id.TV_hintsUsed);
		TV_hints = (TextView) findViewById(R.id.TV_hints_available);
		TV_wrong = (TextView) findViewById(R.id.TV_wrongTry);
		TV_perfect = (TextView) findViewById(R.id.TV_perfect);
		TV_resolved = (TextView) findViewById(R.id.TV_resolved);
		BTN_reset = (Button) findViewById(R.id.BTN_reset_database);
		BTN_reset.setOnClickListener(this);
	}

	private void initData() {
		int totalLevels = db.getCountLevels();
		int solvedLevels = db.getCountSolvedLevels();
		int twoStars = db.getCountSolvedLevel2Stars();
		int oneStar = db.getCountSolvedLevel1Stars();
		TV_levels.setText(Integer.toString(solvedLevels) + "/" + Integer.toString(totalLevels));
		TV_score.setText(Integer.toString(Stats.getInstance().getStat(Stats.KEY_SCORE, this)) + "/" + Integer.toString(totalLevels * 3));
		TV_perfect.setText(Integer.toString(Stats.getInstance().getStat(Stats.KEY_3X, this)) + "/" + Integer.toString(solvedLevels));
		TV_one.setText(Integer.toString(oneStar) + "/" + Integer.toString(solvedLevels));
		TV_two.setText(Integer.toString(twoStars) + "/" + Integer.toString(solvedLevels));
		TV_hintsUsed.setText(Integer.toString(Stats.getInstance().getStat(Stats.KEY_USEDHINTS, this)) + "/" + Integer.toString(solvedLevels * 3));
		TV_hints.setText(Integer.toString(Stats.getInstance().getCoins(this)));
		TV_wrong.setText(Integer.toString(Stats.getInstance().getStat(Stats.KEY_WRONG, this)));
		TV_resolved.setText(Integer.toString(Stats.getInstance().getStat(Stats.KEY_RESOLVED, this)) + "/" + Integer.toString(solvedLevels));
	}

	@Override
	public void onClick(View v) {
		SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
		if (v.getId() == R.id.BTN_back) {
			finish();
		} else if (v.getId() == R.id.BTN_reset_database) {
			resetDialog(this).show();
		}
	}

	private AlertDialog resetDialog(final Context c) {
		Builder dialog = new AlertDialog.Builder(this);

		dialog.setTitle(R.string.are_you_sure).setIcon(R.drawable.ic_launcher).setMessage(R.string.reset_database_message_body).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				DbAdapter db = new DbAdapter(c);
				db.restartDatabase();
				Stats.getInstance().resetStats(c);
				initData();
			}
		});

		dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return dialog.create();
	}

}
