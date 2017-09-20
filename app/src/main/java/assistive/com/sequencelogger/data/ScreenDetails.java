package assistive.com.sequencelogger.data;

/**
 * Created by unzi on 08/03/2017.
 */

public class ScreenDetails {

   private int screenWidth;
    private int screenHeight ;
    private float density;
    private int densityDpi;
    private  float scaledDensity;

    public ScreenDetails(){

    }

    public ScreenDetails( int screenWidth, int screenHeight, float density, int densityDpi,  float scaledDensity){
        this.scaledDensity=scaledDensity;
        this.screenHeight=screenHeight;
        this.screenWidth=screenWidth;
        this.density = density;
        this.densityDpi =densityDpi;

    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public int getDensityDpi() {
        return densityDpi;
    }

    public void setDensityDpi(int densityDpi) {
        this.densityDpi = densityDpi;
    }

    public float getScaledDensity() {
        return scaledDensity;
    }

    public void setScaledDensity(float scaledDensity) {
        this.scaledDensity = scaledDensity;
    }
}
