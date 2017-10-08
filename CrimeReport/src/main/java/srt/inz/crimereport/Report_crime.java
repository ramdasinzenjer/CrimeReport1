package srt.inz.crimereport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Report_crime extends Activity{
	
	Spinner sp_dist;  ArrayAdapter<String> s1; String sloc;
	
	EditText einc; String sinc,sdate,sh_id,hos;	Button brpt,bview;	TextView tdate;
	
	Spinner sp_sect; ArrayAdapter<String> s2; String ssect,sect,hs;
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	List<String> lables;
	
	ApplicationPreference appPreference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_crime);
		sp_dist=(Spinner)findViewById(R.id.spinner_loc);
		sp_sect=(Spinner)findViewById(R.id.spinner_sect);
		
		einc=(EditText)findViewById(R.id.editText_incident);
		brpt=(Button)findViewById(R.id.button_report);
		tdate=(TextView)findViewById(R.id.textView_date);
		bview=(Button)findViewById(R.id.button1_viewreptdcrimes);
		appPreference=(ApplicationPreference)getApplication();
		
		Toast.makeText(getApplicationContext(), "LOC : "+appPreference.getLattitude()
				+"-"+appPreference.getLongitude(), Toast.LENGTH_SHORT).show();
		
		bview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),View_crimestat.class);
				startActivity(i);
			}
		});
		
		String[] dis = getResources().getStringArray(R.array.districts);
		
		SharedPreferences sh=getSharedPreferences("skey", MODE_WORLD_READABLE);
		sh_id=sh.getString("suid", "");
        
        s1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dis);
	    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp_dist.setAdapter(s1);
	    sp_dist.setOnItemSelectedListener(new OnItemSelectedListener()
        {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sloc=arg0.getItemAtPosition(arg2).toString();
				((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
			
				new getsect_det().execute();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
        	
        });
	    DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		Calendar calobj = Calendar.getInstance();
		sdate=df.format(calobj.getTime());
		tdate.setText(sdate);
		
	    brpt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sinc=einc.getText().toString();	
				openDialog();
				
			}
		});
	}
	
	public void openDialog(){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	      alertDialogBuilder.setMessage("Are you sure to proceed requset?...");
	      
	      alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	            
	        new Report_incident().execute(); 
	            
	         }
	      });
	      
	      alertDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	        	
	        	 finish();
	         }
	      });
	      
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();
	   }
	
	public class Report_incident extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters = null;
            try {
                urlParameters =  "user_id=" + URLEncoder.encode(sh_id, "UTF-8") + "&&"
                        + "crime_incident=" + URLEncoder.encode(sinc, "UTF-8") + "&&"
                        +"location=" + URLEncoder.encode(ssect+", "+sloc, "UTF-8") + "&&"
                        
                  +"lat=" + URLEncoder.encode(appPreference.getLattitude(), "UTF-8") + "&&"
                  +"lng=" + URLEncoder.encode(appPreference.getLongitude(), "UTF-8") + "&&"
                        
                        + "datetime=" + URLEncoder.encode(sdate, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            hos = Connectivity.excutePost(Constants.COMPLAINTREG_URL,
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
	public class getsect_det extends AsyncTask<String, String, String>
	{

	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters = null;
            try {
                urlParameters =  "district=" + URLEncoder.encode(sloc, "UTF-8") ;
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            hs = Connectivity.excutePost(Constants.SECTIONSFETCH_URL,
                    urlParameters);
            Log.e("You are at", "" + hs);
       return hs;
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(hs.contains("success"))
			{
				parsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), hs, Toast.LENGTH_SHORT).show();
				oslist.clear();
				lables.clear();
				sp_sect.setAdapter(null);
			}
		}
		
	}
	public void parsingmethod()
	{
		try
		{
			JSONObject jobject=new JSONObject(hs);
			JSONObject jobject1=jobject.getJSONObject("Event");
			JSONArray ja=jobject1.getJSONArray("Details");
			int length=ja.length();
			
			lables = new ArrayList<String>();
			oslist.clear();
			lables.clear();
			sp_sect.setAdapter(null);
			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				sect=data1.getString("section_name");
				
				/*semail=data1.getString("cargoagency_email");
				sphone=data1.getString("cargoagency_phone");
				sinsure=data1.getString("insurance");*/
				
				// Adding value HashMap key => value
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("section_name", sect);
	            oslist.add(map);
	            
	            lables.add(oslist.get(i).get("section_name"));
	            
	            
	            s2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lables);
			    s2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			    sp_sect.setAdapter(s2);
			    sp_sect.setOnItemSelectedListener(new OnItemSelectedListener()
		        {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						ssect=arg0.getItemAtPosition(arg2).toString();
						((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub	
					}
		        	
		        });
	           
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}

}
