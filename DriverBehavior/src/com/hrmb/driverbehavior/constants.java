package com.hrmb.driverbehavior;

public class constants {
	
	// the maximum score in driving (the best)
	public static final int max_score = 10;
	// the minimum score in driving (the worst)
	public static final int min_score = 0;
	// default weights for each sensor data	
	public static final int W_ACC = 1;
	public static final int W_LINACC = 1;
	public static final int W_GYR = 1;
	public static final int W_GRA = 1;
	public static final int W_LIGHT = 1;
	public static final int W_MAG = 1;
	public static final int W_SOUNDLEVEL = 1;
	public static final int W_LOC = 1;	
	public static final int W_DATE = 1;
	public static final int W_TIME = 1;	
	
	// the window size
	public static final int windowSize = 100; // it must be 2000 for every 100 sec
	//delay between getting each sample data
	public static final int SAMPLE_DELAY = 50;
	
	// tolerance of normal driving
	// we use this measure for evaluating the current score.
	// for example if the current score is 5 and tolerance is 2
	// we consider good driving between 3 to 7
	// lower than 3 is bad , and higher than 7 is excellent
	public static final float normScoreTolerance = 1.5f;

}
