package srt.inz.crimereport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import srt.inz.crimereport.User_reg.usr_reg;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Adminaddofficer extends Activity{

		EditText en,ee,eph,eid,ep;
		String sn,se,sph,sid,sp,sdist,sityp,hos;
		
		Spinner sp_dist; ArrayAdapter<String> s1;
		Button brg;
		
		RadioGroup rgcardtyp;
		RadioButton rbcardtyp;
		int seltyp; 
		
		
		Spinner sp_sect; ArrayAdapter<String> s2; String sect,hs;
		ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
		List<String> lables;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.addofficer);
			
			en=(EditText)findViewById(R.id.oet_nam);
			ee=(EditText)findViewById(R.id.oet_email);
			eph=(EditText)findViewById(R.id.oet_phone);
			eid=(EditText)findViewById(R.id.oet_uid);
			ep=(EditText)findViewById(R.id.oet_ps);
			
			sp_dist=(Spinner)findViewById(R.id.osp_dist);
			sp_sect=(Spinner)findViewById(R.id.osp_sect);
			
			brg=(Button)findViewById(R.id.obt_register);
			rgcardtyp=(RadioGroup)findViewById(R.id.oradioGroupUsr);
			
			
			String[] dis = getResources().getStringArray(R.array.districts);
	        
	        s1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dis);
		    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    sp_dist.setAdapter(s1);
		    sp_dist.setOnItemSelectedListener(new OnItemSelectedListener()
	        {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					sdist=arg0.getItemAtPosition(arg2).toString();
					((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
					new getsect_det().execute();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub	
				}
	        	
	        });
		    	
			brg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sn=en.getText().toString();
					se=ee.getText().toString();
					sph=eph.getText().toString();
					sid=eid.getText().toString();
					sp=ep.getText().toString();
					
					seltyp=rgcardtyp.getCheckedRadioButtonId();
					rbcardtyp=(RadioButton)findViewById(seltyp);
					sityp=rbcardtyp.getText().toString();
					
					new officer_reg().execute();
					
				}
			});
			
		}
		
		public class officer_reg extends AsyncTask<String, String, String>
	    {
	    	

	    		@Override
	    		protected String doInBackground(String... arg0) {
	    			// TODO Auto-generated method stub
	    			
	    			String urlParameters = null;
		            try {
		                urlParameters =  "name=" + URLEncoder.encode(sn, "UTF-8") + "&&"
		                        + "email=" + URLEncoder.encode(se, "UTF-8") + "&&"
		                        +"phone=" + URLEncoder.encode(sph, "UTF-8") + "&&"
		                        
		                        + "section=" + URLEncoder.encode(sect, "UTF-8") + "&&"
		                        +"user_id=" + URLEncoder.encode(sid, "UTF-8") + "&&"
		                        + "password=" + URLEncoder.encode(sp, "UTF-8") + "&&"
		                        +"id_type=" + URLEncoder.encode("Officer", "UTF-8") + "&&"
		                        + "district=" + URLEncoder.encode(sdist, "UTF-8");
		            } catch (UnsupportedEncodingException e) {
		                // TODO Auto-generated catch block
		                e.printStackTrace();
		            }

		            hos = Connectivity.excutePost(Constants.REGISTEROFFICER_URL,
		                    urlParameters);
		            Log.e("You are at", "" + hos);
		       return hos;
	    		}
	    		@Override
	    		protected void onPostExecute(String result) {
	    			// TODO Auto-generated method stub
	    			if(hos.contains("success"))
	    			{
	    				Toast.makeText(getApplicationContext(), "Officer Successfully added", Toast.LENGTH_LONG).show();	
	    				Intent i=new Intent(getApplicationContext(),Admin_home.class);
	    				startActivity(i);
	    			}
	    			else
	    			{
	    				Toast.makeText(getApplicationContext(), "Officer Registration error : "+hos, Toast.LENGTH_LONG).show();
	    			}
	    			super.onPostExecute(result);
	    			
	    	
	    }
	    	}

		
		
		public class getsect_det extends AsyncTask<String, String, String>
		{

		
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				String urlParameters = null;
	            try {
	                urlParameters =  "district=" + URLEncoder.encode(sdist, "UTF-8") ;
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
							sect=arg0.getItemAtPosition(arg2).toString();
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
