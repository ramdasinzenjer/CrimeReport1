package srt.inz.crimereport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class User_reg extends Activity{
	EditText en,ee,eph,eid,ead,ep,ecp;
	String sn,se,sph,sid,sad,sp,scp,sdist,sityp,hos;
	
	Spinner sp_dist; ArrayAdapter<String> s1;
	Button brg;
	
	RadioGroup rgcardtyp;
	RadioButton rbcardtyp;
	int seltyp; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_reg);
		
		en=(EditText)findViewById(R.id.et_nam);
		ee=(EditText)findViewById(R.id.et_email);
		eph=(EditText)findViewById(R.id.et_phone);
		eid=(EditText)findViewById(R.id.et_uid);
		ead=(EditText)findViewById(R.id.et_addr);
		ep=(EditText)findViewById(R.id.et_ps);
		ecp=(EditText)findViewById(R.id.et_cps);
		sp_dist=(Spinner)findViewById(R.id.sp_dist);
		
		brg=(Button)findViewById(R.id.bt_register);
		rgcardtyp=(RadioGroup)findViewById(R.id.radioGroupUsr);
		
		
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
				sad=ead.getText().toString();
				sp=ep.getText().toString();
				scp=ecp.getText().toString();
				
				seltyp=rgcardtyp.getCheckedRadioButtonId();
				rbcardtyp=(RadioButton)findViewById(seltyp);
				sityp=rbcardtyp.getText().toString();
				
				new usr_reg().execute();
				
			}
		});
		
	}
	
	public class usr_reg extends AsyncTask<String, String, String>
    {
    	

    		@Override
    		protected String doInBackground(String... arg0) {
    			// TODO Auto-generated method stub
    			
    			String urlParameters = null;
	            try {
	                urlParameters =  "name=" + URLEncoder.encode(sn, "UTF-8") + "&&"
	                        + "email=" + URLEncoder.encode(se, "UTF-8") + "&&"
	                        +"phone=" + URLEncoder.encode(sph, "UTF-8") + "&&"
	                        
	                        + "address=" + URLEncoder.encode(sad, "UTF-8") + "&&"
	                        +"user_id=" + URLEncoder.encode(sid, "UTF-8") + "&&"
	                        + "password=" + URLEncoder.encode(sp, "UTF-8") + "&&"
	                        +"id_type=" + URLEncoder.encode(sityp, "UTF-8") + "&&"
	                        + "district=" + URLEncoder.encode(sdist, "UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	            hos = Connectivity.excutePost(Constants.REGISTER_URL,
	                    urlParameters);
	            Log.e("You are at", "" + hos);
	       return hos;
    		}
    		@Override
    		protected void onPostExecute(String result) {
    			// TODO Auto-generated method stub
    			if(hos.contains("success"))
    			{
    				Toast.makeText(getApplicationContext(), "Successfully registered. Please log in.", Toast.LENGTH_LONG).show();	
    				Intent i=new Intent(getApplicationContext(),MainPage.class);
    				startActivity(i);
    			}
    			else
    			{
    				Toast.makeText(getApplicationContext(), "Registration error : "+hos, Toast.LENGTH_LONG).show();
    			}
    			super.onPostExecute(result);
    			
    	
    }
    	}

}
