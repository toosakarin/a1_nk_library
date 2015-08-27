package com.fuhu.konnect.mail.effect;

import android.graphics.Paint;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.mail.effect.IPaintThinEffect;

public class PaintEffectRedThin extends IPaintThinEffect {

    Paint m_Paint = new Paint();

	@Override
	public void apply() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void cancel() {

    }

    @Override
	public int getEffectIconRes() {
		return R.drawable.mail_icon_red_thin;
	}

	@Override
	public Paint getPaint() {
		super.getPaint();

		// for drawing path
		m_Paint.setColor(0xffe91c2f);
		
		return m_Paint;
	}

}
