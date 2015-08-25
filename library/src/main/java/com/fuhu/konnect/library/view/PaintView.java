package com.fuhu.konnect.library.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.fuhu.konnect.library.mail.Effect;
import com.fuhu.konnect.library.mail.EffectCtl;
import com.fuhu.konnect.library.mail.OnEffectUpdateListener;
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
     * The tool bar is a list to show the effect of drawing email
     */
    private ScrollView mSubjectScrollWrapper;

    private LinearLayout mSubjectWrapper;

    private RecyclerView mContentListWrapper;

    private RecyclerView mContentGridWrapper;

    //Date source
    private ArrayList<View> mSubjectList;

    /**
     * This is a controller for effect updating
     */
    private OnEffectUpdateListener mOnEffectUpdateListener;





    //quick mapping table
//    private HashMap<View, ArrayList<View>> mContentTable;



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

        mDrawingView = new DrawingView(ctx);
        mSubjectScrollWrapper = new ScrollView(ctx);
        mSubjectWrapper = new LinearLayout(ctx);
        mContentListWrapper = new RecyclerView(ctx);


        RecyclerView.LayoutManager lManager = new LinearLayoutManager(ctx);
        mContentListWrapper.setLayoutManager(lManager);


        int subjectScrollerId = 0;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            subjectScrollerId = GenerateIntID.generateViewId();
        else
            subjectScrollerId = View.generateViewId();
        mSubjectScrollWrapper.setId(subjectScrollerId);

        /**
         * Sets layout params
         */
        ScrollView.LayoutParams subjectWrapperLp = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.MATCH_PARENT);
        mSubjectScrollWrapper.addView(mSubjectWrapper, subjectWrapperLp);

        RelativeLayout.LayoutParams subjectScrollerLp = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        subjectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        this.addView(mSubjectScrollWrapper, subjectScrollerLp);

        //The location of content list wrapper is same as subject wrapper
        RelativeLayout.LayoutParams contentListWrapperLp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        subjectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        this.addView(mContentListWrapper, contentListWrapperLp);

        RelativeLayout.LayoutParams drawingViewLp = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        drawingViewLp.addRule(RIGHT_OF, subjectScrollerId);
        this.addView(mDrawingView, drawingViewLp);

        /**
         * for testing
         */
        mSubjectScrollWrapper.setBackgroundColor(Color.BLUE);
        mDrawingView.setBackgroundColor(Color.RED);


        this.invalidate();
    }

    public void setEffectCtl(EffectCtl ctl) {

    }

    public void addMainEffect(View view, Effect effect) {

    }
}
