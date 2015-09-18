package com.fuhu.konnect.library.paint;

import android.view.View;
import android.view.ViewGroup;

/**
 * StickerCtrl is a controller which is used to manage the sticker with given container. This
 * interface defines the function to operate the sticker on the container such as add, remove, move
 * up and move down etc.<br/>
 * Note: The sticker must be a instance of android.view.View {@link com.fuhu.konnect.library.view.StickerView}
 * <p/>
 * Author: Jack Tseng (jack.tseng@fuhu.com)
 */
public interface StickerCtrl<T extends View> {

//    public void setOnFocusChangeListener(StickerView.OnFocusChangeListener listener);
//
//    public void setOnButtonClickListener(StickerView.OnButtonClickListener listener);

    /**
     * Sets the container for wrapping stickers
     * @param container
     */
    public void setStickerWrapper(ViewGroup container);

    /**
     * Returns the container which be used to wrap stickers
     * @return
     */
    public ViewGroup getStickerWrapper();

    /**
     * Adds the sticker into container
     * @param v
     */
    public void addSticker(T v);

    /**
     * Removes the sticker of container
     * @param v
     */
    public void removeSticker(T v);

    /**
     * Removes all sticker of container
     */
    public void removeAllSticker();

    /**
     * Moves up the sticker of container
     * @param child
     */
    public void moveUp(T child);

    /**
     * Moves down the sticker of container
     * @param child
     */
    public void moveDown(T child);

    /**
     * Moves the sticker to the given index of child list of container
     * @param index
     * @param child
     */
    public void moveTo(int index, T child);

    /**
     * Enables the sticker can be edited or not
     * @param isEditable
     */
    public void setEditable(boolean isEditable);

}
