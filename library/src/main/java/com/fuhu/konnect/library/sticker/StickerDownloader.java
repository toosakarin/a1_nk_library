package com.fuhu.konnect.library.sticker;

/**
 * StickerDownloader defines the format of downloading
 *
 * Created by jacktseng on 2015/7/28.
 */
public interface StickerDownloader {

    /**
     * Downloads stickers from server with given sticker category id, and the
     * StickerStorage object can help to store stickers after downloading
     *
     * @param stickerCategoryId
     * @param storage
     */
    public void download(String stickerCategoryId, StickerStorage storage);

}
