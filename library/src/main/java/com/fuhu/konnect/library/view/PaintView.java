package com.fuhu.konnect.library.view;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.fuhu.konnect.library.Debug;
import com.fuhu.konnect.library.mail.EffectCtrl;
import com.fuhu.konnect.library.mail.OnEffectUpdateListener;
import com.fuhu.konnect.library.mail.StickerCtrl;
import com.fuhu.konnect.library.mail.effect.Effect;
import com.fuhu.konnect.library.mail.effect.EffectGroup;
import com.fuhu.konnect.library.mail.effect.EraserEffects;
import com.fuhu.konnect.library.mail.effect.IColorWallPaperEffect;
import com.fuhu.konnect.library.mail.effect.IEraserEffect;
import com.fuhu.konnect.library.mail.effect.IEraserEffectAll;
import com.fuhu.konnect.library.mail.effect.IMultipleWallPaperEffect;
import com.fuhu.konnect.library.mail.effect.IPaintEffect;
import com.fuhu.konnect.library.mail.effect.ISingleWallPaperEffect;
import com.fuhu.konnect.library.mail.effect.IStickerEffect;
import com.fuhu.konnect.library.mail.effect.NoneEffect;
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

    private ImageButton mBackEffectButton;

    /**
     * The tool bar is a list to show the effect of drawing email
     */
    private ScrollView mMainEffectScroller;


    private LinearLayout mMainEffectWrapper;

//    private RecyclerView mContentListWrapper;
    private RecyclerView mSubEffectListWrapper;
//    private SubEffectAdapter mSubEffectAdapter;

    private RecyclerView mEffectContentWrapper;
    private EffectContentAdapter mEffectContentAdapter;

    //Date source
    private ArrayList<View> mMainEffectViewList;

    /**
     * This is a controller for effect updating
     */
    private OnEffectUpdateListener mOnEffectUpdateListener;

    private EffectCtrl mEffectCtrl;

    private SubEffectAdapter mSubEffectAdapter;


    private int mEffectContentWrapperWidth;
    private int mEffectContentWrapperHeight;

    private StickerCtrl mStickerCtrl;

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

        //
        mStickerCtrl = new DefaultStickerCtrl();


        mMainEffectViewList = new ArrayList<>();

        mEffectToolbar = new RelativeLayout(ctx);
        mBackEffectButton = new ImageButton(ctx);
        mDrawingWrapper = new FrameLayout(ctx);
        mDrawingView = new DrawingView(ctx, null); //jack
        mMainEffectScroller = new ScrollView(ctx);
        mMainEffectWrapper = new LinearLayout(ctx);
        mSubEffectListWrapper = new RecyclerView(ctx);
        mEffectContentWrapper = new RecyclerView(getContext());

        mMainEffectWrapper.setOrientation(LinearLayout.VERTICAL);

        LinearLayoutManager rmSubEffect = new LinearLayoutManager(getContext());
        mSubEffectListWrapper.setLayoutManager(rmSubEffect);
        mSubEffectListWrapper.setVisibility(View.GONE);

        GridLayoutManager rmEffectContent = new GridLayoutManager(getContext(), 2);
        rmEffectContent.setOrientation(GridLayoutManager.HORIZONTAL);
        mEffectContentWrapper.setLayoutManager(rmEffectContent);
        mEffectContentWrapper.setVisibility(View.GONE);

        mBackEffectButton.setVisibility(View.INVISIBLE);
        mBackEffectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchEffectToolbar(false);
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
            mDrawingView.setBackgroundColor(Color.WHITE);
            mEffectContentWrapper.setBackgroundColor(Color.YELLOW);
        }

        this.invalidate();
    }

    public void setBackEffectButtonImage(Bitmap image) {
        if(mBackEffectButton != null)
            mBackEffectButton.setImageBitmap(image);
    }

    public ImageButton getBackEffectButton() {
        return mBackEffectButton;
    }

    public void setSubEffectAdapter(SubEffectAdapter adapter) {
        mSubEffectAdapter = adapter;
        mSubEffectAdapter.mPaintView = this;
        mSubEffectListWrapper.setAdapter(mSubEffectAdapter);
    }

    public void setEffectContentAdapter(EffectContentAdapter adapter) {
        mEffectContentAdapter = adapter;
//        mEffectContentAdapter.setCurrentEffect(this);
        mEffectContentAdapter.setPaintView(this);
        mEffectContentWrapper.setAdapter(mEffectContentAdapter);
    }

    public void setEffectCtl(EffectCtrl ctrl) {
        mEffectCtrl = ctrl;
    }

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

    public StickerCtrl getStickerCtrl() {
        return mStickerCtrl;
    }

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
                    mSubEffectAdapter.notifyDataSetChanged();
                    switchEffectToolbar(true);
                }
                return true;
            }
        });
    }

    private void setSubEffectViewOnClick(View v, final Effect e) {
        v.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Effect newEffect = e;

                    if(mEffectCtrl != null)
                        mEffectCtrl.applyEffect(PaintView.this, newEffect);

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


    public void openEffectContent(Effect effect) {
//        if(mEffectContentAdapter == null) return;
//        mEffectContentAdapter.setCurrentEffect(this);
//
//        if(mEffectContentWrapper == null) {
//            mEffectContentWrapper = new RecyclerView(getContext());
//            GridLayoutManager lManager = new GridLayoutManager(getContext(), 2);
//            lManager.setOrientation(GridLayoutManager.HORIZONTAL);
//            mEffectContentWrapper.setLayoutManager(lManager);
//            mEffectContentWrapper.setSubEffectAdapter(mEffectContentAdapter);
//
//            if((mEffectContentWrapperWidth & mEffectContentWrapperHeight) == 0) {
//                mDrawingWrapper.addView(mEffectContentWrapper);
//            }
//            else {
//                FrameLayout.LayoutParams lp =
//                        new FrameLayout.LayoutParams(mEffectContentWrapperWidth, mEffectContentWrapperHeight);
//                lp.gravity = Gravity.CENTER;
//                mDrawingWrapper.addView(mEffectContentWrapper, lp);
//            }
//            invalidate();
//        }
        if(effect == null) return;
        if(mEffectContentAdapter == null) return;
        if(mEffectContentWrapper == null) return;
//        mEffectContentAdapter.setCurrentEffect(this);
        mEffectContentAdapter.setCurrentEffect(effect);
        mEffectContentWrapper.setVisibility(View.VISIBLE);
        //Lets the effect content to the top of screen of the drawing wrapper
        mDrawingWrapper.bringChildToFront(mEffectContentWrapper);
        invalidate();
    }

    public void closeEffectContent() {
//        if(mEffectContentWrapper == null) return;
//        mDrawingWrapper.removeView(mEffectContentWrapper);
//        mEffectContentWrapper.removeAllViews();
//        mEffectContentWrapper = null;
        if(mEffectContentWrapper == null) return;
        mEffectContentWrapper.removeAllViews();
        mEffectContentWrapper.setVisibility(View.INVISIBLE);
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

        private void setPaintView(PaintView pv) {
            mPaintView = pv;
        }

        private void setCurrentEffect(Effect effect) {
            mCurrentEffect = effect;
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

            /**
             * Pre-process of effect update
             */
            closeEffectContent();


            /**
             * Assigns a new effect
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
            } else if(newEffect instanceof IEraserEffectAll) {



            }
            else if(newEffect instanceof PaintEffects) {
                IPaintEffect paintEffect = (IPaintEffect) ((EffectGroup) newEffect).getSubEffects().get(0);
                drawingView.setPaint(paintEffect.getPaint());
                drawingView.setEditable(true);
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
                openEffectContent(newEffect);
            }
            else if(newEffect instanceof ISingleWallPaperEffect
                    || newEffect instanceof IColorWallPaperEffect || newEffect instanceof IMultipleWallPaperEffect) {
                openEffectContent(newEffect);
            } else if(newEffect instanceof NoneEffect) {

            }



            if(mOnEffectUpdateListener != null)
                mOnEffectUpdateListener.onEffectUpdated(PaintView.this, newEffect, mCurrentEffect);

            mCurrentEffect = newEffect;
        }
    }

    /**
     * This class is an instance of StickerCtrl
     */
    private class DefaultStickerCtrl implements StickerCtrl<StickerView> {

        private FrameLayout mStickerWrapper;

        private StickerView.OnFocusChangeListener mOnStickerFocusListener = new StickerView.OnFocusChangeListener() {
            @Override
            public void onFocused(StickerView view) {
                ArrayList<StickerView> views = getChildren(mStickerWrapper);
                for(int i=0; i<views.size(); i++) {
                    StickerView sv =  views.get(i);
                    if(sv != view)
                        sv.hideControl();
                }
            }
        };

        private StickerView.OnButtonClickListener mOnStickerButtonClickListener = new StickerView.OnButtonClickListener() {
            @Override
            public void onClick(StickerView view, int btn_code) {

                switch (btn_code) {
                    case StickerView.OnButtonClickListener.CONFIRM:
                        break;
                    case StickerView.OnButtonClickListener.REMOVE:
                        removeSticker(view);
                        break;
                    case StickerView.OnButtonClickListener.RESIZE:
                        //nothing to do
                        break;
                    case StickerView.OnButtonClickListener.ROTATE:
                        //nothing to do
                        break;
                    case StickerView.OnButtonClickListener.MOVE_UP:
                        moveUp(view);
                        break;
                    case StickerView.OnButtonClickListener.MOVE_DOWN:
                        moveDown(view);
                        break;
                }

            }
        };

        private DefaultStickerCtrl() {
            mStickerWrapper = new FrameLayout(getContext());
        }

        private ArrayList<StickerView> getChildren(ViewGroup parent) {
            if(parent == null) return null;

            ArrayList<StickerView> rtn = new ArrayList<>();

            int childCount = mStickerWrapper.getChildCount();
            for(int i=0; i<childCount; i++) {
                rtn.add((StickerView) mStickerWrapper.getChildAt(i));
            }

            return rtn;
        }

        private void setViews(ArrayList<StickerView> views) {
            if(mStickerWrapper == null) return;
            for(int i=0; i< views.size(); i++) {
                mStickerWrapper.addView(views.get(i));
            }
            mStickerWrapper.invalidate();
        }

        private void resetViews(ArrayList<StickerView> views) {
            mStickerWrapper.removeAllViews();
            setViews(views);
        }

        @Override
        public void setOnFocusChangeListener(StickerView.OnFocusChangeListener listener) {
            mOnStickerFocusListener = listener;
        }

        @Override
        public void setOnButtonClickListener(StickerView.OnButtonClickListener listener) {
            mOnStickerButtonClickListener = listener;
        }

        @Override
        public ViewGroup getStickerWrapper() {
            return mStickerWrapper;
        }

        @Override
        public void addSticker(StickerView v) {
            if(mStickerWrapper == null) return;
            v.setOnFocusChangeListener(mOnStickerFocusListener); //sets focus listener
            v.setOnButtonClickListener(mOnStickerButtonClickListener);
            mStickerWrapper.addView(v);
        }

        @Override
        public void removeSticker(StickerView v) {
            if(mStickerWrapper == null) return;
            mStickerWrapper.removeView(v);
        }

        @Override
        public void removeAllSticker() {
            if(mStickerWrapper == null) return;
            mStickerWrapper.removeAllViews();
        }

        @Override
        public void moveUp(StickerView child) {
            if(mStickerWrapper == null) return;
            int index = mStickerWrapper.indexOfChild(child);
            if(++index < mStickerWrapper.getChildCount())
            move(index, child);
        }

        @Override
        public void moveDown(StickerView child) {
            if(mStickerWrapper == null) return;
            int index = mStickerWrapper.indexOfChild(child);
            if(--index >= 0)
                move(index, child);
        }

        @Override
        public void moveTo(int index, StickerView child) {
            if(mStickerWrapper == null) return;
            move(index, child);
        }

        private void move(int index, StickerView v) {
            ArrayList<StickerView> childList = getChildren(mStickerWrapper);

            //swap views
            int _index = childList.indexOf(v);
            StickerView _v = childList.get(index);
            childList.set(index, v);
            childList.set(_index, _v);

            resetViews(childList);
        }
    }


}
