package assistive.com.sequencelogger.data;

/**
 * Created by unzi on 03/03/2017.
 */

public class AppDetails{
    private String packageName;
    private String sourceDir;
    private String versionName;
    private int version;


    public AppDetails(){

    }

    public AppDetails(String packageName, String sourceDir, String versionName, int version){
        this.packageName = packageName;
        this.sourceDir = sourceDir;
        this.versionName = versionName;
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
