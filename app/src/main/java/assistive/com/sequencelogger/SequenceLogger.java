package assistive.com.sequencelogger;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


/**
 * Created by andre on 25-May-15.
 */
public class SequenceLogger extends AccessibilityService {
    final String TAG = SequenceLogger.class.getSimpleName();
    private SequenceManager sm = null;

    private int clickCounter;
    private int stateCounter;

    private String currentPackage;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        AccessibilityNodeInfo source = event.getSource();

        if (event != null) {
            //Log.d(TAG, "Event Type:" + AccessibilityEvent.eventTypeToString(event.getEventType()));
            String event_package = "";
            if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                clickCounter = 1;
                //Log.d(TAG, "Click");

                if (event.getPackageName() != null) {
                    event_package = event.getPackageName().toString();
                    currentPackage = event_package;
                }


                String eventText = "";
                if (event.getText().size() > 0) {
                    eventText = event.getText().get(0).toString();
                }

                //Log.d(TAG, "Event:" + eventText);

                sm.addStep(source, eventText, event_package);
                clickTimeout.removeCallbacks(click);
                clickTimeout.postDelayed(click, 1500);
            } else {
                clickCounter--;
                if (event.getPackageName() != null) {
                    backDetector.postDelayed(new Runnable() {
                        public void run() {
                            if (clickCounter < 0) {
                                sm.possibleBack();
                                clickCounter++;
                            }
                        }
                    }, 50);
                }
            }
        }
        //Log.d(TAG, "Click Counter:" + clickCounter);

    }

    Handler backDetector = new Handler();
    Handler clickTimeout = new Handler();

    Runnable click = new Runnable() {
        public void run() {
            if (clickCounter > 0)
                clickCounter = 0;
        }
    };


    /**
     * Get root parent from node source
     *
     * @param source
     * @return
     */
    private AccessibilityNodeInfo getRootParent(AccessibilityNodeInfo source) {
        AccessibilityNodeInfo current = source;
        while (current.getParent() != null) {
            AccessibilityNodeInfo oldCurrent = current;
            current = current.getParent();
            oldCurrent.recycle();
        }
        return current;
    }


    @Override
    public void onInterrupt() {

    }

    @Override
    public void onServiceConnected() {
        sm = sm.sharedInstance();
        currentPackage = "";

        Intent myIntent = new Intent(this, SignIn.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);

        BroadcastReceiver mReceiver = new AppAndScreenListener();
        registerReceiver(mReceiver, filter);
        Log.d(TAG, "CONNECTED");

    }

}
