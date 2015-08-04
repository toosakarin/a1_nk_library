package com.fuhu.konnect.library.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/6/9.
 */
public class ImageListConfuser implements ImageConfuser {

    private int mBaseWidth;
    private int mBaseHeight;

    private ArrayList<Bitmap> mImageList = new ArrayList<Bitmap>();

    public ImageListConfuser(int w, int h) {
        mBaseWidth = w;
        mBaseHeight = h;
    }

    public void setWidth(int w) {
        mBaseWidth = w;
    }

    public void setHeight(int h) {
        mBaseHeight = h;
    }

    @Override
    public void setRecycleAfterConfuse(boolean isRecyclable) {

    }

    public void addImage(Bitmap image) {
        mImageList.add(image);
    }

    public void setImage(int index, Bitmap image) {
        mImageList.set(index, image);
    }

    @Override
    public void recycle() {

    }

    @Override
    public void clear() {
        mImageList.clear();
    }

    @Override
    public Bitmap getImage(int hierarchy) {
        return null;
    }

    @Override
    public Bitmap confuse() {
        Bitmap rtn = Bitmap.createBitmap(mBaseWidth, mBaseHeight, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(rtn);

        for(Bitmap image : mImageList) {
            canvas.drawBitmap(image, 0, 0, paint);
        }

        return rtn;
    }
}
