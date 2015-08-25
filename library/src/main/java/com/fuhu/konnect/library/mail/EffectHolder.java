package com.fuhu.konnect.library.mail;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/8/24.
 */
public interface EffectHolder {

    public void clean();

    public ArrayList<Effect> getSubEffects(Effect effect);

    public ArrayList<Effect> getAllEffects();


}
