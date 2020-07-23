package com.unindra.flq.data;

/**
 * One question in challenge mode
 * 
 *
 */
public class Question {

	private String goodAnswer;
	private String bad1;
	private String bad2;
	private String bad3;
	private int image;
	
	public Question(String goodAnswer, String bad1, String bad2, String bad3, int image){
		this.goodAnswer = goodAnswer;
		this.bad1 = bad1;
		this.bad2 = bad2;
		this.bad3 = bad3;
		this.image = image;
	}

	public String getGoodAnswer() {
		return goodAnswer;
	}

	public String getBad1() {
		return bad1;
	}

	public String getBad2() {
		return bad2;
	}

	public String getBad3() {
		return bad3;
	}

	public int getImage() {
		return image;
	}

}
