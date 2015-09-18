package com.fuhu.konnect.library.paint.effect;

/**
 * The effect is an influence for painting such as drawing line, erasing and writing text etc. According to
 * above, this interface defines some basic operations of effect.
 * <p/>
 * Author: Jack Tseng (jack.tseng@fuhu.com)
 */
public interface Effect {

    public void apply();

    public void cancel();

    public int getEffectIconRes();

//    public ArrayList<Effect> getSubEffects();

}
