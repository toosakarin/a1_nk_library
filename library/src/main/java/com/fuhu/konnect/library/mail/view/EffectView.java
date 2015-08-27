package com.fuhu.konnect.library.mail.view;

import android.content.Context;
import android.widget.RelativeLayout;

import com.fuhu.konnect.library.mail.effect.Effect;

/**
 * Created by jacktseng on 2015/8/25.
 */
public abstract class EffectView extends RelativeLayout {

    public EffectView(Context context) {
        super(context);
    }

    public abstract Effect getEffect();

}
