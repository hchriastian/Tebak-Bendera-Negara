package com.unindra.flq.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.futuristicbear.americanquiz.R;
import com.unindra.flq.Resources;
import com.unindra.flq.X;
import com.unindra.flq.states.Difficulty;
import com.unindra.flq.states.EpisodeState;
import com.unindra.flq.states.LevelState;
import com.unindra.framework.Logger;

/**
 * Database of all levels in game. There are episodes and levels which are linked to episode based on L_EPISODE foreign key.
 *
 *
 */
public class DbAdapter extends SQLiteOpenHelper {

	private static final String DBNAME = "AMERICANQUIZ_DB";
	private static final String TABLE_EPISODES = "episodes";
	private static final String TABLE_LEVELS = "levels";

	// EPISODES TABLE FIELD NAMES
	private static final String E_ID = "id";
	private static final String E_NAME = "name";
	private static final String E_REQPOINTS = "required_points";
	private static final String E_STATE = "state";

	// LEVELS TABLE FIELD NAMES
	private static final String L_ID = "id";
	private static final String L_NAME = "name";
	private static final String L_IMAGE = "image";
	private static final String L_EPISODE = "episode"; // FOREIGN KEY
	private static final String L_HINTS = "hints";
	private static final String L_STATE = "state";
	private static final String L_DIFFICULTY = "difficulty";
	private static final String L_WIKI = "wiki";

	
	//increase this value on update of database
	private static final int DB_VERSION = 1;

	public DbAdapter(Context context) {
		super(context, DBNAME, null, DB_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_EPISODES + " (" + E_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + E_NAME + " INT NOT NULL, " + E_REQPOINTS + " INT NOT NULL, " + E_STATE + " INT NOT NULL);");

		db.execSQL("CREATE TABLE " + TABLE_LEVELS + " (" + L_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + L_NAME + " TEXT NOT NULL, " + L_IMAGE + " INT NOT NULL, " + L_STATE + " INT NOT NULL DEFAULT " + LevelState.NOTSOLVED + ", " + L_HINTS + " INT NOT NULL, " + L_DIFFICULTY + " TEXT NOT NULL, " + L_WIKI + " TEXT NOT NULL, " + L_EPISODE + " INT NOT NULL, " + "FOREIGN KEY(" + L_EPISODE + ") REFERENCES " + TABLE_EPISODES + "(" + E_ID + ")" + ");");

		insertData(db);

	}

	/**
	 * called when DB_VERSION is increased. Add new levels here.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * called when application is first time initialized. Should be called only
	 * by onCreate method
	 * 
	 * @param db database
	 */
	private void insertData(SQLiteDatabase db) {

		// EPISODES
		createEpisode(db, R.string.ep_01, 0, EpisodeState.OPEN);
		createEpisode(db, R.string.ep_02, 25, EpisodeState.CLOSED);
		createEpisode(db, R.string.ep_03, 50, EpisodeState.CLOSED);
		createEpisode(db, R.string.ep_04, 75, EpisodeState.CLOSED);
		createEpisode(db, R.string.ep_05, 100, EpisodeState.CLOSED);
		createEpisode(db, R.string.ep_06, 125, EpisodeState.CLOSED);
		createEpisode(db, R.string.ep_07, 150, EpisodeState.CLOSED);
		createEpisode(db, R.string.ep_08, 175, EpisodeState.CLOSED);
		createEpisode(db, R.string.ep_09, 200, EpisodeState.CLOSED);
		//LEVEL01
		createLevel(db, R.string.e01x01,Resources.e01x01, 1, R.array.e01x01, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/Indonesia");
		createLevel(db, R.string.e01x02,Resources.e01x02, 1, R.array.e01x02, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x03,Resources.e01x03, 1, R.array.e01x03, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x04,Resources.e01x04, 1, R.array.e01x04, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x05,Resources.e01x05, 1, R.array.e01x05, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x06,Resources.e01x06, 1, R.array.e01x06, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x07,Resources.e01x07, 1, R.array.e01x07, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x08,Resources.e01x08, 1, R.array.e01x08, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x09,Resources.e01x09, 1, R.array.e01x09, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x10,Resources.e01x10, 1, R.array.e01x10, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x11,Resources.e01x11, 1, R.array.e01x11, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x12,Resources.e01x12, 1, R.array.e01x12, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x13,Resources.e01x13, 1, R.array.e01x13, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x14,Resources.e01x14, 1, R.array.e01x14, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e01x15,Resources.e01x15, 1, R.array.e01x15, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");

		//LEVEL02
		createLevel(db, R.string.e02x01,Resources.e02x01, 2, R.array.e02x01, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x02,Resources.e02x02, 2, R.array.e02x02, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x03,Resources.e02x03, 2, R.array.e02x03, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x04,Resources.e02x04, 2, R.array.e02x04, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x05,Resources.e02x05, 2, R.array.e02x05, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x06,Resources.e02x06, 2, R.array.e02x06, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x07,Resources.e02x07, 2, R.array.e02x07, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x08,Resources.e02x08, 2, R.array.e02x08, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x09,Resources.e02x09, 2, R.array.e02x09, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x10,Resources.e02x10, 2, R.array.e02x10, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x11,Resources.e02x11, 2, R.array.e02x11, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x12,Resources.e02x12, 2, R.array.e02x12, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x13,Resources.e02x13, 2, R.array.e02x13, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x14,Resources.e02x14, 2, R.array.e02x14, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e02x15,Resources.e02x15, 2, R.array.e02x15, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");

		//LEVEL03
		createLevel(db, R.string.e03x01,Resources.e03x01, 3, R.array.e03x01, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x02,Resources.e03x02, 3, R.array.e03x02, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x03,Resources.e03x03, 3, R.array.e03x03, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x04,Resources.e03x04, 3, R.array.e03x04, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x05,Resources.e03x05, 3, R.array.e03x05, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x06,Resources.e03x06, 3, R.array.e03x06, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x07,Resources.e03x07, 3, R.array.e03x07, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x08,Resources.e03x08, 3, R.array.e03x08, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x09,Resources.e03x09, 3, R.array.e03x09, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x10,Resources.e03x10, 3, R.array.e03x10, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x11,Resources.e03x11, 3, R.array.e03x11, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x12,Resources.e03x12, 3, R.array.e03x12, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x13,Resources.e03x13, 3, R.array.e03x13, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x14,Resources.e03x14, 3, R.array.e03x14, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e03x15,Resources.e03x15, 3, R.array.e03x15, Difficulty.EASY,"http://en.m.wikipedia.org/wiki/");

		//LEVEL04
		createLevel(db, R.string.e04x01,Resources.e04x01, 4, R.array.e04x01, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x02,Resources.e04x02, 4, R.array.e04x02, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x03,Resources.e04x03, 4, R.array.e04x03, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x04,Resources.e04x04, 4, R.array.e04x04, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x05,Resources.e04x05, 4, R.array.e04x05, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x06,Resources.e04x06, 4, R.array.e04x06, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x07,Resources.e04x07, 4, R.array.e04x07, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x08,Resources.e04x08, 4, R.array.e04x08, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x09,Resources.e04x09, 4, R.array.e04x09, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x10,Resources.e04x10, 4, R.array.e04x10, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x11,Resources.e04x11, 4, R.array.e04x11, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x12,Resources.e04x12, 4, R.array.e04x12, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x13,Resources.e04x13, 4, R.array.e04x13, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x14,Resources.e04x14, 4, R.array.e04x14, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e04x15,Resources.e04x15, 4, R.array.e04x15, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");

		//LEVEL05
		createLevel(db, R.string.e05x01,Resources.e05x01, 5, R.array.e05x01, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x02,Resources.e05x02, 5, R.array.e05x02, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x03,Resources.e05x03, 5, R.array.e05x03, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x04,Resources.e05x04, 5, R.array.e05x04, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x05,Resources.e05x05, 5, R.array.e05x05, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x06,Resources.e05x06, 5, R.array.e05x06, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x07,Resources.e05x07, 5, R.array.e05x07, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x08,Resources.e05x08, 5, R.array.e05x08, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x09,Resources.e05x09, 5, R.array.e05x09, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x10,Resources.e05x10, 5, R.array.e05x10, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x11,Resources.e05x11, 5, R.array.e05x11, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x12,Resources.e05x12, 5, R.array.e05x12, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x13,Resources.e05x13, 5, R.array.e05x13, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x14,Resources.e05x14, 5, R.array.e05x14, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e05x15,Resources.e05x15, 5, R.array.e05x15, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");

		//LEVEL06
		createLevel(db, R.string.e06x01,Resources.e06x01, 6, R.array.e06x01, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x02,Resources.e06x02, 6, R.array.e06x02, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x03,Resources.e06x03, 6, R.array.e06x03, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x04,Resources.e06x04, 6, R.array.e06x04, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x05,Resources.e06x05, 6, R.array.e06x05, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x06,Resources.e06x06, 6, R.array.e06x06, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x07,Resources.e06x07, 6, R.array.e06x07, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x08,Resources.e06x08, 6, R.array.e06x08, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x09,Resources.e06x09, 6, R.array.e06x09, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x10,Resources.e06x10, 6, R.array.e06x10, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x11,Resources.e06x11, 6, R.array.e06x11, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x12,Resources.e06x12, 6, R.array.e06x12, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x13,Resources.e06x13, 6, R.array.e06x13, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x14,Resources.e06x14, 6, R.array.e06x14, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e06x15,Resources.e06x15, 6, R.array.e06x15, Difficulty.MEDIUM,"http://en.m.wikipedia.org/wiki/");

		//LEVEL07
		createLevel(db, R.string.e07x01,Resources.e07x01, 7, R.array.e07x01, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x02,Resources.e07x02, 7, R.array.e07x02, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x03,Resources.e07x03, 7, R.array.e07x03, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x04,Resources.e07x04, 7, R.array.e07x04, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x05,Resources.e07x05, 7, R.array.e07x05, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x06,Resources.e07x06, 7, R.array.e07x06, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x07,Resources.e07x07, 7, R.array.e07x07, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x08,Resources.e07x08, 7, R.array.e07x08, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x09,Resources.e07x09, 7, R.array.e07x09, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x10,Resources.e07x10, 7, R.array.e07x10, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x11,Resources.e07x11, 7, R.array.e07x11, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x12,Resources.e07x12, 7, R.array.e07x12, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x13,Resources.e07x13, 7, R.array.e07x13, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x14,Resources.e07x14, 7, R.array.e07x14, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e07x15,Resources.e07x15, 7, R.array.e07x15, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");

		//LEVEL08
		createLevel(db, R.string.e08x01,Resources.e08x01, 8, R.array.e08x01, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x02,Resources.e08x02, 8, R.array.e08x02, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x03,Resources.e08x03, 8, R.array.e08x03, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x04,Resources.e08x04, 8, R.array.e08x04, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x05,Resources.e08x05, 8, R.array.e08x05, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x06,Resources.e08x06, 8, R.array.e08x06, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x07,Resources.e08x07, 8, R.array.e08x07, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x08,Resources.e08x08, 8, R.array.e08x08, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x09,Resources.e08x09, 8, R.array.e08x09, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x10,Resources.e08x10, 8, R.array.e08x10, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x11,Resources.e08x11, 8, R.array.e08x11, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x12,Resources.e08x12, 8, R.array.e08x12, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x13,Resources.e08x13, 8, R.array.e08x13, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x14,Resources.e08x14, 8, R.array.e08x14, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e08x15,Resources.e08x15, 8, R.array.e08x15, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");

		//LEVEL09
		createLevel(db, R.string.e09x01,Resources.e09x01, 9, R.array.e09x01, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x02,Resources.e09x02, 9, R.array.e09x02, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x03,Resources.e09x03, 9, R.array.e09x03, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x04,Resources.e09x04, 9, R.array.e09x04, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x05,Resources.e09x05, 9, R.array.e09x05, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x06,Resources.e09x06, 9, R.array.e09x06, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x07,Resources.e09x07, 9, R.array.e09x07, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x08,Resources.e09x08, 9, R.array.e09x08, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x09,Resources.e09x09, 9, R.array.e09x09, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x10,Resources.e09x10, 9, R.array.e09x10, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x11,Resources.e09x11, 9, R.array.e09x11, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x12,Resources.e09x12, 9, R.array.e09x12, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x13,Resources.e09x13, 9, R.array.e09x13, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x14,Resources.e09x14, 9, R.array.e09x14, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");
		createLevel(db, R.string.e09x15,Resources.e09x15, 9, R.array.e09x15, Difficulty.HARD,"http://en.m.wikipedia.org/wiki/");

		// DONT FORGET TO ADD LEVELS FROM ONUPGRADE WHEN MAKING UPDATE OF DB!!!!

	}

	/**
	 * Add episode to database
	 * @param db database
	 * @param name name
	 * @param points requested points to unlock
	 * @param state state
	 */
	private void createEpisode(SQLiteDatabase db, int name, int points, int state) {
		ContentValues values = new ContentValues();
		values.put(E_NAME, name);
		values.put(E_REQPOINTS, points);
		values.put(E_STATE, state);
		db.insert(TABLE_EPISODES, null, values);
	}

	/**
	 * Add level to database
	 * @param db database
	 * @param name names divided by ,
	 * @param image reference to image
	 * @param episode number of episode
	 * @param hints reference to hints
	 * @param difficulty difficullty
	 * @param wiki wiki page
	 */
	private void createLevel(SQLiteDatabase db, int name, int image, int episode, int hints, String difficulty, String wiki) {
		ContentValues values = new ContentValues();
		values.put(L_NAME, X.context.getResources().getString(name));
		values.put(L_IMAGE, image);
		values.put(L_EPISODE, episode);
		values.put(L_HINTS, hints);
		values.put(L_STATE, LevelState.NOTSOLVED);
		values.put(L_DIFFICULTY, difficulty.toString());
		values.put(L_WIKI, wiki);
		db.insert(TABLE_LEVELS, null, values);
	}

	public List<Episode> getAllEpisodes() {
		List<Episode> episodeList = new ArrayList<Episode>();
		String selectQuery = "SELECT * FROM " + TABLE_EPISODES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				episodeList.add(new Episode(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)));
			} while (cursor.moveToNext());
		}
		db.close();
		return episodeList;
	}

	public List<Level> getLevelsInEpisode(int episodeId) {
		List<Level> levelList = new ArrayList<Level>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LEVELS + " where " + L_EPISODE + " = ?", new String[] { Integer.toString(episodeId) });

		if (cursor.moveToFirst()) {
			do {
				levelList.add(new Level(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7)));
			} while (cursor.moveToNext());
		}
		Logger.log("Zobral som:" + levelList.size());
		db.close();
		return levelList;
	}

	public int getScoreInEpisode(int episodeId) {
		int result = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + L_STATE + " FROM " + TABLE_LEVELS + " where " + L_EPISODE + " = ?", new String[] { Integer.toString(episodeId) });

		if (cursor.moveToFirst()) {
			do {
				if (cursor.getInt(0) != LevelState.RESOLVED) {
					result += cursor.getInt(0);
				}
			} while (cursor.moveToNext());
		}
		db.close();

		return result;
	}

	public int getSolvedLevelsInEpisode(int episodeId) {
		int result = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + L_STATE + " FROM " + TABLE_LEVELS + " where " + L_EPISODE + " = ?", new String[] { Integer.toString(episodeId) });

		if (cursor.moveToFirst()) {
			do {
				if (cursor.getInt(0) != LevelState.NOTSOLVED) {
					result++;
				}
			} while (cursor.moveToNext());
		}
		db.close();

		return result;
	}

	public List<Level> getAllLevels() {
		List<Level> levelList = new ArrayList<Level>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LEVELS, null);

		if (cursor.moveToFirst()) {
			do {
				levelList.add(new Level(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7)));
			} while (cursor.moveToNext());
		}
		db.close();
		return levelList;
	}

	public int getCountLevels() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("SELECT count(*) FROM " + TABLE_LEVELS, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();

		return count;
	}

	public int getCountSolvedLevels() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("SELECT count(*) FROM " + TABLE_LEVELS + " WHERE " + L_STATE + "!= " + LevelState.NOTSOLVED, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();

		return count;
	}

	public int getCountSolvedLevel2Stars() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("SELECT count(*) FROM " + TABLE_LEVELS + " WHERE " + L_STATE + "= " + LevelState.SOLVED_TWO, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();

		return count;
	}

	public int getCountSolvedLevel1Stars() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("SELECT count(*) FROM " + TABLE_LEVELS + " WHERE " + L_STATE + "= " + LevelState.SOLVED_ONE, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();

		return count;
	}

	public void updateLevelState(Level l) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(L_STATE, l.getState());
		db.update(TABLE_LEVELS, values, L_ID + " = ?", new String[] { String.valueOf(l.getId()) });
		db.close();
	}

	public boolean isNewEpisodeOpen(int actScore) {
		int nextEpisode = getFirstClosedEpisode();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + E_REQPOINTS + " ," + E_STATE + " FROM " + TABLE_EPISODES + " where " + E_ID + " = ?", new String[] { Integer.toString(nextEpisode) });
		int dbRes = 0;
		// inicializacia na open kvoli toastu ak by nevedel dostat hodnotu z db
		int state = EpisodeState.OPEN;
		if (cursor.moveToFirst()) {
			dbRes = cursor.getInt(0);
			state = cursor.getInt(1);
		}
		db.close();

		if (state == EpisodeState.CLOSED && (dbRes - actScore <= 0)) {
			openNextEpisode(nextEpisode);
			return true;
		} else {
			return false;
		}
	}

	private int getFirstClosedEpisode() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + E_ID + " FROM " + TABLE_EPISODES + " where " + E_STATE + " = ?", new String[] { Integer.toString(EpisodeState.CLOSED) });
		int dbRes = -1;
		if (cursor.moveToFirst()) {
			dbRes = cursor.getInt(0);
		}
		db.close();
		return dbRes;
	}

	private void openNextEpisode(int episodeId) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(E_STATE, EpisodeState.OPEN);
		db.update(TABLE_EPISODES, values, L_ID + " = ?", new String[] { String.valueOf(episodeId) });
		db.close();
	}

	public Question getQuestion(String difficullty) {
		List<Level> levelList = new ArrayList<Level>();
		List<Integer> randomList = new ArrayList<Integer>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LEVELS + " where " + L_DIFFICULTY + " = ?", new String[] { difficullty });
		Random rand = new Random();
		while (levelList.size() < 4) {
			int position = rand.nextInt(cursor.getCount());
			if (!randomList.contains(position)) {
				randomList.add(position);
				if (cursor.moveToPosition(position)) {
					levelList.add(new Level(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7)));
				}
			}
		}
		db.close();
		Logger.log("DB: GETQUESTION: " + levelList.get(0).getName());
		return new Question(levelList.get(0).getName(), levelList.get(1).getName(), levelList.get(2).getName(), levelList.get(3).getName(), levelList.get(0).getImage());
	}

	public int getNumberOfRecords(String difficullty) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LEVELS + " where " + L_DIFFICULTY + " = ?", new String[] { difficullty });
		int count = cursor.getCount();
		Logger.log("DB: NUMBER OF RECORDS " + count);
		db.close();
		return count;
	}

	/**
	 * Drop all tables from database and recreate them. Clear all database data.
	 */
	public void restartDatabase() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EPISODES);
		onCreate(db);
		db.close();
	}

	public ArrayList<String> getAllClubNames() {
		ArrayList<String> levelList = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + L_NAME + " FROM " + TABLE_LEVELS + " ORDER BY " + L_NAME + " ASC", null);

		if (cursor.moveToFirst()) {
			do {
				String[] newStr = cursor.getString(0).split(",");
				levelList.add(newStr[0]);
			} while (cursor.moveToNext());
		}
		db.close();
		return levelList;
	}

}
