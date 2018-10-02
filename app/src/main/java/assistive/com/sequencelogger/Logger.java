package assistive.com.sequencelogger;

import android.util.Log;

public class Logger {

    private static boolean debug = true;

    public static void debug(String TAG, String message){
        if(debug){
            Log.d(TAG, message);
        }
    }

    public static void error(String TAG, String message){
        if(debug){
            Log.e(TAG, message);
        }
    }
}
