package assistive.com.sequencelogger.data;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unzi on 03/03/2017.
 */

public class User {

    private String uid;
    private  String manufacturer;
    private String model ;
    private String product ;
    private int sdk;
    private List <AppDetails> installedApps;
    private long lastUpdate;

    private ScreenDetails screenDetails;

    public User(){

    }

    public User(String uid, String manufacturer, String model, String product, int sdk, ArrayList<AppDetails> installedApps, ScreenDetails screenDetails){
        this.uid = uid;
        this.manufacturer = manufacturer;
        this.model = model;
        this.product = product;
        this.sdk = sdk;
        this.installedApps = installedApps;
        this.lastUpdate=System.currentTimeMillis();
        this.screenDetails= screenDetails;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getSdk() {
        return sdk;
    }

    public void setSdk(int sdk) {
        this.sdk = sdk;
    }

    public List<AppDetails> getInstalledApps() {
        return installedApps;
    }

    public void setInstalledApps(List<AppDetails> installedApps) {
        this.installedApps = installedApps;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ScreenDetails getScreenDetails() {
        return screenDetails;
    }

    public void setScreenDetails(ScreenDetails screenDetails) {
        this.screenDetails = screenDetails;
    }
}
