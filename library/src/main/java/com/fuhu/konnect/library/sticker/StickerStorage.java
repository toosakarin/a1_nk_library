package com.fuhu.konnect.library.sticker;

import android.graphics.Bitmap;

import java.io.IOException;
import java.util.List;

/**
 * Created by jacktseng on 2015/7/27.
 */
public interface StickerStorage {

    public void setRootPath(String path);

    public String getRootPath();

    public void saveSticker(String categoryId, String categoryName, String stickerId, Bitmap image) throws IOException;

    public void saveCategory(StickerCategory category) throws IOException;

    public StickerCategory loadCategory(String categoryId);

    public List<StickerCategory> loadAllCategory();

    public Bitmap loadSticker(Sticker sticker);




}
