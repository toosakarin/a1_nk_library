package com.fuhu.konnect.library.sticker;

import java.util.ArrayList;

/**
 * StickerProvider is charge of defining how to provide the sticker.
 * As design, StickerManager gets the sticker by StickerProvider, so
 * developer must to create yur own provider into your application first,
 * and sets it to the StickerManager before using the service of manager.
 *
 * Created by jacktseng on 2015/7/27.
 */
public interface StickerProvider {

    /**
     * Gets a StickerCategory of given category id
     *
     * @param categoryId
     * @return
     */
    public StickerCategory getCategory(String categoryId);

    /**
     * Gets the list of StickerCategory of all categories
     *
     * @return
     */
    public ArrayList<StickerCategory> getCategories();

//    public Sticker getSticker(String stickerId);

    /**
     * Notifies provider to update its stickers
     * Note: Usually be used on extension provider
     */
    public void update();

}
