package com.fuhu.konnect.mail.effect;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.IPaintThinEffect;

public class PaintEffectSilverThin extends IPaintThinEffect {

    {
        paint.setColor(0xffffffff);
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
		return R.drawable.mail_icon58;
	}
}
