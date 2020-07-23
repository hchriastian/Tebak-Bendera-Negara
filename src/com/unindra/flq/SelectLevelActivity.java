package com.unindra.flq;

import java.util.ArrayList;
import java.util.List;

import com.futuristicbear.americanquiz.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.unindra.flq.data.DbAdapter;
import com.unindra.flq.data.Level;
import com.unindra.flq.data.Stats;
import com.unindra.flq.states.LevelState;
import com.unindra.framework.AdController;
import com.unindra.framework.Analytics;
import com.unindra.framework.Logger;
import com.unindra.framework.SoundHandler;

import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class SelectLevelActivity extends X implements OnItemClickListener, OnClickListener {

	private GridView gridView;
	private LevelAdapter levelAdapter;
	public int episodeId;
	private DbAdapter db;
	private List<Level> levelList;
	protected Level levelToDialog;
	private Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DbAdapter(this);
		c = this;
		Bundle b = getIntent().getExtras();
		episodeId = b.getInt("EPISODE_ID");
		levelList = db.getLevelsInEpisode(episodeId);

		setContentView(R.layout.select_level);
		
		new AdController(this, this, AdController.TYPE_BANNER);

		initHeaderWithScorePanel("LEVEL " + Integer.toString(episodeId), R.drawable.icon_question, Stats.getInstance().getStat(Stats.KEY_SCORE, this), Stats.getInstance().getCoins(this), this);

		levelAdapter = new LevelAdapter(this);
		
		//new LogoResourceLoader().execute(levelList);

		gridView = (GridView) findViewById(R.id.GV_levels);
		gridView.setAdapter(levelAdapter);
		gridView.setOnItemClickListener(this);
		
		if(!ImageLoader.getInstance().isInited())
			MainMenuActivity.configImageLoader();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateHeaderData(Stats.getInstance().getStat(Stats.KEY_SCORE, this), Stats.getInstance().getCoins(this));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
		Logger.log("TOOT SA TU ZAVOLALO OMG");
		if (levelList.get(arg2).getState() == LevelState.NOTSOLVED) {
			Intent i = new Intent(this, LevelViewActivity.class);
			i.putExtra("LEVEL", levelList.get(arg2));
			int requestCode = 0;
			startActivityForResult(i, requestCode);
		} else {
			// ak je uz level vyrieseny
			levelToDialog = levelList.get(arg2);
			LayoutInflater inflater = this.getLayoutInflater();
			new ResultDialog(this, android.R.style.Theme_Translucent_NoTitleBar, inflater.inflate(R.layout.level_solved_dialog, null), levelToDialog, -1).show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// update text view skore
		updateHeaderData(Stats.getInstance().getStat(Stats.KEY_SCORE, this), Stats.getInstance().getCoins(this));

		if (resultCode == -1) {
			// do nothing
		} else if (resultCode == 1) {

			// zober z databazy updatnuty level list a updatni adapter
			levelList = db.getLevelsInEpisode(episodeId);
			levelAdapter.notifyDataSetChanged();
			// zobraz vitazny dialog
			if (data != null) {
				Bundle levelData = data.getExtras();
				levelToDialog = levelData.getParcelable("LEVEL");
				int lCoins = levelData.getInt("COINS");
				LayoutInflater inflater = this.getLayoutInflater();
				new ResultDialog(this, android.R.style.Theme_Translucent_NoTitleBar, inflater.inflate(R.layout.level_solved_dialog, null), levelToDialog, lCoins).show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.BTN_back) {
			SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
			finish();
		}
	}

	protected void startWikiPage() {
		if (isOnline()) {
			Intent intent = new Intent(this, WikiPageActivity.class);
			
			String url = levelToDialog.getWiki() + levelToDialog.getName().replaceAll(" ", "_");
			Logger.log("WIKI URL: " + url);
			intent.putExtra("URL", url);
			intent.putExtra("CLUB", levelToDialog.getName());
			startActivity(intent);
		} else {
			Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
		}
	}

	private class ResultDialog extends Dialog implements android.view.View.OnClickListener {

		ImageView IV_star1;
		ImageView IV_star2;
		ImageView IV_star3;

		public ResultDialog(Context context, int theme, View view, Level l, int coins) {
			super(context, theme);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setLayout(getScreenWidth() - getScreenWidth() / 5, LayoutParams.WRAP_CONTENT);// getScreenHeight()
																										// -
																										// getScreenHeight()/4);
			getWindow().setGravity(Gravity.CENTER);
			setContentView(view);
			setCanceledOnTouchOutside(true);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.dimAmount = 0.6f;
			getWindow().setAttributes(lp);

			// getWindow().getDecorView().setBackgroundResource(R.drawable.result_dialog_background);
			ImageButton BTN_close = (ImageButton) view.findViewById(R.id.IV_closeView);
			BTN_close.setOnClickListener(this);
			Button BTN_wiki = (Button) view.findViewById(R.id.BTN_wiki);
			BTN_wiki.setOnClickListener(this);
			ImageView IV_logo = (ImageView) view.findViewById(R.id.IV_logo);
			IV_star1 = (ImageView) view.findViewById(R.id.IV_star1);
			IV_star2 = (ImageView) view.findViewById(R.id.IV_star2);
			IV_star3 = (ImageView) view.findViewById(R.id.IV_star3);

			TextView TV_score_title = (TextView) view.findViewById(R.id.TV_score_title);
			TextView TV_coins = (TextView) view.findViewById(R.id.TV_coinscost);
			LinearLayout LL_coins = (LinearLayout) view.findViewById(R.id.LL_coins);
			if (coins > -1) {
				LL_coins.setVisibility(View.VISIBLE);
				TV_coins.setText("+" + Integer.toString(coins));
			} else {
				LL_coins.setVisibility(View.GONE);
			}

			if (l.getImage() > Resources.TOTAL_LEVELS) {
				IV_logo.setBackgroundResource(l.getImage());
			} else {
				IV_logo.setImageBitmap(Resources.loadBitmap(Integer.toString(l.getImage()) + ".png"));
			}
			TV_score_title.setText(l.getName());

			switch (l.getState()) {
			case 1:
				setStarsDrawable(R.drawable.star_full, R.drawable.star_empty, R.drawable.star_empty);
				break;
			case 2:
				setStarsDrawable(R.drawable.star_full, R.drawable.star_full, R.drawable.star_empty);
				break;
			case 3:
				setStarsDrawable(R.drawable.star_full, R.drawable.star_full, R.drawable.star_full);
				break;
			case 4:
				setStarsDrawable(R.drawable.star_full, R.drawable.star_full, R.drawable.star_full);
				break;
			}
			animate(R.anim.star1, IV_star1);
			animate(R.anim.star2, IV_star2);
			animate(R.anim.star3, IV_star3);
		}

		private void setStarsDrawable(int star1, int star2, int star3) {
			IV_star1.setBackgroundResource(star1);
			IV_star2.setBackgroundResource(star2);
			IV_star3.setBackgroundResource(star3);
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.IV_closeView) {
				cancel();
				if (db.isNewEpisodeOpen(Stats.getInstance().getStat(Stats.KEY_SCORE, c))) {
					Toast.makeText(SelectLevelActivity.this, R.string.level_unlocked, Toast.LENGTH_SHORT).show();
				}
			} else if (v.getId() == R.id.BTN_wiki) {
				SoundHandler.getInstance().playSound(context, SoundHandler.SOUND_CLICK);
				Analytics.getInstance().sendUiClickEvent(Analytics.BUTTON_WIKI);
				startWikiPage();
			}

		}

	}

	private class LevelAdapter extends BaseAdapter {

		private Context c;

		public LevelAdapter(Context c) {
			this.c = c;
		}

		@Override
		public int getCount() {
			return levelList.size();
		}

		@Override
		public Object getItem(int position) {
			return levelList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View logoView;
			// if (convertView == null) {
			logoView = new View(c);
			logoView = inflater.inflate(R.layout.level_image, null);
			ImageView image = (ImageView) logoView.findViewById(R.id.IV_level_image);
			// image.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
			// levelList.get(position).getImage(), getScreenWidth() / 3,
			// getScreenWidth() / 3));

			String imageUri = "assets://"+ levelList.get(position).getImage() +".png"; 
			System.out.println("imguri: " + imageUri);
			ImageLoader.getInstance().displayImage(imageUri, image);
			
			//image.setImageBitmap(bitmapImages[position]);

			// } else {
			// logoView = (View) convertView;
			// }
			ImageView badge = (ImageView) logoView.findViewById(R.id.IV_badge);
			if (levelList.get(position).getState() == LevelState.NOTSOLVED) {
				badge.setVisibility(View.INVISIBLE);
			} else {
				badge.setVisibility(View.VISIBLE);
				if (getDeviceAPILevel() > 10) {
					image.setAlpha((float) 0.6);
				}
			}
			return logoView;
		}

	}
}
