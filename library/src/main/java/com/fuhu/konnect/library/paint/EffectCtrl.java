package com.fuhu.konnect.library.paint;

import com.fuhu.konnect.library.paint.effect.Effect;
import com.fuhu.konnect.library.view.EffectContentView;
import com.fuhu.konnect.library.view.PaintView;

/**
 * Created by jacktseng on 2015/8/25.
 */
public interface EffectCtrl {

    public Effect getCurrentEffect();

    public void clearEffect();

    public void applyEffect(PaintView pv, Effect effect);

    public void applyEffectContent(PaintView pv, EffectContentView contentView);

}
