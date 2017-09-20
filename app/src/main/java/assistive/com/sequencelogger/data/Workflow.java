package assistive.com.sequencelogger.data;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by unzi on 03/03/2017.
 */
public class Workflow {
    private final String TAG = Workflow.class.getSimpleName();

    private ArrayList<Step> steps;
    private String packageName;


    public Workflow( ){
    }

    public Workflow(String packageName){
        this.packageName =packageName;
        steps = new ArrayList<Step>();
    }

    public void addStep(Step step){
        Log.d(TAG,"STEP:" + step.getEventText() + " " + step.getType());
        steps.add(step);
    }

    public int size(){
        return steps.size();
    }

    public ArrayList<Step> getSteps(){
        return steps;
    }

    public String getPackageName(){
        return packageName;
    }

    public void removeLastBacks() {
        for(int i=steps.size()-1;i>=0;i--){
            if(steps.get(i).getType().equals("Back"))
                steps.remove(i);
            else{
                break;
            }
        }
    }
}
