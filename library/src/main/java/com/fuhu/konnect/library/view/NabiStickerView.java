package com.fuhu.konnect.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Created by jacktseng on 2015/9/2.
 */
public class NabiStickerView extends StickerView {


    /**
     * control buttons
     */
    private RectF mCheckButton = new RectF();
    private RectF mCrossButton = new RectF();
    private RectF mSideIcon = new RectF();

    private Bitmap mCheckImg, mCrossImg, mResizeImg, mRotateImg;


    public NabiStickerView(Context context) {
        super(context);
    }

    public NabiStickerView(Context context, int resId) {
        super(context, resId);
    }

    public NabiStickerView(Context context, Bitmap image) {
        super(context, image);
    }

    public void setCheckButtonImage(Bitmap image) {
        mCheckImg = image;
    }

    public void setCrossButtonImage(Bitmap image) {
        mCrossImg = image;
    }

    public void setResizeButtonImage(Bitmap image) {
        mResizeImg = image;
    }

    public void setRotateButtonImage(Bitmap image) {
        mRotateImg = image;
    }


    /**
     * Overrides this function to add the addition button on the sticker.
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean rtn = super.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                if (mShouldDrawFullBackground && mCheckButton.contains(event.getX(), event.getY())) {
                    mShouldDrawFullBackground = mShouldDrawBackground = false;
                    invalidate();

                    if(mOnButtonClickListener != null)
                        mOnButtonClickListener.onClick(this, OnButtonClickListener.CONFIRM);

                } else if (mCrossButton.contains(event.getX(), event.getY())) {
                    if (mOnButtonClickListener != null)
                        mOnButtonClickListener.onClick(this, OnButtonClickListener.REMOVE);
                }
                break;
        }

        return rtn;
    }




    @Override
    protected void onDraw(Canvas canvas) {
        // draws image resource
        super.onDraw(canvas);

        if (mShouldDrawBackground || mShouldDrawFullBackground) {
            canvas.save();
            canvas.scale(1 / mCurrentScale, 1 / mCurrentScale);
            /* */
//            drawFrame(canvas);
            if (mShouldDrawFullBackground) {
                drawControl(canvas);
            } else {
                drawCenterCrossButton(canvas);
            }
            drawSideIcons(canvas);
            /* */
            canvas.restore();
        }
    }



    /**
     * draws control buttons
     *
     * @param canvas
     */
    private void drawControl(Canvas canvas) {
        float w = this.getWidth() * mCurrentScale;
        float h = this.getHeight() * mCurrentScale;
        float top = h - mButtonSide * mDensity;
        float bottom = top + mButtonSide * mDensity;

        // ======
        // draws check button
        float left = w / 2 + mButtonSpacing * mDensity;
        float right = left + mButtonSide * mDensity;
        mCheckButton.set(left, top, right, bottom);
        canvas.drawBitmap(mCheckImg, null, mCheckButton, null);

        // draws cross button
        left = w / 2 - (mButtonSide + mButtonSpacing) * mDensity;
        right = left + mButtonSide * mDensity;
        mCrossButton.set(left, top, right, bottom);
        canvas.drawBitmap(mCrossImg, null, mCrossButton, null);
        // ======

        refreshRectTouchArea(mCheckButton);
        refreshRectTouchArea(mCrossButton);
    }

    /**
     * draws cross button at the bottom center
     *
     * @param canvas
     */
    private void drawCenterCrossButton(Canvas canvas) {
        float w = this.getWidth() * mCurrentScale;
        float h = this.getHeight() * mCurrentScale;
        float left = w / 2 - mButtonSide / 2 * mDensity;
        float top = h - mButtonSide * mDensity;
        float right = left + mButtonSide * mDensity;
        float bottom = top + mButtonSide * mDensity;
        mCrossButton.set(left, top, right, bottom);
        canvas.drawBitmap(mCrossImg, null, mCrossButton, null);
        refreshRectTouchArea(mCrossButton);
    }

    /**
     * draw side icons. resize icon is on the left
     *
     * @param canvas
     */
    private void drawSideIcons(Canvas canvas) {
        float w = this.getWidth() * mCurrentScale;
        float h = this.getHeight() * mCurrentScale;

        float top = h / 2 - mButtonSide / 2 * mDensity;
        float bottom = top + mButtonSide * mDensity;

        // left side resize icon
        // left = 0;
        float right = mButtonSide * mDensity;

        mSideIcon.set(0, top, right, bottom);
        canvas.drawBitmap(mResizeImg, null, mSideIcon, null);

        // right side rotate icon
        // right = width
        float left = w - mButtonSide * mDensity;
        mSideIcon.set(left, top, (int) w, bottom);
        canvas.drawBitmap(mRotateImg, null, mSideIcon, null);
    }

    private void refreshRectTouchArea(RectF rect) {
        rect.set(rect.left / mCurrentScale, rect.top / mCurrentScale, rect.right / mCurrentScale, rect.bottom
                / mCurrentScale);
    }
}
