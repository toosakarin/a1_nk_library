package com.fuhu.konnect.library.mail;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/8/24.
 */
public interface Effect {

    public void apply();

    public void cancel();

    public int getEffectIconRes();

    public ArrayList<Effect> getSubEffects();


}
