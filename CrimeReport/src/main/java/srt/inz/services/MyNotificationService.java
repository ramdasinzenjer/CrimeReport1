package srt.inz.services;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import srt.inz.crimereport.ApplicationPreference;
import srt.inz.crimereport.R;
import srt.inz.crimereport.Viewcrimes;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi") public class MyNotificationService extends Service{

	String response;
	
	
	private MyThread mythread;
    public boolean isRunning = false; String svhid,parseresult;
    private static String TAG = MyNotificationService.class.getSimpleName();
    String crime_incident,location,datetime,user_id,status;
	ApplicationPreference applicationPreference;

	public MyNotificationService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		//Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
		applicationPreference=(ApplicationPreference)getApplication();
		mythread  = new MyThread();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// Perform your long running operations here.
		
		if(!isRunning){
            mythread.start();
            isRunning = true;
        }
		//Toast.makeText(this, "Location Service Started", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
		
	}
	
	class MyThread extends Thread{
        static final long DELAY = 10000;
        @Override
        public void run(){          
            while(isRunning){
                Log.d("Service","Running");
                try {                   
                	new RequestfetchApiTask().execute();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }
         
    }
	public class RequestfetchApiTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters = null;
	        try {
	        	urlParameters="";
	            		
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        response = Connectivity.excutePost(Constants.CRIMELISTRET_URL,
	                urlParameters);
	        Log.e("You are at", "" + response);

	        return response;
			
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String s) {
			// TODO Auto-generated method stub
			super.onPostExecute(s);
			if(response.contains("success"))
            {	                     
             //Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();
              	noti_parser(response);   
            }
			else
			{
				Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_SHORT).show();
			}
			
		}	
	}

/////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public void notification(Intent inte,String stim)
	{
		
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); 	
		PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, inte, 0);	
		// this is it, we'll build the notification!
		// in the addAction method, if you don't want any icon, just set the first param to 0
		Notification mNotification = new Notification.Builder(getApplicationContext())		
			.setContentTitle("Crime Alert !!!")
			.setContentText("Crime Reported on "+stim+ ".")
			.setTicker("New Message Alert!")
			/*.setNumber(++numMessages)*/
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentIntent(pIntent)
			.setSound(soundUri)		
			//.addAction(0, "View", pIntent)
			//.addAction(0, "Stop", pIntent)			
			.build();
		
		NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		// If you want to hide the notification after it was selected, do the code below
		 mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		notificationManager.notify(0, mNotification);
		
		// Toast.makeText(getApplicationContext(), "Message recieved", Toast.LENGTH_LONG).show();
	}
	
	public void noti_parser(String result)
	{
		try
		{
			JSONObject  jObject = new JSONObject(result);
			JSONObject  jObject1 = jObject.getJSONObject("Event");
			JSONArray ja = jObject1.getJSONArray("Details"); 
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject c = ja.getJSONObject(i);
				crime_incident = c.getString("crime_incident");
				location=c.getString("location");
				datetime=c.getString("datetime");
				user_id=c.getString("user_id");
				status=c.getString("status");
				
				
				if (status.equals("0")) {
					
					Intent intentAlarm= new Intent(getBaseContext(), Viewcrimes.class);
					notification(intentAlarm,datetime);
				} else {

					Log.e("Error", ""+result);
				}
				
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}
}
