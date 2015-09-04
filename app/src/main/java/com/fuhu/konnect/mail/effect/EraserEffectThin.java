package com.fuhu.konnect.mail.effect;

import android.graphics.Paint;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.mail.effect.IEraserEffect;

public class EraserEffectThin extends IEraserEffect {

    private Paint m_Paint = new Paint();

	@Override
	public void apply() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void cancel() {

    }

    @Override
	public int getEffectIconRes() {
		return R.drawable.mail_icon_eraser_thin;
	}

	@Override
	public Paint getPaint()
	{
		super.getPaint();
		
		m_Paint.setStrokeWidth(35);
		
		return m_Paint;
	}

}
