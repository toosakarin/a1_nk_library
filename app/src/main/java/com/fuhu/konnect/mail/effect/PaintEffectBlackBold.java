package com.fuhu.konnect.mail.effect;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.IPaintBoldEffect;

public class PaintEffectBlackBold extends IPaintBoldEffect {

    {
        paint.setColor(0xff000000);
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
		return R.drawable.mail_icon53;
	}


}
