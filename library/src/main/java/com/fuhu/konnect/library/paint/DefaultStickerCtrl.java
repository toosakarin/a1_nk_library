package com.fuhu.konnect.library.paint;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fuhu.konnect.library.view.StickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an instance of StickerCtrl which implementing all function to operate
 * {@link com.fuhu.konnect.library.paint.StickerCtrl}
 * <p/>
 * Author: Jack Tseng (jack.tseng@fuhu.com)
 */
public class DefaultStickerCtrl implements StickerCtrl<StickerView> {

    private ViewGroup mStickerWrapper;

    private StickerView.OnFocusChangeListener mOnStickerFocusListener = new StickerView.OnFocusChangeListener() {
        @Override
        public void onFocused(StickerView view) {
            ArrayList<StickerView> views = getChildren(mStickerWrapper);
            for(int i=0; i<views.size(); i++) {
                StickerView sv =  views.get(i);
                if(sv != view)
                    sv.hideControl();
            }
        }
    };

    private StickerView.OnButtonClickListener mOnStickerButtonClickListener = new StickerView.OnButtonClickListener() {
        @Override
        public void onClick(StickerView view, int btn_code) {

            switch (btn_code) {
                case StickerView.OnButtonClickListener.CONFIRM:
                    break;
                case StickerView.OnButtonClickListener.REMOVE:
                    removeSticker(view);
                    break;
                case StickerView.OnButtonClickListener.RESIZE:
                    //nothing to do
                    break;
                case StickerView.OnButtonClickListener.ROTATE:
                    //nothing to do
                    break;
                case StickerView.OnButtonClickListener.MOVE_UP:
                    moveUp(view);
                    break;
                case StickerView.OnButtonClickListener.MOVE_DOWN:
                    moveDown(view);
                    break;
            }

        }
    };

    public DefaultStickerCtrl(ViewGroup container) {
        mStickerWrapper = container;
    }

    private ArrayList<StickerView> getChildren(ViewGroup parent) {
        if(parent == null) return null;

        ArrayList<StickerView> rtn = new ArrayList<>();

        int childCount = mStickerWrapper.getChildCount();
        for(int i=0; i<childCount; i++) {
            rtn.add((StickerView) mStickerWrapper.getChildAt(i));
        }

        return rtn;
    }

    private void setViews(ArrayList<StickerView> views) {
        if(mStickerWrapper == null) return;
        for(int i=0; i< views.size(); i++) {
            mStickerWrapper.addView(views.get(i));
        }
        mStickerWrapper.invalidate();
    }

    private void resetViews(ArrayList<StickerView> views) {
        mStickerWrapper.removeAllViews();
        setViews(views);
    }

//        @Override
//        public void setOnFocusChangeListener(StickerView.OnFocusChangeListener listener) {
//            mOnStickerFocusListener = listener;
//        }
//
//        @Override
//        public void setOnButtonClickListener(StickerView.OnButtonClickListener listener) {
//            mOnStickerButtonClickListener = listener;
//        }


    @Override
    public void setStickerWrapper(ViewGroup container) {
        mStickerWrapper = container;
    }

    @Override
    public ViewGroup getStickerWrapper() {
        return mStickerWrapper;
    }

    @Override
    public void addSticker(StickerView v) {
        if(mStickerWrapper == null) return;
        v.setOnFocusChangeListener(mOnStickerFocusListener); //sets focus listener
        v.setOnButtonClickListener(mOnStickerButtonClickListener);
        /**
         * Adds layout params when it first adds into sticker wrapper
         */
        if(v.getLayoutParams() == null) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            v.setLayoutParams(lp);
        }
        mStickerWrapper.addView(v);
    }

    @Override
    public void removeSticker(StickerView v) {
        if(mStickerWrapper == null) return;
        mStickerWrapper.removeView(v);
    }

    @Override
    public void removeAllSticker() {
        if(mStickerWrapper == null) return;
        mStickerWrapper.removeAllViews();
    }

    @Override
    public void moveUp(StickerView child) {
        if(mStickerWrapper == null) return;
        int index = mStickerWrapper.indexOfChild(child);
        if(++index < mStickerWrapper.getChildCount())
            move(index, child);
    }

    @Override
    public void moveDown(StickerView child) {
        if(mStickerWrapper == null) return;
        int index = mStickerWrapper.indexOfChild(child);
        if(--index >= 0)
            move(index, child);
    }

    @Override
    public void moveTo(int index, StickerView child) {
        if(mStickerWrapper == null) return;
        move(index, child);
    }

    private void move(int index, StickerView v) {
        ArrayList<StickerView> childList = getChildren(mStickerWrapper);

        //swap views
        int _index = childList.indexOf(v);
        StickerView _v = childList.get(index);
        childList.set(index, v);
        childList.set(_index, _v);

        resetViews(childList);
    }

    @Override
    public void setEditable(boolean isEditable) {
        if(mStickerWrapper == null) return;
        List<StickerView> childSets = getChildren(mStickerWrapper);
        for(StickerView v : childSets) {
            v.setEditable(isEditable);
        }
    }
}
