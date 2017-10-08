package srt.inz.connnectors;


public interface Constants {

    //Progress Message
    String LOGIN_MESSAGE="Logging in...";
    String REGISTER_MESSAGE="Register in...";

    //Urls
    String BASE_URL="https://chrimeinzenjer.000webhostapp.com/";
   
    String REGISTER_URL=BASE_URL+"c_userreg.php?";
    String LOGIN_URL=BASE_URL+"c_userlogin.php?";
    String COMPLAINTREG_URL=BASE_URL+"complaint_reglc.php?";    
    String SECTIONSFETCH_URL=BASE_URL+"getsections.php?";
    String CRIMESTATUS_URL=BASE_URL+"update_crstat.php?";    
    String RETLAWLIST=BASE_URL+"ret_lawlist.php?";
    String HELPLIST=BASE_URL+"ret_helplist.php?";    
    String REGLIST_URL=BASE_URL+"ret_reglist.php?";    
    String USERSTATUSUP_URL=BASE_URL+"up_userstat.php?";
    String CRIMELISTUSER_URL=BASE_URL+"ret_crimelistusr.php?";
    String CRIMELISTRET_URL =BASE_URL+"ret_crimelist.php?";    
    String UPDATELAWS_URL =BASE_URL+"lawpoint_up.php?";
    
    String HELPCENTERUPDATE_URL=BASE_URL+"helpcenter_up.php?";
    String NEWSFEED_URL=BASE_URL+"ret_newsfeed.php?";
    String NEWSFEEDUP_URL=BASE_URL+"update_newsfeed.php?";
    
    String NEWSFEEDALL_URL=BASE_URL+"ret_newsfeedall.php?";
    String UPDATENEWSSTATUS_URL=BASE_URL+"update_newsstatus.php?";
    String CRIMELOCATION_URL=BASE_URL+"crimelocfetch.php?";
    String REGISTEROFFICER_URL=BASE_URL+"officer_userreg.php?";
    
    //Details
    String PASSWORD="Password";
    String USERNAME="Username";
    String LOGINSTATUS="LoginStatus";
    String SERVICESTATUS="ServiceStatus";
    String MYLOCATIONLAT="MyLocationLat";
    String MYLOCATIONLON="MyLocationLon";
    String USERTYPE="UserType";
    
    //SharedPreference
    String PREFERENCE_PARENT="Parent_Pref";
	
   
}
