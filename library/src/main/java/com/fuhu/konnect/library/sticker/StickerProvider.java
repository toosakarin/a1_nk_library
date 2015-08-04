package com.fuhu.konnect.library.sticker;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/7/27.
 */
public interface StickerProvider {

    public StickerCategory getCategory(String categoryId);

    public ArrayList<StickerCategory> getCategories();

//    public Sticker getSticker(String stickerId);

    public void update();

}
