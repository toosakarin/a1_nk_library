package com.fuhu.konnect.library.mail;

import android.view.View;

import com.fuhu.konnect.library.mail.effect.Effect;

/**
 * Created by jacktseng on 2015/8/24.
 */
public interface OnEffectUpdateListener {

    public void onEffectUpdated(View paintView, Effect newEffect, Effect oldEffect);
}
