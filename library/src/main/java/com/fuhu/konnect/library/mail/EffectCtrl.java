package com.fuhu.konnect.library.mail;

import com.fuhu.konnect.library.mail.effect.Effect;
import com.fuhu.konnect.library.view.PaintView;

/**
 * Created by jacktseng on 2015/8/25.
 */
public interface EffectCtrl {

    public Effect getCurrentEffect();

    public void applyEffect(PaintView pv, Effect effect);

}
