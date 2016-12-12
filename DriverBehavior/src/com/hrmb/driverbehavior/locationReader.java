package com.hrmb.driverbehavior;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class locationReader implements LocationListener {
	
	private LocationManager lm;
	private Location myCurrentLocation;
	
	public locationReader(LocationManager lm){
		this.lm = lm;
		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        this.myCurrentLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location != null) {
            this.myCurrentLocation = location;            
        }
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public Location getCurrentLocation(){
		return myCurrentLocation;
	}

}
