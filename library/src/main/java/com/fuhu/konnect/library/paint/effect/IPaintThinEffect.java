package com.fuhu.konnect.library.paint.effect;

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
