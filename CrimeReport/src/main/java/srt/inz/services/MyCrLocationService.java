package srt.inz.services;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import srt.inz.crimereport.ApplicationPreference;
import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class MyCrLocationService extends Service implements LocationListener {
	
	static String stplace,r;
	Location location; double latitude,longitude;
	ApplicationPreference applicationPreference;

	public MyCrLocationService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		//Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
		applicationPreference=(ApplicationPreference)getApplication();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// Perform your long running operations here.
		
		getmyloc();
		//Toast.makeText(this, "Location Service Started", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
		
	}
	
	@Override
	public void onProviderDisabled(String arg0) {
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

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
			Geocoder gc= new Geocoder(this, Locale.ENGLISH);
	        // Getting latitude of the current location
	        latitude =  location.getLatitude();
	
	        // Getting longitude of the current location
	        longitude =  location.getLongitude();
	
	try {
		List<Address> addresses = gc.getFromLocation(latitude,longitude, 1);
		
		if(addresses != null) {
			Address returnedAddress = addresses.get(0);
			StringBuilder strReturnedAddress = new StringBuilder("");
			for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) 
			{
				strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
				
			}
		
			stplace=strReturnedAddress.toString();
			
			 // 	applicationPreference.setMyLocation(stplace);
		//	Toast.makeText( getBaseContext(),stplace,Toast.LENGTH_SHORT).show();
			
			applicationPreference.setLattitude(String.valueOf(latitude));
			applicationPreference.setLongitude(String.valueOf(longitude));
			
		}	
		else{
			Toast.makeText(getBaseContext(),"GPS Disabled",Toast.LENGTH_SHORT).show();
		
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	
	public void getmyloc()
	{
		try {
						
			 LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			    // Creating a criteria object to retrieve provider
			    Criteria criteria = new Criteria();

			    // Getting the name of the best provider
			    String provider = locationManager.getBestProvider(criteria, true);

			    // Getting Current Location
			    location = locationManager.getLastKnownLocation(provider);
			    

			    if(location!=null){
			            onLocationChanged(location);
			            
			    }

			    locationManager.requestLocationUpdates(provider, 120000, 0, this);
		} catch (Exception e) {
			// TODO: handle exception
			
			e.printStackTrace();
		}
	}
	
}