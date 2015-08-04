package com.fuhu.konnect.library.sticker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.fuhu.konnect.library.Debug;
import com.fuhu.konnect.library.utility.ParamChecker;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacktseng on 2015/7/30.
 */
public class StickerStorageImpl implements StickerStorage {

    public static final String TAG = StickerStorageImpl.class.getSimpleName();

    private static final String FILE_SEPARATOR = "_";
    private static final String STICKER_FILE_FORMAT_DEFAULT = ".png";

    private String mRootPath;


    public StickerStorageImpl(String root) {
        mRootPath = root;
    }

    @Override
    public void setRootPath(String path) {
        mRootPath = path;
    }

    @Override
    public String getRootPath() {
        return mRootPath;
    }

    @Override
    public void saveCategory(StickerCategory category) throws IOException {
        if(category == null) return;
        if(!ParamChecker.isString(mRootPath)) return;

        String categoryDirName = category.getId() +
                ((ParamChecker.isString(category.getName())) ? FILE_SEPARATOR + category.getName() : "");

        File f = new File(mRootPath + File.separator + categoryDirName);
        if(!f.exists())
            if(!f.mkdirs()) throw new IOException("can't make category directory");

        /**
         * clean directory
         */
        Debug.dumpLog(TAG, "clean category directory...");
        for(File stickerFile : f.listFiles())
            stickerFile.delete();

        Debug.dumpLog(TAG, "saving sticker to storage...");
        for(Sticker sticker : category.getStickers()) {
            File file = new File(f.getPath() + File.separator + sticker.getId() + STICKER_FILE_FORMAT_DEFAULT);
            if(!file.createNewFile()) {
                Log.w(TAG, "creating sticker file failed");
                continue;
            }


        }
    }

    @Override
    public void saveSticker(String categoryId, String categoryName, String stickerId, Bitmap stickerImage) throws IOException{
        do {
            if(!ParamChecker.isString(mRootPath)) break;
            if(!ParamChecker.isString(categoryId)) break;
            if(!ParamChecker.isString(stickerId)) break;
            if(stickerImage == null || stickerImage.isRecycled()) break;

            String categoryDir = mRootPath + File.separator + categoryId +
                    ((categoryName != null) ? FILE_SEPARATOR + categoryName : "");
            String stickerFilename = categoryDir + File.separator + stickerId + STICKER_FILE_FORMAT_DEFAULT;

            /**
             * check sticker category dir
             */
            File f = new File(categoryDir);
            if(!f.exists())
                if(!f.mkdirs()) throw new IOException("can't make category dir");
            /**
             * check sticker image file
             */
            f = new File(stickerFilename);
            if(!f.exists())
                if(!f.createNewFile()) throw new IOException("create sticker file failed");

            /**
             * store sticker image to file
             */
            Debug.dumpLog(TAG, "saving sticker image...");
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
            stickerImage.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } while (false);
    }

    @Override
    public StickerCategory loadCategory(String categoryId) {
        return null;
    }

    @Override
    public List<StickerCategory> loadAllCategory() {
        ArrayList<StickerCategory> rtn = new ArrayList<>();

        do {
            if(!ParamChecker.isString(mRootPath)) break;

            File root = new File(mRootPath);
            if(!root.isDirectory()) {
                Log.w(TAG, "doesn't has any exist sticker directory");
                break;
            }

            /**
             * load sticker category
             */
            File[] categoryList = root.listFiles();
            for(File cf : categoryList ) {
                if(!cf.isDirectory())
                    continue;

                Debug.dumpLog(TAG, "loading sticker category - " + cf.getName());

                String[] categoryDir = cf.getName().split(FILE_SEPARATOR);
                String categoryId = categoryDir[0];
                String categoryName = (categoryDir.length == 2) ? categoryDir[1] : categoryId;
                StickerCategory category = new StickerGroup(categoryId, categoryName);

                /**
                 * load sticker
                 */
                File[] stickerList = cf.listFiles();
                for(File sf : stickerList) {
                    if(!sf.isFile())
                        continue;

                    category.addSticker(sf.getName(), sf.getPath());
                }

                rtn.add(category);
            }
        } while(false);

        return rtn;
    }

    @Override
    public Bitmap loadSticker(Sticker sticker) {

        Bitmap rtn = null;
        do {
            if(sticker == null) break;
            if(!(sticker.getResourceId() instanceof String)) break;

            Debug.dumpLog(TAG, "loading sticker - " + sticker.getCategoryId() + "/" + sticker.getId());
            String stickerFilename = (String) sticker.getResourceId();
            rtn = BitmapFactory.decodeFile(stickerFilename);
        } while(false);

        return rtn;
    }

}
