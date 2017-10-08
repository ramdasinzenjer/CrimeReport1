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

@SuppressWarnings("deprecation")
public class Approve_user extends Activity{
	
	ListView aprlist,rglist;	ListAdapter apr_adapter,rg_adapter;
	ArrayList<HashMap<String, String>> oslist_apr = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> oslist_rg = new ArrayList<HashMap<String, String>>();
	String response1,response2,response3;
	
	String sname,sid,stype,sphone,smail,saddress,spass; String sname1,sid1,stype1,sphone1,smail1,saddress1,spass1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.approve_user);
		aprlist=(ListView)findViewById(R.id.listViewaprusr);
		rglist=(ListView)findViewById(R.id.listViewregusr);
		
		new reg_userlist().execute();
		new apr_userlist().execute();
		
	}
	
	class reg_userlist extends AsyncTask<String, String, String>
	  {
		  
		  @Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			  String urlParameters = null;
	            try {
	                urlParameters =  "status=" + URLEncoder.encode("0", "UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	            response1 = Connectivity.excutePost(Constants.REGLIST_URL,
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
				rg_parsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), "No new registrations", Toast.LENGTH_SHORT).show();
			}
		}
	  }
	  
	  
	  public void rg_parsingmethod() {
			try
			{
				JSONObject jobject=new JSONObject(response1);
				JSONObject jobject1=jobject.getJSONObject("Event");
				JSONArray ja=jobject1.getJSONArray("Details");
				rglist.setAdapter(null);
				oslist_rg.clear();
				
				int length=ja.length();
				for(int i=0;i<length;i++)
				{
		          
					JSONObject c = ja.getJSONObject(i);
		            // Storing  JSON item in a Variable
		            sname = c.getString("name");
		            sid=c.getString("user_id");
		            stype=c.getString("id_type");
		            sphone=c.getString("phone");
		            smail=c.getString("email");
		            saddress=c.getString("address");
		            spass=c.getString("password");
		            
		            // Adding value HashMap key => value
		            HashMap<String, String> map = new HashMap<String, String>();
		            map.put("name", sname);
		            map.put("user_id", sid);
		            map.put("id_type", stype);
		            map.put("phone", sphone);
		            map.put("email", smail);
		            map.put("address", saddress);
		            
		            map.put("show", "Name : "+sname+"\n ID No: "+sid+"\t Type: "+stype+"\n Email : "+smail);
		            oslist_rg.add(map);
		            
		            rg_adapter = new SimpleAdapter(getApplicationContext(), oslist_rg,
		                R.layout.layout_single,
		                new String[] {"show"}, new int[] {R.id.textView_single});
		            rglist.setAdapter(rg_adapter);
		            rglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		               @Override
		               public void onItemClick(AdapterView<?> parent, View view,
		                                            int position, long id) {  
		            	 String  value=oslist_rg.get(+position).get("name");
		            	 
		              Toast.makeText(getApplicationContext(), ""+value+" : Long press to approve User. ", Toast.LENGTH_SHORT).show();
		             
		            	   
		               }
		                });
		            rglist.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							new user_approve().execute();
							return false;
						}
					});
		            }
			}
		        catch (JSONException e) {
		          e.printStackTrace();
		        }
		       }
	  class apr_userlist extends AsyncTask<String, String, String>
	  {
		 
		  @Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			  String urlParameters = null;
	            try {
	                urlParameters =  "status=" + URLEncoder.encode("1", "UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	            response2 = Connectivity.excutePost(Constants.REGLIST_URL,
	                    urlParameters);
	            Log.e("You are at", "" + response2);
	       return response2;
		}
		  @Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(response2.contains("success"))
			{
				apr_parsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), "No users approved yet.", Toast.LENGTH_SHORT).show();
			}
		}
	  }
	  
	  
	  public void apr_parsingmethod() {
			try
			{
				JSONObject jobject=new JSONObject(response2);
				JSONObject jobject1=jobject.getJSONObject("Event");
				JSONArray ja=jobject1.getJSONArray("Details");
				aprlist.setAdapter(null);
				oslist_apr.clear();
				
				int length=ja.length();
				for(int i=0;i<length;i++)
				{
		          
					JSONObject c = ja.getJSONObject(i);
		            // Storing  JSON item in a Variable
		            sname1 = c.getString("name");
		            sid1=c.getString("user_id");
		            stype1=c.getString("id_type");
		            sphone1=c.getString("phone");
		            smail1=c.getString("email");
		            saddress1=c.getString("address");
		            
		            // Adding value HashMap key => value
		            HashMap<String, String> map = new HashMap<String, String>();
		            map.put("name", sname1);
		            map.put("user_id", sid1);
		            map.put("id_type", stype1);
		            map.put("phone", sphone1);
		            map.put("email", smail1);
		            map.put("address", saddress1);
		            
		            map.put("show", "Name : "+sname1+"\n ID No: "+sid1+"\t Type: "+stype1+"\n Email : "+smail1);
		            oslist_apr.add(map);
		            
		            apr_adapter = new SimpleAdapter(getApplicationContext(), oslist_apr,
		                R.layout.layout_single2,
		                new String[] {"show"}, new int[] {R.id.textView_single1});
		            aprlist.setAdapter(apr_adapter);
		            aprlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		               @Override
		               public void onItemClick(AdapterView<?> parent, View view,
		                                            int position, long id) {  
		            	 String  value=oslist_apr.get(+position).get("name");
		            	 
		              Toast.makeText(getApplicationContext(), ""+value, Toast.LENGTH_SHORT).show();
		              
		            	   
		               }
		                });
		            }
			}
		        catch (JSONException e) {
		          e.printStackTrace();
		        }
		       }

	  class user_approve extends AsyncTask<String, String, String>
	  {
		  
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters = null;
            try {
                urlParameters =  "user_id=" + URLEncoder.encode(sid, "UTF-8") + "&&"
                        + "password=" + URLEncoder.encode(spass, "UTF-8") + "&&"
                        + "id_type=" + URLEncoder.encode(stype,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            response3 = Connectivity.excutePost(Constants.USERSTATUSUP_URL,
                    urlParameters);
            Log.e("You are at", "" + response3);
       return response3;
			
		}
		  @Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(response3.contains("success"))
			{
				Toast.makeText(getApplicationContext(), "User approval completed.", Toast.LENGTH_SHORT).show();
				m_refresh();
			}
			else {
				Toast.makeText(getApplicationContext(), "Error in approval.", Toast.LENGTH_SHORT).show();
			}
		}
	  }
	  
	  public void m_refresh()
	  {
		  finish();
		  startActivity(getIntent());
	  }
	  
}
