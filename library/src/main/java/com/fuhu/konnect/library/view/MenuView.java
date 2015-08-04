package com.fuhu.konnect.library.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by jacktseng on 2015/6/5.
 */
public class MenuView extends RelativeLayout {
    public static final String TAG = MenuView.class.getSimpleName();

    private static int DEFAULT_CONTENT_SPAN_COUNT = 2;
    private static int DEFAULT_MENU_WIDTH_RATIO = 1;
    private static int DEFAULT_CONTENT_WIDTH_RATIO = 4;

    private int mMenuWidthRatio = DEFAULT_MENU_WIDTH_RATIO;
    private int mContentWidthRatio = DEFAULT_CONTENT_WIDTH_RATIO;

    private RecyclerView mMenuRecycleView;
    private RecyclerView mContentRecycleView;

    private RecyclerView.LayoutManager mMenuLayoutManager;
    private RecyclerView.LayoutManager mContentLayoutManager;

    private RecyclerView.Adapter mMenuAdapter;
    private RecyclerView.Adapter mContentAdapter;


    public MenuView(Context context) {
        super(context);
        init();
    }

    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Context ctx = getContext();
        mMenuRecycleView = new RecyclerView(ctx);
        mContentRecycleView = new RecyclerView(ctx);
        mMenuLayoutManager = new LinearLayoutManager(ctx);
        mContentLayoutManager = new GridLayoutManager(ctx, DEFAULT_CONTENT_SPAN_COUNT);

        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        double wMenuRatio = Math.rint(((double) mMenuWidthRatio / (double)(mMenuWidthRatio + mContentWidthRatio)));
        double wContentRatio = 1 - wMenuRatio;

        RelativeLayout.LayoutParams lpContent = new LayoutParams((int) (w*wContentRatio), h);
        RelativeLayout.LayoutParams lpMenu = new LayoutParams((int) (w*wMenuRatio), h);
        lpMenu.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpContent.addRule(RelativeLayout.RIGHT_OF, mMenuRecycleView.getId());

        mMenuRecycleView.setLayoutParams(lpMenu);
        mContentRecycleView.setLayoutParams(lpContent);

    }

    private void release() {
        mMenuRecycleView.removeAllViews();
        mContentRecycleView.removeAllViews();
    }




}
