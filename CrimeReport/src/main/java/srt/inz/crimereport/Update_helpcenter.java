package srt.inz.crimereport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.app.Activity;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Update_helpcenter extends Activity{
	
	ListView helplist;	ListAdapter help_adapter;
	ArrayList<HashMap<String, String>> oslist_help = new ArrayList<HashMap<String, String>>();
	
	String district,help_address,help_no;	
	String response1;
	
	Spinner sp_dist; ArrayAdapter<String> s1; String sdist,sadd,sph,hos;
	Button b_uphc;	EditText eadd,eph;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_helpcenter);
		sp_dist=(Spinner)findViewById(R.id.spinner_dst);
		helplist=(ListView)findViewById(R.id.listViewhelp2);
		b_uphc=(Button)findViewById(R.id.button1_uphcenter);
		eadd=(EditText)findViewById(R.id.editText_hadd);
		eph=(EditText)findViewById(R.id.editText_helpcontact);
		
		String[] dst = getResources().getStringArray(R.array.districts);
        
        s1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dst);
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
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
        	
        });
		
		new mhelp_list().execute();
		b_uphc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sadd=eadd.getText().toString();
				sph=eph.getText().toString();
				new Up_helpcenter().execute();
				
			}
		});
	}
	class mhelp_list extends AsyncTask<String, String, String>
	  {
		  @Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			  String urlParameters = null;
	            try {
	                urlParameters =  "";
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	            response1 = Connectivity.excutePost(Constants.HELPLIST,
	                    urlParameters);
	            Log.e("You are at", "" + response1);
	       return response1;
		}
		  @Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(response1.contains("success"))
			{
				parsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), "No Help centers available.", Toast.LENGTH_SHORT).show();
			}
		}
	  }
	  
	  
	  public void parsingmethod() {
			try
			{
				JSONObject jobject=new JSONObject(response1);
				JSONObject jobject1=jobject.getJSONObject("Event");
				JSONArray ja=jobject1.getJSONArray("Details");
				helplist.setAdapter(null);
				oslist_help.clear();
				
				int length=ja.length();
				for(int i=0;i<length;i++)
				{
		          
					JSONObject c = ja.getJSONObject(i);
		            // Storing  JSON item in a Variable
					district = c.getString("district");
					help_address=c.getString("help_address");
					help_no=c.getString("help_no");
		            
		            
		            // Adding value HashMap key => value
		            HashMap<String, String> map = new HashMap<String, String>();
		            map.put("district", district);
		            map.put("help_address", help_address);
		            map.put("help_no", help_no);
		            
		            
		            map.put("show", "District : "+district+"\n Address : "+help_address+"\n\n Helpline Number : "+help_no);
		            oslist_help.add(map);
		            
		            help_adapter = new SimpleAdapter(getApplicationContext(), oslist_help,
		                R.layout.layout_single,
		                new String[] {"show"}, new int[] {R.id.textView_single});
		            helplist.setAdapter(help_adapter);
		           
		          
		            
		            }
			}
		        catch (JSONException e) {
		          e.printStackTrace();
		        }
		    }
	  
	  public void m_refresh()
	  {
		  finish();
		  startActivity(getIntent());
	  }
	  
	  public class Up_helpcenter extends AsyncTask<String, String, String>
		{
		
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
			
				String urlParameters = null;
	            try {
	                urlParameters =  "district=" + URLEncoder.encode(sdist, "UTF-8") + "&&"
	                        + "help_address=" + URLEncoder.encode(sadd, "UTF-8") + "&&"
	                        + "help_no=" + URLEncoder.encode(sph,"UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	            hos = Connectivity.excutePost(Constants.HELPCENTERUPDATE_URL,
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
					Toast.makeText(getApplicationContext(), "Help Center Successfully updated.", Toast.LENGTH_SHORT).show();	
					m_refresh();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Error in updation.", Toast.LENGTH_SHORT).show();
				}	
			}
			
			
		}
	 
}
