package com.hrmb.driverbehavior;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button start_button;
	Button stop_button;
	TextView result_textView;
	
	SensorManager sm;
	// the data variable stores the current window size data.
	sensordata[] data;
	sensordata currentdata = new sensordata();
	sensors sensorobj;
	
	private LocationManager lm;
	private locationReader locReader;
	private behaviorAnalyser analyser;
	
	// index in the data
	private final int windowSize = constants.windowSize;
	private int index =0;	
    private static final int sampleRate = 8000;
	private AudioRecord audio;
	private int bufferSize;
	private Thread thread;
	private static final int SAMPLE_DELAY = constants.SAMPLE_DELAY;
	
	//
	private boolean start = false;	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        start_button = (Button) findViewById(R.id.start_button);
        stop_button = (Button) findViewById(R.id.stop_button);
        result_textView = (Button) findViewById(R.id.result_textView);
        
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);         
        sm = (SensorManager) getSystemService(SENSOR_SERVICE); 
        data = new sensordata[windowSize];
        sensorobj = new sensors(sm);        
        locReader = new locationReader(lm);
        analyser = new behaviorAnalyser(); 
        
        try {
			bufferSize = AudioRecord
					.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
							AudioFormat.ENCODING_PCM_16BIT);
		} catch (Exception e) {
			android.util.Log.e("TrackingFlow", "Exception", e);
		}
        
        start_button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				start = true;
				
			}
		});
        
        stop_button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// stop the process
				start = false;
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
protected void onResume() {		
    	
		super.onResume();
		
		// we must retrive weight values and etc.. from database into analyser
		analyser.readValuesFromDB();
		
		audio = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
				AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize);

		audio.startRecording();
		thread = new Thread(new Runnable() {
			public void run() {
				while(thread != null && !thread.isInterrupted()){
					//Let's make the thread sleep for a the approximate sampling time
					try{Thread.sleep(SAMPLE_DELAY);}catch(InterruptedException ie){ie.printStackTrace();}
					
					currentdata = sensorobj.getSensorData();
					readAudioBuffer();//After this call we can get the last value assigned to the currentdata.SOUNDLEVEL variable
					// read the data from other sensors					
					
					Location myloc = locReader.getCurrentLocation();
					currentdata.LOC_LAT = (float) myloc.getLatitude();
					currentdata.LOC_LNG = (float) myloc.getLongitude();
					
					// adding date and time to data
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");							
					currentdata.CURR_DATE = sdf.format(new Date());
					Calendar cal = Calendar.getInstance(); 
					int millisecond = cal.get(Calendar.MILLISECOND);
				    int second = cal.get(Calendar.SECOND);
				    int minute = cal.get(Calendar.MINUTE);						        
			        //24 hour format
				    int hourofday = cal.get(Calendar.HOUR_OF_DAY);
				    currentdata.CURR_TIME = hourofday + ":" + minute + ":"+second+ ":"+millisecond;
				    currentdata.frameRate = millisecond;
				    
				  
				    	
				    //storing data into array
					index = index % windowSize;
					data[index] = currentdata;
					index ++;
															
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {																													
							
							//calling calculating driving score
							// this part has not been used for now. 
							float driving_score = analyser.calcDrivingScore(currentdata);
							// we add a bar for showing the goodness of behavior
							// but for now we just show it using a textView
							float lower_norm = driving_score - constants.normScoreTolerance;
							float upper_norm = driving_score + constants.normScoreTolerance;
							String drivingStatus = "Good";
							if(driving_score > constants.min_score && driving_score < lower_norm){								
								drivingStatus = "Bad";
							}else if ( driving_score >= lower_norm && driving_score < upper_norm){
								drivingStatus = "Good";
							}else if (driving_score >=upper_norm)
								drivingStatus = "Excellent";
																											
							
							if(index == constants.windowSize){
								//result2_textView.append("index = windowSize \n");
								//calculating driving parameters
								sensordata[] data2 = new sensordata[constants.windowSize];
								System.arraycopy(data, 0, data2, 0, constants.windowSize);
								drivingParameters result = new drivingParameters();
								computeDrivingParams bt = new computeDrivingParams();
							    bt.execute(data2);	
							    
							    try {
							    	result = bt.get();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ExecutionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							    
							    result_textView.setText(" Analyse Driving: \n" + 
							    		"Number of Right Lane Changes: "+ result.rightLaneChange + "\n" +
										"Number of Left Lane Changes: "+ result.leftLaneChange + "\n" +
										"Number of Normal Brakes: "+ result.normalBrake + "\n" + 
										"Number of Normal Brakes: "+ result.fastBrake + "\n" +
										"Number of Turn Right Normal: "+ result.turnRightNormal + "\n" +
										"Number of Turn Left Normal: "+ result.turnLeftNormal + "\n" +
										"Number of Turn Right Fast: "+ result.turnRightFast + "\n" +
										"Number of Turn Left Fast: "+ result.turnLeftFast + "\n" +
										"Number of U Turn Normal: "+ result.uTurnNormal + "\n" + 
										"Number of U Turn Fast: "+ result.uTurnFast + "\n");
							}												    													
																					
						}
					});
				
				    
				}				    
			}
		});
		thread.start();
		
	
	}
    
    /**
	 * Functionality that gets the sound level out of the sample
	 */
	private void readAudioBuffer() {

		try {
			short[] buffer = new short[bufferSize];

			int bufferReadResult = 1;

			if (audio != null) {

				// Sense the voice...
				bufferReadResult = audio.read(buffer, 0, bufferSize);
				double sumLevel = 0;
				for (int i = 0; i < bufferReadResult; i++) {
					sumLevel += buffer[i];
				}
				currentdata.SOUNDLEVEL = (float)Math.abs((sumLevel / bufferReadResult));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {		
		
		super.onPause();					
				
		// we must store weights and other analyser variables into database
		analyser.storeValuesToDB();
		
		thread.interrupt();
		thread = null;
		try {
			if (audio != null) {
				audio.stop();
				audio.release();
				audio = null;
			}
		} catch (Exception e) {e.printStackTrace();}
	}
	
	private class computeDrivingParams extends AsyncTask<sensordata, Void, drivingParameters>{

		@Override
		protected drivingParameters doInBackground(sensordata... arg0) {
			// TODO Auto-generated method stub
			//return analyser.analyseDrivingParam(arg0);
			drivingParameters result2 = new drivingParameters();
			
			//this part is not necessary, I just do it , to match your method definition
			// we can put all the code here
			double[] Gyro_X = new double[arg0.length];
			double[] Gyro_y = new double[arg0.length];
			double[] Gyro_Z = new double[arg0.length];
			double[] Acc_X = new double[arg0.length];
			double[] Acc_Y = new double[arg0.length];
			double[] Acc_Z = new double[arg0.length];
			int[] time = new int[arg0.length]; // I am not sure about the size of time
			int[] result = new int[10];
			int[] timeResult = new int[arg0.length]; // I am not sure about the size of time
			
			for(int i=0; i<arg0.length; i++){
				Gyro_X[i] = arg0[i].GYR_X;
				Gyro_y[i] = arg0[i].GYR_Y;
				Gyro_Z[i] = arg0[i].GYR_Z;
				Acc_X[i] = arg0[i].ACC_X;
				Acc_Y[i] = arg0[i].ACC_Y;
				Acc_Z[i] = arg0[i].ACC_Z;				
				time[i] = arg0[i].frameRate;				
				
			}
			
			BehaviorDetection(Gyro_X,Gyro_y,Gyro_Z,Acc_X,Acc_Y,Acc_Z,time,result,timeResult);
			
			result2.rightLaneChange = result[0];
			result2.leftLaneChange = result[1];
			result2.normalBrake = result[2];
			result2.fastBrake = result[3];
			result2.turnRightNormal = result[4];
			result2.turnRightFast = result[5];
			result2.turnLeftNormal = result[6];
			result2.turnLeftFast = result[7];
			result2.uTurnNormal = result[8];
			result2.uTurnFast = result[9];
			
			return result2;
		}
		
	}
	
	private void BehaviorDetection(double[] Gyro_X, double[] Gyro_y, double [] Gyro_Z, double[] Acc_X, 
			double[] Acc_Y, double[] Acc_Z, int[] time, int[] result, int[] timeResult){
		// you can put your code here...
		
	}
    
}
