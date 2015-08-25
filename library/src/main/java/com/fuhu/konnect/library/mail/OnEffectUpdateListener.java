package com.fuhu.konnect.library.mail;

import android.media.effect.Effect;
import android.view.View;

/**
 * Created by jacktseng on 2015/8/24.
 */
public interface OnEffectUpdateListener {

    public void onEffectUpdated(View drawingView, Effect newEffect, Effect oldEffect);
}
