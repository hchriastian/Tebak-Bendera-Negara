package com.unindra.flq.data;


/**
 * Episode contain levels. Has id from database, name, reqested points to unlock it and state(open/closed)
 * 
 *
 */
public class Episode {

	private int id;
	private int name;
	private int reqPoints;
	private int state;
	
	public Episode(int id, int name, int reqPoints, int state){
		this.id = id;
		this.name = name;
		this.reqPoints = reqPoints;
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public int getName() {
		return name;
	}

	public int getReqPoints() {
		return reqPoints;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	
}
