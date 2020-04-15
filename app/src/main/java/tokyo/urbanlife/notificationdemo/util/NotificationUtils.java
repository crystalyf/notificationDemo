package tokyo.urbanlife.notificationdemo.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import tokyo.urbanlife.notificationdemo.R;
import tokyo.urbanlife.notificationdemo.util.notification.HeadsUp;
import tokyo.urbanlife.notificationdemo.util.notification.HeadsUpManager;

public class NotificationUtils {

    private static final String PUSH_CHANNEL = "NDEMO";
    private static final String TAG = "NotificationUtils";

    public static synchronized void genreNotification(final Context context, final String contentTitle, final String message, String thumbnailUrl, final int requestCode, final Intent intent, final int notificationId, Boolean isPop) {
        if (TextUtils.isEmpty(thumbnailUrl)) {
            popNotification(context, contentTitle, message, null, requestCode, intent, notificationId);
        } else {
            try {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        popNotification(context, contentTitle, message, null, requestCode, intent, notificationId);
                    }
                }, 2000);

                RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true); //不加入内存缓存，默认会加入
                Bitmap img = Glide.with(context)
                        .asBitmap()
                        .load(thumbnailUrl)
                        .apply(requestOptions)
                        .into(LayoutUtils.getScreenWidth(context), LayoutUtils.getScreenWidth(context))
                        .get();
                timer.cancel();
                Log.v(TAG, "currentId: " + Thread.currentThread().getId() + "");
                final int imageWidth = LayoutUtils.getScreenWidth(context) - LayoutUtils.dpToPx(context, 40);
                if (isPop) {
                    popNotification(context, contentTitle, message,
                            Bitmap.createScaledBitmap(img, imageWidth, LayoutUtils.getScreenWidth(context) / 2, true),
                            requestCode, intent, notificationId);
                } else {
                    popNormalNotification(context, contentTitle, message,
                            Bitmap.createScaledBitmap(img, imageWidth, LayoutUtils.getScreenWidth(context) / 2, true),
                            requestCode, intent, notificationId);
                }
                img.recycle();
                Log.v(TAG, "already BitMap recycle");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private static void popNotification(Context context, String contentTitle, String message, Bitmap thumbnail, int requestCode, Intent intent, int notificationId) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            HeadsUpManager headsUpManager = HeadsUpManager.getInstant(context);
            HeadsUp.Builder builder = new HeadsUp.Builder(context)
                    .setContentTitle(contentTitle)
                    .setSmallIcon(R.mipmap.cat_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_MAX)  //default不弹横幅，只有max弹横幅
                    .setContentText(message);
            if (thumbnail != null) {
                NotificationCompat.BigPictureStyle pictureStyle = new NotificationCompat.BigPictureStyle();
                pictureStyle.bigPicture(thumbnail);
                builder.setStyle(pictureStyle);
            }
            headsUpManager.notify(notificationId, builder.buildHeadUp());
        } else {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL, PUSH_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(context, PUSH_CHANNEL);
            builder.setContentTitle(contentTitle);
            builder.setSmallIcon(R.mipmap.cat_icon);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.cat_icon));
            builder.setAutoCancel(true);
            builder.setContentIntent(pendingIntent);
            builder.setContentText(message);

            //fake just for try double notificationChannel
//            NotificationManager notificationManager1 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationChannel channel1 =  new NotificationChannel(PUSH_CHANNEL, "AAA", NotificationManager.IMPORTANCE_HIGH);
//            notificationManager1.createNotificationChannel(channel1);

            if (thumbnail != null) {
                Notification.BigPictureStyle pictureStyle = new Notification.BigPictureStyle();
                pictureStyle.bigPicture(thumbnail);
                builder.setStyle(pictureStyle);
            }
            notificationManager.notify(1, builder.build());
        }
    }

    private static void popNormalNotification(Context context, String contentTitle, String message, Bitmap thumbnail, int requestCode, Intent intent, int notificationId) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            HeadsUpManager headsUpManager = HeadsUpManager.getInstant(context);
            HeadsUp.Builder builder = new HeadsUp.Builder(context)
                    .setContentTitle(contentTitle)
                    .setSmallIcon(R.mipmap.cat_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_DEFAULT)  //default不弹横幅，只有max弹横幅
                    .setContentText(message);
            if (thumbnail != null) {
                NotificationCompat.BigPictureStyle pictureStyle = new NotificationCompat.BigPictureStyle();
                pictureStyle.bigPicture(thumbnail);
                builder.setStyle(pictureStyle);
            }
            headsUpManager.notify(notificationId, builder.buildHeadUp());
        } else {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL, PUSH_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(context, PUSH_CHANNEL);
            builder.setContentTitle(contentTitle);
            builder.setSmallIcon(R.mipmap.cat_icon);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.cat_icon));
            builder.setAutoCancel(true);
            builder.setContentIntent(pendingIntent);
            builder.setContentText(message);

            if (thumbnail != null) {
                Notification.BigPictureStyle pictureStyle = new Notification.BigPictureStyle();
                pictureStyle.bigPicture(thumbnail);
                builder.setStyle(pictureStyle);
            }
            notificationManager.notify(1, builder.build());
        }
    }


}
