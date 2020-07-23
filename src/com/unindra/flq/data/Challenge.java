package com.unindra.flq.data;

import java.util.ArrayList;
import java.util.List;

import com.unindra.framework.Logger;

/**
 * Challenge object for challenge mode
 * 
 *
 */
public class Challenge {

	private String dificullty;
	private int time;
	private int numOfQuestions = -1;
	private int numOfTrue = 0;
	private int numOfFalse = 0;
	private int score = 0;
	private long qStartTime;
	private long qEndTime;
	private List<Question> questions = new ArrayList<Question>();
	private List<String> goodQuestions = new ArrayList<String>();
	
	public Challenge(String dificullty, int time){
		this.dificullty = dificullty;
		this.time = time;
	}
	
	
	/**
	 * generate next question
	 * @param db database
	 * @param startTime timestamp
	 * @return
	 */
	public Question nextQuestion(DbAdapter db, long startTime){
		qStartTime = startTime;
		Boolean switcher = true;
		Question q = null;
		if(goodQuestions.size() == db.getNumberOfRecords(dificullty)){
			goodQuestions.clear();
		}
		while(switcher){
			q = db.getQuestion(dificullty);
			if(!goodQuestions.contains(q.getGoodAnswer())){
				goodQuestions.add(q.getGoodAnswer());
				questions.add(q);
				numOfQuestions++;
				switcher = false;
			}
		}
		Logger.log("CHALL LOOP: " + q.getGoodAnswer());
		return q;
		
	}
	
	/**
	 * Check if is good answer for question
	 * @param answer answer
	 * @param endTime timestamp
	 * @return true if is good answer
	 */
	public Boolean isGoodAnswer(String answer, long endTime){
		qEndTime = endTime;
		if(answer.equalsIgnoreCase(questions.get(questions.size()-1).getGoodAnswer())){
			numOfTrue++;
			score += calculateScore();
			return true;
		} else {
			numOfFalse++;
			score -= calculateScore();
			return false;
		}
	}
	
	/**
	 * calculate score for question. The faster answer, more points are calculated
	 * @return number of points
	 */
	private int calculateScore(){
		int actTime = (int) ((qEndTime - qStartTime) / 100);
		if (actTime != 0){
			return 500 / actTime;
		} else {
			return 500;
		}
	}

	public int getTime() {
		return time;
	}

	public int getNumOfQuestions() {
		return numOfQuestions;
	}

	public int getNumOfTrue() {
		return numOfTrue;
	}

	public int getNumOfFalse() {
		return numOfFalse;
	}

	public int getScore() {
		return score;
	}

	public List<Question> getQuestions() {
		return questions;
	}
	
	
	
	
}
