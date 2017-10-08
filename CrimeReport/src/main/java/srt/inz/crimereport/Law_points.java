package srt.inz.crimereport;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Law_points extends Activity{
	
	ListView lawp;	ListAdapter law_adapter;
	ArrayList<HashMap<String, String>> oslist_law = new ArrayList<HashMap<String, String>>();
	
	String section,year,details;
	
	String response1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.law_points);
		lawp=(ListView)findViewById(R.id.listViewlawpoints);
		
		new law_pointlist().execute();
		
	}
	
	class law_pointlist extends AsyncTask<String, String, String>
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

	            response1 = Connectivity.excutePost(Constants.RETLAWLIST,
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
				Toast.makeText(getApplicationContext(), "No law points available.", Toast.LENGTH_SHORT).show();
			}
		}
	  }
	  
	  
	  public void parsingmethod() {
			try
			{
				JSONObject jobject=new JSONObject(response1);
				JSONObject jobject1=jobject.getJSONObject("Event");
				JSONArray ja=jobject1.getJSONArray("Details");
				lawp.setAdapter(null);
				oslist_law.clear();
				
				int length=ja.length();
				for(int i=0;i<length;i++)
				{
		          
					JSONObject c = ja.getJSONObject(i);
		            // Storing  JSON item in a Variable
					section = c.getString("section");
		            year=c.getString("year");
		            details=c.getString("details");
		            
		            
		            // Adding value HashMap key => value
		            HashMap<String, String> map = new HashMap<String, String>();
		            map.put("section", section);
		            map.put("year", year);
		            map.put("details", details);
		            
		            
		            map.put("show", "IPC Section : "+section+"\n Year : "+year+"\n Point : "+details);
		            oslist_law.add(map);
		            
		            law_adapter = new SimpleAdapter(getApplicationContext(), oslist_law,
		                R.layout.layout_single,
		                new String[] {"show"}, new int[] {R.id.textView_single});
		            lawp.setAdapter(law_adapter);
		            lawp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		               @Override
		               public void onItemClick(AdapterView<?> parent, View view,
		                                            int position, long id) {  
		            	 String  value=oslist_law.get(+position).get("details");
		            	 
		              Toast.makeText(getApplicationContext(), ""+value, Toast.LENGTH_SHORT).show();
		             
		            	   
		               }
		                });
		            
		            }
			}
		        catch (JSONException e) {
		          e.printStackTrace();
		        }
		       }

}
