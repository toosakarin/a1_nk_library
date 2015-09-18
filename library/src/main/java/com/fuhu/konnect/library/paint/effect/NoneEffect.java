package com.fuhu.konnect.library.paint.effect;

/**
 * The NoneEffect is a tag which indicating to clear all of effect.
 * <p/>
 * Author: Jack Tseng (jack.tsneg@fuhu.com)
 */
public class NoneEffect implements Effect {

    @Override
    public void apply() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public int getEffectIconRes() {
        return 0;
    }
}
