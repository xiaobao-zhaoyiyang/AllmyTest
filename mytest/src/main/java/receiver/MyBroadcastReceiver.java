package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yo on 2016/6/8.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MyBroadcastReceiver", "接收广播");
        String action = intent.getAction();
        if (action.equals("broadcastTest")){
            Log.i("MyBroadcastReceiver", "接收广播:broadcastTest");
        }else if (action.equals("broad")){
            Log.i("MyBroadcastReceiver", "接收广播:broad");
        }
    }
}
