package com.fuhu.konnect.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.fuhu.konnect.library.Debug;
import com.fuhu.konnect.library.image.ImageConfuser;
import com.fuhu.konnect.library.image.MapImageConfuser;
import com.fuhu.konnect.library.utility.GenerateIntID;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jacktseng on 2015/6/8.
 */
public class SubjectView extends RelativeLayout {

    public static final String TAG = SubjectView.class.getSimpleName();

    public static final int SUBJECT_COUNT_MAX = 10;
    public static final int CONTENT_SPAN_DEFAULT = 2;

    public static final int SUBJECT_ALIGN_LEFT      = 0;
    public static final int SUBJECT_ALIGN_TOP       = 1;
    public static final int SUBJECT_ALIGN_RIGHT     = 2;
    public static final int SUBJECT_ALIGN_BOTTOM    = 3;


    public static final int CONTENT_ORIENTATION_HORIZONTAL  = LinearLayoutManager.HORIZONTAL;
    public static final int CONTENT_ORIENTATION_VERTICAL    = LinearLayoutManager.VERTICAL;


    //scroll
    private FrameLayout mSubjectScrollView;

    //subject wrapper
    private LinearLayout mSubjectWrapper;
    //content wrapper
    private RecyclerView mContentWrapper;
    //subject list
    private ArrayList<View> mSubjectList;
    //content adapter
    private ContentAdapter mContentAdapter;
    //current subject
    private View mCurrentSubject;
    //subject record
    private HashMap<View, SubjectRecord> mSubjectRecordMap;
    //subject click listener
    private OnClickListener mOnClickListener;
    //
//    private ImageConfuser mImageConfuser;

    private int mSubjectAlign;

    private int mContentOrientation;

    /**
     * SubjectRecord is a private bean corresponding to subject which including hierarchy of layout and
     * content key of resource of sub menu.
     */
    private class SubjectRecord {
        View Subject;
        int Hierarchy;
        Object ContentKey;

        private SubjectRecord(View subject, int h, Object key) {
            Subject = subject;
            Hierarchy = h;
            ContentKey = key;
        }
    }

    private HashMap<Object, ContentSelector> mContentSelectorMap = new HashMap<Object, ContentSelector>();
    public static class ContentSelector {
        public Object Key;
        public Object Value;
        public int Hierarchy;

        public ContentSelector(Object key, Object value) {
            Key = key;
            Value = value;
        }

        private ContentSelector(int hierarchy, Object key, Object value) {
            Key = key;
            Value = value;
            Hierarchy = hierarchy;
        }
    }

    /**
     * The interface is invoked from adapter when subject view or content view to be clicked
     */
    public interface OnClickListener {
        public void onSubjectClick(View v);
        public void onContentClick(View v);
    }

    public SubjectView(Context context) {
        super(context);
        init();
    }

    public SubjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SubjectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mSubjectAlign = SUBJECT_ALIGN_LEFT;
        mContentOrientation = CONTENT_ORIENTATION_VERTICAL;

        mSubjectWrapper = new LinearLayout(getContext());
        mContentWrapper = new RecyclerView(getContext());
        mSubjectList =  new ArrayList<View>();
        mSubjectRecordMap = new HashMap<View, SubjectRecord>();
        mCurrentSubject = null;

        RecyclerView.LayoutManager lManager = new GridLayoutManager(getContext(), CONTENT_SPAN_DEFAULT);
        mContentWrapper.setLayoutManager(lManager);

        setSubjectAlign(SUBJECT_ALIGN_LEFT);

    }

    /**
     * Releases all resource of this view
     */
    public void release() {
        if(mSubjectScrollView != null)
            mSubjectScrollView.removeAllViews();
        mSubjectScrollView = null;
        if(mSubjectWrapper != null)
            mSubjectWrapper.removeAllViews();
        mSubjectWrapper = null;
        mCurrentSubject = null;
        if(mContentWrapper != null)
            mContentWrapper.removeAllViews();
        mContentWrapper = null;

        if(mContentAdapter != null)
            mContentAdapter.release();
        mContentAdapter = null;

        if(mSubjectList != null)
            mSubjectList.clear();
        if(mSubjectRecordMap != null)
            mSubjectRecordMap.clear();
        mOnClickListener = null;

        removeAllViews();
    }

    /**
     * Sets the layout align of subject of this view, the align has four places are left, top, right
     * , and bottom
     * @param align
     */
    public void setSubjectAlign(int align) {
        if(align != SUBJECT_ALIGN_LEFT && align != SUBJECT_ALIGN_TOP &&
                align != SUBJECT_ALIGN_RIGHT && align != SUBJECT_ALIGN_BOTTOM) return;

        Debug.dumpLog(TAG, "sets layout params for resetting align of subject toolbar");

        int subjectScrollerId = 0;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            subjectScrollerId = GenerateIntID.generateViewId();
        else
            subjectScrollerId = View.generateViewId();

        FrameLayout subjectScroller = null;
        ScrollView.LayoutParams subjectWrapperLp = null;
        RelativeLayout.LayoutParams subjectScrollerLp = null;
        RelativeLayout.LayoutParams contentWrapperLp = null;
        switch (align) {
            case SUBJECT_ALIGN_LEFT:
                subjectScroller = new ScrollView(getContext());
                mSubjectWrapper.setOrientation(LinearLayout.VERTICAL);
                subjectWrapperLp = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.MATCH_PARENT);
                subjectScrollerLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                subjectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                contentWrapperLp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                contentWrapperLp.addRule(RIGHT_OF, subjectScrollerId);
                break;
            case SUBJECT_ALIGN_TOP:
                subjectScroller = new HorizontalScrollView(getContext());
                mSubjectWrapper.setOrientation(LinearLayout.HORIZONTAL);
                subjectWrapperLp = new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT);
                subjectScrollerLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                subjectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                subjectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                contentWrapperLp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                contentWrapperLp.addRule(RelativeLayout.BELOW, subjectScrollerId);
                break;
            case SUBJECT_ALIGN_RIGHT:
                subjectScroller = new ScrollView(getContext());
                mSubjectWrapper.setOrientation(LinearLayout.VERTICAL);
                subjectWrapperLp = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.MATCH_PARENT);
                subjectScrollerLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                subjectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                contentWrapperLp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                contentWrapperLp.addRule(LEFT_OF, subjectScrollerId);
                break;
            case SUBJECT_ALIGN_BOTTOM:
                subjectScroller = new HorizontalScrollView(getContext());
                mSubjectWrapper.setOrientation(LinearLayout.HORIZONTAL);
                subjectWrapperLp = new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT);
                subjectScrollerLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                subjectScrollerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                contentWrapperLp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                contentWrapperLp.addRule(ABOVE, subjectScrollerId);
                break;
        }

        do {
            if(subjectScroller == null) break;
            if(subjectWrapperLp == null) break;
            if(subjectScrollerLp == null) break;
            if(contentWrapperLp == null) break;

            //release
            if(mSubjectScrollView != null)
                mSubjectScrollView.removeAllViews();

            //adjust layout
            mSubjectScrollView = subjectScroller;
            mSubjectScrollView.setId(subjectScrollerId);
            mSubjectWrapper.setLayoutParams(subjectWrapperLp);
            mSubjectScrollView.addView(mSubjectWrapper);
            mSubjectScrollView.setLayoutParams(subjectScrollerLp);
            this.addView(mSubjectScrollView);
            mContentWrapper.setLayoutParams(contentWrapperLp);
            if(mContentWrapper.getParent() == null)
                this.addView(mContentWrapper);

            mSubjectAlign = align;
            this.invalidate();

            Debug.dumpLog(TAG, "resets subject toolbar success");
        } while(false);


    }

    /**
     * Sets the content spans of grid count of this view
     * @param span
     */
    public void setContentSpan(int span) {
        if(span < 1) return;
        if(mContentWrapper.getLayoutManager() == null) return;

        RecyclerView.LayoutManager lManager = mContentWrapper.getLayoutManager();
        if(lManager instanceof GridLayoutManager) {
            ((GridLayoutManager) lManager).setSpanCount(span);
            mContentWrapper.invalidate();
        }
    }

    /**
     * Sets the orientation of content display of this view
     * @param orientation
     */
    public void setContentOrientation(int orientation) {
        if(orientation != CONTENT_ORIENTATION_HORIZONTAL &&
                orientation != CONTENT_ORIENTATION_VERTICAL) return;
        if(mContentWrapper.getLayoutManager() == null) return;

        RecyclerView.LayoutManager lManager = mContentWrapper.getLayoutManager();
        if(lManager instanceof GridLayoutManager) {
            ((GridLayoutManager) lManager).setOrientation(orientation);
            mContentOrientation = orientation;
            mContentWrapper.invalidate();
        }
    }

    /**
     * Sets the adapter of ContentAdapter to this view for displaying contents
     * @param adapter
     */
    public void setAdapter(ContentAdapter adapter) {
        mContentAdapter = adapter;
        mContentAdapter.mSubjectView = this;
        mContentWrapper.setAdapter(mContentAdapter);

        /**
         * update avatar by confusing all subject's image
         */
        Debug.dumpLog(TAG, "adapter is confusing the select content because a new adapter is assigned");
        mContentAdapter.confuseSelectContent();
    }

    /**
     * Sets the OnClickListener to listen the event of both subject and content views
     * @param listener
     */
    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    /**
     * Adds a new subject view to the subject toolbar of this view
     * @param subject
     */
    public void addSubject(View subject) {
        if(subject == null) return;
        if(mSubjectList.size() >= SUBJECT_COUNT_MAX) return;

        /**
         * Assigns the OnClickListener for changing content
         */
        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentSubject = v;
                if (mContentAdapter != null) {
                    mContentAdapter.onSubjectChanged(v);
                    mContentAdapter.notifyDataSetChanged();
                }

                if(mOnClickListener != null)
                    mOnClickListener.onSubjectClick(v);
            }
        });

        mSubjectList.add(subject);
        mSubjectWrapper.addView(subject);

        /**
         * for first adding subject
         */
        if(mCurrentSubject == null)
            mCurrentSubject = subject;
    }

    /**
     * Returns the wrapper of subject toolbar of this view
     * @return
     */
    public ViewGroup getSubjectWrapper() {
        return mSubjectWrapper;
    }

    /**
     * Returns the wrapper of contents of grid of this view
     * @return
     */
    public RecyclerView getContentWrapper() {
        return mContentWrapper;
    }

    /**
     * Returns the align of subject of this view
     * @return
     */
    public int getSubjectAlign() {
        return mSubjectAlign;
    }

    /**
     * Returns the orientation of content display of this view
     * @return
     */
    public int getContentOrientation() {
        return mContentOrientation;
    }

    /**
     * Returns a currently used subject
     * @return
     */
    public View getCurrentSubject() {
        return mCurrentSubject;
    }

    /**
     * Adds the content selector which is a key pair with given key of the subject and corresponding
     * value of the subject. That is indicating which content of this subject is selected. In
     * addition, the hierarchy represents the z-order of layout of the content of this subject
     * @param hierarchy
     * @param key
     * @param value
     */
    public void addContentSelector(int hierarchy, Object key, Object value) {
        if(!mContentSelectorMap.containsKey(key))
            mContentSelectorMap.put(key, new ContentSelector(hierarchy, key, value));
    }

    /**
     * Removes the subject view of subject toolbar of this view
     * @param subject
     */
    public void removeSubject(View subject) {
        if(subject == null) return;
        mSubjectList.remove(subject);
        mSubjectRecordMap.remove(subject);
        mSubjectWrapper.removeView(subject);
    }


    /**
     *
     * @param <VH>
     */
    public abstract static class ContentAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

        private SubjectView mSubjectView;

        private ImageConfuser mImageConfuser;

        public abstract VH onCreateViewHolder(ViewGroup viewGroup, int viewType, View currentSubject);

//        public abstract Object onBindViewHolder(VH holder, View currentSubject, int position);
        public abstract ContentSelector onBindViewHolder(VH holder, View currentSubject, int position);

//        public abstract Bitmap getSelectedContentBitmap(Object key);
        public abstract Bitmap getSelectedContentBitmap(Object key, Object value);

//        public abstract void onContentConfused(Object[] keys, Bitmap confusedImage);
        public abstract void onContentConfused(ArrayList<ContentSelector> selectors, Bitmap confusedImage);

        //jack@150617
        public abstract void onSubjectChanged(View subject);

        public void release() {
            mSubjectView = null;
            if(mImageConfuser != null) {
                mImageConfuser.recycle();
                mImageConfuser.clear();
            }
        }

        public void setConfuse(int w, int h) {
            if(w <= 0 || h <= 0) return;
            setImageConfuser(new MapImageConfuser(w, h));
        }

        public void setImageConfuser(ImageConfuser confuser) {
            mImageConfuser = confuser;
//            if(mSubjectView != null)
//                confuseSelectContent();
            notifyConfuseChanged();
        }

        public void setConfuseAutoRecyclable(boolean isAuto) {
            if(mImageConfuser != null)
                mImageConfuser.setRecycleAfterConfuse(isAuto);
        }

        public void notifyConfuseChanged() {
            if(mSubjectView != null)
                confuseSelectContent();
        }


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return onCreateViewHolder(parent, viewType, mSubjectView.mCurrentSubject);
        }

        //jack150623
        @Override
        public void onBindViewHolder(VH holder, int position) {
            final ContentSelector contentSelector = onBindViewHolder(holder, mSubjectView.mCurrentSubject, position);

//            if(contentSelector != null && mSubjectView != null) {
            if(mSubjectView != null) {

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mSubjectView == null) return;

                        /**
                         * update select key content
                         */
                        if(contentSelector != null) {
                            ContentSelector _contentSelector = mSubjectView.mContentSelectorMap.get(contentSelector.Key);
                            _contentSelector.Value = contentSelector.Value;
                        }

                        /**
                         * confusing select contents
                         */
                        Debug.dumpLog(TAG, "starts to confuse the content of current subject ");
                        confuseSelectContent();

                        if(mSubjectView.mOnClickListener != null)
                            mSubjectView.mOnClickListener.onContentClick(v);
                    }
                });
            }
        }

        //jack@150623
        private void confuseSelectContent() {
            if(mImageConfuser == null) return;

            Bitmap rtnBitmap = null;

            ImageConfuser confuser = mImageConfuser;
            ArrayList<ContentSelector> selectorList = new ArrayList<ContentSelector>(mSubjectView.mContentSelectorMap.values());

            for (int i = 0; i < selectorList.size(); i++) {
                ContentSelector selector = selectorList.get(i);
                Bitmap image = getSelectedContentBitmap(selector.Key, selector.Value);
                if(image != null)
                    confuser.setImage(selector.Hierarchy, image);
            }

            rtnBitmap = confuser.confuse();
            confuser.clear();

            onContentConfused(selectorList, rtnBitmap);
        }
    }



}
