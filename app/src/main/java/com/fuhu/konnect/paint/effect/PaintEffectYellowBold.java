package com.fuhu.konnect.paint.effect;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.IPaintBoldEffect;

public class PaintEffectYellowBold extends IPaintBoldEffect {

    {
        paint.setColor(0xfff9eb56);
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
		return R.drawable.mail_icon39;
	}
}
