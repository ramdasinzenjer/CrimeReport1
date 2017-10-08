package srt.inz.crimereport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import srt.inz.services.MyCrLocationService;
import android.app.Activity;
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


public class MainPage extends Activity {

	EditText euid,epwd;
	Button lgb,rgb;
	
	Spinner sp_typ; ArrayAdapter<String> s1;
	String suid,spwd,styp,sh;
	
	ApplicationPreference appPreference;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        euid=(EditText)findViewById(R.id.editText_uid);
        epwd=(EditText)findViewById(R.id.editText_pwd);
        lgb=(Button)findViewById(R.id.button_log);
        rgb=(Button)findViewById(R.id.button_reg);
        sp_typ=(Spinner)findViewById(R.id.spinner_cardtyp);
        appPreference=(ApplicationPreference)getApplication();
        
        
        if(appPreference.getServiceStatus()==false)
        {
        mstartService();
        }
        
        String[] typ = getResources().getStringArray(R.array.cardtyp);
        
        s1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typ);
	    s1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp_typ.setAdapter(s1);
	    sp_typ.setOnItemSelectedListener(new OnItemSelectedListener()
        {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				styp=arg0.getItemAtPosition(arg2).toString();
				((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
        	
        });
        
        lgb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//lkjh
				suid=euid.getText().toString();
				spwd=epwd.getText().toString();
				SharedPreferences sh=getSharedPreferences("skey", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed=sh.edit();
				ed.putString("suid", suid);
				ed.commit();
				
				new user_loginn().execute();
				
			}
		});
        
        rgb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),User_reg.class);
				startActivity(i);
			}
		});
        
    }
    
    public class user_loginn extends AsyncTask<String, String, String>
    {

    	@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
				
				String urlParameters = null;
	            try {
	                urlParameters =  "user_id=" + URLEncoder.encode(suid, "UTF-8") + "&&"
	                        + "password=" + URLEncoder.encode(spwd, "UTF-8") + "&&"
	                        + "type=" + URLEncoder.encode(styp,"UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	            sh = Connectivity.excutePost(Constants.LOGIN_URL,
	                    urlParameters);
	            Log.e("You are at", "" + sh);
	       return sh;
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			if(sh.contains("success"))
			{
				appPreference.setLoginStatus(true);
				appPreference.setUserId(suid);
				appPreference.setUserType(styp);
				if(styp.contains("Admin"))
				{
					Intent i=new Intent(getApplicationContext(), Admin_home.class);
					startActivity(i);
					Toast.makeText(getApplicationContext(), "Welcome Admin ...", Toast.LENGTH_SHORT).show();
				}
				else if(styp.contains("Officer"))
				{
					Intent i=new Intent(getApplicationContext(), Officer_home.class);
					startActivity(i);
					Toast.makeText(getApplicationContext(), "Welcome Officer ...", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent i=new Intent(getApplicationContext(), User_home.class);
					startActivity(i);
					Toast.makeText(getApplicationContext(), "Successfully Logged in.", Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Usernmae or Password error.", Toast.LENGTH_SHORT).show();							
			}
			super.onPostExecute(result);
		
		}
		
	}
    
    public void mstartService() {
		startService(new Intent(this, srt.inz.services.MyCrLocationService.class));
		        	        
	}

	// Stop the service
	public void stopService() {
		stopService(new Intent(this, srt.inz.services.MyCrLocationService.class));
	
	}
}
