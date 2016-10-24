package com.rs.timepass.Utils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by hardikjani on 10/12/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        System.out.println("Refreshed token: " + refreshedToken);
        if (Pref.getValue(getApplicationContext(), Config.PREF_PUSH_ID, "") != null) {
            Pref.setValue(getApplicationContext(), Config.PREF_PUSH_ID, refreshedToken);
        }
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}