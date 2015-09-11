package com.fuhu.konnect.library.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by jacktseng on 2015/9/4.
 */
public abstract class EffectContentView extends RelativeLayout {

    //Indicates which view is a real button of EffectContentView
    private View mButton;

    /**
     * PreOnClickListener is invoked before mButton's onClick when onClick event is dispatch by
     * onTouchEvent.
     */
    private OnClickListener mPreOnClickListener;

    /**
     * Gets the content of this effect.
     * </p>
     * Note: This function is used to obtain the element of event content of sub effect, now just
     * <br> focus to gets the sticker bitmap or background resource id.
     *
     * @return
     */
    public abstract Object getContent();

    public EffectContentView(Context context, View btn) {
        super(context);
        mButton = (btn == null) ? this : btn;
    }

    protected void setClickButton(View btn) {
        mButton = btn;
    }

    protected void setPreOnClickListener(OnClickListener listener) {
        mPreOnClickListener = listener;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mButton == null) mButton = this;
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
                if(mButton != this) {
                    Rect rect = new Rect();
                    mButton.getHitRect(rect);
                    if(!rect.contains(x, y))
                        break;
                }

                if(mPreOnClickListener != null)
                    mPreOnClickListener.onClick(this);
                if(mButton != null)
                    mButton.performClick();
                break;
        }
        return true;
    }

}
