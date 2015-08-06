package com.fuhu.konnect.library.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.fuhu.konnect.library.utility.ParamChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is an instance of StickerManage, it's used to operate the
 * sticker relationship work. The class provides a default extension
 * provider of StickerProvider and a default storage of StickerStorage
 *
 * Created by jacktseng on 2015/7/28.
 */
class StickerManagerImpl implements StickerManager {

    /** Root directory of storage */
    private static final String STICKER_STORAGE_FOLDER = "chat_ext_sticker";

    /** String for sticker decode  */
    private static String STICKER_DECODE_PREFIX = "#::#";

    private Context mContext;

    /** Instance of StickerDownloader for sticker downloading */
    private StickerDownloader mDownLoader;

    /** Instance of StickerStore for storing sticker to external storage */
    private StickerStorage mExtStorage;

    /** Instance of StickerProvider for fetching sticker from application */
    private StickerProvider mDefaultProvider;

    /** Instance of StickerProvider for fetching sticker from external storage */
    private StickerProvider mExtProvider;

    /** The cache for stickers quickly searching */
    private HashMap<String, Sticker> mStickerTable = new HashMap<>();

    private OnDownloadListener mOnDownloadListener;

    public static String createStickerCode(String categoryId, String stickerId) {
        return STICKER_DECODE_PREFIX + categoryId + "_" + stickerId;
    }


    StickerManagerImpl(Context ctx) {
        mContext = ctx;

        /**
         * create default storage of StickerStorage
         */
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + STICKER_STORAGE_FOLDER;
        mExtStorage = new StickerStorageImpl(path);

        /**
         * create default ext sticker provider of StickerProvider
         */
        mExtProvider = new StickerProvider() {

            ArrayList<StickerCategory> mCategoryList;

            @Override
            public StickerCategory getCategory(String categoryId) {
                if(!ParamChecker.isValid(categoryId)) return null;

                for(StickerCategory category : mCategoryList)
                    if(category.getId().equals(categoryId))
                        return category;

                return null;
            }

            @Override
            public ArrayList<StickerCategory> getCategories() {
                return new ArrayList(mCategoryList);
            }

            @Override
            public void update() {
                mCategoryList = (ArrayList) mExtStorage.loadAllCategory();
            }
        };
        mExtProvider.update();
    }

    @Override
    public void setOnDownloadListener(OnDownloadListener listener) {
        mOnDownloadListener = listener;
    }

    @Override
    public void setDownloader(StickerDownloader downloader) {
        mDownLoader = downloader;
    }

    @Override
    public StickerDownloader getDownloader() {
        return mDownLoader;
    }

    @Override
    public void setStorage(StickerStorage storage) {
        mExtStorage = storage;
    }

    @Override
    public StickerStorage getStorage() {
        return mExtStorage;
    }

    @Override
    public void setDefaultProvider(StickerProvider provider) {
        mDefaultProvider = provider;
        notifyProviderUpdate();
    }

    @Override
    public StickerProvider getDefaultProvider() {
        return mDefaultProvider;
    }

    @Override
    public void setExtProvider(StickerProvider provider) {
        mExtProvider = provider;
        notifyProviderUpdate();
    }

    @Override
    public StickerProvider getExtProvider() {
        return mExtProvider;
    }

    @Override
    public StickerCategory getCategory(String categoryId) {
        StickerCategory rtn = null;
        do {
            if(!ParamChecker.isValid(categoryId)) break;

            if(mDefaultProvider != null)
                rtn = mDefaultProvider.getCategory(categoryId);

            if(rtn != null) break;

            if(mExtProvider != null)
                rtn = mExtProvider.getCategory(categoryId);

        } while (false);

        return rtn;
    }

    @Override
    public Sticker getSticker(String categoryId, String stickerId) {
        Sticker rtn = null;

        StickerCategory category;
        do {
            if(!ParamChecker.isValid(stickerId)) break;
            if((category = getCategory(categoryId)) == null) break;

            for(Sticker sticker :category.getStickers())
                if(sticker.getId().equals(stickerId)) {
                    rtn = sticker;
                    break;
                }

        } while(false);

        return rtn;
    }

    @Override
    public ArrayList<StickerCategory> getCategories() {
        ArrayList<StickerCategory> rtn = new ArrayList<>();
        if(mDefaultProvider != null)
            rtn.addAll(mDefaultProvider.getCategories());
        if(mExtProvider != null)
            rtn.addAll(mExtProvider.getCategories());

        return rtn;
    }

    @Override
    public void notifyProviderUpdate() {
        mStickerTable.clear();

        for(StickerCategory category : getCategories())
            for(Sticker sticker : category.getStickers()) {
                String code = encodeSticker(sticker);
                mStickerTable.put(code, sticker);
            }
    }

    @Override
    public String encodeSticker(Sticker sticker) {
        if(sticker == null) return null;
        return createStickerCode(sticker.getCategoryId(), sticker.getId());
    }

    @Override
    public Sticker decodeSticker(String code) {
        Sticker rtn = null;

        do {
            if(!ParamChecker.isValid(code)) break;
            if(mStickerTable == null) break;
            if(!mStickerTable.containsKey(code)) break;

            rtn = mStickerTable.get(code);
        } while(false);

        return rtn;
    }

    @Override
    public Bitmap loadStickerBitmap(Sticker sticker) {
        if(sticker == null) return null;

        Bitmap rtn = null;

        if(sticker.getResourceId() instanceof Integer) { //default sticker provider
            rtn = BitmapFactory.decodeResource(mContext.getResources(), (Integer) sticker.getResourceId());
        }
        else if(sticker.getResourceId() instanceof String) { //ext sticker provider
            if(mExtStorage != null)
                rtn = mExtStorage.loadSticker(sticker);
        }

        return rtn;
    }

    @Override
    public void download(String categoryId) {
        if(!ParamChecker.isValid(categoryId)) return;
        if(mDownLoader == null) return;
//        if(mExtStorage == null) return;

        mDownLoader.download(categoryId, mExtStorage);

        /**
         * notify provider to update the sticker and rebuilding the sticker
         * table for quick search
         */
        if(mExtProvider != null) {
            mExtProvider.update();
            notifyProviderUpdate();
        }

        if(mOnDownloadListener != null)
            mOnDownloadListener.onDownloaded();
    }

}
