package srt.inz.crimereport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import srt.inz.services.MyNotificationService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Admin_home extends Activity{
	
	Button bvau,bul,bvah,bvcr;
	
	ApplicationPreference apppref;
	ListView mylist;	String resdb,hos; String det,tp,tm,ab,st;
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	ListAdapter adapter;
	
	ImageButton imb; Button baof;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_home);
		bvau=(Button)findViewById(R.id.button_vauser);
		bul=(Button)findViewById(R.id.button_vulaw);
		bvah=(Button)findViewById(R.id.button_vahelp);
		bvcr=(Button)findViewById(R.id.button_vcrime);
		baof=(Button)findViewById(R.id.mbtaddofficer);
		
		mylist=(ListView)findViewById(R.id.mlist_news01);
		apppref=(ApplicationPreference)getApplication();
		imb=(ImageButton)findViewById(R.id.img_btadd01);
		
		startService(new Intent(getApplicationContext(), MyNotificationService.class));
		
		baof.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Adminaddofficer.class);
				startActivity(i);
			}
		});
		
		bvau.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Approve_user.class);
				startActivity(i);
				
			}
		});
		bul.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Intent i=new Intent(getApplicationContext(),Update_law.class);
			startActivity(i);
			
			}
		});
		bvah.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Update_helpcenter.class);
				startActivity(i);
			}
		});
		
		
		bvcr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Viewcrimes.class);
				startActivity(i);
				
			}
		});
		
		imb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent i=new Intent(getApplicationContext(),UpdateNewsfeed.class);
				startActivity(i);
			}
		});
		
		new NewsFeedApiTask().execute();
	}

	public class NewsFeedApiTask extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters=null;
			try {
				urlParameters="";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			resdb = Connectivity.excutePost(Constants.NEWSFEEDALL_URL,
                    urlParameters);
			Log.e("NewsFetch", resdb);
			return resdb;
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			keyparser(resdb);
			//Toast.makeText(getApplicationContext(), ""+resdb, Toast.LENGTH_SHORT).show();
		}
}
	public void keyparser(String result)
	{
		try
		{
			JSONObject  jObject = new JSONObject(result);
			JSONObject  jObject1 = jObject.getJSONObject("Event");
			JSONArray ja = jObject1.getJSONArray("Details"); 
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1= ja.getJSONObject(i);
				String topic=data1.getString("topic");
				String details=data1.getString("details");
				String timedate=data1.getString("timedate");
				String added_by=data1.getString("added_by");
				String status=data1.getString("status");
				
				// Adding value HashMap key => value
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("topic", topic);
	            map.put("details", details);
	            map.put("timedate", timedate);
	            map.put("added_by", added_by);
	            map.put("status", status);
	           	            
	            map.put("notification", "Topic : "+topic+"\n Updated on : "+timedate);
	        	            
	            oslist.add(map);
	            
	            adapter = new SimpleAdapter(getApplicationContext(), oslist,
	                R.layout.layout_single,
	                new String[] {"notification"}, new int[] {R.id.textView_single});
	            mylist.setAdapter(adapter);
	            
	            mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	               
				@Override
	               public void onItemClick(AdapterView<?> parent, View view,
	                                            int position, long id) {               
	               Toast.makeText(getApplicationContext(), 
	            		   "Added by : "+oslist.get(+position).get("added_by"), 
	            		   Toast.LENGTH_LONG).show();	               
	               ab=oslist.get(+position).get("added_by");
	               tm=oslist.get(+position).get("timedate");
	               det=oslist.get(+position).get("details");
	               tp=oslist.get(+position).get("topic");
	               st=oslist.get(+position).get("status");
	              openDialog();
	               
				}
	                });
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}
	
	public void openDialog(){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	           
	    	 alertDialogBuilder.setTitle("Topic : "+tp);
		      alertDialogBuilder.setMessage("Details : "+det
		    		  +"\n Added by : "+ab+"\n Date :"+tm);
	    	if(st.equals("0"))
	    	{
	    		alertDialogBuilder.setNegativeButton("Approve", new DialogInterface.OnClickListener() {
	   	         @Override
	   	         public void onClick(DialogInterface arg0, int arg1) {
	   	            
	   	            Toast.makeText(getApplicationContext(),"News approved.",Toast.LENGTH_SHORT).show();
	   	         new Update_newsfeedstat().execute();
	   	         }
	   	      });
	    	}
		      alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	            
	            Toast.makeText(getApplicationContext(),"Ok...",Toast.LENGTH_SHORT).show();
	            
	         }
	      });
	      
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();
	   }
	public class Update_newsfeedstat extends AsyncTask<String, String, String>
	{
	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters = null;
            try {
                urlParameters =  "topic=" + URLEncoder.encode(tp, "UTF-8") + "&&"
                        +"timedate=" + URLEncoder.encode(tm, "UTF-8") + "&&" 
                        +"status=" + URLEncoder.encode("1", "UTF-8") ;
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            hos = Connectivity.excutePost(Constants.UPDATENEWSSTATUS_URL,
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
	
	public void syslogout(View view)
	{
		apppref.setLoginStatus(false);
		Intent i=new Intent(getApplicationContext(),MainPage.class);
		startActivity(i);
	}
}
