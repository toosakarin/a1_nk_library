package com.fuhu.konnect.library.sticker;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * This class is charge of providing, storing and downloading stickers. Goals to keep extension of
 * the sticker flow, StickerManager is based on interface to work. StickerManager is consist of
 * three interface as follow:
 * <br/>
 * <ul>
 *     <li>StickerProvider: StickerProvider must keeps all of stickers which loading from application
 *     or storage.StickerManager has two StickerProvider are default provider and extension provider.
 *     The default StickerProvider is charge of providing stickers which are put in application
 *     package. Furthermore,the extension StickerProvider is responsible for giving stickers from
 *     external storage.
 *     </li>
 *     <li>StickerStorage: StickerStorage provides storage operation such as read, write the sticker
 *     to external storage.
 *     </li>
 *     <li>StickerDownloader: StickerDownloader is responsible for downloading stickers with given
 *     sticker category id from web server. Stickers will be saved in external storage after
 *     downloading by StickerStorage.
 *     </li>
 * </ul>
 *<p/>
 * To use StickerManager works, developer need to set the default sticker provider first. The
 * default sticker provider is an instance of StickerProvider which developer must be extend by
 * yourself.
 * <br/>
 * Each interface are able to be change by developer extending the standard interface.
 * <p/>
 * Author: Jack Tseng (jack.tseng@gamil.com)
 */
public interface StickerManager {

    /**
     * This listener will be call when download process is done.
     */
    public interface OnDownloadListener {
        public void onDownloaded();
    }

    public void setOnDownloadListener(OnDownloadListener listener);

    /**
     * Sets an instance of StickerDownloader
     *
     * @param downloader
     */
    public void setDownloader(StickerDownloader downloader);

    /**
     * Gets the instance of StickerDownloader
     *
     * @return
     */
    public StickerDownloader getDownloader();

    /**
     * Sets an instance of StickerStorage
     *
     * @param storage
     */
    public void setStorage(StickerStorage storage);

    /**
     * Gets the instance of StickerStorage
     * <br/>
     * Note: Default StickerStorage supports basic sticker storage feature such as reading and
     * writing to external storage
     *
     * @return
     */
    public StickerStorage getStorage();

    /**
     * Sets default provider which is an instance of StickerProvider to provide stickers winch are
     * put in application package
     *
     * @param provider
     */
    public void setDefaultProvider(StickerProvider provider);

    /**
     * Gets the default StickerProvider
     *
     * @return
     */
    public StickerProvider getDefaultProvider();

    /**
     * Sets an extension provider which is an instance of StickerProvider to provide stickers from
     * external storage
     *
     * @param provider
     */
    public void setExtProvider(StickerProvider provider);

    /**
     * Gets the extension StickerProvider
     *
     * @return
     */
    public StickerProvider getExtProvider();

    /**
     * Gets a sticker category with given category id
     *
     * @param categoryId
     * @return
     */
    public StickerCategory getCategory(String categoryId);

    /**
     * Gets a sticker with given category id and sticker id
     *
     * @param categoryId
     * @param stickerId
     * @return
     */
    public Sticker getSticker(String categoryId, String stickerId);

    /**
     * Gets a list of all sticker category from both default and extension provider
     *
     * @return
     */
    public ArrayList<StickerCategory> getCategories();

    /**
     * Notifies manager to update providers to get newly stickers
     */
    public void notifyProviderUpdate();

    /**
     * Gets a sticker code which are encoded from given sticker to represent this sticker
     *
     * @param sticker
     * @return
     */
    public String encodeSticker(Sticker sticker);

    /**
     * Gets a sticker which are decoded from given sticker code
     *
     * @param stickerCode
     * @return
     */
    public Sticker decodeSticker(String stickerCode);

    /**
     * Returns a image of given sticker which are loaded from StickerStorage
     *
     * @param sticker
     * @return
     */
    public Bitmap loadStickerBitmap(Sticker sticker);

    /**
     * Downloads stickers from server with given category id
     *
     * @param categoryId special category of stickers
     */
    public void download(String categoryId);
}
