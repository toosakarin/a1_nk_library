package com.fuhu.konnect.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.fuhu.konnect.library.Debug;
import com.fuhu.konnect.library.paint.DefaultStickerCtrl;
import com.fuhu.konnect.library.paint.EffectCtrl;
import com.fuhu.konnect.library.paint.StickerCtrl;
import com.fuhu.konnect.library.paint.effect.Effect;
import com.fuhu.konnect.library.paint.effect.EffectGroup;
import com.fuhu.konnect.library.paint.effect.IColorWallPaperEffect;
import com.fuhu.konnect.library.paint.effect.IEraserEffect;
import com.fuhu.konnect.library.paint.effect.IEraserEffectAll;
import com.fuhu.konnect.library.paint.effect.IMultipleWallPaperEffect;
import com.fuhu.konnect.library.paint.effect.IPaintEffect;
import com.fuhu.konnect.library.paint.effect.ISingleWallPaperEffect;
import com.fuhu.konnect.library.paint.effect.IStickerEffect;
import com.fuhu.konnect.library.paint.effect.NoneEffect;
import com.fuhu.konnect.library.utility.GenerateIntID;

import java.util.ArrayList;

/**
* PaintView provides the way to build a flexible drawing view of various effect which are used to
* draw. PaintView is consist of two major instances which are EffectCtrl and StickerCtrl, the
* EffectCtrl provides the way to handle effect updating, and the StickerCtrl supports to use the
* sticker. You can implement your instances of this two interfaces and sets to PaintView to instead
* of default.
* Once you have created a view, here are a few steps that you must be known:
* <p/>
* 1. Implements the effect for drawing<br/>
* There are some basic effect are defined that you can implement those interfaces to make the
* effect by yourself, see {@link com.fuhu.konnect.library.paint.effect.IPaintEffect}, {@link com.fuhu.konnect.library.paint.effect.IColorWallPaperEffect}
* , {@link com.fuhu.konnect.library.paint.effect.IEraserEffect}, {@link com.fuhu.konnect.library.paint.effect.IEraserEffectAll},
* {@link com.fuhu.konnect.library.paint.effect.IStickerEffect}, {@link com.fuhu.konnect.library.paint.effect.ISingleWallPaperEffect},
* {@link com.fuhu.konnect.library.paint.effect.ISingleWallPaperEffect} etc.
* Furthermore, you can also extend the Effect interface to build newly types of effect for
* PaintView using, but at this time, you needs to handle this effect's updating by yourself, please
* see below section 5.
* <p/>
* 2. Puts the effect into effect group and adds the effect group into PaintView with corresponding
* button view for choosing
* clicking<br/>
* PaintView needs to add {@link com.fuhu.konnect.library.paint.effect.EffectGroup} which you are
* implemented to provide relational sub effects. The EffectGroup can be as a class of effect which
* has a corresponding view of button for clicking and trigger it to show sub effects or do something
* else. You should use {@link #addMainEffect(android.view.View, com.fuhu.konnect.library.paint.effect.EffectGroup)}
* to assign the EffectGroup with a corresponding clickable view.
* <p/>
* 3. Sets the sub effect adapter into PaintView for sub effect list displaying<br/>
* Once the button of main effect is clicked, the PaintView will change the main effect list to show
* the sub effect. The sub effect list are automatically inserted to the list using an
* {@link com.fuhu.konnect.library.view.PaintView.SubEffectAdapter} that pulls view by your designing.
* <p/>
* 4. Sets the effect content adapter into PaintView for effect content list display when sub effect
* is selected<br/>
* The effect content will be shown when the sub effect of sub effect list is clicked. The effect
* content list are automatically inserted to the grid using an
* {@link com.fuhu.konnect.library.view.PaintView.EffectContentAdapter} that pulls view by your
* designing.
* <p/>
* 5. Handles the effect when sub effect or effect content is selected
* PaintView predefine few common effects interface and correspond handling, so if you uses those
* predefine effects, you don't do anything that the PaintView still work fine. However, you can
* sets the listener for catching an effect updating of PaintView, and do something that you want
* to do, see more about {@link com.fuhu.konnect.library.view.PaintView.OnEffectUpdateListener}.
 *
 * <p/>
 * Author: Jack Tseng (jack.tseng@fuhu.com)
 */
public class PaintView extends RelativeLayout {

    public static final String TAG = PaintView.class.getSimpleName();

    //Layout of user interface

    private DrawingView mDrawingView;

    /**
     * This wrapper is including drawing view, stickers and texts on the screen
     */
    private FrameLayout mDrawingWrapper;

    private RelativeLayout mEffectToolbar;

    private ImageButton mBackEffectButton;

    /**
     * The tool bar is a list to show the effect of drawing email
     */
    private ScrollView mMainEffectScroller;


    private LinearLayout mMainEffectWrapper;

    private RecyclerView mSubEffectListWrapper;

    private RecyclerView mEffectContentWrapper;
    private EffectContentAdapter mEffectContentAdapter;

    //Date source
    private ArrayList<View> mMainEffectViewList;

    /**
     * This is a controller for effect updating
     */
    private OnEffectUpdateListener mOnEffectUpdateListener;

    private OnEffectContentClickListener mOnEffectContentClickListener;

    private EffectCtrl mEffectCtrl;

    private SubEffectAdapter mSubEffectAdapter;


    private int mEffectContentWrapperWidth;
    private int mEffectContentWrapperHeight;

    private StickerCtrl mStickerCtrl;


    public PaintView(Context context) {
        super(context);
        init();
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Context ctx = getContext();
        if(ctx == null) {
            Log.e(TAG, "can't be initialize");
            return;
        }

        //Creates the default EffectCtrl instance
        mEffectCtrl = new DefaultEffectCtrl();

        //
//        mStickerCtrl = new DefaultStickerCtrl();
        mStickerCtrl = new DefaultStickerCtrl(new FrameLayout(ctx));


        mMainEffectViewList = new ArrayList<>();

        mEffectToolbar = new RelativeLayout(ctx);
        mBackEffectButton = new ImageButton(ctx);
        mDrawingWrapper = new FrameLayout(ctx);
        mDrawingView = new DrawingView(ctx, null); //jack
        mMainEffectScroller = new ScrollView(ctx);
        mMainEffectWrapper = new LinearLayout(ctx);
        mSubEffectListWrapper = new RecyclerView(ctx);
        mEffectContentWrapper = new RecyclerView(getContext());

        mMainEffectScroller.setVerticalScrollBarEnabled(false);
        mMainEffectWrapper.setOrientation(LinearLayout.VERTICAL);

        LinearLayoutManager rmSubEffect = new LinearLayoutManager(getContext());
        mSubEffectListWrapper.setLayoutManager(rmSubEffect);
        mSubEffectListWrapper.setVisibility(View.GONE);

        GridLayoutManager rmEffectContent = new GridLayoutManager(getContext(), 2);
        rmEffectContent.setOrientation(GridLayoutManager.HORIZONTAL);
        mEffectContentWrapper.setLayoutManager(rmEffectContent);
        mEffectContentWrapper.setBackgroundColor(Color.parseColor("#FFD9EC"));
        mEffectContentWrapper.setVisibility(View.GONE);

        mBackEffectButton.setVisibility(View.INVISIBLE);
        mBackEffectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                switchEffectToolbar(false);
                cleanEffect();
            }
        });

        /**
         * Sets layout params
         */

        int subjectScrollerId = 0;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            subjectScrollerId = GenerateIntID.generateViewId();
        else
            subjectScrollerId = View.generateViewId();
//        mMainEffectScroller.setId(subjectScrollerId);
        mEffectToolbar.setId(subjectScrollerId);

        //Adds main effect wrapper into scroller
        ScrollView.LayoutParams mainEffectWrapperLp = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.MATCH_PARENT);
        mMainEffectScroller.addView(mMainEffectWrapper, mainEffectWrapperLp);
        //Sets location of main effect bar
        RelativeLayout.LayoutParams mainEffectScrollerLp = new LayoutParams(LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        mainEffectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        this.addView(mMainEffectScroller, mainEffectScrollerLp);
        mainEffectScrollerLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mEffectToolbar.addView(mMainEffectScroller, mainEffectScrollerLp);

        //Sets the location of sub effect list wrapper is same as subject wrapper
        RelativeLayout.LayoutParams subEffectListWrapperLp = new LayoutParams(LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        mainEffectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        this.addView(mSubEffectListWrapper, subEffectListWrapperLp);
        subEffectListWrapperLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mEffectToolbar.addView(mSubEffectListWrapper, subEffectListWrapperLp);

        RelativeLayout.LayoutParams effectToolbarLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        this.addView(mEffectToolbar, effectToolbarLp);

        RelativeLayout.LayoutParams drawingViewLp = new LayoutParams(LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        drawingViewLp.addRule(RIGHT_OF, subjectScrollerId);
//        this.addView(mDrawingView, drawingViewLp);
        mDrawingWrapper.addView(mDrawingView,drawingViewLp);

        //Adds the effect content into the DrawingWrapper
        mDrawingWrapper.addView(mEffectContentWrapper);

        //Adds the default sticker wrapper into the DrawingWrapper
        setStickerCtrl(mStickerCtrl);

        RelativeLayout.LayoutParams drawingWrapperLp = new LayoutParams(LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        drawingWrapperLp.addRule(RIGHT_OF, subjectScrollerId);
        this.addView(mDrawingWrapper, drawingWrapperLp);

        //The switch button for sub effect back to main effect
        RelativeLayout.LayoutParams backEffectBtnLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        backEffectBtnLp.addRule(RIGHT_OF, subjectScrollerId);
        backEffectBtnLp.addRule(ALIGN_PARENT_TOP);
        this.addView(mBackEffectButton, backEffectBtnLp);


        /**
         * for testing
         */
        if(Debug.IS_DEBUG) {
            mMainEffectScroller.setBackgroundColor(Color.BLUE);
            mSubEffectListWrapper.setBackgroundColor(Color.LTGRAY);
            mEffectContentWrapper.setBackgroundColor(Color.YELLOW);
        }

        this.invalidate();
    }

    private void setMainEffectViewOnClick(View v, final EffectGroup e) {
        v.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (x < 0 || x > v.getWidth() || y < 0 || y > v.getHeight()) {
                    return false;
                }

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Log.e(TAG, "main effect is clicked!");

                    EffectGroup newEffect = e;
                    mCurrentEffectGroup = newEffect;
                    if(mEffectCtrl != null)
                        mEffectCtrl.applyEffect(PaintView.this, mCurrentEffectGroup);

                    //Shows the sub effect toolbar
//                    mSubEffectAdapter.notifyDataSetChanged();
                    if(newEffect.getSubEffects() != null)
                        switchEffectToolbar(true);
                    v.performClick();
                }
                return true;
            }
        });
    }

    private void setSubEffectViewOnClick(View v, final Effect e) {
        v.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (x < 0 || x > v.getWidth() || y < 0 || y > v.getHeight()) {
                    return false;
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Effect newEffect = e;

                    v.performClick();

                    if(mEffectCtrl != null)
                        mEffectCtrl.applyEffect(PaintView.this, newEffect);
//                    v.performClick();
                }
                return true;
            }
        });
    }

    private void switchEffectToolbar(boolean isShowSubEffect) {
        if(isShowSubEffect) {
            //Sets the width of sub effect bar according to the main effect bar
            mSubEffectListWrapper.getLayoutParams().width = mMainEffectScroller.getWidth();

            mMainEffectScroller.setVisibility(View.GONE);
            mSubEffectListWrapper.setVisibility(View.VISIBLE);
            mBackEffectButton.setVisibility(View.VISIBLE);
        }
        else {
            mMainEffectScroller.setVisibility(View.VISIBLE);
            mSubEffectListWrapper.setVisibility(View.GONE);
            mBackEffectButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * To clean all of effects of this view
     */
    public void clean() {
        if(mStickerCtrl != null)
            mStickerCtrl.removeAllSticker();
        if(mDrawingView != null)
            mDrawingView.clean();
    }

    @Deprecated
    public void setBackEffectButtonImage(Bitmap image) {
        if(mBackEffectButton != null)
            mBackEffectButton.setImageBitmap(image);
    }

    /**
     * Returns the back button which is used for changing the sub effect toolbar back to the main
     * effect toolbar
     * @return
     */
    public ImageButton getBackEffectButton() {
        return mBackEffectButton;
    }

    /**
     * Returns a ViewGroup which is a wrapper to hold the DrawingView
     * @return
     */
    public ViewGroup getDrawingWrapper() {
        return mDrawingWrapper;
    }

    /**
     * Returns the DrawView of this view. The DrawingView is a view that you can draw it by touching
     * screen
     * @return
     */
    public DrawingView getDrawingView() {
        return mDrawingView;
    }

    /**
     * Returns the wrapper of effect toolbar which including both main effect toolbar and sub effect
     * toolbar
     * @return
     */
    public View getEffectWrapper() {
        return mEffectToolbar;
    }

    /**
     * Returns the effect of current usage
     * @return
     */
    public Effect getCurrentEffect() {
        Effect rtn = null;
        if(mEffectCtrl != null)
            rtn = mEffectCtrl.getCurrentEffect();
        return rtn;
    }

    /**
     * Returns the instance of StickerCtrl which default is a DefaultStickerCtrl object
     * @return
     */
    public StickerCtrl getStickerCtrl() {
        return mStickerCtrl;
    }

    /**
     * Sets the data behind the sub effect of this view
     * @param adapter
     */
    public void setSubEffectAdapter(SubEffectAdapter adapter) {
        mSubEffectAdapter = adapter;
        mSubEffectAdapter.mPaintView = this;
        mSubEffectListWrapper.setAdapter(mSubEffectAdapter);
    }

    /**
     * Sets the data behind the effect content of sub effect of this view
     * @param adapter
     */
    public void setEffectContentAdapter(EffectContentAdapter adapter) {
        mEffectContentAdapter = adapter;
        mEffectContentAdapter.setPaintView(this);
        mEffectContentWrapper.setAdapter(mEffectContentAdapter);
    }

    /**
     * Sets the instance of EffectCtrl to this view
     * @param ctrl
     */
    public void setEffectCtl(EffectCtrl ctrl) {
        mEffectCtrl = ctrl;
    }

    /**
     * Sets the instance of StickerCtrl to this view
     * @param ctrl
     */
    public void setStickerCtrl(StickerCtrl ctrl) {
        if(ctrl == null) return;
        if(ctrl.getStickerWrapper() == null) return;
        if(mDrawingWrapper == null) return;

        mStickerCtrl = ctrl;

        /**
         * Adds the sticker wrapper which providing from StickerCtrl into the DrawingView
         */
        FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        View stickerWrapper = ctrl.getStickerWrapper();
        mDrawingWrapper.addView(stickerWrapper, lp);

        if(Debug.IS_DEBUG)
            stickerWrapper.setBackgroundColor(Color.argb(90, 0, 0, 0));

        invalidate();
    }

    /**
     * Allows the sticker of this view can be editable or not
     * @param isEnable
     */
    public void setStickerEnable(boolean isEnable) {
        if(mStickerCtrl != null)
            mStickerCtrl.setEditable(isEnable);
    }

    /**
     * Sets the layout parameters of EffectContent of this view
     * @param w
     * @param h
     */
    public void setEffectContentSize(int w, int h) {
        mEffectContentWrapperWidth = w;
        mEffectContentWrapperHeight = h;
        if((mEffectContentWrapperWidth & mEffectContentWrapperHeight) > 0) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mEffectContentWrapper.getLayoutParams();
            lp.width = mEffectContentWrapperWidth;
            lp.height = mEffectContentWrapperHeight;
            lp.gravity = Gravity.CENTER;
            mEffectContentWrapper.setLayoutParams(lp);
        }
    }

    /**
     * Register a callback handler when there is a update in this view
     * @param listener
     */
    public void setOnEffectUpdateListener(OnEffectUpdateListener listener) {
        mOnEffectUpdateListener = listener;
    }

    /**
     * Register a callback handler when there is a event content to be clicked in this view
     * @param listener
     */
    public void setOnEffectContentClickListener(OnEffectContentClickListener listener) {
        mOnEffectContentClickListener = listener;
    }

    /**
     * Sets the background to DrawingView of this view
     * @param bg
     */
    public void setDrawingBackground(Bitmap bg) {
        if(bg == null) return;
        if(mDrawingWrapper != null)
            mDrawingWrapper.setBackground(new BitmapDrawable(getResources(), bg));
    }




    /**
     * Adds the EffectGroup object with given view as a pair to show on the main effect toolbar. The
     * view here will be added into the main effect toolbar showing on the PaintView . Moreover, this
     * view is registered a click listener to be invoked passing the EffectGroup object to show the
     * sub effect toolbar
     * @param view
     * @param effect
     */
    public void addMainEffect(View view, EffectGroup effect) {
        if(view == null) return;

        setMainEffectViewOnClick(view, effect);
        mMainEffectViewList.add(view);

        //Adds view to wrapper to show on the screen
        mMainEffectWrapper.addView(view);
        invalidate();
    }



    /**
     * Assigns a new effect to this view. This effect is actually passed to the EffectCtrl object of
     * this view
     * @param effect
     */
    public void applyEffect(Effect effect) {
        if(mEffectCtrl != null)
            mEffectCtrl.applyEffect(PaintView.this, effect);
    }

    /**
     * Cancels current effect of this view and backs to the main effect toolbar
     */
    public void cleanEffect() {
        if(mEffectCtrl != null)
            mEffectCtrl.clearEffect();
        switchEffectToolbar(false);
    }

    /**
     * Shows the effect content with given effect on the screen
     * @param effect
     */
    public void openEffectContent(Effect effect) {
        if(effect == null) return;
        if(mEffectContentAdapter == null) return;
        if(mEffectContentWrapper == null) return;
        mEffectContentAdapter.setPaintView(this);
        mEffectContentAdapter.setEffectCtrl(mEffectCtrl);
        mEffectContentWrapper.setVisibility(View.VISIBLE);

        //Lets the effect content to the top of screen of the drawing wrapper
        mDrawingWrapper.bringChildToFront(mEffectContentWrapper);

        //Resets the adapter to make sure getting the newly holder by onCreateHolder()
        mEffectContentWrapper.setAdapter(mEffectContentAdapter);

        invalidate();
    }

    /**
     * Closes the effect content of this view
     */
    public void closeEffectContent() {
        if(mEffectContentWrapper == null) return;
        mEffectContentWrapper.removeAllViews();
        mEffectContentWrapper.setVisibility(View.INVISIBLE);

        //Removes current adapter for creating newly holder of opening EffectContentWrapper next
        // time
        mEffectContentWrapper.setAdapter(null);
    }

    /**
     * The param is a effect which indicating current effect of main effect
     */
    private EffectGroup mCurrentEffectGroup;

    public abstract static class SubEffectAdapter <VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

        private PaintView mPaintView;

        public abstract void onBindEffectToHolder(VH holder, Effect currentEffect, Effect effect);


        @Override
        public void onBindViewHolder(VH holder, int position) {
            Effect effect = null;
            Effect currentEffect = null;
            if(mPaintView != null && mPaintView.mCurrentEffectGroup != null
                    && mPaintView.mCurrentEffectGroup.getSubEffects() != null)
                effect =  mPaintView.mCurrentEffectGroup.getSubEffects().get(position);
            if(mPaintView != null)
                currentEffect = mPaintView.getCurrentEffect();

            onBindEffectToHolder(holder, currentEffect, effect);

            mPaintView.setSubEffectViewOnClick(holder.itemView, effect);
        }

        @Override
        public int getItemCount() {
            int rtn = 0;
            do {
                if(mPaintView == null) break;
                if(mPaintView.mCurrentEffectGroup == null) break;
                if(mPaintView.mCurrentEffectGroup.getSubEffects() == null) break;

                rtn = mPaintView.mCurrentEffectGroup.getSubEffects().size();
            } while (false);

            return rtn;
        }
    }


    public static class EffectContentHolder extends RecyclerView.ViewHolder {

        public EffectContentView effectContentView;

        public EffectContentHolder(EffectContentView itemView) {
            super(itemView);
            effectContentView = itemView;
        }

    }

    public abstract static class EffectContentAdapter <VH extends EffectContentHolder> extends RecyclerView.Adapter<VH> {

        private static String TAG = EffectContentAdapter.class.getSimpleName();

        PaintView mPaintView;
        private EffectCtrl mEffectCtrl;

        public abstract VH onCreateViewHolder(Effect currentEffect, ViewGroup parent, int viewType);

        public abstract void onBindViewHolder(Effect currentEffect, VH holder, int position);

        public abstract int getItemCount(Effect currentEffect);


        private void setPaintView(PaintView pv) {
            mPaintView = pv;
        }

        private void setEffectCtrl(EffectCtrl ctrl) {
            mEffectCtrl = ctrl;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            Effect currentEffect = null;
            if(mEffectCtrl != null)
                currentEffect = mEffectCtrl.getCurrentEffect();
            return onCreateViewHolder(currentEffect, parent, viewType);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            if(mEffectCtrl == null) {
                Log.e(TAG, "there is no EffectCtrl instance be found!");
                return;
            }

            onBindViewHolder(mEffectCtrl.getCurrentEffect(), holder, position);

            /**
             * Registers a callback to be called when this content view is clicked
             */
            EffectContentView view = holder.effectContentView;
            view.setPreOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (mPaintView != null)
//                        mPaintView.closeEffectContent();

                    if (mEffectCtrl != null) {
                        EffectContentView ecv = (EffectContentView) v;
                        mEffectCtrl.applyEffectContent(mPaintView, ecv.getContent());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            Effect currentEffect = null;
            if(mEffectCtrl != null)
                currentEffect = mEffectCtrl.getCurrentEffect();
            return getItemCount(currentEffect);
        }
    }

    /**
     * This interface will be invoked when the default EffectCtrl is assign the new effect by
     * applyEffect().
     */
    public interface OnEffectUpdateListener {
        public boolean onPreUpdate(View paintView, Effect newEffect, Effect oldEffect);
        public void onPostUpdate(View paintView, Effect currentEffect);
    }

    public interface OnEffectContentClickListener {
        public void onEffectContentClick(View paintView, Effect currentEffect, Object content);
    }

    private class DefaultEffectCtrl implements EffectCtrl {

        private Effect mCurrentEffect;

        @Override
        public Effect getCurrentEffect() {
            return mCurrentEffect;
        }

        @Override
        public void clearEffect() {
            applyEffect(PaintView.this, new NoneEffect());
        }

        @Override
        public void applyEffect(View view, Effect newEffect) {

            PaintView paintView = (view instanceof PaintView) ? (PaintView) view : null;

            //just to stop current effect
            if(mCurrentEffect != null)
                mCurrentEffect.cancel();

            /**
             * Pre-process of effect update
             */
            closeEffectContent();
            setStickerEnable(true);

            boolean isEnableUpdate = true;
            if(mOnEffectUpdateListener != null)
                isEnableUpdate = mOnEffectUpdateListener.onPreUpdate(PaintView.this, newEffect, mCurrentEffect);

            //If onPreUpdate() returns false, then return this function to break this updating
            if(!isEnableUpdate) return;



            /**
             * Assigns a new effect
             */
            if(paintView == null) return;

            final DrawingView drawingView = paintView.getDrawingView();
            final ViewGroup drawingWrapper = paintView.getDrawingWrapper();
            drawingView.setEditable(false);

            if(newEffect instanceof IPaintEffect) {
                drawingView.setPaint(((IPaintEffect) newEffect).getPaint());
                drawingView.setEditable(true);
                setStickerEnable(false);
            }
            else if(newEffect instanceof IEraserEffect) {
                drawingView.setEraser(((IEraserEffect) newEffect).getPaint());
                drawingView.setEditable(true);
                setStickerEnable(false);
            }
            else if(newEffect instanceof IEraserEffectAll) {

                //nothing to do

            }
//            else if(newEffect instanceof PaintEffects) {
//                IPaintEffect paintEffect = (IPaintEffect) ((EffectGroup) newEffect).getSubEffects().get(0);
//                drawingView.setPaint(paintEffect.getPaint());
//                drawingView.setEditable(true);
//            } else if(newEffect instanceof EraserEffects) {
//                // auto select the second effect
//                IEraserEffect eraserEffect = null;
//                ArrayList<Effect> effects = ((EffectGroup) newEffect).getSubEffects();
//                for(int i=0; i<effects.size(); i++)
//                    if(effects.get(i) instanceof IEraserEffect) {
//                        eraserEffect = (IEraserEffect) effects.get(i);
//                        break;
//                    }
//                drawingView.setEraser(eraserEffect.getPaint());
//            }
            else if(newEffect instanceof IStickerEffect) {
                openEffectContent(newEffect);
            }
            else if(newEffect instanceof ISingleWallPaperEffect) {
                drawingWrapper.setBackgroundResource(((ISingleWallPaperEffect) newEffect).getWallPaperResId());
            }
            else if(newEffect instanceof IColorWallPaperEffect || newEffect instanceof IMultipleWallPaperEffect) {
                openEffectContent(newEffect);
            } else if(newEffect instanceof NoneEffect) {

                //noting to do

            }

            //Assigns current effect and to update the sub effects data
            mCurrentEffect = newEffect;
            mSubEffectAdapter.notifyDataSetChanged();

            if(mOnEffectUpdateListener != null)
                mOnEffectUpdateListener.onPostUpdate(PaintView.this, mCurrentEffect);
        }

//        @Override
//        public void applyEffectContent(PaintView pv, EffectContentView contentView) {
//
//            closeEffectContent();
//
//            if(contentView == null)
//                return;
//
//            if(mCurrentEffect instanceof IStickerEffect) {
//                View stickerView = (View) contentView.getContent();
//                //Adds sticker into the PaintView
//                if(mStickerCtrl != null)
//                    mStickerCtrl.addSticker(stickerView);
//            } else if(mCurrentEffect instanceof IColorWallPaperEffect) {
//                Integer bgId = (Integer) contentView.getContent();
//                mDrawingWrapper.setBackgroundColor(getResources().getColor(bgId));
//                invalidate();
//            } else if(mCurrentEffect instanceof IMultipleWallPaperEffect) {
//                Integer bgId = (Integer) contentView.getContent();
//                mDrawingWrapper.setBackgroundResource(bgId);
//                invalidate();
//            }
//        }
        @Override
        public void applyEffectContent(View view, Object content) {

            PaintView pv = (view instanceof PaintView) ? (PaintView) view : null;

            closeEffectContent();

            if(content == null)
                return;

            if(mCurrentEffect instanceof IStickerEffect) {
                if(content instanceof View) {
                    //Adds sticker into the PaintView
                    if (mStickerCtrl != null)
                        mStickerCtrl.addSticker((View) content);
                }
                else
                    Log.e(TAG, "a wrong instance of class of effect content for the IStickerEffect");

            } else if(mCurrentEffect instanceof IColorWallPaperEffect) {
                if(content instanceof Integer) {
                    mDrawingWrapper.setBackgroundColor(getResources().getColor((Integer) content));
                    invalidate();
                }
                else
                    Log.e(TAG, "a wrong instance of class of effect content for the IColorWallPaperEffect effect");
            } else if(mCurrentEffect instanceof IMultipleWallPaperEffect) {
                if(content instanceof Integer) {
                    mDrawingWrapper.setBackgroundResource((Integer) content);
                    invalidate();
                }
                else
                    Log.e(TAG, "a wrong instance of class of effect content for the IMultipleWallPaperEffect");
            }

            if(mOnEffectContentClickListener != null)
                mOnEffectContentClickListener.onEffectContentClick(PaintView.this, mCurrentEffect, content);
        }
    }


}
