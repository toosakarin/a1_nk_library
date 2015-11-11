package com.fuhu.konnect;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fuhu.konnect.library.view.PhotoView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PhotoDemoFragment extends Fragment {

    public static final String TAG = PhotoDemoFragment.class.getSimpleName();

    View mRootView;

    PhotoView mPhotoView;

    /**
     * PhotoAdapter for photo list display
     */
    MyPhotoListAdapter mPhotoListAdapter;

    /**
     * PhotoAdapter for photo page display
     */
    PagerAdapter mPhotoPageAdapter;

    /**
     * photo item for demo using
     */
    ArrayList<PhotoItem> mItemList = new ArrayList<PhotoItem>();

    /**
     * Item for demo using
     */
    class PhotoItem {
        int drawableId;
        int w;
        int h;
        String name;
        String date;

        Bitmap bitmap;

        public PhotoItem(int drawable) {
            this.drawableId = drawable;
        }

        public PhotoItem(int drawable, int w, int h) {
            this.drawableId = drawable;
            this.w = w;
            this.h = h;
        }

        public void createBitmap() {
            int size = w*h;
            int[] src = new int[size];

            Array.setInt(src, 0, drawableId);

//            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bitmap);
//            canvas.drawColor(drawableId);
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Drawable drawable = getResources().getDrawable(drawableId);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);



        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        com.fuhu.konnect.library.Debug.IS_DEBUG = true;

        mRootView = inflater.inflate(R.layout.frag_photo_demo, container, false);
        doPhoto();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5000);
//                } catch(Exception e) {
//                    e.printStackTrace();
//                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mPhotoListAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        }).start();

        return mRootView;
    }

    public void doPhoto() {
        //
//        mItemList.add(new PhotoItem(Color.RED));
//        mItemList.add(new PhotoItem(Color.YELLOW));
//        mItemList.add(new PhotoItem(Color.BLACK));
//        mItemList.add(new PhotoItem(Color.BLUE));
//        mItemList.add(new PhotoItem(Color.GRAY));
//        mItemList.add(new PhotoItem(Color.GREEN));
//        mItemList.add(new PhotoItem(Color.LTGRAY));
        mItemList.add(new PhotoItem(R.drawable.selector_blue));
        mItemList.add(new PhotoItem(R.drawable.selector_red));
        mItemList.add(new PhotoItem(R.drawable.selector_purple));
        mItemList.add(new PhotoItem(R.drawable.selector_green));
        mItemList.add(new PhotoItem(R.drawable.selector_yello));
        mItemList.add(new PhotoItem(R.drawable.selector_black));

        /**
         * For photo page display using
         */
//        ArrayList<Bitmap> photoRawDataList = new ArrayList<Bitmap>(); //default item

        for(int i=0; i<mItemList.size(); i++) {
            int w = (int) (Math.random()*900f);
            int h = (int) (Math.random()*700f);
            if(w < 300) w = 500;
            if(h < 400) h = 400;

            PhotoItem item = mItemList.get(i);
            item.w = w;
            item.h = h;
            item.name = "Jack_" + i;
            item.date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

            //For photo page display using
            item.createBitmap();
//            photoRawDataList.add(item.bitmap); //default item

        }

        mPhotoView = (PhotoView) mRootView.findViewById(R.id.pv_photoView);
//        mPhotoView.setPhotoListSpanCount(4);
        mPhotoListAdapter = new MyPhotoListAdapter(getActivity());
        mPhotoListAdapter.setItems(mItemList);
        mPhotoView.setAdapter(mPhotoListAdapter);
        mPhotoView.setItemAnimator(new DefaultItemAnimator());

        /**
         * For photo page display
         */
//        mPhotoPageAdapter = new PhotoView.DefaultPhotoPageAdapter(this, photoRawDataList); //default item
        mPhotoPageAdapter = new MyPhotoPageAdapter(getActivity());
        ((MyPhotoPageAdapter) mPhotoPageAdapter).setItems(mItemList);
        mPhotoView.setPhotoPageAdapter(mPhotoPageAdapter);
        mPhotoView.setOnPhotoPageChangeListener(new PhotoView.OnPhotoPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.v(TAG, "onPageScrolled()-> pos=" + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.v(TAG, "onPageSelected()-> pos=" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.v(TAG, "onPageScrollStateChanged()-> state=" + state);
            }
        });
        mPhotoView.setOnPhotoListScrollListener(new PhotoView.OnPhotoListScrollListener() {
            @Override
            public void onTop() {
                Log.e(TAG, "onTop");
                doLoading(0);
            }

            @Override
            public void onBottom() {
                Log.e(TAG, "onBottom");
               doLoading(1);
            }

            @Override
            public void onScroll() {
                Log.e(TAG, "onScroll");

            }
        });
    }

    void doLoading(final int ori) {
        showLoadingView(ori);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeLoadingView(ori);
                        }
                    });
                } catch(Exception e) {

                }
            }
        }).start();
    }

    void showLoadingView(int ori) {
        View wrapper;
        View text;
        int d;
        if(ori == 0) {
            wrapper = mRootView.findViewById(R.id.layout_rl_loadWrapperTop);
            text = mRootView.findViewById(R.id.tv_loadingTop);
            d = 20;
        }
        else {
            wrapper = mRootView.findViewById(R.id.layout_rl_loadWrapperBottom);
            text = mRootView.findViewById(R.id.tv_loadingBottom);
            d = -20;
        }

        text.setVisibility(View.VISIBLE);

        TranslateAnimation slideDown = new TranslateAnimation(0, 0, 0, d);
        slideDown.setDuration(500);
        slideDown.setFillEnabled(true);
        wrapper.startAnimation(slideDown);
    }

    void closeLoadingView(int ori) {
        View wrapper;
        View text;
        int d;
        if(ori == 0) {
            wrapper = mRootView.findViewById(R.id.layout_rl_loadWrapperTop);
            text = mRootView.findViewById(R.id.tv_loadingTop);
            d = -20;
        }
        else {
            wrapper = mRootView.findViewById(R.id.layout_rl_loadWrapperBottom);
            text = mRootView.findViewById(R.id.tv_loadingBottom);
            d = 20;
        }

        text.setVisibility(View.GONE);

        TranslateAnimation slideDown = new TranslateAnimation(0, 0, 0, d);
        slideDown.setDuration(500);
        slideDown.setFillEnabled(true);
        wrapper.startAnimation(slideDown);
    }



    /**
     *
     */
    class MyPhotoListAdapter extends PhotoView.PhotoListAdapter <MyPhotoListAdapter.MyPhotoListHolder> {

        Context mContext;
        LayoutInflater mInflater;

        ArrayList<PhotoItem> mPhotoList = new ArrayList<PhotoItem>();

        public MyPhotoListAdapter(Context c) {
            super();

            mContext = c;
            mInflater = LayoutInflater.from(mContext);
        }

        public void setItems(ArrayList<PhotoItem> list) {
            mPhotoList = list;
        }

        @Override
        public MyPhotoListHolder onCreatePhotoListHolder(ViewGroup viewGroup, int viewType) {

//            View v = mInflater.inflate(R.layout.list_item_photo, viewGroup, false);
//            ImageView vPhoto = (ImageView) v.findViewById(R.id.list_item_photo);
//            MyHolder holder = new MyHolder(v, vPhoto);

            View v = mInflater.inflate(R.layout.item_konnect_photo, viewGroup, false);
            ImageView photo = (ImageView) v.findViewById(R.id.photo_iv_photo);
            MyPhotoListHolder holder = new MyPhotoListHolder(v, photo);

            holder.mAvatar = (ImageView) v.findViewById(R.id.photo_iv_avatar);
            holder.mDelete = (ImageView) v.findViewById(R.id.photo_btn_delete);
            holder.mChildName = (TextView) v.findViewById(R.id.photo_tv_childName);
            holder.mData = (TextView) v.findViewById(R.id.photo_tv_date);

            return holder;
        }

        @Override
        public void onPhotoClick(ImageView photo, MyPhotoListHolder holder) {
            Log.d(TAG, "onPhotoClick->");
        }

        @Override
        public void onBindPhotoListHolder(MyPhotoListHolder viewHolder, final int i) {
            MyPhotoListHolder holder = (MyPhotoListHolder) viewHolder;

            if(holder != null) {
                Log.e(TAG, "onBindViewHolder->index " + i);

                final PhotoItem item = mPhotoList.get(i);

                /**
                 * Cause we want to let photo full whole view on the screen, so check the origin w
                 * if it equal to bitmap width, it's need to resize.
                 */
                if(item.w == item.bitmap.getWidth()) {
                    float w = item.w;
                    float h = item.h;
                    int baseW = mPhotoView.getMeasuredWidth() / 2;
                    float newH = (float) Math.rint(((h * baseW) / w));
                    Bitmap resizeBitmap = Bitmap.createScaledBitmap(item.bitmap, baseW, (int) newH, false);
                    item.bitmap.recycle();
                    item.bitmap = resizeBitmap;
                }

//                holder.mPhoto.setLayoutParams(new LinearLayout.LayoutParams(item.w, item.h));
//                holder.mPhoto.setBackgroundColor(item.drawableId);
                holder.mPhoto.setImageBitmap(item.bitmap);
                holder.mChildName.setText(item.name);
                holder.mData.setText(item.date);
                holder.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(PhotoDemoFragment.this.getActivity(), "delete photo - " + i, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            return mPhotoList.size();
        }

        public class MyPhotoListHolder extends PhotoView.PhotoListHolder {
            ImageView mAvatar;
            ImageView mDelete;
            TextView mChildName;
            TextView mData;

            ImageView mPhoto;

            public MyPhotoListHolder(View itemView, ImageView photo) {
                super(itemView, photo);
                mPhoto = photo;
            }
        }
    }


    class MyPhotoPageAdapter extends PagerAdapter {
        Context mContext;
        LayoutInflater mInflater;
        ArrayList<PhotoItem> mItemList;

        public MyPhotoPageAdapter(Context ctx) {
            mContext = ctx;
            mInflater = LayoutInflater.from(mContext);
        }

        public void setItems(ArrayList<PhotoItem> items) {
            mItemList = items;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            PhotoItem item = mItemList.get(position);
            View root = mInflater.inflate(R.layout.item_konnect_photo_page, container, false);
            ((TextView) root.findViewById(R.id.photo_page_tv_childName)).setText(item.name);
            ((TextView) root.findViewById(R.id.photo_page_tv_date)).setText(item.date);
            ((ImageView) root.findViewById(R.id.photo_page_iv_photo)).setImageBitmap(item.bitmap);

            container.addView(root, 0);

            return root;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(object instanceof View)
                container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
