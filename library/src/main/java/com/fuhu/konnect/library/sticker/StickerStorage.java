package com.fuhu.konnect.library.sticker;

import android.graphics.Bitmap;

import java.io.IOException;
import java.util.List;

/**
 * StickerStorage interface defines all operation of accessing external storage,
 * such as reading sticker from storage and writing sticker to storage
 *
 * Created by jacktseng on 2015/7/27.
 */
public interface StickerStorage {

    /**
     * Sets path as directory of root of storage
     *
     * @param path
     */
    public void setRootPath(String path);

    /**
     * Gets root directory of storage
     *
     * @return
     */
    public String getRootPath();

    /**
     * Saves a bitmap of sticker to external storage with given category information,
     * sticker id
     *
     * @param categoryId
     * @param categoryName
     * @param stickerId
     * @param image
     * @throws IOException
     */
    public void saveSticker(String categoryId, String categoryName, String stickerId, Bitmap image) throws IOException;

    /**
     * Saves all stickers of given category to external storage
     *
     * @param category
     * @throws IOException
     */
    public void saveCategory(StickerCategory category) throws IOException;

    /**
     * Gets the list of StickerCategory of given category id from external storage
     *
     * @param categoryId
     * @return
     */
    public StickerCategory loadCategory(String categoryId);

    /**
     * Gets the list of StickerCategory of all categories which were saved in
     * external storage
     *
     * @return
     */
    public List<StickerCategory> loadAllCategory();

    /**
     * Gets a image of given sticker which loading from external storage
     *
     * @param sticker
     * @return
     */
    public Bitmap loadSticker(Sticker sticker);




}
