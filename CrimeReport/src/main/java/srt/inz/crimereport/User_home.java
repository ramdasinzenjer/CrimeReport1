package srt.inz.crimereport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class User_home extends Activity{
	
	ImageView imrc,imhelp,imlaw; 
	
	ApplicationPreference apppref;
	ListView mylist;	String resdb; String det,tp,tm,ab,st;
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	ListAdapter adapter;
	
	ImageButton imb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_home);
		imrc=(ImageView)findViewById(R.id.imageView_crreport);
		imhelp=(ImageView)findViewById(R.id.imageView_crhelp);
		imlaw=(ImageView)findViewById(R.id.imageView_crlaw);
		mylist=(ListView)findViewById(R.id.mlist_news);
		apppref=(ApplicationPreference)getApplication();
		imb=(ImageButton)findViewById(R.id.img_btadd);
		
		Button b=(Button)findViewById(R.id.mlgbtttt);
		
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				apppref.setLoginStatus(false);
				Intent i=new Intent(getApplicationContext(),MainPage.class);
				startActivity(i);
				
			}
		});
		
		imrc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Report_crime.class);
				startActivity(i);
			}
		});
		imhelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Help_center.class);
				startActivity(i);
			}
		});
		imlaw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Law_points.class);
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
				urlParameters="status="+ URLEncoder.encode("1", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			resdb = Connectivity.excutePost(Constants.NEWSFEED_URL,
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
								
				// Adding value HashMap key => value
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("topic", topic);
	            map.put("details", details);
	            map.put("timedate", timedate);
	            map.put("added_by", added_by);
	           	            
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

				}
	                });
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}
	
}
