package com.fuhu.konnect.library.view;

import android.content.Context;
import android.view.View;

import com.fuhu.konnect.library.view.EffectContentView;

/**
 * Created by jacktseng on 2015/9/4.
 */
public abstract class EffectContentStickerView extends EffectContentView {

    public EffectContentStickerView(Context context, View btn) {
        super(context, btn);
    }

    public abstract View getContent();

}
