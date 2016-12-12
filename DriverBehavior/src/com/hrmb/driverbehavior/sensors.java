package com.hrmb.driverbehavior;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class sensors implements SensorEventListener {
	
	SensorManager sm;
	sensordata data;	
	
	public sensors(SensorManager sm){
		this.sm = sm;
		sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sm.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE), sm.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), sm.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_GRAVITY), sm.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_LIGHT), sm.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), sm.SENSOR_DELAY_NORMAL);		
		
		data = new sensordata();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
        	
        	data.ACC_X = event.values[0];
        	data.ACC_Y = event.values[1];
        	data.ACC_Z = event.values[2];
        }
        
        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
        	
        	data.GYR_X = event.values[0];
        	data.GYR_Y = event.values[1];
        	data.GYR_Z = event.values[2];
        } 
        
        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
        	
        	data.LINACC_X = event.values[0];
        	data.LINACC_Y = event.values[1];
        	data.LINACC_Z = event.values[2];
        } 
        
        if (sensor.getType() == Sensor.TYPE_GRAVITY) {
        	
        	data.GRA_X = event.values[0];
        	data.GRA_Y = event.values[1];
        	data.GRA_Z = event.values[2];
        } 
        
        if (sensor.getType() == Sensor.TYPE_LIGHT) {
        	
        	data.LIGHT = event.values[0];        	
        } 
        
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
        	
        	data.MAG_X = event.values[0];
        	data.MAG_Y = event.values[1];
        	data.MAG_Z = event.values[2];
        }                  
        
		
	}
	
	public sensordata getSensorData(){
		return data;
	}	
	

}
