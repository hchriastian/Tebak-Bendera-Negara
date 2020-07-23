package com.unindra.flq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.futuristicbear.americanquiz.R;
import com.unindra.flq.data.LeaderboardRecord;
import com.unindra.flq.states.Difficulty;
import com.unindra.framework.AdController;
import com.unindra.framework.SoundHandler;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class LeaderboardActivity extends X implements OnTabChangeListener, OnClickListener {

	private TabHost tabHost;
	private ListView content;
	private Context context;
	private ArrayList<LeaderboardRecord> recordList;
	private String currentTab;
	private ProgressBar progressBar;
	private TextView tvMyPosition;
	private TextView tvTopScores;
	private TabWidget tabWidget;
	private LinearLayout myScore;
	private TextView tvMyRank;
	private TextView tvMyName;
	private TextView tvMyScore;
	private ItemAdapter itemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leaderboard);
		context = this.getApplicationContext();
		Bundle data = getIntent().getExtras();
		currentTab = Difficulty.MEDIUM;
		if (data != null && !data.getString("DIFF").equals(Difficulty.EASY)) {
			currentTab = data.getString("DIFF");
		}
		tabHost = (TabHost) findViewById(R.id.TH_leaderboard_tabhost);
		content = (ListView) findViewById(R.id.LV_leaderboards);
		progressBar = (ProgressBar) findViewById(R.id.PG_leaderboard);
		tvMyPosition = (TextView) findViewById(R.id.TV_myposition);
		tvTopScores = (TextView) findViewById(R.id.TV_topScores);
		myScore = (LinearLayout) findViewById(R.id.LL_leaderboard_item);
		tvMyRank = (TextView) findViewById(R.id.TV_rank);
		tvMyName = (TextView) findViewById(R.id.TV_name);
		tvMyScore = (TextView) findViewById(R.id.TV_score);
		recordList = new ArrayList<LeaderboardRecord>();
		itemAdapter = new ItemAdapter();
		content.setAdapter(itemAdapter);
		initHeader(R.string.head_leaderboard, R.drawable.icon_leaderboard, this);
		initTabs();
	}

	private void initTabs() {

		tabHost.setup();

		addTab(Difficulty.EASY);
		addTab(Difficulty.MEDIUM);
		addTab(Difficulty.HARD);

		tabHost.setOnTabChangedListener(this);
		tabHost.setCurrentTabByTag(currentTab);

		tabWidget = tabHost.getTabWidget();

		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			tabWidget.getChildAt(i).setBackgroundResource(R.drawable.tab_xml);
			tabWidget.getChildAt(i).setClickable(false);
		}

	}

	private void addTab(String label) {
		TabSpec spec = tabHost.newTabSpec(label);
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_button, tabHost.getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.TV_title);
		title.setText(label.toUpperCase());
		spec.setContent(R.id.LV_leaderboards);
		spec.setIndicator(tabIndicator);
		tabHost.addTab(spec);
	}

	@Override
	public void onTabChanged(String tabId) {
		showProgressBar(View.VISIBLE);
		recordList.clear();
		if (tabId.equals(Difficulty.EASY)) {
			getOnlineData("easy");
		} else if (tabId.equals(Difficulty.MEDIUM)) {
			getOnlineData("medium");
		} else if (tabId.equals(Difficulty.HARD)) {
			getOnlineData("hard");
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.BTN_back) {
			SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
			finish();
		}
	}

	protected void showProgressBar(int visibility) {
		progressBar.setVisibility(visibility);

		if (visibility == View.INVISIBLE) {
			for (int i = 0; i < tabWidget.getChildCount(); i++) {
				tabWidget.getChildAt(i).setClickable(true);
			}
		} else if (visibility == View.VISIBLE && tabWidget != null) {
			for (int i = 0; i < tabWidget.getChildCount(); i++) {
				tabWidget.getChildAt(i).setClickable(false);
			}
		}
	}

	private void getOnlineData(String diff) {
		tvMyPosition.setVisibility(View.INVISIBLE);
		myScore.setVisibility(View.INVISIBLE);
		tvTopScores.setVisibility(View.INVISIBLE);
		GetLeaderboad a = new GetLeaderboad();
		a.execute(Config.ONLINE_LEADERBOARD_URL, Config.SECRET_PASSWORD_LEADERBOAR, "show", diff);
	}

	private class GetLeaderboad extends AsyncTask<String, String, String> {

		private boolean isDeviceRanked = false;
		private int myPosition;
		private int myScore;

		@Override
		protected String doInBackground(String... uri) {
			HttpResponse response;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("secret", uri[1]));
			nameValuePairs.add(new BasicNameValuePair("id", uri[2]));
			nameValuePairs.add(new BasicNameValuePair("diff", uri[3]));

			String responseString = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(uri[0]);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httppost);
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
					JSONArray jArray;
					try {
						jArray = new JSONArray(responseString);
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject jObject = jArray.getJSONObject(i);
							String name = jObject.getString("name");
							int score = jObject.getInt("score");
							if (jObject.getString("device").equalsIgnoreCase(sha1(getDeviceId()))) {
								myPosition = i + 1;
								myScore = score;
								isDeviceRanked = true;
							}
							recordList.add(new LeaderboardRecord(name, score));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			return responseString;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			showProgressBar(View.INVISIBLE);
			LeaderboardActivity.this.tvMyPosition.setVisibility(View.VISIBLE);
			LeaderboardActivity.this.tvTopScores.setVisibility(View.VISIBLE);
			LeaderboardActivity.this.myScore.setVisibility(View.VISIBLE);
			if (!isDeviceRanked) {
				LeaderboardActivity.this.tvMyRank.setText("N/A");
				LeaderboardActivity.this.tvMyName.setText(getPlayerNameFromPreferences(LeaderboardActivity.this));
				LeaderboardActivity.this.tvMyScore.setText("N/A");
			} else {
				LeaderboardActivity.this.tvMyRank.setText(myPosition + ".");
				LeaderboardActivity.this.tvMyName.setText(getPlayerNameFromPreferences(LeaderboardActivity.this));
				LeaderboardActivity.this.tvMyScore.setText(Integer.toString(myScore));
			}
			//itemAdapter.notifyDataSetChanged();
			//content.setAdapter(new ItemAdapter());
			itemAdapter.notifyDataSetChanged();
		}
	}

	private class ItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return recordList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View itemView;

			itemView = new View(context);
			itemView = inflater.inflate(R.layout.leaderboard_item, null);
			if (arg0 % 2 == 0) {
				itemView.setBackgroundColor(Color.rgb(205, 205, 205));
			}
			TextView rank = (TextView) itemView.findViewById(R.id.TV_rank);
			TextView name = (TextView) itemView.findViewById(R.id.TV_name);
			TextView score = (TextView) itemView.findViewById(R.id.TV_score);
			rank.setText(Integer.toString(arg0 + 1) + ".");
			name.setText(recordList.get(arg0).getName());
			score.setText(Integer.toString(recordList.get(arg0).getScore()));

			return itemView;
		}

	}

}
