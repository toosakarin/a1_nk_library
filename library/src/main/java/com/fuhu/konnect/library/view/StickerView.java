package com.fuhu.konnect.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;


public class StickerView extends ImageView {

    final private String TAG = StickerView.class.getSimpleName();

    /**
     * min and max values of scale supported
     */
    protected static float mMinScale = 0.5f;
    protected static float mMaxScale = 1.0f;

    /**
     * length of icons in pixels
     */
    protected static float mButtonSide = 60;

    /**
     * space between view's central line and icon edges in pixels
     */
    protected static float mButtonSpacing = 24;

    /**
     * thickness of dotted frame
     */
    protected static float mBoarderWidth = 10;

    /**
     * density is a ratio of screen of device
     */
    protected static float mDensity;

    protected Context mContext;
    protected Paint mPaint = new Paint();

    /**
     * Indicates this view was attached to windows to avoid attached function twice
     */
    protected boolean mIsAttachedToWindows = false;

    /**
     * indicates whether we're dragging or scaling/rotating
     */
    protected boolean mDragging = true;

    /**
     * last dragged position
     */
    protected int mLastX, mLastY;

    /**
     * related to scaling operation
     */
    private Matrix mMatrix = new Matrix();
    private float mInitDistance = 0;
    private float mLastScale = 1;
    protected float mCurrentScale = 1;

    /**
     * related to rotate operation
     */
    private float mLastDegree = 0;

    /**
     * control buttons
     */
    private boolean mIsEnableRemoveBtn = true;
    private boolean mIsEnableMoveBtn = true;
    private RectF mButtonRemove = new RectF();
    private RectF mButtonMoveUp = new RectF();
    private RectF mButtonMoveDown = new RectF();

    /**
     * indicator whether to draw control icons
     */
    protected boolean mShouldDrawFullBackground = true;
    protected boolean mShouldDrawBackground = false;

    /**
     * indicator whether to draw frame of this view
     */
    protected boolean mIsEnableFrame = true;

    /**
     * indicator whether sticker will respond to touch event
     */
    private boolean mIsEditable = true;
    // reports resizing gesture to us
    private ScaleGestureDetector mScaleGestureDetector;

    /**
     * call back function supplied by activity / fragment
     */
    protected OnFocusChangeListener mOnFocusChangeListener;
    protected OnButtonClickListener mOnButtonClickListener;
    /**
     * assigned from parent
     */
    private int mIndex = -1;

    private int mBitmapWidth = 0;
    private int mBitmapHeight = 0;

    /**
     *
     */
    public interface OnFocusChangeListener {
        public void onFocused(StickerView view);
    }

    public interface OnButtonClickListener {
        public static final int CONFIRM     = 1;
        public static final int REMOVE      = 2;
        public static final int RESIZE      = 3;
        public static final int ROTATE      = 4;
        public static final int MOVE_UP     = 5;
        public static final int MOVE_DOWN   = 6;

        public void onClick(StickerView view, int btn_code);
    }

    public StickerView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param resId    image resource id for the imageview
     */
    public StickerView(Context context, int resId) {
        this(context, BitmapFactory.decodeResource(context.getResources(), resId));
    }

    public StickerView(Context context, Bitmap image, boolean showFullMenu) {
        this(context, image);
        mShouldDrawFullBackground = showFullMenu;
    }

    public static final int WIDGET_TYPE_STICKER = 0;
    public static final int WIDGET_TYPE_TEXT = 1;
    public static final int WIDGET_TYPE_PHOTO = 2;
    int mWidgetType = WIDGET_TYPE_STICKER;
    public StickerView(Context context, Bitmap image, boolean showFullMenu, int WidgetType) {
        this(context, image);
        mShouldDrawFullBackground = showFullMenu;
        mWidgetType = WidgetType;
    }

    public StickerView(Context context, Bitmap image) {
        super(context);
        this.mContext = context;

        //Configs of sticker
        mMinScale = 0.5f;
        mMaxScale = 1.0f;
        mButtonSide = 60;
        mButtonSpacing = 24;
        mBoarderWidth = 10;

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Style.STROKE);

        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);

        this.setImageBitmap(image);
        mBitmapWidth = image.getWidth();
        mBitmapHeight = image.getHeight();

        float scale = 1f / 1.5f;
        this.setScaleX(scale);
        this.setScaleY(scale);
        mCurrentScale = scale;
        mLastScale = scale;
    }

    /**
     * Not used.
     * @param gainFocus
     * @param direction
     * @param previouslyFocusedRect
     */
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus) {
            mShouldDrawBackground = true;
        } else {
            mShouldDrawBackground = mShouldDrawFullBackground = false;
        }
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

	/*
     * public methods
	 */

    /**
     * signals to hide control buttons when drawing
     */
    public void hideControl() {
        mShouldDrawFullBackground = false;
        mShouldDrawBackground = false;
        invalidate();
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        mOnFocusChangeListener = listener;
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mOnButtonClickListener = listener;
    }

    public void enableRemoveButton(boolean isEnable) {
        mIsEnableRemoveBtn = isEnable;
    }

    public void enableMoveButton(boolean isEnable) {
        mIsEnableRemoveBtn = isEnable;
    }


    /**
     * toggle edit mode of this StickerWidget
     *
     * @param isEditable
     */
    public void setEditable(boolean isEditable) {
        hideControl();
        mIsEditable = isEditable;
    }

    /**
     * sets private int mIndex.
     *
     * @param index
     */
    public void setIndex(int index) {
        this.mIndex = index;
    }

    public void setMinScale(float minScale) {
        mMinScale = minScale;
    }

    public void setMaxScale(float maxScale) {
        mMaxScale = maxScale;
    }

    /**
     * @return private int mIndex
     */
    public int getIndex() {
        return mIndex;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //If it's not first to attache, we should return this function to avoid initialize again
        if(mIsAttachedToWindows)
            return;

        LayoutParams params = getLayoutParams();
        if (params != null) {
            // maximum width and height would be the length of the diagonal of the original image
            int diagonal = (int) Math.round(Math.sqrt(mBitmapWidth * mBitmapWidth + mBitmapHeight * mBitmapHeight));
            Log.e(TAG, "width: " + mBitmapWidth);
            Log.e(TAG, "height: " + mBitmapHeight);
            Log.e(TAG, "diagonal: " + diagonal);
            // only needed 2 * mButtonSide for buttons to lie on the white frame
            // but include another 2 * mButtonSide anyway so the image will fit within the frame
            //params.width = diagonal + (int) (4 * mButtonSide);
            //params.height = diagonal + (int) (4 * mButtonSide);
            switch (mWidgetType) {
                case WIDGET_TYPE_TEXT: {
                    int val = diagonal + (int) (0.5 * mButtonSide);
                    if(val < 150){
                        val = 150;
                    }
                    params.width = val;
                    params.height = val;
                    break;
                }
                case WIDGET_TYPE_PHOTO: {
                    params.width = diagonal + (int) (2.5 * mButtonSide / mContext.getResources().getDisplayMetrics().density);
                    params.height = diagonal + (int) (2.5 * mButtonSide / mContext.getResources().getDisplayMetrics().density);
                    break;
                }
                case WIDGET_TYPE_STICKER:
                default:{
                    params.width = diagonal + (int) (0.8 * mButtonSide / mContext.getResources().getDisplayMetrics().density);
                    params.height = diagonal + (int) (0.8 * mButtonSide / mContext.getResources().getDisplayMetrics().density);
                    break;
                }
            }

            Log.e(TAG, "width: " + params.width);
            Log.e(TAG, "height: " + params.height);

            this.setLayoutParams(params);

            // translate the image so it is shown in the center of the view
            mMatrix.postTranslate((params.width - mBitmapWidth) / 2, (params.height - mBitmapHeight) / 2);
            this.setImageMatrix(mMatrix);
        }
        mDensity = mContext.getResources().getDisplayMetrics().density;
        mPaint.setStrokeWidth(mBoarderWidth * mDensity);
        this.setScaleType(ScaleType.MATRIX);

        //Sets the flag to avoid do this again
        mIsAttachedToWindows = true;
        if(mOnFocusChangeListener != null)
            mOnFocusChangeListener.onFocused(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsEditable) {
            return false;
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if(mOnFocusChangeListener != null)
                    mOnFocusChangeListener.onFocused(this);

                mShouldDrawBackground = true;
                mLastX = (int) event.getRawX();
                mLastY = (int) event.getRawY();
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mLastDegree = getRotationDegree(event);
                mDragging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDragging) {
                    drag(event);
                } else {
                    float currentDegree = getRotationDegree(event);
                    float delta_rotate = currentDegree - mLastDegree;
                    rotate(delta_rotate);
                    mLastDegree = currentDegree;
                    // resize will be handled by ScaleGestureDetector
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mLastScale = mCurrentScale;
                mDragging = true;
                // this essentially skips the next drag(event)
                mLastX = Integer.MAX_VALUE;
                mLastY = Integer.MAX_VALUE;
                break;
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                if (mButtonRemove.contains(x, y)) {
                    if(mOnButtonClickListener != null)
                        mOnButtonClickListener.onClick(this, OnButtonClickListener.REMOVE);
                }
                else if(mButtonMoveUp.contains(x, y)) {
                    if(mOnButtonClickListener != null)
                        mOnButtonClickListener.onClick(this, OnButtonClickListener.MOVE_UP);
                }
                else if(mButtonMoveDown.contains(x, y)) {
                    if(mOnButtonClickListener != null)
                        mOnButtonClickListener.onClick(this, OnButtonClickListener.MOVE_DOWN);
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                invalidate();
                break;
            default:
                break;
        }
        return mScaleGestureDetector.onTouchEvent(event);
    }

    private int mLeft = -1, mTop = -1, mRight = -1, mBottom = -1;

    @Override
    public void layout(int l, int t, int r, int b) {
        if (mLeft == -1 && mTop == -1 && mRight == -1 && mBottom == -1) {
            // when we do not have our desired position
            super.layout(l, t, r, b);
        } else {
            super.layout(mLeft, mTop, mRight, mBottom);
        }
    }

    /**
     * the order of the steps must be: 1. canvas save 2. draw control & frame 3.
     * restore
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // draws image resource
        super.onDraw(canvas);
        if (mShouldDrawBackground || mShouldDrawFullBackground) {
            canvas.save();
            canvas.scale(1 / mCurrentScale, 1 / mCurrentScale);
            /* */
            if(mIsEnableFrame)
                drawFrame(canvas);
            if(mIsEnableRemoveBtn)
                drawCenterCrossButton(canvas);
            if(mIsEnableMoveBtn)
                drawMoveButton(canvas);
            /* */
            canvas.restore();
        }
    }

    private void drag(MotionEvent event) {
        if (mLastX == Integer.MAX_VALUE && mLastY == Integer.MAX_VALUE) {
            mLastX = (int) event.getRawX();
            mLastY = (int) event.getRawY();
            return;
        }
        int dx = (int) event.getRawX() - mLastX;
        int dy = (int) event.getRawY() - mLastY;
        mLeft = getLeft() + dx;
        mBottom = getBottom() + dy;
        mRight = getRight() + dx;
        mTop = getTop() + dy;

        // if (l < 0) {
        // l = 0;
        // r = l + getWidth();
        // }

        // if (t < 0) {
        // t = 0;
        // b = t + getHeight();
        // }

        // if (r > )) {
        // r = screenWidth;
        // l = r - screenWidth;
        // }

        // if (b > screenHeight) {
        // b = screenHeight;
        // t = b - screenHeight;
        // }

        layout(mLeft, mBottom, mRight, mTop);
        mLastX = (int) event.getRawX();
        mLastY = (int) event.getRawY();
        postInvalidate();
    }

    /**
     * @param degree the degree to rotate
     */
    private void rotate(float degree) {
        // rotation degree is additive, need not reset
        mMatrix.postRotate(degree, getWidth() / 2, getHeight() / 2);
        this.setImageMatrix(mMatrix);
    }

    private void resize(float scale) {
        if (scale > mMaxScale) {
            scale = mMaxScale;
        } else if (scale < mMinScale) {
            scale = mMinScale;
        }
        mCurrentScale = scale;
        setScaleX(mCurrentScale);
        setScaleY(mCurrentScale);
    }

	/*
     * Helper math functions
	 */

    /**
     * @param event current motion event
     * @return degree between two pointers
     */
    private float getRotationDegree(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private ScaleGestureDetector.SimpleOnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mInitDistance = mScaleGestureDetector.getCurrentSpan();
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = mScaleGestureDetector.getCurrentSpan() / mInitDistance * mLastScale;
            resize(scale);

            return true;
        }
    };

    /**
     * void drawing function
     */

    /**
     * draws cross button at the bottom center
     *
     * @param canvas
     */
    private void drawCenterCrossButton(Canvas canvas) {
        float w = this.getWidth() * mCurrentScale;
        float h = this.getHeight() * mCurrentScale;
        float left = w / 2 - mButtonSide / 2 * mDensity;
        float top = h - mButtonSide * mDensity;
        float right = left + mButtonSide * mDensity;
        float bottom = top + mButtonSide * mDensity;
        float xLength = (right-left) / 3;

        mButtonRemove.set(left, top, right, bottom);
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Style.STROKE);
        p.setStrokeWidth(5);
        canvas.drawRect(mButtonRemove, p);
        canvas.drawLine(left + xLength, top + xLength, right - xLength, bottom - xLength, p);
        canvas.drawLine(right- xLength, top+ xLength, left+ xLength, bottom- xLength, p);

        refreshRectTouchArea(mButtonRemove);
    }

    /**
     * draws control buttons
     *
     * @param canvas
     */
    private void drawMoveButton(Canvas canvas) {
        float w = this.getWidth() * mCurrentScale;
        float h = this.getHeight() * mCurrentScale;
        float top = 0;
        float bottom = top + mButtonSide * mDensity;
        //The data of arrow to show on the button
        float arrowLength = ((mButtonSide * mDensity) / 5) / ((float) Math.sqrt(2)); //15
        float arrowAxisLength = (mButtonSide * mDensity) / 2; //40
        float arrowPadding = ((mButtonSide * mDensity) - arrowAxisLength) / 2;
        float arrowAxisPos = (mButtonSide * mDensity) / 2;
        Point arrowOri = new Point();
        Path arrowPath = new Path();

        // ======
        Paint p = new Paint();
        p.setStrokeWidth(5);
        p.setStyle(Style.STROKE);

        // draws move up button
        float left = w / 2 + mButtonSpacing * mDensity;
        float right = left + mButtonSide * mDensity;

        mButtonMoveUp.set(left, top, right, bottom);

        //Draws the up arrow into the move up button
        p.setColor(Color.BLUE);
        arrowOri.set((int) (left + arrowAxisPos), (int) (top + arrowPadding));
        arrowPath.moveTo(arrowOri.x, arrowOri.y);
        arrowPath.lineTo(arrowOri.x - arrowLength, arrowOri.y + arrowLength);
        arrowPath.moveTo(arrowOri.x, arrowOri.y - mDensity);
        arrowPath.lineTo(arrowOri.x, arrowOri.y + arrowAxisLength);
        arrowPath.moveTo(arrowOri.x, arrowOri.y);
        arrowPath.lineTo(arrowOri.x + arrowLength, arrowOri.y + arrowLength);
        canvas.drawPath(arrowPath, p); //draws arrow
        canvas.drawRect(mButtonMoveUp, p); //draws rect of button

        // draws move down button
        left = w / 2 - (mButtonSide + mButtonSpacing) * mDensity;
        right = left + mButtonSide * mDensity;
        mButtonMoveDown.set(left, top, right, bottom);

        //Draws the down arrow into the move up button
        p.setColor(Color.GREEN);
        arrowPath.reset();
        arrowOri.set((int) (left + arrowAxisPos), (int) (bottom - arrowPadding));
        arrowPath.moveTo(arrowOri.x, arrowOri.y);
        arrowPath.lineTo(arrowOri.x - arrowLength, arrowOri.y - arrowLength);
        arrowPath.moveTo(arrowOri.x, arrowOri.y + mDensity);
        arrowPath.lineTo(arrowOri.x, arrowOri.y - arrowAxisLength);
        arrowPath.moveTo(arrowOri.x, arrowOri.y);
        arrowPath.lineTo(arrowOri.x + arrowLength, arrowOri.y - arrowLength);
        canvas.drawPath(arrowPath, p); //draws arrow
        canvas.drawRect(mButtonMoveDown, p); //draws rect of button
        // ======

        refreshRectTouchArea(mButtonMoveUp);
        refreshRectTouchArea(mButtonMoveDown);
    }



    private void refreshRectTouchArea(RectF rect) {
        rect.set(rect.left / mCurrentScale, rect.top / mCurrentScale, rect.right / mCurrentScale, rect.bottom
                / mCurrentScale);
    }

    private RectF frame = new RectF();
    private final static float radius = 25f;

    DashPathEffect mDashPathEffect = new DashPathEffect(new float[]{50, 20}, (float) 1.0);

    /**
     * To draw the border of this view
     * @param canvas
     */
    protected void drawFrame(Canvas canvas) {
        float w = this.getWidth() * mCurrentScale;
        float h = this.getHeight() * mCurrentScale;
        float r = radius * mDensity;
        frame.set(0 + mButtonSide * mDensity / 2, 0 + mButtonSide / 2, w - mButtonSide * mDensity / 2, h - mButtonSide
                * mDensity / 2);
        mPaint.setPathEffect(mDashPathEffect);
        canvas.drawRoundRect(frame, r, r, mPaint);
    }

    public void enableDrawFrame(boolean isEnable) {
        mIsEnableFrame = isEnable;
    }

    public void setScale(float scale){
        this.setScaleX(scale);
        this.setScaleY(scale);
        mCurrentScale = scale;
        mLastScale = scale;
    }
}