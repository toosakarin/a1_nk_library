package com.fuhu.konnect.library.paint.effect;

import android.graphics.Paint;

public abstract class IPaintEffect implements Effect {
	
	protected Paint paint = new Paint();

    {
        paint.setXfermode(null);
        paint.setAlpha(0xFF);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

	public Paint getPaint() {
		return paint;
	}
}
