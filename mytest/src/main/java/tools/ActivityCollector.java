package tools;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity的一个管理类，将启动的Activity放到一个集合中，在需要的地方进行添加移除等操作
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }
    public static void removeActivity(Activity activity){
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }
    public static void finishAll(){
        for (Activity activity : activities) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
