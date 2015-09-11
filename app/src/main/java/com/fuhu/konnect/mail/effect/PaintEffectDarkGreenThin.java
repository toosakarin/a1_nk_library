package com.fuhu.konnect.mail.effect;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.IPaintThinEffect;

public class PaintEffectDarkGreenThin extends IPaintThinEffect {

    {
        paint.setColor(0xff1a6b32);
    }

	@Override
	public void apply() {
		// TODO Auto-generated method stub
	}

    @Override
    public void cancel() {

    }

    @Override
	public int getEffectIconRes() {
		return R.drawable.mail_icon44;
	}
}
