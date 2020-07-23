package com.unindra.flq.states;


/**
 * State of level (logo). Not solved, solved for one, two or three stars or resolved.
 * 
 *
 */
public final class LevelState {

	public static final int NOTSOLVED = 0;
	public static final int SOLVED_ONE = 1;
	public static final int SOLVED_TWO = 2;
	public static final int SOLVED_THREE = 3;
	public static final int RESOLVED = 4;
	
	private LevelState(){
		
	}
}
