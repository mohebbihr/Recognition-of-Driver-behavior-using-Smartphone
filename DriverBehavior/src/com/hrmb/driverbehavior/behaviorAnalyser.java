package com.hrmb.driverbehavior;

public class behaviorAnalyser {
	
	// the maximum score in driving (the best)
	private final int max_score = constants.max_score;
	// the minimum score in driving (the worst)
	private final int min_score = constants.min_score;
	// past driving score, default is avg(min,max)
	// this value should be stored and retrived from db 
	private float past_score = (min_score + max_score)/2;
	// current score of driving
	// default value should be avg(min,max) or we must determine this value based on past score???
	private float curr_score = (min_score + max_score)/2;
	
	// the weights for each sensor data
	// these weights should be learned and changed through the time.
	// so, we have to find a way that store these values in database and recover it when
	// this application runs again in future.
	private int W_ACC = constants.W_ACC;
	private int W_LINACC = constants.W_LINACC;
	private int W_GYR = constants.W_GYR;
	private int W_GRA = constants.W_GRA;
	private int W_LIGHT = constants.W_LIGHT;
	private int W_MAG = constants.W_MAG;
	private int W_SOUNDLEVEL = constants.W_SOUNDLEVEL;
	private int W_LOC = constants.W_LOC;
	// should we have weight for data and time??? are we can recognize some abnormal driving in some
	// days of year or some time of day??? the answer is yes
	private int W_DATE = constants.W_DATE;
	private int W_TIME = constants.W_TIME;		
	
	public behaviorAnalyser(){
		//instructor
		
	}	
	
	public void storeValuesToDB(){
		// storing values in database, variables like weights, past score and etc ...
		// this function must be called when the application paused (onPaused)
		
	}
	
	public void readValuesFromDB(){
		// reading values from database, variables like weights, past score and etc...
		// this part has not been implemented yet.
		
	}
	
	public float calcDrivingScore(sensordata data){
		// we analyse the data based on weights of features, and then all the curr_score to the past_score
		// it is not implemented yet
		
		return curr_score;
	}
		
	
	public float getPastScore(){
		return past_score;
	}

}
