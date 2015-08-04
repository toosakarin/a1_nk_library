package com.fuhu.konnect.library.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fuhu.konnect.library.Debug;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/5/26.
 */
public class PhotoView extends RecyclerView {

    public static final String TAG = PhotoView.class.getSimpleName();

    public static final int DISPLAY_MODE_LIST           = 0;
    public static final int DISPLAY_MODE_PAGE           = 1;
    public static final int DISPLAY_LIST_COLUMN_DEFAULT = 2;

    private int mDisplayMode = DISPLAY_MODE_LIST;

    private boolean mIsDisplayChangeable = true;

    private LayoutManager mLayoutManager;

    private int mPhotoListSpanCount = DISPLAY_LIST_COLUMN_DEFAULT;

    /**
     *
     */
    private PhotoPageFragment mPageFragment;

    /**
     *
     */
    private PagerAdapter mPhotoPageAdapter;

    private OnPhotoPageChangeListener mOnPhotoPageChangeListener;

    private OnPhotoListScrollListener mOnPhotoListScrollListener;


    public PhotoView(Context context) {
        super(context);
        init();
    }

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhotoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        /**
         * Important: let view get focus is necessary for catching onKeyDown event in View
         */
        requestFocus();
        setFocusableInTouchMode(true);

        //set default to staggered list
        displayPhotoList(mPhotoListSpanCount);
    }

    public boolean isDisplayChangeable() {
        return mIsDisplayChangeable;
    }

    public void setDisplayChangeable(boolean changeable) {
        mIsDisplayChangeable = changeable;
    }

    @Deprecated
    @Override
    public void setAdapter(Adapter adapter) {
        if(adapter instanceof PhotoAdapter)
            setAdapter((PhotoAdapter) adapter);
    }

    /**
     * The adapter extends RecycleView.Adapter for RecycleView using at list display.
     *
     * @param adapter
     */
    public void setAdapter(PhotoAdapter adapter) {
        super.setAdapter(adapter);
        adapter.bindPhotoViewView(this);
    }

    /**
     * The adapter is a PageAdapter for ViewPager using at page display.
     *
     * @param adapter
     */
    public void setPhotoPageAdapter(PagerAdapter adapter) {
        mPhotoPageAdapter = adapter;
    }

    public PagerAdapter getPhotoPageAdapter() {
        return mPhotoPageAdapter;
    }

    public void setOnPhotoListScrollListener(OnPhotoListScrollListener listener) {
        mOnPhotoListScrollListener = listener;
    }

    public void setOnPhotoPageChangeListener(OnPhotoPageChangeListener listener) {
        mOnPhotoPageChangeListener = listener;
    }

    public ViewPager getPhotoViewPager() {
        return (mPageFragment == null) ? null : mPageFragment.mViewPager;
    }

    public void setPhotoListSpanCount(int spanCount) {
        mPhotoListSpanCount = spanCount;
        displayPhotoList(mPhotoListSpanCount);
    }

    public int getPhotoListSpanCount() {
        return mPhotoListSpanCount;
    }

    public void displayPhotoList(int column) {
        if(!mIsDisplayChangeable)
            return;
        if(column < 1)
            column = DISPLAY_LIST_COLUMN_DEFAULT;

        mLayoutManager = new StaggeredGridLayoutManager(column, OrientationHelper.VERTICAL);
        super.setLayoutManager(mLayoutManager);

        super.setOnScrollListener(new RecyclerView.OnScrollListener() {

            /**
             * IS_SCROLLED is a flag indicating that views of RecycleView are never be scrolled.
             * Once IS_SCROLLED is false, it means we have the event of scroll on top or bottom.
             */
            private boolean IS_SCROLLED = false;

            private int LAST_SCROLL_DY;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Debug.dumpLog(TAG, "onScrolled()-> dx=" + dx + ", dy=" +dy);

                IS_SCROLLED = true;
                LAST_SCROLL_DY = dy;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Debug.dumpLog(TAG, "onScrollStateChanged()-> state=" + newState);

                if(newState == RecyclerView.SCROLL_STATE_IDLE) {

                    DispatchEvent:
                    do {
                        /**
                         * check for scrolling
                         */
                        if(IS_SCROLLED) {
                            if(mOnPhotoListScrollListener != null)
                                mOnPhotoListScrollListener.onScroll();
                            break;
                        }

                        StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();

                        if(LAST_SCROLL_DY <= 0) {
                            /**
                             * check for onTop
                             */
                            int[] views = lm.findFirstVisibleItemPositions(null);
                            if(Debug.IS_DEBUG)
                                for(int i=0; i<views.length; i++)
                                    Log.d(TAG, "view[" + i + "] = " + views[i] );

                            for(int i=0; i<views.length; i++)
                                if(0 == views[i]) {
                                    Log.e(TAG, "onTop");
                                    if(mOnPhotoListScrollListener != null)
                                        mOnPhotoListScrollListener.onTop();
                                    break DispatchEvent;
                                }
                        }
                        else if(LAST_SCROLL_DY > 0) {
                            /**
                             * check for onBottom
                             */
                            int lastViewIndex = recyclerView.getAdapter().getItemCount() - 1;
                            int[] views = lm.findLastVisibleItemPositions(null);
                            if(Debug.IS_DEBUG)
                                for(int i=0; i<views.length; i++)
                                    Log.d(TAG, "view[" + i + "] = " + views[i] );

                            for(int i=0; i<views.length; i++)
                                if(lastViewIndex == views[i]) {
                                    Log.e(TAG, "onBottom");
                                    if(mOnPhotoListScrollListener != null)
                                        mOnPhotoListScrollListener.onBottom();
                                    break DispatchEvent;
                                }
                        }

                    } while(false);

                    IS_SCROLLED = false;
                }
            }

        });

        mPageFragment = null;
        mDisplayMode = DISPLAY_MODE_LIST;
    }

    /**
     * Set photo to page display with special position of photos.`
     * The function is using Dialog with DialogFragment to show fullscreen.
     *
     * @param currentPos Position of photos which you want to show at first.
     */
    public void displayPhotoPage(int currentPos) {
        if(!mIsDisplayChangeable)
            return;

        if(mPhotoPageAdapter == null) {
            Log.w(TAG, "can't change to photo page display cause PageAdapter is null");
            return;
        }

        try {
            Activity activity = (Activity) getContext();
            FragmentManager fragmentManager = activity.getFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putInt(PhotoPageFragment.FLAG_CURRENT_POSITION, currentPos);

            PhotoPageFragment ppfragment = new PhotoPageFragment();
            ppfragment.setArguments(bundle);
            ppfragment.setAdapter(mPhotoPageAdapter);
            ppfragment.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if(mOnPhotoPageChangeListener != null)
                        mOnPhotoPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    if(mOnPhotoPageChangeListener != null)
                        mOnPhotoPageChangeListener.onPageSelected(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if(mOnPhotoPageChangeListener != null)
                        mOnPhotoPageChangeListener.onPageScrollStateChanged(state);
                }
            });
            ppfragment.setOnPageCloseListener(new OnPhotoPageCloseListener() {
                @Override
                public void onFragmentClosed() {
                    mPageFragment = null;
                    mDisplayMode = DISPLAY_MODE_LIST;
                }
            });

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.add(ppfragment, null);
            transaction.commit();

            mPageFragment = ppfragment;
            mDisplayMode = DISPLAY_MODE_PAGE;
            Log.i(TAG, "changing to photo page display...");
        } catch(ClassCastException e) {
            Log.e(TAG, "create fragment failed because can't get fragment manager from current context! (this context dose not an activity)");
            e.printStackTrace();
        }

    }

    /**
     * Catching the back key for controlling page display back to list display.
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Debug.dumpLog(TAG, "onKeyDown()-> keyCode=" + keyCode + ", " + event.toString());
//
//        if(mDisplayMode == DISPLAY_MODE_PAGE) {
//            switch(keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    displayPhotoList(mPhotoListSpanCount);
//                    return true;
//            }
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * PhotoAdapter implements a basic architecture for photo showing and display changing in PhotoView.
     * For PhotoView, user need to extend PhotoAdapter to customize the view of user's application just
     * like another basic adapter of Android.
     *
     * @param <VH> VH is extends PhotoHolder which to keep the photo of ImageView by user Customized.
     */
    public static abstract class PhotoAdapter <VH extends PhotoHolder> extends Adapter<VH> {

        private PhotoView mPhotoView;

        public abstract VH onCreatePhotoHolder(ViewGroup viewGroup, int viewType);

        public abstract void onBindPhotoHolder(VH holder, int position);

        public abstract void onPhotoClick(ImageView photo, VH holder);

        private void bindPhotoViewView(PhotoView view) {
            mPhotoView = view;
            Debug.dumpLog(TAG, "bind PhotoView success (" + view.toString() + ")");
        }

        @Override
        public VH onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            VH holder = onCreatePhotoHolder(viewGroup, viewType);

            if(holder != null) {

                if(holder.mPhoto != null) {
                    holder.mPhoto.setTag(holder);
                    holder.mPhoto.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mPhotoView != null) {
                                if (mPhotoView.mDisplayMode == DISPLAY_MODE_LIST) {
                                    /**
                                     * changing photos to page display
                                     */
                                    if (mPhotoView.isDisplayChangeable()) {
                                        if(view.getTag() instanceof PhotoHolder) {
                                            VH _holder = (VH) view.getTag();
                                            mPhotoView.displayPhotoPage(_holder.Position);
                                        }
                                    }

                                    /**
                                     * pass onClick event of image of photo to subclass
                                     */
                                    if (view.getTag() instanceof PhotoHolder) {
                                        VH _holder = (VH) view.getTag();
                                        onPhotoClick((ImageView) view, _holder);
                                    }
                                }
                            }
                        }
                    });
                }
                else
                    Log.e(TAG, "can't create holder cause the view of photo in holder is null!");
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            onBindPhotoHolder(holder, position);

            holder.setPosition(position);
        }
    }

    /**
     * This holder is a basic holder which just keep an ImageView of photo
     * with its corresponding position of photos.
     */
    public static abstract class PhotoHolder extends ViewHolder {
        int Position;
        ImageView mPhoto;

        public PhotoHolder(View itemView, ImageView photo) {
            super(itemView);
            mPhoto = photo;
        }

        public void setPhoto(ImageView photo) {
            mPhoto = photo;
        }

        public void setPosition(int pos) {
            Position = pos;
        }

        public ImageView getPhoto() {
            return mPhoto;
        }

        public int getCorrespondPosition() { return Position; }
    }

    /**
     *
     */
    public static class PhotoPageFragment extends DialogFragment {
        public static final String TAG = PhotoPageFragment.class.getSimpleName();

        public static final String FLAG_CURRENT_POSITION = "CURRENT_POSITION";

        private ViewPager mViewPager;
        private PagerAdapter mPageAdapter;
        private ViewPager.OnPageChangeListener mOnPageChangeListener;
        private OnPhotoPageCloseListener mOnPageCloseListener;

        public void setAdapter(PagerAdapter adapter) {
            mPageAdapter = adapter;
        }

        public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
            mOnPageChangeListener = listener;
        }

        public void setOnPageCloseListener(OnPhotoPageCloseListener listener) {
            mOnPageCloseListener = listener;
        }

        public PagerAdapter getAdapter() {
            return mPageAdapter;
        }

        public ViewPager getViewPager() {
            return mViewPager;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int currentPos = 0;
            Bundle bundle = getArguments();
            if(bundle != null)
                currentPos = bundle.getInt(FLAG_CURRENT_POSITION);

            Debug.dumpLog(PhotoPageFragment.TAG, "currentPos=" + currentPos);

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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(225, 0,0,0)));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);

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

    public static class DefaultPhotoPageAdapter extends PagerAdapter {
        public static final String TAG = DefaultPhotoPageAdapter.class.getSimpleName();

        private ArrayList<Bitmap> mRawDataList;
        private ArrayList<View> mViews;

        public DefaultPhotoPageAdapter(Context ctx, ArrayList<Bitmap> photos) {
            mRawDataList = photos;
            mViews = new ArrayList<View>();

            for(int i=0; i<photos.size(); i++) {
                Bitmap src = photos.get(i);
                ImageView view = new ImageView(ctx);
                view.setImageBitmap(src);
                mViews.add(view);
            }
        }

        public ArrayList<Bitmap> getPhotos() {
            return mRawDataList;
        }

        public Bitmap getPhoto(int position) {
            return mRawDataList.get(position);
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Debug.dumpLog(TAG, "instantiateItem at pos " + position);

            View v = null;
            try {
                v = mViews.get(position);
                container.addView(v, 0);
            } catch(IllegalStateException e) {
                e.printStackTrace();
            }

            return v;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Debug.dumpLog(TAG, "destroyItem at pos " + position);

            View v = mViews.get(position);
            container.removeView(v);
        }
    }

    public interface OnPhotoListScrollListener {
        public void onTop();
        public void onBottom();
        public void onScroll();
    }

    /**
     * This interface is a preparatory callback for executing some process before pop to other class
     * which use PhotoView when PageAdapter touching, and now is nothing to do.
     */
    public interface OnPhotoPageChangeListener extends ViewPager.OnPageChangeListener{}

    public interface OnPhotoPageCloseListener {
        public void onFragmentClosed();
    }

}
