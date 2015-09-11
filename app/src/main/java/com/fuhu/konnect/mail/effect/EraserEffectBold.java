package com.fuhu.konnect.mail.effect;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.IEraserEffect;


public class EraserEffectBold extends IEraserEffect {

    {
        paint.setStrokeWidth(100);
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
		return R.drawable.mail_icon_eraser;
	}

}
