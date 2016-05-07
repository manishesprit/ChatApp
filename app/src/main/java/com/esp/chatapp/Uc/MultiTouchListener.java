package com.esp.chatapp.Uc;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 * Created by admin on 3/5/16.
 */
public class MultiTouchListener implements View.OnTouchListener {

    private float mPrevX;
    private float mPrevY;

    public Activity activity;

    int finalHeight;
    int finalWidth;


    public MultiTouchListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onTouch(final View view, MotionEvent event) {
        float currX, currY;
        int action = event.getAction();

        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = view.getMeasuredHeight();
                finalWidth = view.getMeasuredWidth();
                return true;
            }
        });

        switch (action) {
            case MotionEvent.ACTION_DOWN: {

                mPrevX = event.getX();
                mPrevY = event.getY();

                System.out.println("mPrevX=====" + mPrevX + "=====mPrevY====" + mPrevY);

                break;
            }

            case MotionEvent.ACTION_MOVE: {

                currX = event.getRawX();
                currY = event.getRawY();


                System.out.println("currX=====" + currX + "=====currY====" + currY + "finalWidth=====" + finalWidth + "=====finalHeight====" + finalHeight);

                if (currX + 50 > finalWidth && currY + 50 > finalHeight) {
                    ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
                    marginParams.setMargins((int) (currX - mPrevX), (int) (currY - mPrevY), 0, 0);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                    view.setLayoutParams(layoutParams);
                }

                break;
            }


            case MotionEvent.ACTION_CANCEL:
                break;

            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

}