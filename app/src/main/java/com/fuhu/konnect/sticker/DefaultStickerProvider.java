package com.fuhu.konnect.sticker;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.sticker.StickerCategory;
import com.fuhu.konnect.library.sticker.StickerGroup;
import com.fuhu.konnect.library.sticker.StickerProvider;
import com.fuhu.konnect.library.utility.ParamChecker;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/7/28.
 */
public class DefaultStickerProvider implements StickerProvider {

    public StickerCategory A_category;
    public StickerCategory B_category;
    public StickerCategory C_category;
    public StickerCategory D_category;
    public StickerCategory E_category;
    public StickerCategory F_category;
    public StickerCategory G_category;
    public StickerCategory H_category;
    public StickerCategory I_category;
    public StickerCategory J_category;

    public ArrayList<StickerCategory> mCategoryList = new ArrayList<>();
//    public HashMap<String, Sticker> mStickerTable = new HashMap<>();

    {
        A_category = new StickerGroup("A", "Dragons");
        A_category.addSticker("1", R.drawable.chat_sticker_a_01);
        A_category.addSticker("2", R.drawable.chat_sticker_a_02);

        B_category = new StickerGroup("B", "Fox");
        B_category.addSticker("1", R.drawable.chat_sticker_b_01);
        B_category.addSticker("2", R.drawable.chat_sticker_b_02);
        B_category.addSticker("3", R.drawable.chat_sticker_b_03);
        B_category.addSticker("4", R.drawable.chat_sticker_b_04);
        B_category.addSticker("5", R.drawable.chat_sticker_b_05);
        B_category.addSticker("6", R.drawable.chat_sticker_b_06);
        B_category.addSticker("7", R.drawable.chat_sticker_b_07);
        B_category.addSticker("8", R.drawable.chat_sticker_b_08);
        B_category.addSticker("9", R.drawable.chat_sticker_b_09);
        B_category.addSticker("10", R.drawable.chat_sticker_b_10);
        B_category.addSticker("11", R.drawable.chat_sticker_b_11);
        B_category.addSticker("12", R.drawable.chat_sticker_b_12);
        B_category.addSticker("13", R.drawable.chat_sticker_b_13);
        B_category.addSticker("14", R.drawable.chat_sticker_b_14);
        B_category.addSticker("15", R.drawable.chat_sticker_b_15);
        B_category.addSticker("16", R.drawable.chat_sticker_b_16);
        B_category.addSticker("17", R.drawable.chat_sticker_b_17);
        B_category.addSticker("18", R.drawable.chat_sticker_b_18);
        B_category.addSticker("19", R.drawable.chat_sticker_b_19);
        B_category.addSticker("20", R.drawable.chat_sticker_b_20);

        C_category = new StickerGroup("C", "KFP");
        C_category.addSticker("1", R.drawable.chat_sticker_c_01);
        C_category.addSticker("2", R.drawable.chat_sticker_c_02);
        C_category.addSticker("3", R.drawable.chat_sticker_c_03);

        mCategoryList.add(A_category);
        mCategoryList.add(B_category);
        mCategoryList.add(C_category);
    }

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
        return new ArrayList<>(mCategoryList);
    }

    @Override
    public void update() {

    }
}
