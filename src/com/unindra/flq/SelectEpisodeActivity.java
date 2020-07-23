package com.unindra.flq;

import java.util.List;

import com.futuristicbear.americanquiz.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.unindra.flq.data.DbAdapter;
import com.unindra.flq.data.Episode;
import com.unindra.flq.data.Stats;
import com.unindra.framework.AdController;
import com.unindra.framework.Analytics;
import com.unindra.framework.SoundHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SelectEpisodeActivity extends X implements OnClickListener {

	private Button BTN_stats;
	private DbAdapter db;
	private Stats statistics;
	private ListView container;
	private ButtonAdapter adapter;
	private List<Episode> episodes;
	private Context context;
	private AdController ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_episode);
		db = new DbAdapter(this);
		context = this;
		episodes = db.getAllEpisodes();
		init();

	}
	
	@Override
	protected void onStart() {
		super.onStart();
		ad = new AdController(this, this, AdController.TYPE_PROMO);
	}

	private void init() {
		initHeaderWithScorePanel(R.string.head_select_episode, R.drawable.icon_question, Stats.getInstance().getStat(Stats.KEY_SCORE, this), Stats.getInstance().getCoins(this), this);
		BTN_stats = (Button) findViewById(R.id.BTN_stats);
		BTN_stats.setOnClickListener(this);
		container = (ListView) findViewById(R.id.LV_episodeButtons);
		adapter = new ButtonAdapter(this);
		container.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateHeaderData(Stats.getInstance().getStat(Stats.KEY_SCORE, this), Stats.getInstance().getCoins(this));
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
		if (v.getId() == R.id.BTN_back) {
			ad.showInterstiial();
			finish();
		} else if (v.getId() == R.id.BTN_stats) {
			ad.showInterstiial();
			Intent i = new Intent(this, StatsActivity.class);
			startActivity(i);
		} else if (v.getId() == R.id.IV_headerb_button) {
			ad.showInterstiial();
			Intent i = new Intent(this, ShopActivity.class);
			startActivity(i);
		} else {
			ad.showInterstiial();
			EasyTracker.getTracker().sendEvent("ui_action", "button_press", "episode_play", (long) v.getId());
			Bundle b = new Bundle();
			b.putInt("EPISODE_ID", v.getId());
			Intent i = new Intent(this, SelectLevelActivity.class);
			i.putExtras(b);
			startActivity(i);
		}
	}

	private class ButtonAdapter extends BaseAdapter {

		private Context c;

		public ButtonAdapter(Context c) {
			this.c = c;
		}

		@Override
		public int getCount() {
			return episodes.size();
		}

		@Override
		public Object getItem(int arg0) {
			return episodes.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View buttonView;
			// if (convertView == null) {
			buttonView = new View(c);
			buttonView = inflater.inflate(R.layout.episode_button, null);
			TextView title = (TextView) buttonView.findViewById(R.id.TV_episode_title);
			TextView score = (TextView) buttonView.findViewById(R.id.TV_episode_score_num);
			TextView logos = (TextView) buttonView.findViewById(R.id.TV_episode_logos_num);
			final LinearLayout container = (LinearLayout) buttonView.findViewById(R.id.LL_episode_button);
			ProgressBar pgLogos = (ProgressBar) buttonView.findViewById(R.id.PG_logos);
			Button play = (Button) buttonView.findViewById(R.id.BTN_play_episode);
			play.setId(episodes.get(position).getId());

			// ak je level locknuty
			if (Stats.getInstance().getStat(Stats.KEY_SCORE, context) < episodes.get(position).getReqPoints()) {

				final int pos = position;
				play.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Analytics.getInstance().sendUiClickEvent(Analytics.BUTTON_EP_LOCKED);
						SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_WRONG);
						animate(R.anim.left_to_right, container);
						vibrate(c, 300);
						int pointsNeeded = episodes.get(pos).getReqPoints() - Stats.getInstance().getStat(Stats.KEY_SCORE, context);
						Toast.makeText(c, Integer.toString(pointsNeeded) + " " + getResources().getString(R.string.points_needed), Toast.LENGTH_SHORT).show();
					}
				});
				buttonView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Analytics.getInstance().sendUiClickEvent(Analytics.BUTTON_EP_LOCKED);
						SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_WRONG);
						animate(R.anim.left_to_right, container);
						vibrate(c, 300);
						int pointsNeeded = episodes.get(pos).getReqPoints() - Stats.getInstance().getStat(Stats.KEY_SCORE, context);
						Toast.makeText(c, Integer.toString(pointsNeeded) + " " + getResources().getString(R.string.points_needed), Toast.LENGTH_SHORT).show();

					}
				});
				pgLogos.setProgress(0);
				play.setBackgroundResource(R.drawable.icon_lock);
				play.setText("");
				score.setText("0");
				logos.setText("0/15");
			} else {
				int numLevelsSolved = db.getSolvedLevelsInEpisode(episodes.get(position).getId());
				pgLogos.setProgress(numLevelsSolved);
				play.setOnClickListener(SelectEpisodeActivity.this);
				play.setText(R.string.play_episode);
				logos.setText(Integer.toString(numLevelsSolved) + "/15");
				score.setText(Integer.toString(db.getScoreInEpisode(episodes.get(position).getId())));
			}
			title.setText(episodes.get(position).getName());
			// } else {
			// buttonView = (View) convertView;
			// }
			return buttonView;
		}

	}

}
