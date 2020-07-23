package com.unindra.flq;

import java.util.Random;

import com.futuristicbear.americanquiz.R;
import com.google.analytics.tracking.android.Log;
import com.google.android.gms.games.leaderboard.OnScoreSubmittedListener;
import com.google.android.gms.games.leaderboard.SubmitScoreResult;
import com.unindra.flq.data.Challenge;
import com.unindra.flq.data.DbAdapter;
import com.unindra.flq.data.Question;
import com.unindra.flq.states.Difficulty;
import com.unindra.framework.AdController;
import com.unindra.framework.Logger;
import com.unindra.framework.SoundHandler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChallengeLoopActivity extends X implements OnClickListener {

	private Challenge challenge;
	private Question question;
	private TextView TV_score;
	private TextView TV_time;
	private ImageView IV_image;
	private Button BTN_answ1;
	private Button BTN_answ2;
	private Button BTN_answ3;
	private Button BTN_answ4;
	private DbAdapter db;
	private Thread timerThread;
	private boolean isThreadRunning;
	private LinearLayout LL_container;
	private Bundle startData;
	private boolean showCounter;
	private boolean bDestroy;
	private AdController ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.log("CHALL LOOP: ON CREATE");
		setContentView(R.layout.challenge_loop);
		db = new DbAdapter(this);
		isThreadRunning = true;
		timerThread = null;
		initViews();
		showCounter = true;
		bDestroy = false;
		startData = getIntent().getExtras();
		challenge = new Challenge(startData.getString("DIFF"), 30);
		question = challenge.nextQuestion(db, System.currentTimeMillis());
		initData(question);
		ad = new AdController(ChallengeLoopActivity.this, ChallengeLoopActivity.this, AdController.TYPE_INTERSTITAL);
		initHeader(R.string.head_challenge, R.drawable.icon_challenge, this);
		Logger.log("CHALL LOOP: onCreate");
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			int time = b.getInt("TIME");
			if (time > 9) {
				TV_time.setText("00:" + Integer.toString(time));
			} else {
				TV_time.setText("00:0" + Integer.toString(time));
			}
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		Logger.log("CHALL LOOP: onStart");
		if (timerThread == null) {
			timerThread = new Thread(new Runnable() {

				private boolean isFinishing = false;

				@Override
				public void run() {
					Logger.log("CHALL LOOP: isThreadRunning " + isThreadRunning);
					for (int i = challenge.getTime(); i >= 0; i--) {
						try {
							while (!isThreadRunning) {
								Logger.log("CHALL LOOP: timer thread neuteka");
								if (ChallengeLoopActivity.this.isFinishing()) {
									Logger.log("CHALL LOOP: timer konci");
									isThreadRunning = true;
									isFinishing = true;
								}
							}
							if (!isFinishing) {
								Message msg = new Message();
								Bundle b = new Bundle();
								b.putInt("TIME", i);
								msg.setData(b);
								handler.sendMessage(msg);
								Thread.sleep(1000);
								if (i == 0 && isThreadRunning) {
									isThreadRunning = false;
									Intent intent = new Intent(ChallengeLoopActivity.this, ChallengeResultActivity.class);
									intent.putExtra("SCORE", challenge.getScore());
									intent.putExtra("DIFF", startData.getString("DIFF"));
									intent.putExtra("ALL", challenge.getNumOfQuestions());
									intent.putExtra("TRUE", challenge.getNumOfTrue());
									intent.putExtra("FALSE", challenge.getNumOfFalse());
									bDestroy = true;
									startActivity(intent);
									ad.showInterstiial();
									finish();
								}
							} else {
								// KONEC
								Logger.log("CHALL LOOP: WE ARE DONE HERE...GOOODBYE");
							}
						} catch (Exception e) {
							// TODO catch exeption
						}
					}

				}
			});

			timerThread.start();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		isThreadRunning = false;
		if (!bDestroy) {
			startActivity(new Intent(this, ChallengeCounterActivity.class));
			showCounter = false;
		}
		Logger.log("CHALL LOOP: onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (showCounter) {
			onPause();
		}
		isThreadRunning = true;
		Logger.log("CHALL LOOP: onResume");
	}

	@Override
	public void onBackPressed() {
		bDestroy = true;
		ad.showInterstiial();
		finish();
	}

	private void initViews() {
		Logger.log("CHALL LOOP: INIT VIEWS");
		LL_container = (LinearLayout) findViewById(R.id.LL_chall_loop);
		TV_score = (TextView) findViewById(R.id.TV_score);
		TV_time = (TextView) findViewById(R.id.TV_timer);
		IV_image = (ImageView) findViewById(R.id.IV_image);
		BTN_answ1 = (Button) findViewById(R.id.BTN_answ1);
		BTN_answ2 = (Button) findViewById(R.id.BTN_answ2);
		BTN_answ3 = (Button) findViewById(R.id.BTN_answ3);
		BTN_answ4 = (Button) findViewById(R.id.BTN_answ4);
		BTN_answ1.setOnClickListener(this);
		BTN_answ2.setOnClickListener(this);
		BTN_answ3.setOnClickListener(this);
		BTN_answ4.setOnClickListener(this);
	}

	private void initData(Question q) {
		Logger.log("CHALL LOOP: INIT DATA START");
		IV_image.setImageBitmap(Resources.loadBitmap(Integer.toString(q.getImage()) + ".png"));
		TV_score.setText(Integer.toString(challenge.getScore()));
		setScoreColor();
		int goodPos = new Random().nextInt(4);
		switch (goodPos) {
		case 0:
			initButtons(BTN_answ1, BTN_answ2, BTN_answ3, BTN_answ4);
			break;
		case 1:
			initButtons(BTN_answ2, BTN_answ1, BTN_answ3, BTN_answ4);
			break;
		case 2:
			initButtons(BTN_answ3, BTN_answ1, BTN_answ2, BTN_answ4);
			break;
		case 3:
			initButtons(BTN_answ4, BTN_answ1, BTN_answ2, BTN_answ3);
			break;
		}
	}

	private void initButtons(Button goodBtn, Button bad1, Button bad2, Button bad3) {
		goodBtn.setText(question.getGoodAnswer().toUpperCase());
		bad1.setText(question.getBad1().toUpperCase());
		bad2.setText(question.getBad2().toUpperCase());
		bad3.setText(question.getBad3().toUpperCase());
	}

	@Override
	public void onClick(View v) {
		if (!TV_time.getText().equals("00:00")) {
			SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
			if (v.getId() == R.id.BTN_answ1) {
				handleButtonClick(BTN_answ1, System.currentTimeMillis());
			}
			if (v.getId() == R.id.BTN_answ2) {
				handleButtonClick(BTN_answ2, System.currentTimeMillis());
			}
			if (v.getId() == R.id.BTN_answ3) {
				handleButtonClick(BTN_answ3, System.currentTimeMillis());
			}
			if (v.getId() == R.id.BTN_answ4) {
				handleButtonClick(BTN_answ4, System.currentTimeMillis());
			}
			if (v.getId() == R.id.BTN_back) {
				bDestroy = true;
				ad.showInterstiial();
				finish();
			}

		}

	}

	private void handleButtonClick(Button btn, long time) {
		if (challenge.isGoodAnswer(btn.getText().toString(), time)) {
			// GOOD ANSWER
		} else {
			// BAD ANSWER
		}
		question = challenge.nextQuestion(db, System.currentTimeMillis());
		animate(R.anim.fade_in, LL_container);
		initData(question);
	}

	
	private void setScoreColor() {
		int score = Integer.parseInt(TV_score.getText().toString());
		if (score < 0) {
			TV_score.setTextColor(getResources().getColor(R.color.red));
		} else {
			TV_score.setTextColor(getResources().getColor(R.color.text_color));
		}
	}

}
