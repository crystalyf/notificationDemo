package tokyo.urbanlife.notificationdemo.util.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;


public class HeadsUp {

    private Context context;

    private long duration = 5;

    private Notification notification;

    private Builder builder;

    private boolean isSticky = false;

    private boolean activateStatusBar = true;

    /**
     * Interval time
     */
    private long interval = 600;
    private int code;
    private List<NotificationCompat.Action> actions;
    private CharSequence titleStr;
    private CharSequence msgStr;
    private int icon;
    private View customView;

    private HeadsUp(Context context) {
        this.context = context;
    }


    public static class Builder extends NotificationCompat.Builder {

        private List<NotificationCompat.Action> actions = new ArrayList<>();
        private HeadsUp headsUp;

        public Builder(Context context) {
            super(context);
            headsUp = new HeadsUp(context);
        }

        /**
         * Set the first line of text in the platform notification template.
         */
        public Builder setContentTitle(CharSequence title) {
            headsUp.setTitle(title);
            super.setContentTitle(title);
            return this;
        }

        /**
         * Set the second line of text in the platform notification template.
         */
        public Builder setContentText(CharSequence text) {
            headsUp.setMessage(text);
            super.setContentText(text);
            return this;
        }

        public Builder setSmallIcon(int icon) {
            headsUp.setSmallIcon(icon);
            super.setSmallIcon(icon);
            return this;
        }

        public Builder setSticky(boolean isSticky) {
            headsUp.setSticky(isSticky);
            return this;
        }

        public HeadsUp buildHeadUp() {
            headsUp.setNotification(this.build());
            headsUp.setActions(actions);
            headsUp.setBuilder(this);
            return headsUp;
        }

        @Override
        public Builder setAutoCancel(boolean autoCancel) {
            super.setAutoCancel(autoCancel);
            return this;
        }

        @Override
        public Builder setColor(int argb) {
            super.setColor(argb);
            return this;
        }

        @Override
        public Builder setDefaults(int defaults) {
            super.setDefaults(defaults);
            return this;
        }

        @Override
        public Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
            headsUp.setSticky(true);
            super.setFullScreenIntent(intent, highPriority);
            return this;
        }

        @Override
        public Builder setOngoing(boolean ongoing) {
            super.setOngoing(ongoing);
            return this;
        }


        @Override
        public Builder setVibrate(long[] pattern) {
            super.setVibrate(pattern);
            return this;
        }


        @Override
        public Builder setLargeIcon(Bitmap icon) {
            super.setLargeIcon(icon);
            return this;
        }

        @Override
        public Builder setLights(int argb, int onMs, int offMs) {
            super.setLights(argb, onMs, offMs);
            return this;
        }

        @Override
        public Builder setWhen(long when) {
            super.setWhen(when);
            return this;
        }

        @Override
        public Builder setShowWhen(boolean show) {
            super.setShowWhen(show);
            return this;
        }

        @Override
        public Builder setSubText(CharSequence text) {
            super.setSubText(text);
            return this;
        }

        @Override
        public Builder setNumber(int number) {
            super.setNumber(number);
            return this;
        }

        @Override
        public Builder setContentInfo(CharSequence info) {
            super.setContentInfo(info);
            return this;
        }

        @Override
        public Builder setProgress(int max, int progress, boolean indeterminate) {
            super.setProgress(max, progress, indeterminate);
            return this;
        }

        @Override
        public Builder setContent(RemoteViews views) {
            super.setContent(views);
            return this;
        }

        @Override
        public Builder setContentIntent(PendingIntent intent) {
            super.setContentIntent(intent);
            return this;
        }

        @Override
        public Builder setDeleteIntent(PendingIntent intent) {
            super.setDeleteIntent(intent);
            return this;
        }

        @Override
        public Builder setTicker(CharSequence tickerText) {
            super.setTicker(tickerText);
            return this;
        }

        @Override
        public Builder setTicker(CharSequence tickerText, RemoteViews views) {
            super.setTicker(tickerText, views);
            return this;
        }

        @Override
        public Builder setSound(Uri sound) {
            super.setSound(sound);
            return this;
        }

        @Override
        public Builder setSound(Uri sound, int streamType) {
            super.setSound(sound, streamType);
            return this;
        }

        @Override
        public Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            super.setOnlyAlertOnce(onlyAlertOnce);
            return this;
        }

        @Override
        public Builder setLocalOnly(boolean b) {
            super.setLocalOnly(b);
            return this;
        }

        @Override
        public Builder setCategory(String category) {
            super.setCategory(category);
            return this;
        }

        @Override
        public Builder setPriority(int pri) {
            super.setPriority(pri);
            return this;
        }

        @Override
        public Builder addPerson(String uri) {
            super.addPerson(uri);
            return this;
        }

        @Override
        public Builder setGroup(String groupKey) {
            super.setGroup(groupKey);
            return this;
        }

        @Override
        public Builder setGroupSummary(boolean isGroupSummary) {
            super.setGroupSummary(isGroupSummary);
            return this;
        }

        @Override
        public Builder setSortKey(String sortKey) {
            super.setSortKey(sortKey);
            return this;
        }

        @Override
        public Builder addExtras(Bundle extras) {
            super.addExtras(extras);
            return this;
        }

        @Override
        public Builder setExtras(Bundle extras) {
            super.setExtras(extras);
            return this;
        }

        @Override
        public Builder addAction(NotificationCompat.Action action) {
            actions.add(action);
            super.addAction(action);
            return this;
        }

        @Override
        public Builder setStyle(NotificationCompat.Style style) {
            super.setStyle(style);
            return this;
        }

        @Override
        public Builder setVisibility(int visibility) {
            super.setVisibility(visibility);
            return this;
        }

        @Override
        public Builder setPublicVersion(Notification n) {
            super.setPublicVersion(n);
            return this;
        }
    }

    private void setSmallIcon(int dRes) {
        icon = dRes;
    }

    private void setTitle(CharSequence titleStr) {
        this.titleStr = titleStr;
    }

    private void setMessage(CharSequence msgStr) {
        this.msgStr = msgStr;
    }


    public Context getContext() {
        return context;
    }

    long getDuration() {
        return duration;
    }


    public long getInterval() {
        return interval;
    }


    CharSequence getTitleStr() {
        return titleStr;
    }

    CharSequence getMsgStr() {
        return msgStr;
    }

    int getSmallIcon() {
        return icon;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }


    public Notification getNotification() {
        return notification;
    }

    private void setNotification(Notification notification) {
        this.notification = notification;
    }


    View getCustomView() {
        return customView;
    }

    public void setCustomView(View customView) {
        this.customView = customView;
    }

    int getCode() {
        return code;
    }

    void setCode(int code) {
        this.code = code;
    }

    protected List<NotificationCompat.Action> getActions() {
        return actions;
    }

    private void setActions(List<NotificationCompat.Action> actions) {
        this.actions = actions;
    }

    Builder getBuilder() {
        return builder;
    }

    private void setBuilder(Builder builder) {
        this.builder = builder;
    }

    boolean isSticky() {
        return isSticky;
    }

    private void setSticky(boolean isSticky) {
        this.isSticky = isSticky;
    }

    boolean isActivateStatusBar() {
        return activateStatusBar;
    }

    public void setActivateStatusBar(boolean activateStatusBar) {
        this.activateStatusBar = activateStatusBar;
    }
}
