package assistive.com.sequencelogger;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import assistive.com.sequencelogger.data.Step;
import assistive.com.sequencelogger.data.User;

/**
 * Created by unzi on 23/02/2017.
 */

public class FirebaseSingleton {

    private final static String TAG = FirebaseSingleton.class.getSimpleName();

    private static FirebaseDatabase database;


    public static FirebaseDatabase getDatabase(){
        if(database==null)  {
             database = FirebaseDatabase.getInstance();
        }
        return  database;
    }

    public static void addWorkflow(String packageName, String appName,ArrayList <Step> steps){
        Logger.debug(TAG, "User:"+FirebaseAuth.getInstance().getCurrentUser().getUid() );

        String packageNameClean = cleanText(packageName);

        //set the name of the app
        getDatabase().getReference().child("Apps").child(packageNameClean).child("name").setValue(appName);

        String workflowId = getDatabase().getReference().child(packageNameClean).push().getKey();

        getDatabase().getReference().child("Apps").child(packageNameClean).child(workflowId).child("UID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        getDatabase().getReference().child("Apps").child(packageNameClean).child(workflowId).child("timestamp").setValue(System.currentTimeMillis());
        for(Step step : steps)
            getDatabase().getReference().child("Apps").child(packageNameClean).child(workflowId).child("Steps").push().setValue(step);
    }

    private static String cleanText(String text) {
        String  result  = text.replace(".","_");
        return result;
    }


    public static void registerUser(User userData) {
        Logger.debug(TAG, "Registering User:" + userData.getUid() );
        getDatabase().getReference().child("Users").child(userData.getUid()).setValue(userData);
    }

    public static void updateUser(User userData) {
        userData.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        getDatabase().getReference().child("Users").child(userData.getUid()).setValue(userData);

    }
}
