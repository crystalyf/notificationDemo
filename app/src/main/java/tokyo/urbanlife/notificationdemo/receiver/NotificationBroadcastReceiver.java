package tokyo.urbanlife.notificationdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import tokyo.urbanlife.notificationdemo.bean.PushInfo;


/**
 * Created by feilang-quzhipeng on 2018/3/1.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {
            PushInfo pushInfo = (PushInfo) intent.getSerializableExtra("PUSH_DATA_EXTRA");
            Log.d("pushInfo", "already click notification");
            if (!TextUtils.isEmpty(pushInfo.getContentMessage())) {
                Log.d("pushInfo_message", pushInfo.getContentMessage());
            }
        }
    }
}
