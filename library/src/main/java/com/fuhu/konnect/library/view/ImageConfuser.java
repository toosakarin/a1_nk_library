package com.fuhu.konnect.library.view;

import android.graphics.Bitmap;

/**
 * Created by jacktseng on 2015/6/9.
 */
public interface ImageConfuser {

    public void setWidth(int w);

    public void setHeight(int h);

    public void setImage(int index, Bitmap bmp);

    public Bitmap getImage(int index);

    public void recycle();

    public void clear();

    public void setRecycleAfterConfuse(boolean isRecyclable);

    public Bitmap confuse();

}
