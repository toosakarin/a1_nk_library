package com.fuhu.konnect.library.view;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.fuhu.konnect.library.mail.EffectCtrl;
import com.fuhu.konnect.library.mail.OnEffectUpdateListener;
import com.fuhu.konnect.library.mail.effect.Effect;
import com.fuhu.konnect.library.mail.effect.EffectGroup;
import com.fuhu.konnect.library.mail.effect.EraserEffects;
import com.fuhu.konnect.library.mail.effect.IColorWallPaperEffect;
import com.fuhu.konnect.library.mail.effect.IEraserEffect;
import com.fuhu.konnect.library.mail.effect.IMultipleWallPaperEffect;
import com.fuhu.konnect.library.mail.effect.IPaintEffect;
import com.fuhu.konnect.library.mail.effect.ISingleWallPaperEffect;
import com.fuhu.konnect.library.mail.effect.IStickerEffect;
import com.fuhu.konnect.library.mail.effect.PaintEffects;
import com.fuhu.konnect.library.utility.GenerateIntID;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/8/20.
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

    /**
     * The tool bar is a list to show the effect of drawing email
     */
    private ScrollView mMainEffectScroller;


    private LinearLayout mMainEffectWrapper;

//    private RecyclerView mContentListWrapper;
    private RecyclerView mSubEffectListWrapper;
    private SubEffectAdapter mSubEffectAdapter;

    private RecyclerView mEffectContentWrapper;
    private EffectContentAdapter mEffectContentAdapter;

    //Date source
    private ArrayList<View> mMainEffectViewList;

    /**
     * This is a controller for effect updating
     */
    private OnEffectUpdateListener mOnEffectUpdateListener;

    private EffectCtrl mEffectCtrl;

    private SubEffectAdapter mAdapter;


    /**
     * For bundling the effect and the view which indicating the effect and to show on the screen
     */
    private class EffectView {
        private Effect effect;
        private View view;

        private EffectView (Effect e, View v) {
            effect = e;
            view = v;
        }
    }



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

        mMainEffectViewList = new ArrayList<>();

        mEffectToolbar = new RelativeLayout(ctx);
        mDrawingWrapper = new FrameLayout(ctx);
        mDrawingView = new DrawingView(ctx);
        mMainEffectScroller = new ScrollView(ctx);
        mMainEffectWrapper = new LinearLayout(ctx);
        mSubEffectListWrapper = new RecyclerView(ctx);

        mMainEffectWrapper.setOrientation(LinearLayout.VERTICAL);

        LinearLayoutManager lManager = new LinearLayoutManager(getContext());
        mSubEffectListWrapper.setLayoutManager(lManager);
        mSubEffectListWrapper.setVisibility(View.GONE);

        int subjectScrollerId = 0;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            subjectScrollerId = GenerateIntID.generateViewId();
        else
            subjectScrollerId = View.generateViewId();
//        mMainEffectScroller.setId(subjectScrollerId);
        mEffectToolbar.setId(subjectScrollerId);

        /**
         * Sets layout params
         */

        //Adds main effect wrapper into scroller
        ScrollView.LayoutParams mainEffectWrapperLp = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.MATCH_PARENT);
        mMainEffectScroller.addView(mMainEffectWrapper, mainEffectWrapperLp);
        //Sets location of main effect bar
        RelativeLayout.LayoutParams mainEffectScrollerLp = new LayoutParams(LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        mainEffectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        this.addView(mMainEffectScroller, mainEffectScrollerLp);
        mEffectToolbar.addView(mMainEffectScroller, mainEffectScrollerLp);

        //Sets the location of sub effect list wrapper is same as subject wrapper
        RelativeLayout.LayoutParams subEffectListWrapperLp = new LayoutParams(LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        mainEffectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        this.addView(mSubEffectListWrapper, subEffectListWrapperLp);
        mEffectToolbar.addView(mSubEffectListWrapper, subEffectListWrapperLp);

        RelativeLayout.LayoutParams effectToolbarLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        this.addView(mEffectToolbar, effectToolbarLp);


        RelativeLayout.LayoutParams drawingViewLp = new LayoutParams(LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        drawingViewLp.addRule(RIGHT_OF, subjectScrollerId);
//        this.addView(mDrawingView, drawingViewLp);
        mDrawingWrapper.addView(mDrawingView,drawingViewLp);

        RelativeLayout.LayoutParams drawingWrapperLp = new LayoutParams(LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        drawingWrapperLp.addRule(RIGHT_OF, subjectScrollerId);
        this.addView(mDrawingWrapper, drawingWrapperLp);

        /**
         * for testing
         */
        mMainEffectScroller.setBackgroundColor(Color.BLUE);
        mSubEffectListWrapper.setBackgroundColor(Color.LTGRAY);
        mDrawingView.setBackgroundColor(Color.RED);


        this.invalidate();
    }

    public void setAdapter(SubEffectAdapter adapter) {
        mAdapter = adapter;
        mAdapter.mPaintView = this;
        mSubEffectListWrapper.setAdapter(mAdapter);
    }

    public void setEffectContentAdapter(EffectContentAdapter adapter) {
        mEffectContentAdapter = adapter;
//        mEffectContentAdapter.setCurrentEffect(this);
//        mEffectContentWrapper.setAdapter(mEffectContentAdapter);
    }

    public void setEffectCtl(EffectCtrl ctrl) {
        mEffectCtrl = ctrl;
    }

    public ViewGroup getDrawingWrapper() {
        return mDrawingWrapper;
    }

    public DrawingView getDrawingView() {
        return mDrawingView;
    }

    public void addMainEffect(View view, EffectGroup effect) {
        if(view == null) return;

        setMainEffectViewOnClick(view, effect);
        mMainEffectViewList.add(view);

        //Adds view to wrapper to show on the screen
        mMainEffectWrapper.addView(view);
        invalidate();
    }

    private void setMainEffectViewOnClick(View v, final EffectGroup e) {
        v.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(TAG, "main effect is clicked!");

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    EffectGroup newEffect = e;

                    mCurrentEffectGroup = newEffect;
                    if(mEffectCtrl != null)
                        mEffectCtrl.applyEffect(PaintView.this, mCurrentEffectGroup);


                    //Shows the sub effect toolbar
                    mAdapter.notifyDataSetChanged();
                    switchEffectToolbar(true);
                }
                return true;
            }
        });
    }

//    private void setViewClick(View v) {
//        v.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if(event.getAction() == MotionEvent.ACTION_UP) {
//                    Effect newEffect = null;
//                    if(v instanceof com.fuhu.konnect.library.mail.view.EffectView)
//                        newEffect = ((EffectView) v).getEffect();
//
//                    /**
//                     * update
//                     */
//
//
//                    if(mEffectCtrl != null)
//                        mEffectCtrl.applyEffect(PaintView.this, newEffect);
//                }
//
//                return false;
//            }
//        });
//    }
    private void setSubEffectViewOnClick(View v, final Effect e) {
        v.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Effect newEffect = e;

                    if(mEffectCtrl != null)
                        mEffectCtrl.applyEffect(PaintView.this, newEffect);

                    //Shows effect content wrapper
                    openEffectContent();
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
        }
        else {
            mMainEffectScroller.setVisibility(View.VISIBLE);
            mSubEffectListWrapper.setVisibility(View.GONE);
        }
    }

    private void openEffectContent() {
        if(mEffectContentAdapter == null) return;
        if(mEffectContentWrapper == null) {
            mEffectContentWrapper = new RecyclerView(getContext());
            GridLayoutManager lManager = new GridLayoutManager(getContext(), 2);
            lManager.setOrientation(GridLayoutManager.HORIZONTAL);
            mSubEffectListWrapper.setLayoutManager(lManager);
            mEffectContentAdapter.setCurrentEffect(this);
            mEffectContentWrapper.setAdapter(mEffectContentAdapter);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(800, 600);
            lp.gravity = Gravity.CENTER;
//            mDrawingWrapper.addView(mEffectContentWrapper, lp);
            this.addView(mEffectContentWrapper, new LayoutParams(800, 500));
            invalidate();
        }
    }

    private void closeEffectContent() {
        if(mEffectContentWrapper == null) return;
        mDrawingWrapper.removeView(mEffectContentWrapper);
        mEffectContentWrapper.removeAllViews();
        mEffectContentWrapper = null;
    }

    /**
     * The param is a effect which indicating current effect of main effect
     */
    private EffectGroup mCurrentEffectGroup;

    public abstract static class SubEffectAdapter <VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

        private PaintView mPaintView;

        public abstract void onBindEffectToHolder(VH holder, Effect effect);


        @Override
        public void onBindViewHolder(VH holder, int position) {
            Effect effect = null;
            if(mPaintView != null && mPaintView.mCurrentEffectGroup != null
                    && mPaintView.mCurrentEffectGroup.getSubEffects() != null)
                effect =  mPaintView.mCurrentEffectGroup.getSubEffects().get(position);

            onBindEffectToHolder(holder, effect);

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

    public abstract static class EffectContentAdapter <VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

        PaintView mPaintView;

        private Effect mCurrentEffect;

        public abstract VH onCreateViewHolder(Effect currentEffect, ViewGroup parent, int viewType);

        public abstract void onBindViewHolder(Effect currentEffect, VH holder, int position);

        public abstract int getItemCount(Effect currentEffect);

        private void setCurrentEffect(PaintView pv) {
            mPaintView = pv;

            if(mPaintView != null || mPaintView.mEffectCtrl != null)
                mCurrentEffect = mPaintView.mEffectCtrl.getCurrentEffect();
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return onCreateViewHolder(mCurrentEffect, parent, viewType);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            if(mPaintView.mEffectCtrl == null) return;
            onBindViewHolder(mCurrentEffect, holder, position);
        }

        @Override
        public int getItemCount() {
            if(mPaintView.mEffectCtrl == null) return 0;
            return getItemCount(mCurrentEffect);
        }
    }

    private class DefaultEffectCtrl implements EffectCtrl {

        private Effect mCurrentEffect;

        @Override
        public Effect getCurrentEffect() {
            return mCurrentEffect;
        }

        @Override
        public void applyEffect(PaintView paintView, Effect newEffect) {

            //just to stop current effect
            if(mCurrentEffect != null)
                mCurrentEffect.cancel();

            closeEffectContent();

            /**
             * Assigns the new effect
             */
            if(paintView == null) return;

            final DrawingView drawingView = paintView.getDrawingView();
            final ViewGroup drawingWrapper = paintView.getDrawingWrapper();
            drawingView.setEditable(false);

            if(newEffect instanceof ISingleWallPaperEffect) {
                drawingWrapper.setBackgroundResource(((ISingleWallPaperEffect) newEffect).getWallPaperResId());
            } else if(newEffect instanceof IPaintEffect) {
                drawingView.setPaint(((IPaintEffect) newEffect).getPaint());
                drawingView.setEditable(true);
            } else if(newEffect instanceof IEraserEffect) {
                drawingView.setEraser(((IEraserEffect) newEffect).getPaint());
                drawingView.setEditable(true);
            } else if(newEffect instanceof PaintEffects) {
                IPaintEffect paintEffect = (IPaintEffect) ((EffectGroup) newEffect).getSubEffects().get(0);
                drawingView.setPaint(paintEffect.getPaint());
            } else if(newEffect instanceof EraserEffects) {
                // auto select the second effect
                IEraserEffect eraserEffect = null;
                ArrayList<Effect> effects = ((EffectGroup) newEffect).getSubEffects();
                for(int i=0; i<effects.size(); i++)
                    if(effects.get(i) instanceof IEraserEffect) {
                        eraserEffect = (IEraserEffect) effects.get(i);
                        break;
                    }
                drawingView.setEraser(eraserEffect.getPaint());
            } else if(newEffect instanceof IStickerEffect) {
                openEffectContent();
            }
            else if(newEffect instanceof ISingleWallPaperEffect
                    || newEffect instanceof IColorWallPaperEffect || newEffect instanceof IMultipleWallPaperEffect) {
                openEffectContent();
            }


            if(mOnEffectUpdateListener != null)
                mOnEffectUpdateListener.onEffectUpdated(PaintView.this, newEffect, mCurrentEffect);

            mCurrentEffect = newEffect;
        }
    }

}
