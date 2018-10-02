package assistive.com.sequencelogger;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import assistive.com.sequencelogger.data.AppDetails;
import assistive.com.sequencelogger.data.Step;
import assistive.com.sequencelogger.data.User;

/**
 * Created by andre on 25-May-15.
 */
public class SequenceLogger extends AccessibilityService {
    final String TAG = SequenceLogger.class.getSimpleName();
    private SequenceManager sm = null;

    private boolean recording = false;
    private String lastPackage = "";
    private String currentPackage;
    private String writtenText = "";
    private AccessibilityNodeInfo writeSource;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event != null) {
            Logger.debug(TAG, event.toString());
        }

        if (recording) {

            boolean back = false;
            boolean home = false;

            if (event != null && !event.getPackageName().equals("assistive.com.sequencelogger")) {

                Logger.debug(TAG, event.toString());

                if (event.getPackageName() != null && event.getClassName() != null && event.getContentDescription() != null) {

                    if (event.getSource() != null ){

                        //back and home detection
                        //PS: only tested on Android AOSP 9 (PIE) rom
                        //Probably other roms have different names and/or resource Ids
                        if(event.getSource().getViewIdResourceName() != null) {
                            if (event.getSource().getViewIdResourceName().equals("com.android.systemui:id/back"))
                                back = true;

                            else if (event.getSource().getViewIdResourceName().equals("com.android.systemui:id/home_button"))
                                home = true;

                            else if (event.getPackageName().equals("com.android.systemui") &&
                                    event.getClassName().equals("android.widget.ImageView") &&
                                    (event.getContentDescription().equals("Anterior") || event.getContentDescription().equals("Back")))
                                back = true;

                            else if (event.getPackageName().equals("com.android.systemui") &&
                                    event.getClassName().equals("android.widget.ImageView") &&
                                    (event.getContentDescription().equals("Página inicial") || event.getContentDescription().equals("Home")))
                                home = true;
                        }
                    }
                }

                AccessibilityNodeInfo source = event.getSource();

                Logger.debug(TAG, event.toString());

                Logger.debug(TAG, "Event Type:" + AccessibilityEvent.eventTypeToString(event.getEventType()));
                String event_package = "";

                //back and home come with sysUI package but we want to keep them inside the app
                if (back || home){
                    event_package = lastPackage;
                }
                else if (event.getPackageName() != null) {
                    event_package = event.getPackageName().toString();
                    currentPackage = event_package;
                }

                //CLICK
                if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {

                    Logger.debug(TAG, "Click");


                    Logger.debug(TAG, "EVENT PKG" + event_package);

                    String eventText = "";
                    if (event.getText().size() > 0) {
                        eventText = event.getText().get(0).toString();
                    }

                    Logger.debug(TAG, "Event:" + eventText);

                    if (back)
                        sm.addStep(new Step("BACK", System.currentTimeMillis()), event_package);
                    else if (home)
                        sm.addStep(new Step("HOME", System.currentTimeMillis()), event_package);
                    else
                        sm.addStep(source, "CLICK", eventText, event_package);

                    lastPackage = event_package;

                    if (!writtenText.equals("")) {
                        sm.addStep(writeSource, "SET_TEXT", writtenText, event_package);
                        writtenText = "";
                        writeSource = null;
                        //WHEN THE USER STOPS THE WORKFLOW WITHOUT CLICK ON ANYTHING ELSE
                        sm.setWrittenText(writtenText);
                        sm.setWriteSource(writeSource);
                    }

                //TEXT
                }else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
                    Logger.debug(TAG, event.getSource().toString());
                    Toast.makeText(getApplicationContext(), "Escrevemos: " + event.getText(), Toast.LENGTH_SHORT).show();
                    writtenText = String.format("%s", event.getText());
                    writeSource = event.getSource();
                    //WHEN THE USER STOPS THE WORKFLOW WITHOUT CLICK ON ANYTHING ELSE
                    sm.setWrittenText(writtenText);
                    sm.setWriteSource(writeSource);
                    sm.setWritePackage(event_package);
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        Logger.debug(TAG, "CONNECTED");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT |
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS |
                AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY |
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS |
                AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED | AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);


        sm = SequenceManager.sharedInstance();
        currentPackage = "";

        recButtonWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        recButtonWindowManager.addView(getManualRecButton(), getRecButtonLayout());



    }

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

    //Button Overlay Stuff
    private WindowManager recButtonWindowManager;
    private Button recButton = null;
    LayoutParams recButtonLayoutParams;
    boolean touchconsumedbyMove = false;
    int recButtonLastX;
    int recButtonLastY;
    int recButtonFirstX;
    int recButtonFirstY;

    View.OnClickListener recButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if (recording){
                SequenceManager sm = SequenceManager.sharedInstance();
                sm.saveWorkflows(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Stop recording", Toast.LENGTH_SHORT).show();
                recButton.setText("Start");
            }else {
                updateUser(getApplication());
                Toast.makeText(getApplicationContext(), "Start recording", Toast.LENGTH_SHORT).show();
                recButton.setText("Stop");
            }
            recording = !recording;
        }
    };

    View.OnTouchListener recButtonOnTouchListener = new View.OnTouchListener() {
        @TargetApi(Build.VERSION_CODES.FROYO)
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            LayoutParams prm = getRecButtonLayout();
            int totalDeltaX = recButtonLastX - recButtonFirstX;
            int totalDeltaY = recButtonLastY - recButtonFirstY;

            switch(event.getActionMasked())
            {
                case MotionEvent.ACTION_DOWN:
                    recButtonLastX = (int) event.getRawX();
                    recButtonLastY = (int) event.getRawY();
                    recButtonFirstX = recButtonLastX;
                    recButtonFirstY = recButtonLastY;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    int deltaX = (int) event.getRawX() - recButtonLastX;
                    int deltaY = (int) event.getRawY() - recButtonLastY;
                    recButtonLastX = (int) event.getRawX();
                    recButtonLastY = (int) event.getRawY();
                    if (Math.abs(totalDeltaX) >= 5  || Math.abs(totalDeltaY) >= 5) {
                        if (event.getPointerCount() == 1) {
                            prm.x += deltaX;
                            prm.y += deltaY;
                            touchconsumedbyMove = true;
                            recButtonWindowManager.updateViewLayout(getManualRecButton(), prm);
                        }
                        else{
                            touchconsumedbyMove = false;
                        }
                    }else{
                        touchconsumedbyMove = false;
                    }
                    break;
                default:
                    break;
            }
            return touchconsumedbyMove;
        }
    };

    private LayoutParams getRecButtonLayout() {
        if (recButtonLayoutParams != null) {
            return recButtonLayoutParams;
        }
        int LAYOUT_FLAG;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        recButtonLayoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        recButtonLayoutParams.gravity = Gravity.TOP | Gravity.END;
        return recButtonLayoutParams;
    }

    private Button getManualRecButton() {
        if (recButton != null) {
            return recButton;
        }
        recButton = new Button(getApplicationContext());
        recButton.setText("Start");
        recButton.setOnClickListener(recButtonOnClickListener);
        recButton.setOnTouchListener(recButtonOnTouchListener);
        return recButton;
    }


}
