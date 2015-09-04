package com.fuhu.konnect.library.mail.effect;

import android.graphics.Paint;

public abstract class IPaintEffect implements Effect {
	
	protected Paint paint = new Paint();

	public Paint getPaint()
	{
		paint.setXfermode(null);
		paint.setAlpha(0xFF);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		
		return paint;
	}
}
