package assistive.com.sequencelogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import assistive.com.sequencelogger.data.AppDetails;
import assistive.com.sequencelogger.data.ScreenDetails;
import assistive.com.sequencelogger.data.User;

/**
 * Created by andre on 27-May-15.
 */
public class AppAndScreenListener extends BroadcastReceiver {
    private final String TAG = AppAndScreenListener.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Logger.debug(TAG, "Received Screen On");
            //SequenceManager sm = SequenceManager.sharedInstance();
            //sm.saveWorkflows();
        }else{
            Logger.debug(TAG, "Received update:" + intent.getAction());
            updateUser(context);
        }
    }

    //TODO remove this duplicate code either from here or from sign in
    public void updateUser(Context c) {

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        int sdk = Build.VERSION.SDK_INT;
        ArrayList<AppDetails> apps = installedApplications(c);

        User userData = new User("",manufacturer,model, product,sdk,apps,null);
        //TODO update instaled apps
        //FirebaseSingleton.updateUser(userData);

    }

    private ArrayList<AppDetails> installedApplications(Context c){
        final PackageManager pm = c.getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList <AppDetails> apps = new ArrayList<AppDetails>();
        for (ApplicationInfo packageInfo : packages) {
            PackageInfo pInfo = null;
            try {
                pInfo = c.getPackageManager().getPackageInfo(packageInfo.packageName, 0);
                apps.add(new AppDetails(packageInfo.packageName,packageInfo.sourceDir,pInfo.versionName,pInfo.versionCode ));

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return apps;
    }

}
