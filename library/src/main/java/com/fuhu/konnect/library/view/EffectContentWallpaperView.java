package com.fuhu.konnect.library.view;

import android.content.Context;
import android.view.View;

/**
 * Created by jacktseng on 2015/9/4.
 */
public abstract class EffectContentWallpaperView extends EffectContentView {

    public EffectContentWallpaperView(Context context, View btn) {
        super(context, btn);
    }

    @Override
    public abstract Integer getContent();
}
