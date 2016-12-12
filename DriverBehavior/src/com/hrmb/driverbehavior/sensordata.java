package com.hrmb.driverbehavior;

import java.io.Serializable;

public class sensordata implements Serializable {
	// accelerometer data
	public double ACC_X = 0;
	public double ACC_Y = 0;
	public double ACC_Z = 0;
	
	// LINEAR_ACCELERATION data
	public double LINACC_X = 0;
	public double LINACC_Y = 0;
	public double LINACC_Z = 0;
	
	// GYROSCOPE data
	public double GYR_X = 0;
	public double GYR_Y = 0;
	public double GYR_Z = 0;
	
	// GRAVITY data
	public double GRA_X = 0;
	public double GRA_Y = 0;
	public double GRA_Z = 0;
	
	// LIGHT data
	public double LIGHT = 0;
	
	// MAGNETIC_FIELD data
	public double MAG_X = 0;
	public double MAG_Y = 0;
	public double MAG_Z = 0;
	
	// SOUND LEVEL data
	public double SOUNDLEVEL = 0;
	
	// LOCATION data
	public double LOC_LNG = 0;
	public double LOC_LAT = 0;
	
	// data and time
	public String CURR_DATE = "";
	public String CURR_TIME = "";
	
	//frame Rate
	public int frameRate = 0;
	
	public sensordata(){
		
	}

}
