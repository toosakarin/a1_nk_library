package com.fuhu.konnect.library.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.fuhu.konnect.library.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by jacktseng on 2015/6/8.
 */
public class MapImageConfuser implements ImageConfuser {

    public static final String TAG = MapImageConfuser.class.getSimpleName();

    private boolean mIsRecycleAfterConfuse = false;

    private int mBaseWidth;
    private int mBaseHeight;

    private HashMap<Integer, Bitmap> mImageMap = new HashMap<Integer,Bitmap>();

    public MapImageConfuser(int w, int h) {
        mBaseWidth = w;
        mBaseHeight = h;
    }

    @Override
    public void setWidth(int w) {
        mBaseWidth = w;
    }

    @Override
    public void setHeight(int h) {
        mBaseHeight = h;
    }

    @Override
    public void setRecycleAfterConfuse(boolean isRecyclable) {
        mIsRecycleAfterConfuse = isRecyclable;
    }

    @Override
    public void setImage(int index, Bitmap bmp) {
        if(index < 0) return;
        if(bmp == null) return;
        if(bmp.isRecycled()) return;
        mImageMap.put(index, bmp);
    }

    @Override
    public Bitmap getImage(int index) {
        return mImageMap.get(index);
    }

    @Override
    public void recycle() {
        ArrayList<Bitmap> imageList = new ArrayList<Bitmap>(mImageMap.values());
        for(Bitmap bmp : imageList)
            if(!bmp.isRecycled())
                bmp.recycle();

        System.gc();
    }

    @Override
    public void clear() {
        mImageMap.clear();
    }

    @Override
    public Bitmap confuse() {
        Bitmap rtn = Bitmap.createBitmap(mBaseWidth, mBaseHeight, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(rtn);

        SortedSet<Integer> keys = new TreeSet<Integer>(mImageMap.keySet());
        for(Integer key : keys) {
            Bitmap bmp = mImageMap.get(key);
            canvas.drawBitmap(bmp, 0, 0, paint);
            Debug.dumpLog(TAG, "draw bitmap [ " + bmp.toString() + " ]");
        }

        //jack@150615
        if(mIsRecycleAfterConfuse)
            recycle();

        return rtn;
    }
}
