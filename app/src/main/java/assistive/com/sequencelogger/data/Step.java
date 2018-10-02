package assistive.com.sequencelogger.data;

import android.graphics.Rect;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by andre on 27-May-15.
 */
public class Step {
    private String type;
    private String text;
    private String content;
    private long timestamp;
    private String activity;
    private String boundsInParent;
    private String  boundsInScreen;
    private String classname;

    private  String closestText;
    private  String eventText;
    private  String eventPackage;
    private  boolean editable;
    private  boolean clickable;
    private  boolean  scrollable ;
    private  boolean checkable ;
    private  boolean longClickable ;
    private  boolean visibleToUser ;
    private  boolean focusable ;
    private  boolean password ;
    private  boolean multiLine;
    private  boolean dismissable;
    private  boolean contextClickable;

    private int width;
    private int height;

    private int xInParent;
    private int yInParent;

    private int xInScreen;
    private int yInScreen;

    private final int THRESHOLD=100;


    private final static String TAG = "SequenceLog";

    public Step(){

    }

    public Step(AccessibilityNodeInfo node, String type, String eventText,String eventPackage, long timestamp){
        if (node!=null) {
            this.type=type;

            this.text = (node.getText() !=null) ? node.getText().toString(): null;
            this.content = (node.getContentDescription() !=null) ? node.getContentDescription().toString() : null;
            this.activity = node.getPackageName().toString();

            Rect boundsP = new Rect();
            node.getBoundsInParent(boundsP);
            this.boundsInParent = boundsP.flattenToString();

            Rect boundsS = new Rect();
            node.getBoundsInScreen(boundsS);
            this.boundsInScreen =  boundsS.flattenToString();
            this.classname = node.getClassName().toString();

            this.width = boundsS.width();
            this.height= boundsS.height();

            this.xInScreen = boundsS.left;
            this.yInScreen = boundsS.top;

            this.xInParent = boundsP.left;
            this.yInParent = boundsP.top;

            clickable = node.isClickable();
            scrollable = node.isScrollable();
            checkable = node.isCheckable();
            longClickable = node.isLongClickable();
            visibleToUser = node.isVisibleToUser();
            focusable = node.isFocusable();
            password = node.isPassword();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                editable = node.isEditable();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    multiLine = node.isMultiLine();
                    dismissable = node.isDismissable();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        contextClickable = node.isContextClickable();
                    }
                }
            }
            this.closestText = getClosestDescription(node);
        }

        this.timestamp = timestamp;
        this.eventText = eventText;
        this.eventPackage = eventPackage;
    }

    public Step(String type, long timestamp) {
        this.type=type;
        this.timestamp=timestamp;
    }


    public String getEventPackage() {
        return eventPackage;
    }

    public String getClosestText() {
        return closestText;
    }

    public String getEventText() {
        return eventText;
    }

    public String getText() {
        return text;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getActivity() {
        return activity;
    }

    public String getBoundsInParent() {
        return boundsInParent;
    }

    public String getBoundsInScreen() {
        return boundsInScreen;
    }

    public String getClassname() {
        return classname;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setBoundsInParent(String boundsInParent) {
        this.boundsInParent = boundsInParent;
    }

    public void setBoundsInScreen(String boundsInScreen) {
        this.boundsInScreen = boundsInScreen;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setClosestText(String closestText) {
        this.closestText = closestText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    public void setEventPackage(String eventPackage) {
        this.eventPackage = eventPackage;
    }

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean getClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean getScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public boolean getCheckable() {
        return checkable;
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    public boolean getLongClickable() {
        return longClickable;
    }

    public void setLongClickable(boolean longClickable) {
        this.longClickable = longClickable;
    }

    public boolean getVisibleToUser() {
        return visibleToUser;
    }

    public void setVisibleToUser(boolean visibleToUser) {
        this.visibleToUser = visibleToUser;
    }

    public boolean getFocusable() {
        return focusable;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public boolean getPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public boolean getMultiLine() {
        return multiLine;
    }

    public void setMultiLine(boolean multiLine) {
        this.multiLine = multiLine;
    }

    public boolean getDismissable() {
        return dismissable;
    }

    public void setDismissable(boolean dismissable) {
        this.dismissable = dismissable;
    }

    public boolean getContextClickable() {
        return contextClickable;
    }

    public void setContextClickable(boolean contextClickable) {
        this.contextClickable = contextClickable;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getXInParent() {
        return xInParent;
    }

    public void setXInParent(int xInParent) {
        this.xInParent = xInParent;
    }

    public int getYInParent() {
        return yInParent;
    }

    public void setYInParent(int yInParent) {
        this.yInParent = yInParent;
    }

    public int getXInScreen() {
        return xInScreen;
    }

    public void setXInScreen(int xInScreen) {
        this.xInScreen = xInScreen;
    }

    public int getYInScreen() {
        return yInScreen;
    }

    public void setYInScreen(int yInScreen) {
        this.yInScreen = yInScreen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * If creating macro is active it sends the text of the clicked node
     */
    private String getClosestDescription(AccessibilityNodeInfo src) {
        try {
            if (src != null) {
                String text;
                if ((text = getDescription(src)) != null) {
                    return cleanText(text);
                }
                else {
                    int numchild = src.getChildCount();
                    for (int i = 0; i < numchild; i++) {
                        if ((text = getDescription(src.getChild(i))) != null) {
                            return  cleanText(text);
                        } else {
                            src.getChild(i).recycle();
                        }
                    }
                    src = src.getParent();
                    numchild = src.getChildCount();
                    for (int i = 0; i < numchild; i++) {
                        if ((text = getDescription(src.getChild(i))) != null) {

                            return cleanText(text);
                        } else {
                            src.getChild(i).recycle();
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    private String cleanText(String text) {
        String  result  = text.replaceAll("\'"," ");
        result = result.substring(0, Math.min(result.length(),THRESHOLD));
        return result;
    }

    /**
     * Gets the node text either getText() or contentDescription
     *
     * @param src
     * @return node text/description null if it doesnt have
     */
    public static String getDescription(AccessibilityNodeInfo src) {
        String text = null;

        if (src.getText() != null || src.getContentDescription() != null) {
            if (src.getText() != null)
                text = src.getText().toString();
            else
                text = src.getContentDescription().toString();
            src.recycle();
        }

        return text;
    }
}
