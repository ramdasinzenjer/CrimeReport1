package srt.inz.crimereport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("WorldReadableFiles") @SuppressWarnings("deprecation")
public class Updatestatus extends Activity{
	
	EditText ets; Button bt; String shid,ss,hos,resp,slt,slng; String shsinc,shsdat,shstat;
	TextView tdisp,tloc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatestatus);
		ets=(EditText)findViewById(R.id.editText_status);
		bt=(Button)findViewById(R.id.button1_upstat);
		tdisp=(TextView)findViewById(R.id.tvdisp);
		tloc=(TextView)findViewById(R.id.tsloc);
		
		SharedPreferences sha=getSharedPreferences("mkeyuid", MODE_WORLD_READABLE);
		shid=sha.getString("mid", "");
		shsinc=sha.getString("min", "");
		shsdat=sha.getString("midt", "");
		shstat=sha.getString("mist", "");
		
		if(shstat.contains("0"))
		{
			tdisp.setText("Current Status: No status");	
		}
		else{
			tdisp.setText("Current Status: "+shstat);
		}
		
		new LocationFetchApiTask().execute();
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ss=ets.getText().toString();
				new upstat_crime().execute();
			}
		});
		
		tloc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences share=getSharedPreferences("mloc", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed=share.edit();
				ed.putString("mlat", slt);
				ed.putString("mlng", slng);
				ed.commit();
				Intent i=new Intent(getApplicationContext(),Moblocator.class);
				startActivity(i);
			}
		});
	}
	public class upstat_crime extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters = null;
            try {
                urlParameters =  "user_id=" + URLEncoder.encode(shid, "UTF-8") + "&&"
                        + "crime_incident=" + URLEncoder.encode(shsinc, "UTF-8") + "&&"
                        +"datetime=" + URLEncoder.encode(shsdat, "UTF-8") + "&&"                        
                        + "status=" + URLEncoder.encode(ss, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            hos = Connectivity.excutePost(Constants.CRIMESTATUS_URL,
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
				Toast.makeText(getApplicationContext(), hos, Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(getApplicationContext(), hos, Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public class LocationFetchApiTask extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters = null;
            try {
                urlParameters = "incident=" + URLEncoder.encode(shsinc, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            resp = Connectivity.excutePost(Constants.CRIMELOCATION_URL,
                    urlParameters);
            Log.e("You are at", "" + resp);
       return resp;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(resp.contains("success"))
			{
				Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
				myparser();
			}
			else
			{
				Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void myparser()
	{
		try
		{
			JSONObject jobject=new JSONObject(resp);
			JSONObject jobject1=jobject.getJSONObject("Event");
			JSONArray ja=jobject1.getJSONArray("Details");
		
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
	          
				JSONObject c = ja.getJSONObject(i);
	            // Storing  JSON item in a Variable
				slt = c.getString("lat");
	            slng=c.getString("lng");
	            tloc.setText("Location : "+slt+","+slng);
	            
	            }
		}
	        catch (JSONException e) {
	          e.printStackTrace();
	        }
	}

}
