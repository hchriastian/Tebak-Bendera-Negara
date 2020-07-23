package com.unindra.flq;

import java.util.Timer;
import java.util.TimerTask;

import com.futuristicbear.americanquiz.R;
import com.unindra.framework.Logger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ChallengeCounterActivity extends X {

	private TextView TV_counter;
	private boolean isThreadRunning;
	private int time;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			int time = b.getInt("TIME");
			if (time > 0) {
				TV_counter.setText(Integer.toString(time));
				scaleAnimation();
			} else {
				TV_counter.setText("");
				Logger.log("CHALL COUNTER: STARTING CHALLENGE LOOP");
				finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_counter);
		TV_counter = (TextView) findViewById(R.id.TV_counter);
		Logger.log("CHALL COUNTER: onCreate");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Logger.log("CHALL COUNTER: onStart");
		isThreadRunning = true;
		time = 3;
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
				if (time >= 0 && isThreadRunning) {
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putInt("TIME", time);
					msg.setData(b);
					handler.sendMessage(msg);
					time--;
				} else {
					isThreadRunning = false;
				}
			}

		}, 0, 1000);
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onPause() {
		super.onPause();
		isThreadRunning = false;
		Logger.log("CHALL COUNTER: onPause");
		finish();
	}
	
	
	

	private void scaleAnimation() {
		Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
		a.reset();
		TV_counter.clearAnimation();
		TV_counter.startAnimation(a);
	}

}
