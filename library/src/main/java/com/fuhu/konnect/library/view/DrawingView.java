package com.fuhu.konnect.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

    private static final String TAG = DrawingView.class.getSimpleName();

    private static int STROKE_WIDTH_DEFAULT = 7;

    private boolean mIsEraserMode;
    private boolean mIsEditable;

    private int mWidth;
    private int mHeight;
    private Path mDrawingPath;
	private Paint mBgPaint;
	private Paint mDrawingPaint;
	private Bitmap mBgBitmap;
	private Canvas mDrawingCanvas;

    public DrawingView(Context context) {
        super(context);
        init();
    }

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

    private void init() {
        mDrawingPaint = new Paint();
        mDrawingPaint.setStrokeWidth(STROKE_WIDTH_DEFAULT);
        mBgPaint = new Paint(Paint.DITHER_FLAG);
        mDrawingPath = new Path();
    }

    private void reset() {
        mBgBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mDrawingCanvas = new Canvas(mBgBitmap);

//        isCleanAll = true;
        mDrawingPath.reset();
    }

	@Override
	protected void onSizeChanged(int w, int h, int oldW, int oldH) {
//		Debug.dumpLog(TAG, "onSizeChanged()");
		mWidth = w;
		mHeight = h;
		reset();
	}

	@Override
	protected void onDraw(Canvas canvas) {
        if(mBgBitmap != null) {
			canvas.drawBitmap(mBgBitmap, 0, 0, mBgPaint);
			canvas.drawPath(mDrawingPath, mDrawingPaint);
		}

	}

	protected Bitmap getDrawingBitmap(){
		return mBgBitmap;
	}

	private float mFromX, mFromY;
	private static final float TOUCH_TOLERANCE = 4;

	private void startDrawing(float startX, float startY) {
		mDrawingPath.reset();
		mDrawingPath.moveTo(startX, startY);
		mFromX = startX;
		mFromY = startY;
	}

	private void doDrawing(float toX, float toY) {
		float dx = Math.abs(toX - mFromX);
		float dy = Math.abs(toY - mFromY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mDrawingPath.quadTo(mFromX, mFromY, (toX + mFromX) / 2, (toY + mFromY) / 2);
			if (mIsEraserMode == true) {
				mDrawingPath.lineTo(mFromX, mFromY);
				// commit the path to our offscreen
				mDrawingCanvas.drawPath(mDrawingPath, mDrawingPaint);
				mDrawingPath.reset();
				mDrawingPath.moveTo(mFromX, mFromY);
			}
			mFromX = toX;
			mFromY = toY;
		}
	}

	private void stopDrawing() {
		mDrawingCanvas.drawPoint(mFromX, mFromY, mDrawingPaint);
		if (mIsEraserMode == false) {
			mDrawingPath.lineTo(mFromX, mFromY);
			// commit the path to our offscreen
			mDrawingCanvas.drawPath(mDrawingPath, mDrawingPaint);
		}

		// kill this so we don't double draw
		mDrawingPath.reset();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
        if(!mIsEditable) return false;

		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startDrawing(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			doDrawing(x, y);
			break;
		case MotionEvent.ACTION_UP:
			stopDrawing();
			break;
		}
        invalidate();

		return true;
	}

	public void setEditable(boolean isEditable){
		this.mIsEditable = isEditable;
	}

	public void setStrokeWidth(float w){
		mDrawingPaint.setStrokeWidth(w);
	}

	public void setPaintColor(int color){
		mDrawingPaint.setColor(color);
	}

	public void clean(){
        reset();
		invalidate();
	}

	public void setPaint(Paint paint) {
		mIsEraserMode = false;
		this.mDrawingPaint = paint;
	}

	public void setEraser(Paint paint) {
		mIsEraserMode = true;
		this.mDrawingPaint = paint;
	}
}
