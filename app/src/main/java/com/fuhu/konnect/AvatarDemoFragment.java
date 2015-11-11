package com.fuhu.konnect;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fuhu.konnect.library.utility.GenerateIntID;
import com.fuhu.konnect.library.view.SubjectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by jacktseng on 2015/6/8.
 */
public class AvatarDemoFragment extends Fragment {

    public static final String TAG = AvatarDemoFragment.class.getSimpleName();

    public static final int AVATAR_WIDTH = 600;

    public static final int AVATAR_HEIGHT = 600;

    public static final int SUBJECT_BUTTON_WIDTH = 200;

    public static int SUBJECT_BUTTON_HEIGHT;

    public static final int[] SUBJECT_SELECTORS =
            {
                R.drawable.selector_blue,
                R.drawable.selector_red,
                R.drawable.selector_purple,
                R.drawable.selector_green,
                R.drawable.selector_yello,
                R.drawable.selector_black
            };

    public static final  int[][]  CONTENT_COLORS =
            {
                    {Color.parseColor("#449def"), Color.parseColor("#2f6699")},
                    {Color.parseColor("#ef4444"), Color.parseColor("#992f2f")},
                    {Color.parseColor("#a276eb"), Color.parseColor("#6a3ab2")},
                    {Color.parseColor("#70c656"), Color.parseColor("#53933f")},
                    {Color.parseColor("#f3ae1b"), Color.parseColor("#bb6008")},
                    {Color.parseColor("#343434"), Color.parseColor("#171717")}
            };

    public static final int SUBJECT_COUNT_MAX = 10;

    public static final int SUBJECT_COUNT_DEFAULT = 3;

    public static final int CONTENT_COUNT_MAX = 10;

    public static final int CONTENT_COUNT_DEFAULT = 6;

    private int mCurrentSelectorIndex = -1;

    private View mRootView;

    private SubjectView mSubjectView;

    private ImageView mAvatarView;

    private MyAdapter mContentAdapter;

    private ArrayList<View> mSubjectList = new ArrayList<View>();
    private HashMap<View, ArrayList<ContentItem>> mContentItemMap = new HashMap<View, ArrayList<ContentItem>>();

    //jack@150623
    private ArrayList<ArrayList<Object>> mKeyList = new ArrayList<ArrayList<Object>>();
    private int mLevelCount = 1;

    /**
     * A item class which is the content of subject, and it consist of location, select status
     * and corresponding resource
     */
    class ContentItem {
        public static final int ITEM_RADIUS = AVATAR_WIDTH / 10;

//        Bitmap Icon;
        Point Location;
        int C1;
        int C2;

        int ResourceId;
//        boolean IsSelected = false;
        Object KeySelect;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.frag_avatar_solid, container, false);
        mAvatarView = (ImageView) mRootView.findViewById(R.id.avatar_solid_iv_Avatar);
        mSubjectView = (SubjectView) mRootView.findViewById(R.id.avatar_solid_slv_SolidMenuView);

        /**
         * computing subject button height
         */
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        SUBJECT_BUTTON_HEIGHT = (dm.heightPixels-45) / 5;

        doAvatar();

        return mRootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        /**
         * release resource of bitmap of content item
         */
//        ArrayList<ArrayList<ContentItem>> contentItemList = new ArrayList<ArrayList<ContentItem>>(mContentItemMap.values());
//        for(ArrayList<ContentItem> itemList : contentItemList) {
//            for(int i=0; i<itemList.size(); i++) {
//                Bitmap bmp = itemList.get(i).Icon;
//                if(bmp != null)
//                    bmp.recycle();
//            }
//            itemList.clear();
//        }
//        contentItemList.clear();

        ((ViewGroup) mRootView).removeAllViews();
        mRootView = null;
        mAvatarView = null;
        mSubjectView.removeAllViews();
        mSubjectView.release();
        mSubjectView = null;
        mSubjectList.clear();
        mContentItemMap.clear();
        mContentAdapter.release();
        mContentAdapter = null;

        System.gc();
    }

    public void doAvatar() {
//        mSubjectView.setSubjectAlign(SubjectView.SUBJECT_ALIGN_BOTTOM);
//        mSubjectView.setSubjectAlign(SubjectView.SUBJECT_ALIGN_RIGHT);
//        mSubjectView.setSubjectAlign(SubjectView.SUBJECT_ALIGN_TOP);
//        mSubjectView.setContentOrientation(SubjectView.CONTENT_ORIENTATION_HORIZONTAL);

        /**
         * initializing subject and it's content
         */

        for(int i=0; i<SUBJECT_COUNT_DEFAULT; i++) {
            addSubjectContent();
        }

        mContentAdapter = new MyAdapter(getActivity());
        mContentAdapter.setConfuse(AVATAR_WIDTH, AVATAR_HEIGHT);
        //jack@150615
        mContentAdapter.setConfuseAutoRecyclable(true);
        mContentAdapter.setCurrentMainMenu(mSubjectList.get(0));
        mContentAdapter.setContentItems(mContentItemMap);
        mSubjectView.setAdapter(mContentAdapter);
        mSubjectView.setOnClickListener(mContentAdapter);

        /**
         * button setting
         */
        mRootView.findViewById(R.id.avatar_solid_btn_addSubject).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mSubjectList.size() >= SUBJECT_COUNT_MAX)
                    return;

                Toast.makeText(mRootView.getContext(), "Add Subject", Toast.LENGTH_SHORT).show();
                addSubjectContent();
                mContentAdapter.notifyConfuseChanged();
            }
        });
        mRootView.findViewById(R.id.avatar_solid_btn_addContent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mRootView.getContext(), "Add Content", Toast.LENGTH_SHORT).show();

                View subject = mContentAdapter.mCurrentSubject;
                int index = mSubjectList.indexOf(subject) % SUBJECT_SELECTORS.length;
                addContent(subject, index);
                mContentAdapter.notifyDataSetChanged();
            }
        });
        mRootView.findViewById(R.id.avatar_solid_btn_leftLayout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mSubjectView.setSubjectAlign(SubjectView.SUBJECT_ALIGN_LEFT);
                mSubjectView.setContentOrientation(SubjectView.CONTENT_ORIENTATION_VERTICAL);
            }
        });
        mRootView.findViewById(R.id.avatar_solid_btn_bottomLayout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mSubjectView.setSubjectAlign(SubjectView.SUBJECT_ALIGN_BOTTOM);
                mSubjectView.setContentOrientation(SubjectView.CONTENT_ORIENTATION_HORIZONTAL);
            }
        });
        mRootView.findViewById(R.id.avatar_solid_btn_rightLayout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mSubjectView.setSubjectAlign(SubjectView.SUBJECT_ALIGN_RIGHT);
                mSubjectView.setContentOrientation(SubjectView.CONTENT_ORIENTATION_VERTICAL);
            }
        });
        mRootView.findViewById(R.id.avatar_solid_btn_topLayout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mSubjectView.setSubjectAlign(SubjectView.SUBJECT_ALIGN_TOP);
                mSubjectView.setContentOrientation(SubjectView.CONTENT_ORIENTATION_HORIZONTAL);
            }
        });
    }

    public View createSubject() {
        mCurrentSelectorIndex = ++mCurrentSelectorIndex % SUBJECT_SELECTORS.length;

        View subject = new ImageView(getActivity());
        subject.setBackground(getResources().getDrawable(SUBJECT_SELECTORS[mCurrentSelectorIndex]));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(SUBJECT_BUTTON_WIDTH, SUBJECT_BUTTON_HEIGHT);
        lp.weight = 1;
        subject.setLayoutParams(lp);

        return subject;
    }

    public ContentItem createContent(int index) {
        ContentItem sub = new ContentItem();
        sub.Location = createContentLocation(ContentItem.ITEM_RADIUS);
        sub.C1 = CONTENT_COLORS[index][0];
        sub.C2 = CONTENT_COLORS[index][1];
        sub.ResourceId = GenerateIntID.generateViewId();

        return sub;
    }

    public Point createContentLocation(int r) {
        int w = AVATAR_WIDTH;
        int h = AVATAR_HEIGHT;

        int x = (int) (Math.random() * w);
        int y = (int) (Math.random() * h);
        x = (x < r) ? r+1 : x;
        x = (x > (w - r)) ? w-r-1 : x;
        y = (y < r) ? r+1 : y;
        y = (y > (h - r)) ? h-r-1 : y;

        return new Point(x, y);
    }

    public Bitmap createContentBitmap(ContentItem item) {
        int w = AVATAR_WIDTH;
        int h = AVATAR_HEIGHT;
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, w, h, item.C1, item.C2, Shader.TileMode.MIRROR);
        paint.setAntiAlias(true);
        paint.setShader(shader);

        canvas.drawCircle(item.Location.x, item.Location.y, ContentItem.ITEM_RADIUS, paint);

        return bmp;
    }

    public void addSubject(View subject, int contentSelectIndex, ArrayList<ContentItem> contentItems) {

        ContentItem contentItem = null;
        Object contentKey = null;
        if(contentSelectIndex < contentItems.size()) {
            contentItem = contentItems.get(contentSelectIndex);
//            contentItem.IsSelected = true;
            contentKey = contentItem.ResourceId;
        }

        int subjectIndex = mSubjectList.size() + 1;

        mContentItemMap.put(subject, contentItems);
        mSubjectList.add(subject);

        //jack@150623
        Object key = new Object();//new String(subject.toString());
        ArrayList<Object> keyList = new ArrayList<Object>();
        keyList.add(key);
        mKeyList.add(keyList);
        contentItem.KeySelect = key;

        mSubjectView.addSubject(subject);
//        mSubjectView.addContentSelector(subjectIndex, subject, contentKey);
        addKeyPair(key, contentKey);

    }



    public void addContent(View subject, int selectorIndex) {
        if(selectorIndex >= SUBJECT_SELECTORS.length)
            return;

        ArrayList<ContentItem> contentItems = mContentItemMap.get(subject);
        if(contentItems != null) {
            ContentItem item = createContent(selectorIndex);
            contentItems.add(item);
        }

    }

    public void addSubjectContent() {
        View subject = createSubject();
        ArrayList<ContentItem> contentItems = new ArrayList<ContentItem>();
        for(int j=0; j<CONTENT_COUNT_DEFAULT; j++) {
            ContentItem item = createContent(mCurrentSelectorIndex);
            contentItems.add(item);
        }

        int contentSelectIndex = (int) (Math.random() * 10 % contentItems.size());
        addSubject(subject, contentSelectIndex, contentItems);

        //jack@150623
        int anotherSelectIndex = (int) (Math.random() * 10 % 6); //
        if(Math.random() > 0.5 && anotherSelectIndex != contentSelectIndex ) {
            Object key = new Object(); //subject.toString() + "_" + 2;
            Object value = contentItems.get(anotherSelectIndex).ResourceId;

            mKeyList.get(mKeyList.size() - 1).add(key);
//            contentItems.get(anotherSelectIndex).IsSelected = true;
            contentItems.get(anotherSelectIndex).KeySelect = key;
            addKeyPair(key, value);
        }


        if(mContentAdapter != null)
            mContentAdapter.notifyDataSetChanged();
    }

    public void addKeyPair(Object key, Object value) {
        mSubjectView.addContentSelector(mLevelCount, key, value);
        mLevelCount++;
    }


    class MyAdapter extends SubjectView.ContentAdapter<MyHolder> implements SubjectView.OnClickListener {
        Context mContext;
        LayoutInflater mInflater;
        View mCurrentSubject;
        HashMap<View, ArrayList<ContentItem>> mItemMap;

        MyAdapter(Context ctx) {
            super();
            mContext = ctx;
            mInflater = LayoutInflater.from(mContext);
        }

        public void release() {
            mContext = null;
            mInflater = null;
            mCurrentSubject = null;
            mItemMap.clear();

        }

        public void setCurrentMainMenu(View view) {
            mCurrentSubject = view;
        }

        void setSubMenu(View main, ArrayList<ContentItem> subContent) {
            if(mItemMap == null)
                mItemMap = new HashMap<View, ArrayList<ContentItem>>();
            mItemMap.put(main, subContent);
        }

        void setContentItems(HashMap<View, ArrayList<ContentItem>> subMenus) {
            mItemMap = subMenus;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup viewGroup, int viewType, View currentSubject) {
            View v = mInflater.inflate(R.layout.item_konnect_avatar_content, viewGroup, false);
            MyHolder holder = new MyHolder(v);
            holder.Icon = (ImageView) v.findViewById(R.id.avatar_solid_submenu_iv_icon);

            /**
             * let element of content to fit device height divided to 3 part.
             */
            int h = mSubjectView.getHeight();
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, h/3);
            v.setLayoutParams(lp);

            return holder;
        }

        //jack@150623
        @Override
        public SubjectView.ContentSelector onBindViewHolder(MyHolder holder, View currentSubject, int position) {
            Object key = null;
            Object value = null;

            //jack@150618
//            mCurrentSubject = currentSubject;
            Log.e(TAG, "onBindViewHolder()->holder=" + holder.toString() + " currentSubject=" + currentSubject + " pos=" + position);

            do {
                if(holder == null) break;
                if(!mItemMap.containsKey(mCurrentSubject)) break;

                //jack@150616
                if(mItemMap.get(mCurrentSubject).size() == 0) break; //for null content

                ContentItem contentItem = mItemMap.get(mCurrentSubject).get(position);
                if(contentItem == null) break;

                //jack@150615
                Bitmap contentImage = createContentBitmap(contentItem);
                holder.Icon.setImageBitmap(contentImage);

//                holder.itemView.setTag(contentItem);
//                if(contentItem.IsSelected)
                if(contentItem.KeySelect != null)
                    holder.itemView.setBackgroundColor(Color.parseColor("#449def"));
                else
                    holder.itemView.setBackgroundColor(Color.WHITE);

                /**
                 * the key of content is a bridge which associating content and relational resource
                 */
                int selectKeyIndex = (int) (Math.random() * 10 % 2);
                int keysIndex = mSubjectList.indexOf(mCurrentSubject);
                ArrayList<Object> keyList = mKeyList.get(keysIndex);

                if(keyList.size() > selectKeyIndex)
                    key = keyList.get(selectKeyIndex);
                else
                    key = keyList.get(0);

                Object[] tag = {contentItem, key};
                holder.itemView.setTag(tag);
                value = contentItem.ResourceId;
            } while(false);

//            return new SubjectView.ContentSelector(currentSubject, key);
            return new SubjectView.ContentSelector(key, value);
        }

        //jack@0623
        @Override
        public Bitmap getSelectedContentBitmap(Object key, Object value) {
            Log.e(TAG, "getSelectedContentBitmap()->key=" + key + " value=" + value);

            Bitmap bmp = null;

            if(value != null) {
                int resourceId = (int) value;
                Set<View> keySet = mItemMap.keySet();

                A:
                for (View subject : keySet) {
                    ArrayList<ContentItem> contentList = mItemMap.get(subject);
                    for (int i = 0; i < contentList.size(); i++) {
                        ContentItem contentItem = contentList.get(i);
                        if (resourceId == contentItem.ResourceId) {
                            //jack@150615
                            bmp = createContentBitmap(contentItem);
                            break A;
                        }
                    }
                }
            }

            return bmp;
        }

        @Override
        public void onSubjectChanged(View subject) {
            mCurrentSubject = subject;
        }

        //jack@150623
        @Override
        public void onContentConfused(ArrayList<SubjectView.ContentSelector> selectors, Bitmap confusedImage) {
            mAvatarView.setImageBitmap(confusedImage);
            mAvatarView.invalidate();
        }

        @Override
        public int getItemCount() {
            int count = 0;
            if(mItemMap.containsKey(mCurrentSubject)) {
                count = mItemMap.get(mCurrentSubject).size();
            }
//            Log.v(TAG, "getItemCount()->count=" + count);

            return count;
        }

        /**
         * implements for SubjectView.OnClickListener
         */
        @Override
        public void onSubjectClick(View v) {

        }

//        @Override
//        public void onContentClick(View v) {
//            if(!(v.getTag() instanceof ContentItem)) return;
//
//            ContentItem item = (ContentItem) v.getTag();
//
//            ArrayList<ContentItem> contentItemList = mItemMap.get(mCurrentSubject);
//            for(ContentItem _item : contentItemList) {
//                if(_item.ResourceId == item.ResourceId)
//                    _item.IsSelected = true;
//                else
//                    _item.IsSelected = false;
//            }
//
//            notifyDataSetChanged();
//        }

        @Override
        public void onContentClick(View v) {
            Object[] tag = (Object[]) v.getTag();
            if(!(tag[0] instanceof ContentItem)) return;
            if(tag[1] == null) return; //key code

            ContentItem item = (ContentItem) tag[0];
            Object key = tag[1];

            ArrayList<ContentItem> contentItemList = mItemMap.get(mCurrentSubject);
            for(ContentItem _item : contentItemList) {
                if(_item.KeySelect == key)
                    _item.KeySelect = null;
                else if(_item.ResourceId == item.ResourceId)
                    _item.KeySelect = key;
            }

            notifyDataSetChanged();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView Icon;

        public MyHolder(View itemView) {
            super(itemView);
        }

    }
}
