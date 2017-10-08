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
import android.net.Uri;
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

public class Help_center extends Activity{
	ListView helplist;	ListAdapter help_adapter;
	ArrayList<HashMap<String, String>> oslist_help = new ArrayList<HashMap<String, String>>();
	
	String district,help_address,help_no;
	
	String response1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_center);
		helplist=(ListView)findViewById(R.id.listViewhelp);
		
		new help_list().execute();
		
	}
	
	class help_list extends AsyncTask<String, String, String>
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
		            helplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		               @Override
		               public void onItemClick(AdapterView<?> parent, View view,
		                                            int position, long id) {  
		            	 String  value=oslist_help.get(+position).get("help_no");
		            	 
		              Toast.makeText(getApplicationContext(), value+" : Long press to call Helpline Number !...", Toast.LENGTH_SHORT).show();
		             
		            	   
		               }
		                });
		            helplist.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							String  phnum=oslist_help.get(+position).get("help_no");
							m_call(phnum);
							return false;
						}
					});
		            
		            }
			}
		        catch (JSONException e) {
		          e.printStackTrace();
		        }
		       }
	  public void m_call(String number)
	  {
		  Intent phoneIntent = new Intent(Intent.ACTION_CALL); 
			phoneIntent.setData(Uri.parse("tel:"+number));
		      try{
		         startActivity(phoneIntent);
		      }
		      
		      catch (android.content.ActivityNotFoundException ex){
		         Toast.makeText(getApplicationContext(),"Error in call facility",Toast.LENGTH_SHORT).show();
		      }
	  }
}
