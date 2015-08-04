package com.fuhu.konnect.library.sticker;

import com.fuhu.konnect.library.utility.ParamChecker;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/7/27.
 */
public class StickerGroup implements StickerCategory {

    public String id;
    public String name;
    public Sticker defaultSticker;
    private ArrayList<Sticker> mStickerList = new ArrayList<>();

    public StickerGroup(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public StickerGroup(ArrayList<Sticker> stickers) {
        mStickerList = stickers;
    }

    public StickerGroup(String id, String name, Sticker defaultSticker, ArrayList<Sticker> stickers) {
        this(id, name);
        this.defaultSticker = defaultSticker;
        this.mStickerList = stickers;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setDefaultSticker(Sticker sticker) {
        defaultSticker = sticker;
    }

    @Override
    public Sticker getDefaultSticker() {
        return defaultSticker;
    }

    @Override
    public ArrayList<Sticker> getStickers() {
        return mStickerList;
    }

    @Override
    public void setStickers(ArrayList<Sticker> stickers) {
        mStickerList = stickers;
    }

    @Override
    public void addSticker(final String id, final Object resourceId) {
        if(!ParamChecker.isString(id)) return;
        if(resourceId == null) return;
        if(!(resourceId instanceof Integer) && !(resourceId instanceof String))
            throw new ClassFormatError("resource id is not Integer or String!");

        Sticker sticker = new Sticker() {

            @Override
            public String getId() {
                return id;
            }

            @Override
            public String getCategoryId() {
                return StickerGroup.this.id;
            }

            @Override
            public Object getResourceId() {
                return resourceId;
            }
        };
        mStickerList.add(sticker);
    }

    @Override
    public void removeSticker(String id) {
        Sticker sticker = null;
        for(int i=0; i< mStickerList.size(); i++)
            if(mStickerList.get(i).getId().equals(id)) {
                sticker = mStickerList.get(i);
                break;
            }
        mStickerList.remove(sticker);
    }
}
