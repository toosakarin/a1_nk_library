package com.fuhu.konnect.library.sticker;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/7/27.
 */
public interface StickerCategory {

    public String getId();

    public String getName();

    public void setDefaultSticker(Sticker sticker);

    public Sticker getDefaultSticker();

    public ArrayList<Sticker> getStickers();

    public void setStickers(ArrayList<Sticker> stickers);

    public void addSticker(String id, Object resourceId);

    public void removeSticker(String id);

}
