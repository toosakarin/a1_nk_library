package com.fuhu.konnect.library.mail.effect;

import android.graphics.Paint;

public abstract class IPaintBoldEffect extends IPaintEffect {
	
	@Override
	public Paint getPaint()
	{
		super.getPaint();
		paint.setStrokeWidth(20);
		
		return paint;
	}
}
