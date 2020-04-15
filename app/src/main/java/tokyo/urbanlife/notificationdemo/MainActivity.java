package tokyo.urbanlife.notificationdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import tokyo.urbanlife.notificationdemo.bean.PushInfo;
import tokyo.urbanlife.notificationdemo.receiver.NotificationBroadcastReceiver;
import tokyo.urbanlife.notificationdemo.util.NotificationUtils;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_normal_notification;
    Button btn_notify_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_normal_notification = findViewById(R.id.btn_normal_notification);
        btn_notify_notification = findViewById(R.id.btn_notify_notification);
        btn_normal_notification.setOnClickListener(this);
        btn_notify_notification.setOnClickListener(this);
    }

    private void TestFcm(Boolean isPop) {
        String json = "{\"contentTitle\":\"20191107千岛湖天气情况\",\"contentMessage\":\"云卷云舒，疏影横斜\",\"thumbnailImageUrl\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573106304427&di=04536badfcec93b1126e5a60a554a3d0&imgtype=0&src=http%3A%2F%2Fimage9.huangye88.com%2F2015%2F04%2F10%2Ffcee0252c52903d8.jpg\"}";
        if (TextUtils.isEmpty(json)) {
            return;
        }
        PushInfo pushInfo = new Gson().fromJson(json, PushInfo.class);
        if (null == pushInfo) {
            return;
        }
        Intent intent = new Intent(this, NotificationBroadcastReceiver.class);
        intent.setAction("PUSH_CLICK_DOWN");
        Bundle bundle = new Bundle();
        bundle.putSerializable("PUSH_DATA_EXTRA", pushInfo);
        intent.putExtras(bundle);
        NotificationUtils.genreNotification(this, pushInfo.getContentTitle(), pushInfo.getContentMessage(), pushInfo.getThumbnailImageUrl(), 1, intent, 1, isPop);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_normal_notification:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        TestFcm(false);
                        /**
                         * 要执行的操作
                         */
                    }
                }.start();
                break;

            case R.id.btn_notify_notification:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        TestFcm(true);
                        /**
                         * 要执行的操作
                         */
                    }
                }.start();
                break;
        }
    }
}
