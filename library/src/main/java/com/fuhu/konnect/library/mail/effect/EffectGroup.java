package com.fuhu.konnect.library.mail.effect;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/8/26.
 */
public interface EffectGroup extends Effect {

    public ArrayList<Effect> getSubEffects();
}
