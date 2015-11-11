package com.fuhu.konnect.paint.effect;


import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.Effect;

import java.util.ArrayList;

public class CameraEffect extends com.fuhu.konnect.library.paint.effect.CameraEffect {

	@Override
	public void apply() {
		// TODO Auto-generated method stub
	}

    @Override
    public void cancel() {

    }

    @Deprecated
    @Override
    public ArrayList<Effect> getSubEffects() {
        return null;
    }

    @Override
	public int getEffectIconRes() {
		return R.drawable.mail_icon_camera;
	}
}
