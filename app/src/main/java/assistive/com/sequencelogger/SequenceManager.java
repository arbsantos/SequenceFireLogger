package assistive.com.sequencelogger;

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


    public SequenceManager() {
        workflows = new ArrayList<Workflow>();
        currentPackage = "";
    }

    public static SequenceManager sharedInstance() {
        if (mSharedInstance == null)
            mSharedInstance = new SequenceManager();
        return mSharedInstance;
    }

    public void createSequence() {
        workflows = new ArrayList<Workflow>();

    }

    public void addStep(AccessibilityNodeInfo node, String eventText, String event_package) {
        long time = System.currentTimeMillis();

        if (!currentPackage.equals(event_package)) {
            currentPackage = event_package;
            if (currentWorkflow != null)
                workflows.add(currentWorkflow);
            currentWorkflow = new Workflow(event_package);
        }

        currentWorkflow.addStep(new Step(node, eventText, event_package, time));
    }


    public void possibleBack() {
        long time = System.currentTimeMillis();
        if (currentWorkflow != null)
            currentWorkflow.addStep(new Step("Back", time));
    }

    public void saveWorkflows() {
        if (currentWorkflow!=null && currentWorkflow.size() > 0) {
            workflows.add(currentWorkflow);
        }
        currentWorkflow = null;

        cleanWorkflowsBacks(workflows);

        for (Workflow workflow : workflows) {

            String packageName = workflow.getPackageName();
            ArrayList<Step> value = workflow.getSteps();

            //workflows have to contain at least 3 steps
            if (value.size() >2) {
                FirebaseSingleton.addWorkflow(packageName, value);
            }

            // do what you have to do here
            // In your case, an other loop.
        }

        createSequence();


    }

    private void cleanWorkflowsBacks(ArrayList<Workflow> workflows) {
        for (Workflow workflow : workflows) {

            workflow.removeLastBacks();


            // do what you have to do here
            // In your case, an other loop.
        }
    }


}
