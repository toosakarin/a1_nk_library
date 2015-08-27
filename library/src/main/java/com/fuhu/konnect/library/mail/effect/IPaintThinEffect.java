package com.fuhu.konnect.library.mail.effect;

import android.graphics.Paint;

public abstract class IPaintThinEffect extends IPaintEffect {
	
	@Override
	public Paint getPaint()
	{
		super.getPaint();
		paint.setStrokeWidth(10);
		
		return paint;
	}
}
