package srt.inz.crimereport;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class Viewcrimes extends Activity{
	
	ListView crlist;	ListAdapter adapter;
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	
	String crime_incident,location,datetime,user_id,status;
	
	String response1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewcrime);
		crlist=(ListView)findViewById(R.id.listViewCRIME);
		
		new crime_list().execute();
	}

	class crime_list extends AsyncTask<String, String, String>
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

	            response1 = Connectivity.excutePost(Constants.CRIMELISTRET_URL,
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
				crlist.setAdapter(null);
				oslist.clear();
				
				int length=ja.length();
				for(int i=0;i<length;i++)
				{
		          
					JSONObject c = ja.getJSONObject(i);
		            // Storing  JSON item in a Variable
					crime_incident = c.getString("crime_incident");
					location=c.getString("location");
					datetime=c.getString("datetime");
					user_id=c.getString("user_id");
					status=c.getString("status");
		            
		            // Adding value HashMap key => value
		            HashMap<String, String> map = new HashMap<String, String>();
		            map.put("crime_incident", crime_incident);
		            map.put("location", location);
		            map.put("datetime", datetime);
		            map.put("user_id", user_id);
		            map.put("status", status);
		            
		            map.put("show", "Crime reported at : "+location+"\n Details : "+crime_incident+"\n Date and Time : "+datetime+"\n Reported by : "+user_id);
		            oslist.add(map);
		            
		            adapter = new SimpleAdapter(getApplicationContext(), oslist,
		                R.layout.layout_single,
		                new String[] {"show"}, new int[] {R.id.textView_single});
		            crlist.setAdapter(adapter);
		            crlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		               @Override
		               public void onItemClick(AdapterView<?> parent, View view,
		                                            int position, long id) {  
		            	 /*String  value=oslist.get(+position).get("user_id");*/
		            	 
		              Toast.makeText(getApplicationContext(), "Long click to update status.", Toast.LENGTH_SHORT).show();
		             
		            	   
		               }
		                });
		            crlist.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							String  muid=oslist.get(+position).get("user_id");
							String mincident=oslist.get(+position).get("crime_incident");
							String midat=oslist.get(+position).get("datetime");
							String mstat=oslist.get(+position).get("status");
							
							SharedPreferences sha=getSharedPreferences("mkeyuid", MODE_WORLD_READABLE);
							SharedPreferences.Editor ed=sha.edit();
							ed.putString("mid", muid);
							ed.putString("min", mincident);
							ed.putString("midt", midat);
							ed.putString("mist", mstat);
							ed.commit();
							
							Intent i=new Intent(getApplicationContext(),Updatestatus.class);
							startActivity(i);
							
							return false;
						}
					});
		            
		            }
			}
		        catch (JSONException e) {
		          e.printStackTrace();
		        }
		       }
	
}
