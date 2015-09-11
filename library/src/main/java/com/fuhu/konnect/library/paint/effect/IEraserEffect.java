package com.fuhu.konnect.library.paint.effect;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public abstract class IEraserEffect implements Effect{
	
	protected Paint paint = new Paint();

    {
        paint.setXfermode(null);
        paint.setAlpha(0x00);
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

	public Paint getPaint() {
		return paint;
	}
}
