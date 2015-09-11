package com.fuhu.konnect.mail.effect;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.IPaintBoldEffect;

public class PaintEffectRedBold extends IPaintBoldEffect {

    {
        paint.setColor(0xffe91c2f);
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
		return R.drawable.mail_icon_red_bold;
	}

}
