package com.fuhu.konnect.library.sticker;

import java.util.ArrayList;

/**
 * StickerCategory is a sticker group be used to keeping same category stickers
 *
 * Created by jacktseng on 2015/7/27.
 */
public interface StickerCategory {

    /**
     * Gets primary key of sticker category
     *
     * @return
     */
    public String getId();

    /**
     * Gets name of sticker category
     *
     * @return
     */
    public String getName();

    /**
     * Sets sticker to represent this category
     *
     * @param sticker
     */
    public void setDefaultSticker(Sticker sticker);

    /**
     * Gets default sticker of category which will be shown as a subject
     *
     * @return
     */
    public Sticker getDefaultSticker();

    /**
     * Gets the sticker list which are all of this category
     *
     * @return
     */
    public ArrayList<Sticker> getStickers();

    /**
     * Sets stickers to this category
     *
     * @param stickers
     */
    public void setStickers(ArrayList<Sticker> stickers);

    /**
     * Adds sticker into category with given sticker id and corresponding resource id which
     * is an instance of String or Integer
     *
     * @param id
     * @param resourceId
     */
    public void addSticker(String id, Object resourceId);

    /**
     *Removes sticker with special sticker id
     *
     * @param id
     */
    public void removeSticker(String id);

}
