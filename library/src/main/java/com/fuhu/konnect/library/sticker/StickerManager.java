package com.fuhu.konnect.library.sticker;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/7/27.
 */
public interface StickerManager {

    public static final int STICKER_PROVIDER_DEFAULT    = 0x11;
    public static final int STICKER_PROVIDER_EXT        = 0x21;

    public interface OnDownloadListener {
        public void onDownloaded();
    }

    public void setOnDownloadListener(OnDownloadListener listener);

    public void setDownloader(StickerDownloader downloader);

    public StickerDownloader getDownloader();

    public void setStorage(StickerStorage storage);

    public StickerStorage getStorage();

    public void setDefaultProvider(StickerProvider provider);

    public StickerProvider getDefaultProvider();

    public void setExtProvider(StickerProvider provider);

    public StickerProvider getExtProvider();

    public StickerCategory getCategory(String categoryId);

    public Sticker getSticker(String categoryId, String stickerId);

    public ArrayList<StickerCategory> getCategories();

    public void notifyProviderUpdate();

    public String encodeSticker(Sticker sticker);

    public Sticker decodeSticker(String code);

    public Bitmap loadStickerBitmap(Sticker sticker);

    /**
     * Download the sticker from server with given category id
     * @param categoryId special category of stickers
     */
    public void download(String categoryId);
}
