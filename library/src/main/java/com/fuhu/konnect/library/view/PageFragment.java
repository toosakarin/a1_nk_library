package com.fuhu.konnect.library.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.fuhu.konnect.library.Debug;

/**
 * Created by jacktseng on 2015/9/22.
 */
public class PageFragment extends DialogFragment {
    public static final String TAG = PageFragment.class.getSimpleName();

    public static final String FLAG_CURRENT_POSITION = "CURRENT_POSITION";

    private ViewPager mViewPager;
    private PagerAdapter mPageAdapter;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private OnCloseListener mOnPageCloseListener;

    public interface OnCloseListener {
        public void onFragmentClosed();
    }

    public void setAdapter(PagerAdapter adapter) {
        mPageAdapter = adapter;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    public void setOnPageCloseListener(OnCloseListener listener) {
        mOnPageCloseListener = listener;
    }

    public PagerAdapter getAdapter() {
        return mPageAdapter;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
//        attributes.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
//        getActivity().getWindow().setAttributes(attributes);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
//        attributes.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
//        getActivity().getWindow().setAttributes(attributes);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int currentPos = 0;
        Bundle bundle = getArguments();
        if(bundle != null)
            currentPos = bundle.getInt(FLAG_CURRENT_POSITION);

        Debug.dumpLog(PageFragment.TAG, "creating view with currentPos=" + currentPos);

        Context ctx = inflater.getContext();
        mViewPager = new ViewPager(ctx);
//            mPageAdapter = new DefaultPhotoPageAdapter(container.getContext(), mPhotoRawDataList);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setCurrentItem(currentPos);


        return mViewPager;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /**
         * it's a tips for how dialog show with fullscreen by adding RelativeLayout to dialog's root view
         */
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.argb(225, 0, 0, 0)));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        /**
         * Sets to hide the status bar, this command needs to after setting the size of dialog.
         * There is a tip for increasing user experience of hiding status bar, that is setting the
         * layout behind the status bar, and when hides status bar, we can avoid the animation of
         * resizing screen of hiding status bar.
         */
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);



        Debug.dumpLog(TAG, "creating dialog");

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        mViewPager.removeAllViews();
        if(mOnPageCloseListener != null)
            mOnPageCloseListener.onFragmentClosed();

        mOnPageChangeListener = null;
        mOnPageCloseListener = null;
    }
}
