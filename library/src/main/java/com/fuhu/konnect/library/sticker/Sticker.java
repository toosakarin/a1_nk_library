package com.fuhu.konnect.library.sticker;

/**
 * This class is a bean of Sticker
 *
 * Created by jacktseng on 2015/7/27.
 */
public interface Sticker<T> {

    /**
     * Gets primary key of sticker
     *
     * @return
     */
    public String getId();

    /**
     * Gets sticker category id of this sticker
     *
     * @return
     */
    public String getCategoryId();

    /**
     * Gets resource id which corresponding to sticker image is an accessible location.
     * The resource id is only an instance of String or Integer.
     *
     * @return
     */
    public T getResourceId();

}
