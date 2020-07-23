package com.unindra.flq.data;

/**
 * One row in leaderboard
 * 
 *
 */
public class LeaderboardRecord {

	private String name;
	private int score;
	
	public LeaderboardRecord(String name, int score){
		this.score = score;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}
	
}
