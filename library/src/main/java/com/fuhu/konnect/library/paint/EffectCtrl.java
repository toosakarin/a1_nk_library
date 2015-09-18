package com.fuhu.konnect.library.paint;

import android.view.View;

import com.fuhu.konnect.library.paint.effect.Effect;

/**
 * EffectCtrl is a controller providing the way to apply the effect or content of effect which
 * you given.
 * <p/>
 * Author: Jack Tseng (jack.tseng@fuhu.com)
 */
public interface EffectCtrl {

    /**
     * Returns the effect which is currently using
     * @return
     */
    public Effect getCurrentEffect();

    /**
     * Clears the effect of currently effect
     */
    public void clearEffect();

    /**
     * Applies the effect with given view which is the container of this effect
     * @param view
     * @param effect
     */
    public void applyEffect(View view, Effect effect);

    /**
     * Applies the content of effect with given view which is a container of the effect
     * @param view
     * @param content
     */
//    public void applyEffectContent(PaintView pv, EffectContentView contentView);
    public void applyEffectContent(View view, Object content);


}
