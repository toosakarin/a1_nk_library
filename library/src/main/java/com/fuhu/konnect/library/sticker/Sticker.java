package com.fuhu.konnect.library.sticker;

/**
 * Created by jacktseng on 2015/7/27.
 */
public interface Sticker<T> {

    public String getId();

    public String getCategoryId();

    public T getResourceId();

}
