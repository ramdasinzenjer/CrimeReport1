package srt.inz.crimereport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateNewsfeed extends Activity{
	
	EditText etopic,edetails;	 String stopic,sdetails,sdate,hos;
	Button bupnews;	ApplicationPreference apprPreference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatenewsfeed);
		etopic=(EditText)findViewById(R.id.edittopic);
		edetails=(EditText)findViewById(R.id.editdet);
		bupnews=(Button)findViewById(R.id.bt_newsup);
		apprPreference=(ApplicationPreference)getApplication();
		
		bupnews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				stopic=etopic.getText().toString();
				sdetails=edetails.getText().toString();
				
				DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
				Calendar calobj = Calendar.getInstance();
				sdate=df.format(calobj.getTime());
				
				new Update_newsfeed().execute();
			}
		});
		
	}
	
	public void Upnews (View view)
	{
		stopic=etopic.getText().toString();
		sdetails=edetails.getText().toString();
		
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		Calendar calobj = Calendar.getInstance();
		sdate=df.format(calobj.getTime());
		
		new Update_newsfeed().execute();
	}
	
	public class Update_newsfeed extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters = null;
            try {
                urlParameters =  "topic=" + URLEncoder.encode(stopic, "UTF-8") + "&&"
                        +"details=" + URLEncoder.encode(sdetails, "UTF-8") + "&&"
                        +"timedate=" + URLEncoder.encode(sdate, "UTF-8") + "&&" 
                        +"status=" + URLEncoder.encode("1", "UTF-8") + "&&" 
                        +"added_by=" + URLEncoder.encode(apprPreference.getUserId(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            hos = Connectivity.excutePost(Constants.NEWSFEEDUP_URL,
                    urlParameters);
            Log.e("You are at", "" + hos);
       return hos;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(hos.contains("success"))
			{
				Toast.makeText(getApplicationContext(), ""+hos, Toast.LENGTH_LONG).show();	
			}
			else
			{
				Toast.makeText(getApplicationContext(), ""+hos, Toast.LENGTH_SHORT).show();
			}	
		}
		
		
	}

}
