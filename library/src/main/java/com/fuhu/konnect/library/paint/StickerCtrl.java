package com.fuhu.konnect.library.paint;

import android.view.View;
import android.view.ViewGroup;

import com.fuhu.konnect.library.view.StickerView;

/**
 * Created by jacktseng on 2015/9/1.
 */
public interface StickerCtrl<T extends View> {

    public void setOnFocusChangeListener(StickerView.OnFocusChangeListener listener);

    public void setOnButtonClickListener(StickerView.OnButtonClickListener listener);

    public ViewGroup getStickerWrapper();

    public void addSticker(T v);

    public void removeSticker(T v);

    public void removeAllSticker();

    public void moveUp(T child);

    public void moveDown(T child);

    public void moveTo(int index, T child);

    public void setEditable(boolean isEditable);

}
