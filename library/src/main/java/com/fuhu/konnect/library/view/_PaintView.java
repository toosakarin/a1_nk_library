package com.fuhu.konnect.library.view;

/**
 * Created by jacktseng on 2015/8/20.
 */
//public class _PaintView extends RelativeLayout {
//
//    public static final String TAG = _PaintView.class.getSimpleName();
//
//    //Layout of user interface
//
//    private DrawingView mDrawingView;
//
//    /**
//     * The tool bar is a list to show the effect of drawing email
//     */
//    private ScrollView mMainEffectScroller;
//
//    private ScrollView mSubEffectListScroller;
//
//    private LinearLayout mMainEffectWrapper;
//
////    private RecyclerView mContentListWrapper;
//    private LinearLayout mSubEffectWrapper;
//
//    private RecyclerView mContentGridWrapper;
//
//    //Date source
//    private ArrayList<EffectView> mMainEffectViewList;
//
//    /**
//     * This is a controller for effect updating
//     */
//    private OnEffectUpdateListener mOnEffectUpdateListener;
//
//    private EffectCtrl mEffectCtrl;
//
//    private Adapter mAdapter;
//
//
//
//
//    //quick mapping table
////    private HashMap<View, ArrayList<View>> mContentTable;
//
//
//
//    public _PaintView(Context context) {
//        super(context);
//        init();
//    }
//
//    public _PaintView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public _PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    private void init() {
//        Context ctx = getContext();
//        if(ctx == null) {
//            Log.e(TAG, "can't be initialize");
//            return;
//        }
//
//        mMainEffectViewList = new ArrayList<>();
//
//        mDrawingView = new DrawingView(ctx);
//        mMainEffectScroller = new ScrollView(ctx);
//        mMainEffectWrapper = new LinearLayout(ctx);
//        mSubEffectListScroller = new ScrollView(ctx);
//        mSubEffectWrapper = new LinearLayout(ctx);
//
//
//        int subjectScrollerId = 0;
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
//            subjectScrollerId = GenerateIntID.generateViewId();
//        else
//            subjectScrollerId = View.generateViewId();
//        mMainEffectScroller.setId(subjectScrollerId);
//
//        /**
//         * Sets layout params
//         */
//
//        //Adds main effect wrapper into scroller
//        ScrollView.LayoutParams mainEffectWrapperLp = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.MATCH_PARENT);
//        mMainEffectScroller.addView(mMainEffectWrapper, mainEffectWrapperLp);
//        //Sets location of main effect bar
//        LayoutParams mainEffectScrollerLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//        mainEffectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        this.addView(mMainEffectScroller, mainEffectScrollerLp);
//
//        //Adds sub effect wrapper into scroller
//        ScrollView.LayoutParams subEffectWrapperLp = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.MATCH_PARENT);
//        mMainEffectScroller.addView(mSubEffectWrapper, subEffectWrapperLp);
//        //Sets the location of sub effect list wrapper is same as subject wrapper
//        LayoutParams subEffectScrollerLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        mainEffectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        this.addView(mSubEffectListScroller, subEffectScrollerLp);
//
//
//        LayoutParams drawingViewLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//        drawingViewLp.addRule(RIGHT_OF, subjectScrollerId);
//        this.addView(mDrawingView, drawingViewLp);
//
//        /**
//         * for testing
//         */
//        mMainEffectScroller.setBackgroundColor(Color.BLUE);
//        mDrawingView.setBackgroundColor(Color.RED);
//
//
//        this.invalidate();
//    }
//
//    public void setAdapter(Adapter adapter) {
//        mAdapter = adapter;
//    }
//
//    public void setEffectCtl(EffectCtrl ctrl) {
//        mEffectCtrl = ctrl;
//    }
//
//    public void addMainEffect(EffectView view) {
//
//
//
//        mMainEffectViewList.add(view);
//    }
//
//    private void setViewClick(View v) {
//        v.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if(event.getAction() == MotionEvent.ACTION_UP) {
//                    Effect newEffect = null;
//                    if(v instanceof EffectView)
//                        newEffect = ((EffectView) v).getEffect();
//
//                    /**
//                     * update
//                     */
//
//
//                    if(mEffectCtrl != null)
//                        mEffectCtrl.applyEffect(null, newEffect);
//                }
//
//                return false;
//            }
//        });
//    }
//
//    private void updateSubEffect(Effect mainEffect) {
//        if(mainEffect == null) return;
//        if(mSubEffectWrapper == null) return;
//
//        //clean
//        mSubEffectWrapper.removeAllViews();
//
//        ArrayList<Effect> subEffects = mainEffect.getSubEffects();
//        for(int i=0; i<subEffects.size(); i++) {
//            Effect effect = subEffects.get(i);
//            EffectView ev = mAdapter.getSubEffectView(effect);
//            setViewClick(ev);
//            mSubEffectWrapper.addView(ev);
//        }
//
//
//    }
//
//    public interface Adapter {
//        public EffectView getMainEffectView(Effect effect);
//        public EffectView getSubEffectView(Effect effect);
//    }
//}
