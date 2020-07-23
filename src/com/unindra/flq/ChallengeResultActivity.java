package com.unindra.flq;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.futuristicbear.americanquiz.R;
import com.unindra.flq.data.Stats;
import com.unindra.flq.states.Difficulty;
import com.unindra.framework.Analytics;
import com.unindra.framework.Logger;
import com.unindra.framework.SoundHandler;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ChallengeResultActivity extends X implements OnClickListener {

	private int score;
	private int coinsEarned;
	private int numOfQuestions;
	private int numOfTrue;
	private int numOfFalse;
	private TextView TV_score;
	private TextView TV_allQuestions;
	private TextView TV_numOfTrue;
	private TextView TV_numOfFalse;
	private TextView TV_coins;
	private Button BTN_menu;
	private String diff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_result);
		Bundle data = getIntent().getExtras();
		score = data.getInt("SCORE");
		numOfQuestions = data.getInt("ALL");
		numOfFalse = data.getInt("FALSE");
		numOfTrue = data.getInt("TRUE");
		diff = data.getString("DIFF");
		if (score < 0) {
			coinsEarned = 0;
		} else {
			coinsEarned = score / 12;
			Stats.getInstance().setStat(Stats.KEY_COINS, coinsEarned, this);
		}
		init();

		Analytics.getInstance().sendChallengeEvent("GENERATED NUM", Integer.toString(numOfQuestions));
		Analytics.getInstance().sendChallengeEvent("TRUE NUM ", Integer.toString(numOfTrue));
		Analytics.getInstance().sendChallengeEvent("FALSE NUM", Integer.toString(numOfFalse));

		if (isOnline()) {
			new PostResultToOnlineDatabase().execute(Config.ONLINE_LEADERBOARD_URL, Config.SECRET_PASSWORD_LEADERBOAR, "insert", diff, getPlayerNameFromPreferences(this), Integer.toString(score), getDeviceId());
		}
		
	}

	private void init() {
		// Init Header
		initHeader(R.string.head_chall_result, R.drawable.icon_challenge, this);
		BTN_menu = (Button) findViewById(R.id.BTN_main_menu);
		BTN_menu.setOnClickListener(this);
		TV_score = (TextView) findViewById(R.id.TV_score);
		TV_allQuestions = (TextView) findViewById(R.id.TV_allQuestions);
		TV_numOfTrue = (TextView) findViewById(R.id.TV_correct);
		TV_numOfFalse = (TextView) findViewById(R.id.TV_wrong);
		TV_score.setText(Integer.toString(score));
		TV_coins = (TextView) findViewById(R.id.TV_coins_earned);
		TV_coins.setText("+ " + Integer.toString(coinsEarned));
		TV_allQuestions.setText(getResources().getQuantityString(R.plurals.logo, numOfQuestions, numOfQuestions));
		TV_numOfTrue.setText(getResources().getQuantityString(R.plurals.logo, numOfTrue, numOfTrue));
		TV_numOfFalse.setText(getResources().getQuantityString(R.plurals.logo, numOfFalse, numOfFalse));

		LinearLayout llTop = (LinearLayout) findViewById(R.id.LL_answers);
		ImageView ivCoins = (ImageView) findViewById(R.id.IV_coins);
		TextView tvScoreLabel = (TextView) findViewById(R.id.TV_score_label);
		animate(R.anim.chal_res1, llTop);
		animate(R.anim.chal_res2, TV_score);
		animate(R.anim.chal_res2, tvScoreLabel);
		animate(R.anim.chal_res3, TV_coins);
		animate(R.anim.chal_res3, ivCoins);
	}
	

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.BTN_back || v.getId() == R.id.BTN_main_menu) {
			SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
			finish();
		}
	}
	
	
	
	private class PostResultToOnlineDatabase extends AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... params) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("secret", params[1]));
			nameValuePairs.add(new BasicNameValuePair("id", params[2]));
			nameValuePairs.add(new BasicNameValuePair("diff", params[3]));
			nameValuePairs.add(new BasicNameValuePair("name", params[4]));
			nameValuePairs.add(new BasicNameValuePair("score", params[5]));
			nameValuePairs.add(new BasicNameValuePair("dev_id", sha1(params[6])));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(params[0]);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				return Integer.parseInt(responseString);

			} catch (Exception e) {
				return -1;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			Logger.log("POSTED TO THE LEADERBOARD");
		}

	}

	
	

}
