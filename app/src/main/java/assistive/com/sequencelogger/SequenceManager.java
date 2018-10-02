package assistive.com.sequencelogger;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;

import assistive.com.sequencelogger.data.Step;
import assistive.com.sequencelogger.data.Workflow;


public class SequenceManager {

    private final static String TAG = SequenceManager.class.getSimpleName();
    private static SequenceManager mSharedInstance = null;

    private Workflow currentWorkflow;

    private ArrayList<Workflow> workflows;
    private String currentPackage;


    private String writtenText = "";
    private AccessibilityNodeInfo writeSource;
    private String writePackage;

    private SequenceManager() {
        workflows = new ArrayList<Workflow>();
        currentPackage = "";
    }

    public static SequenceManager sharedInstance() {
        if (mSharedInstance == null)
            mSharedInstance = new SequenceManager();
        return mSharedInstance;
    }

    public String getWrittenText() {
        return writtenText;
    }

    public void setWrittenText(String writtenText) {
        this.writtenText = writtenText;
    }

    public AccessibilityNodeInfo getWriteSource() {
        return writeSource;
    }

    public void setWriteSource(AccessibilityNodeInfo writeSource) {
        this.writeSource = writeSource;
    }

    public String getWritePackage() {
        return writePackage;
    }

    public void setWritePackage(String writePackage) {
        this.writePackage = writePackage;
    }

    public void createSequence() {
        workflows = new ArrayList<Workflow>();

    }


    public void addStep(AccessibilityNodeInfo node, String type, String eventText, String event_package) {
        long time = System.currentTimeMillis();

        Logger.debug(TAG, currentPackage);
        Logger.debug(TAG, event_package);

        if (!currentPackage.equals(event_package)) {
            currentPackage = event_package;
            if (currentWorkflow != null)
                workflows.add(currentWorkflow);
            currentWorkflow = new Workflow(event_package);
        }
        currentWorkflow.addStep(new Step(node, type, eventText, event_package, time));
    }

    public void addStep(Step step, String event_package) {

        if (!currentPackage.equals(event_package)) {
            currentPackage = event_package;
            if (currentWorkflow != null)
                workflows.add(currentWorkflow);
            currentWorkflow = new Workflow(event_package);
        }
        currentWorkflow.addStep(step);
    }


    public void saveWorkflows(Context ctx) {

        //Save the last text event if any
        if(!writtenText.equals("")){
            this.addStep(writeSource, "SET_TEXT", writtenText, writePackage);
        }

        if (currentWorkflow!=null && currentWorkflow.size() > 0) {
            workflows.add(currentWorkflow);
        }
        currentWorkflow = null;

        for (Workflow workflow : workflows) {

            String packageName = workflow.getPackageName();
            ArrayList<Step> value = workflow.getSteps();

            //workflows have to contain at least 3 steps
            Logger.debug(TAG, "SIZE: " + value.size());
            FirebaseSingleton.addWorkflow(packageName,getApplicationName(ctx, packageName), value);

        }

        createSequence();


    }

    public String getApplicationName(Context ctx, final String packageName) {
        String appName = "unknown";
        PackageManager packageManager = ctx.getPackageManager();
        try {
            appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return appName;
    }

}
