package com.fuhu.konnect.library.paint.effect;

import java.util.ArrayList;

/**
 * The EffectGroup is a collection of effects, and it's an effect too.
 * <p/>
 * Author: Jack Tseng (jack.tsneg@fuhu.com)
 */
public interface EffectGroup extends Effect {

    public ArrayList<Effect> getSubEffects();
}
