package com.fuhu.konnect.library.sticker;

import android.content.Context;

/**
 * Created by jacktseng on 2015/7/28.
 */
public class StickerManagerFactory {

    private static StickerManagerImpl INSTANCE;

    public static StickerManager getInstance(Context ctx) {
        if(INSTANCE == null) {
            INSTANCE = new StickerManagerImpl(ctx);
        }
        return INSTANCE;
    }


}
