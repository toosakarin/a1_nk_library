package com.fuhu.konnect.library.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by jacktseng on 2015/9/4.
 */
public class EffectContentView extends RelativeLayout {

    private View mButton;


    protected interface OnEffectContentViewClickListener {
        public void onClick(EffectContentView view);
    }


    public EffectContentView(Context context) {
        super(context);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public boolean onTouchEvent(MotionEvent ev) {

        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                Rect rect = new Rect();
                this.getHitRect(rect);
                if(rect.contains(x,y))
                    performClick();
//                if(mButton != null) {
//                    Rect rect = new Rect();
//                    mButton.getHitRect(rect);
//                    if(rect.contains(x, y)) {
//                        mButton.performClick();
//                    }
//                }

                break;
        }

        return true;
    }

}
