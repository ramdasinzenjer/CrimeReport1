package srt.inz.crimereport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
   
	ApplicationPreference appPref;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashpage);
        appPref = (ApplicationPreference) getApplication();
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
            	if(appPref.getServiceStatus()==false)
                {
                mstartService();
                }
            	 Boolean status = appPref.getLoginStatus();
                 if (status==true&&appPref.getUserType().equals("Admin")) {
                	
                	 Intent i = new Intent(SplashScreen.this, Admin_home.class);
                     startActivity(i);
                     finish();
                  
                 }
                 else if(status==true&&appPref.getUserType().equals("Officer")){

                     Intent i = new Intent(SplashScreen.this, Officer_home.class);
                     startActivity(i);
                     finish();
                 }
                 else if(status==true&&!appPref.getUserType().equals("admin")){

                     Intent i = new Intent(SplashScreen.this, User_home.class);
                     startActivity(i);
                     finish();
                 }
                 else
                 {
                	 Intent i = new Intent(SplashScreen.this, MainPage.class);
                     startActivity(i);
                     finish();
                 }

                 // close this activity
                 finish();
                   
            }
        }, SPLASH_TIME_OUT);

    }
    public void mstartService() {
		startService(new Intent(this, srt.inz.services.MyCrLocationService.class));
		        	        
	}

	// Stop the service
	public void stopService() {
		stopService(new Intent(this, srt.inz.services.MyCrLocationService.class));
	
	}
}
