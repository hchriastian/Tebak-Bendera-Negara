package com.unindra.flq.data;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Object of level in normal game mode. Implements parcelable to transfer it between activities.
 * 
 *
 */
public class Level implements Parcelable {

	private int id;
	private String[] names;
	private int image;
	private int state;
	private int hints;
	private String difficulty;
	private String wiki;
	private int episodeId;
	
	/**
	 * 
	 * @param id id from database
	 * @param name names from database
	 * @param image logo image
	 * @param state state of level
	 * @param hints hints
	 * @param difficulty difficullty
	 * @param wiki wiki page
	 * @param episodeId which episode
	 */
	public Level(int id, String name, int image, int state, int hints, String difficulty, String wiki, int episodeId){
		this.id = id;
		this.names = name.split(",");
		this.image = image;
		this.state = state;
		this.hints = hints;
		this.difficulty = difficulty;
		this.wiki = wiki;
		this.episodeId = episodeId;
	}
	
	public Level(Parcel in){
		String[] stringNames = new String[in.readInt()];
		int[] intData = new int[4];
		
		in.readStringArray(stringNames);
		this.names = stringNames;
		this.hints = in.readInt();
		this.difficulty = in.readString();
		this.wiki = in.readString();
		
		in.readIntArray(intData);
		this.id = intData[0];
		this.image = intData[1];
		this.state = intData[2];
		this.episodeId = intData[3];
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public String getWiki(){
		return wiki;
	}

	public int getId() {
		return id;
	}

	/**
	 * 
	 * @return all names
	 */
	public String[] getNames() {
		return names;
	}
	
	/**
	 * 
	 * @return first name in array for question purposes
	 */
	public String getName(){
		return names[0];
	}
	/**
	 * 
	 * @param i index of name
	 * @return name of element on index
	 */
	public String getName(int i){
		return names[i];
	}

	public int getImage() {
		return image;
	}

	public int getHints(){
		return hints;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public int getEpisodeId() {
		return episodeId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.names.length);
		dest.writeStringArray(this.names);
		dest.writeInt(hints);
		dest.writeString(difficulty);
		dest.writeString(wiki);
		dest.writeIntArray(new int[] {this.id, this.image, this.state, this.episodeId});
		
	}
	
	public static final Parcelable.Creator<Level> CREATOR = new Parcelable.Creator<Level>() {

		@Override
		public Level createFromParcel(Parcel source) {
			return new Level(source);
		}

		@Override
		public Level[] newArray(int size) {
			return new Level[size];
		}
	};
	
	
}
