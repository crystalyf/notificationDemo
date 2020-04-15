package tokyo.urbanlife.notificationdemo.util.notification;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tokyo.urbanlife.notificationdemo.R;

@SuppressLint("ViewConstructor")
public class FloatView extends LinearLayout {

    private float touchX = 0;
    private float startY = 0;
    public LinearLayout rootView;
    public int originalLeft;
    private float validWidth;
    private VelocityTracker velocityTracker;
    private int maxVelocity;

    private ScrollOrientationEnum scrollOrientationEnum = ScrollOrientationEnum.NONE;

    public static WindowManager.LayoutParams winParams = new WindowManager.LayoutParams();

    public FloatView(final Context context) {
        super(context);
        int viewWidth;
        LinearLayout view = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.notification_bg, null);
        maxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        rootView = (LinearLayout) view.findViewById(R.id.rootView);
        addView(view);
        viewWidth = context.getResources().getDisplayMetrics().widthPixels;
        validWidth = viewWidth / 2.0f;
        originalLeft = 0;
    }

    private void setCustomView(View view) {
        rootView.addView(view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    private HeadsUp headsUp;
    private long cutDown;
    private Handler mHandle = null;
    private CutDownTime cutDownTime;

    private class CutDownTime extends Thread {

        @Override
        public void run() {
            super.run();


            while (cutDown > 0) {
                try {
                    Thread.sleep(1000);
                    cutDown--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (cutDown == 0) {
                mHandle.sendEmptyMessage(0);
            }


        }
    }

    public HeadsUp getHeadsUp() {
        return headsUp;
    }

    private int pointerId;

    public boolean onTouchEvent(MotionEvent event) {
        float rawX;
        float rawY;
        rawX = event.getRawX();
        rawY = event.getRawY();
        acquireVelocityTracker(event);
        cutDown = headsUp.getDuration();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                startY = event.getRawY();
                pointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                switch (scrollOrientationEnum) {
                    case NONE:
                        if (Math.abs((rawX - touchX)) > 20) {
                            scrollOrientationEnum = ScrollOrientationEnum.HORIZONTAL;

                        } else if (startY - rawY > 20) {
                            scrollOrientationEnum = ScrollOrientationEnum.VERTICAL;

                        }

                        break;
                    case HORIZONTAL:
                        updatePosition((int) (rawX - touchX));
                        break;
                    case VERTICAL:
                        if (startY - rawY > 20) {
                            cancel();
                        }
                        break;
                }

                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                int dis = (int) velocityTracker.getYVelocity(pointerId);
                if (scrollOrientationEnum == ScrollOrientationEnum.NONE) {
                    if (headsUp.getNotification().contentIntent != null) {

                        try {
                            headsUp.getNotification().contentIntent.send();
                            cancel();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }


                int toX;
                if (preLeft > 0) {
                    toX =preLeft + Math.abs(dis);
                } else {
                    toX = preLeft - Math.abs(dis);
                }
                if (toX <= -validWidth) {
                    float preAlpha = 1 - Math.abs(preLeft) / validWidth;
                    preAlpha = preAlpha >= 0 ? preAlpha : 0;
                    translationX(preLeft, -(validWidth + 10), preAlpha, 0);
                } else if (toX <= validWidth) {
                    float preAlpha = 1 - Math.abs(preLeft) / validWidth;
                    preAlpha = preAlpha >= 0 ? preAlpha : 0;
                    translationX(preLeft, 0, preAlpha, 1);

                } else {
                    float preAlpha = 1 - Math.abs(preLeft) / validWidth;
                    preAlpha = preAlpha >= 0 ? preAlpha : 0;
                    translationX(preLeft, validWidth + 10, preAlpha, 0);
                }
                preLeft = 0;
                scrollOrientationEnum = ScrollOrientationEnum.NONE;
                break;
        }

        return super.onTouchEvent(event);

    }

    /**
     * @param event add MotionEvent to VelocityTracker
     * @see VelocityTracker#obtain()
     * @see VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(MotionEvent event) {
        if (null == velocityTracker) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
    }


    private int preLeft;

    private void updatePosition(int left) {

        float preAlpha = 1 - Math.abs(preLeft) / validWidth;
        float leftAlpha = 1 - Math.abs(left) / validWidth;
        preAlpha = preAlpha >= 0 ? preAlpha : 0;
        leftAlpha = leftAlpha >= 0 ? leftAlpha : 0;
        translationX(preLeft, left, preAlpha, leftAlpha);

        preLeft = left;
    }


    private void translationX(float fromX, float toX, float formAlpha, final float toAlpha) {
        ObjectAnimator a1 = ObjectAnimator.ofFloat(rootView, "alpha", formAlpha, toAlpha);
        ObjectAnimator a2 = ObjectAnimator.ofFloat(rootView, "translationX", fromX, toX);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(a1, a2);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (toAlpha == 0) {
                    HeadsUpManager.getInstant(getContext()).dismiss();
                    HeadsUpManager.getInstant(getContext()).clear();

                    cutDown = -1;
                    if (velocityTracker != null) {
                        velocityTracker.clear();
                        try {
                            velocityTracker.recycle();
                        } catch (IllegalStateException e) {
                            e.fillInStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.start();
    }

    public void setNotification(final HeadsUp headsUp) {

        this.headsUp = headsUp;

        mHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                HeadsUpManager.getInstant(getContext()).animDismiss(headsUp);
            }
        };

        cutDownTime = new CutDownTime();

        if (!headsUp.isSticky()) {
            cutDownTime.start();
        }

        cutDown = headsUp.getDuration();

        if (headsUp.getCustomView() == null) {

            View defaultView = LayoutInflater.from(getContext()).inflate(R.layout.notification, rootView, false);
            rootView.addView(defaultView);
            ImageView imageView = (ImageView) defaultView.findViewById(R.id.iconIM);
            TextView titleTV = (TextView) defaultView.findViewById(R.id.titleTV);
            TextView timeTV = (TextView) defaultView.findViewById(R.id.timeTV);
            TextView messageTV = (TextView) defaultView.findViewById(R.id.messageTV);
            imageView.setImageResource(headsUp.getSmallIcon());
            titleTV.setText(headsUp.getTitleStr());
            messageTV.setText(headsUp.getMsgStr());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
            timeTV.setText(simpleDateFormat.format(new Date()));
        } else {
            setCustomView(headsUp.getCustomView());
        }
    }

    private void cancel() {
        HeadsUpManager.getInstant(getContext()).animDismiss();
        HeadsUpManager.getInstant(getContext()).clear();
        cutDown = -1;
        cutDownTime.interrupt();

        if (velocityTracker != null) {
            try {
                velocityTracker.clear();
                velocityTracker.recycle();
            } catch (IllegalStateException e) {
                e.fillInStackTrace();
            }
        }
    }

    private enum ScrollOrientationEnum {
        VERTICAL, HORIZONTAL, NONE
    }
}
