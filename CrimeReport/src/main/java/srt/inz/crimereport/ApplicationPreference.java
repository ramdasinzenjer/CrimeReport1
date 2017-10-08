package srt.inz.crimereport;

import srt.inz.connnectors.Constants;
import android.app.Application;
import android.content.SharedPreferences;


 public class ApplicationPreference extends Application {
    private static SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    String Username,Password; String mlattitude,mlongitude;
    Boolean LoginStatus,ServiceStatus; String styp;

    public Boolean getLoginStatus() {
        LoginStatus= appSharedPrefs.getBoolean(Constants.LOGINSTATUS, false);     
        return LoginStatus;
    }

    public void setLoginStatus(Boolean loginStatus) {
        prefsEditor.putBoolean(Constants.LOGINSTATUS,loginStatus);
        prefsEditor.commit();
    }
    public Boolean getServiceStatus() {
    	ServiceStatus= appSharedPrefs.getBoolean(Constants.SERVICESTATUS, false);     
        return ServiceStatus;
    }

    public void setServiceStatus(Boolean serviceStatus) {
        prefsEditor.putBoolean(Constants.SERVICESTATUS,serviceStatus);
        prefsEditor.commit();
    }
    
    public String getUserId() {
    	Username= appSharedPrefs.getString(Constants.USERNAME, "");     
        return Username;
    }
    
    
    public void setUserType(String usertype) {
        prefsEditor.putString(Constants.USERTYPE,usertype);
        prefsEditor.commit();
    }

    public String getUserType() {
    	styp= appSharedPrefs.getString(Constants.USERTYPE, "");     
        return styp;
    }
    
    
    

    public void setUserId(String username) {
        prefsEditor.putString(Constants.USERNAME,username);
        prefsEditor.commit();
    }

    public String getLongitude() {
    	mlongitude= appSharedPrefs.getString(Constants.MYLOCATIONLON, "");     
        return mlongitude;
    }

    public void setLongitude(String longitude) {
        prefsEditor.putString(Constants.MYLOCATIONLON,longitude);
        prefsEditor.commit();
    }
    
    public String getLattitude() {
    	mlattitude= appSharedPrefs.getString(Constants.MYLOCATIONLAT, "");     
        return mlattitude;
    }

    public void setLattitude(String lattitude) {
        prefsEditor.putString(Constants.MYLOCATIONLAT,lattitude);
        prefsEditor.commit();
    }
    
    @SuppressWarnings("static-access")
	@Override
    public void onCreate() {
        super.onCreate();
        this.appSharedPrefs = getApplicationContext().getSharedPreferences(
                Constants.PREFERENCE_PARENT, MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
        
    }

}
