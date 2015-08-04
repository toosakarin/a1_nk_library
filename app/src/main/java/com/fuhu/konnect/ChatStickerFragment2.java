package com.fuhu.konnect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fuhu.konnect.library.sticker.Sticker;
import com.fuhu.konnect.library.sticker.StickerCategory;
import com.fuhu.konnect.library.sticker.StickerDownloader;
import com.fuhu.konnect.library.sticker.StickerManager;
import com.fuhu.konnect.library.sticker.StickerManagerFactory;
import com.fuhu.konnect.library.sticker.StickerStorage;
import com.fuhu.konnect.library.utility.ParamChecker;
import com.fuhu.konnect.library.view.SubjectView;
import com.fuhu.konnect.sticker.DefaultStickerProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jacktseng on 2015/7/21.
 */
public class ChatStickerFragment2 extends Fragment {

    public static final String TAG = ChatStickerFragment2.class.getSimpleName();

    public static final int STICKER_WIDTH = 800;

    public static final int STICKER_HEIGHT = 250;

    public static final int SUBJECT_BUTTON_WIDTH = 200;

    public static final int SUBJECT_BUTTON_HEIGHT = 100;

    //number of subject be created
    public static final int SUBJECT_COUNT_DEFAULT = 6;

    //number of content be created for subject
    public static final int CONTENT_COUNT_DEFAULT = 12;



    private View mRootView;

    private ListView mStickerChatListView;

    private ArrayList<String> mStickerChatList = new ArrayList<>();

    private SubjectView mSubjectView;

    private MyAdapter mSVContentAdapter;

    /**
     * data set of Subject & it's content
     */
    private ArrayList<View> mStickerSubjectList = new ArrayList<>();
    private HashMap<View, ArrayList<Sticker>> mStickerContentMap = new HashMap<>();

    /**
     * indicate number of subject exist for dispatching subject background
     */
    private int mNewSubjectIndex = -1;

    /**
     * StickerManager
     */
    private final String DOWNLOAD_STICKER_CATEGORY_ID_G = "g";
    private final String DOWNLOAD_STICKER_CATEGORY_ID_H = "h";
    private StickerManager mStickerManager;
    private Dialog mCurrentDialog;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.frag_chat_sticker_demo, container, false);
        mStickerChatListView = (ListView) mRootView.findViewById(R.id.chat_sticker_lv_stickerListView);
        mSubjectView = (SubjectView) mRootView.findViewById(R.id.chat_sticker_sv_stickerSubjectView);

        doSticker();

        return mRootView;
    }

    private void doSticker() {
        /**
         * init
         */
        mSubjectView.release();
        mSubjectView.init();
        mStickerSubjectList.clear();
        mStickerContentMap.clear();
        mStickerManager = null;
        if(mCurrentDialog != null)
            mCurrentDialog.dismiss();
        mCurrentDialog = null;

        /**
         * StickerManager
         */
        mStickerManager = StickerManagerFactory.getInstance(this.getActivity());
        mStickerManager.setDefaultProvider(new DefaultStickerProvider());
        setStickerDownloader(mStickerManager);

//        mStickerManager.download(DOWNLOAD_STICKER_CATEGORY_ID_G);


        /**
         * setting Subject's content
         */
        ArrayList<StickerCategory> categories = mStickerManager.getDefaultProvider().getCategories(); //add default sticker
        categories.addAll(mStickerManager.getExtProvider().getCategories()); //add ext provider sticker
        for(StickerCategory category : categories) {
            ImageView iv = new ImageView(this.getActivity());
//            iv.setImageResource((Integer) category.getStickers().get(0).getResourceId());
            Sticker categoryDefaultSticker = category.getStickers().get(0);
            Bitmap stickerImage = mStickerManager.loadStickerBitmap(categoryDefaultSticker);
            iv.setImageBitmap(stickerImage);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(SUBJECT_BUTTON_WIDTH, SUBJECT_BUTTON_HEIGHT);
            lp.weight = 1;
            iv.setLayoutParams(lp);

            //jack@150731
            /**
             * set delete category function when if is a ext category
             */
            if(categoryDefaultSticker.getResourceId() instanceof String) {
                final Sticker _sticker = categoryDefaultSticker;
                iv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View view) {

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Alert")
                                .setMessage("Want to delete this stickers?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Log.d(TAG, "delete sticker category");

                                        /**
                                         * delete all stickers of this category on ext storage
                                         */
                                        File f = new File((String) _sticker.getResourceId());
                                        f = f.getParentFile();
                                        if (f.isDirectory()) {
                                            for (File stickerFile : f.listFiles())
                                                stickerFile.delete();
                                            f.delete();
                                        }

                                        /**
                                         * remove this subject of sticker category and it's content pf stickers
                                         */

                                        mStickerContentMap.remove(view);
                                        mStickerSubjectList.remove(view);
                                        mSubjectView.removeSubject(view);
                                        mSVContentAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("NO", null)
                                .show();

                        return true;
                    }
                });
            }

            mSubjectView.addSubject(iv);

            mStickerSubjectList.add(iv);
            mStickerContentMap.put(iv, category.getStickers());
        }

        //jack@150731
        /**
         * add the add sticker button
         */
        ImageView iv = new ImageView(getActivity());
        iv.setImageResource(R.drawable.chat_sticker_add_sticker);
        mSubjectView.addSubject(iv);


        /**
         * SubjectView setting
         */
        mSubjectView.setSubjectAlign(SubjectView.SUBJECT_ALIGN_BOTTOM);
        mSubjectView.setContentOrientation(SubjectView.CONTENT_ORIENTATION_HORIZONTAL);
        mSubjectView.setContentSpan(1);
        /**
         * StickerListView setting
         */
        mStickerChatListView.setAdapter(new MyStickerListAdapter(mStickerChatList));

        mSVContentAdapter = new MyAdapter(getActivity());
        mSVContentAdapter.setCurrentSubject(mStickerSubjectList.get(0));
        mSVContentAdapter.setContentItems(mStickerContentMap);
        mSubjectView.setAdapter(mSVContentAdapter);
        mSubjectView.setOnClickListener(mSVContentAdapter);
    }

    public void setStickerDownloader(StickerManager manager) {

        if(manager == null) return;

        StickerDownloader downloader = new StickerDownloader() {
            @Override
            public void download(String stickerCategoryId, StickerStorage storage) {

                if(!ParamChecker.isString(stickerCategoryId)) return;
                if(storage == null) return;

                String categoryName = null;
                int[] downloadList = null;
                if(stickerCategoryId == DOWNLOAD_STICKER_CATEGORY_ID_G) {
                    categoryName = "Shrek";
                    downloadList = new int[]{
                            R.drawable.chat_sticker_g_01,
                            R.drawable.chat_sticker_g_02,
                            R.drawable.chat_sticker_g_03,
                            R.drawable.chat_sticker_g_04,
                            R.drawable.chat_sticker_g_05
                    };
                }
                else if(stickerCategoryId == DOWNLOAD_STICKER_CATEGORY_ID_H) {
                    categoryName = "Tong";
                    downloadList = new int[]{
                            R.drawable.chat_sticker_h_01,
                            R.drawable.chat_sticker_h_02,
                            R.drawable.chat_sticker_h_03,
                            R.drawable.chat_sticker_h_04,
                            R.drawable.chat_sticker_h_05,
                            R.drawable.chat_sticker_h_06,
                            R.drawable.chat_sticker_h_07,
                            R.drawable.chat_sticker_h_08,
                            R.drawable.chat_sticker_h_09,
                            R.drawable.chat_sticker_h_10
                    };
                }


                if(!ParamChecker.isString(categoryName)) return;
                if(downloadList == null) return;

                for(int i=0; i<downloadList.length; i++) {
                    int download = downloadList[i];
                    String stickerId = "0" + String.valueOf(i+1);
                    Bitmap stickerImage = BitmapFactory.decodeResource(getActivity().getResources(), download);

                    try {
                        storage.saveSticker(stickerCategoryId, categoryName, stickerId, stickerImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        manager.setDownloader(downloader);

    }






    public void addSubject(View subject, ArrayList<Sticker> stickers) {

        mStickerContentMap.put(subject, stickers);
        mStickerSubjectList.add(subject);

        mSubjectView.addSubject(subject);
//        mSubjectView.addContentSelector(subjectIndex, subject, contentKey);
//        addKeyPair(key, contentKey);
    }


    /**
     * for sticker chat ListView
     */
    class MyStickerListAdapter extends BaseAdapter {

        private ArrayList<String> mItemList = new ArrayList<>();

        MyStickerListAdapter(ArrayList<String> items) {
            mItemList = items;
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public Object getItem(int i) {
            return mItemList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_konnect_chat_sitcker_content, viewGroup, false);
                view.setTag(view.findViewById(R.id.item_chat_sticker_iv_sticker));
            }

            ImageView v = (ImageView) view.getTag();
//            Sticker item = mItemList.get(i);
//            Bitmap bitmap = createContentBitmap(item);
            Sticker item = mStickerManager.decodeSticker(mItemList.get(i));
            Bitmap bitmap = mStickerManager.loadStickerBitmap(item);

            if(bitmap != null)
                v.setImageBitmap(bitmap);
            else
                v.setImageResource(R.drawable.chat_sticker_sticker_not_found);

            return view;
        }

    }

    /**
     * for SubjectView
     */
    class MyAdapter extends SubjectView.ContentAdapter<MyHolder> implements SubjectView.OnClickListener {
        Context mContext;
        LayoutInflater mInflater;
        View mCurrentSubject;
        HashMap<View, ArrayList<Sticker>> mItemMap;

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

        public void setCurrentSubject(View view) {
            mCurrentSubject = view;
        }

        void setSubMenu(View main, ArrayList<Sticker> subContent) {
            if(mItemMap == null)
                mItemMap = new HashMap<View, ArrayList<Sticker>>();
            mItemMap.put(main, subContent);
        }

        void setContentItems(HashMap<View, ArrayList<Sticker>> subMenus) {
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
            int w = mSubjectView.getWidth();
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w / 5, RelativeLayout.LayoutParams.MATCH_PARENT);
            v.setLayoutParams(lp);

            return holder;
        }

        //jack@150623
        @Override
        public SubjectView.ContentSelector onBindViewHolder(MyHolder holder, View currentSubject, int position) {
            Object key = null;
            final Object value = null;

            Log.e(TAG, "onBindViewHolder()->holder=" + holder.toString() + " currentSubject=" + currentSubject + " pos=" + position);

            boolean isError = true;
            do {
                if(holder == null) break;
                if(!mItemMap.containsKey(mCurrentSubject)) break;

                if(mItemMap.get(mCurrentSubject).size() == 0) break; //for null content

                Sticker sticker = mItemMap.get(mCurrentSubject).get(position);
                if(sticker == null) break;

//                Bitmap contentImage = createContentBitmap(sticker);
                Bitmap contentImage = mStickerManager.loadStickerBitmap(sticker);
                holder.Icon.setImageBitmap(contentImage);

                Object tag = sticker;
                holder.itemView.setTag(tag);

                isError = false;
            } while(false);

            if(isError) {
                if(holder != null)
                    holder.itemView.setTag(null);
            }


//            return new SubjectView.ContentSelector(key, value);
            return null;
        }

        @Override
        public Bitmap getSelectedContentBitmap(Object key, Object value) {
            return null;
        }

        @Override
        public void onSubjectChanged(View subject) {
            mCurrentSubject = subject;
        }

        @Override
        public void onContentConfused(ArrayList<SubjectView.ContentSelector> selectors, Bitmap confusedImage) {

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

        @Override
        public void onSubjectClick(View v) {

            /**
             * check if this click is the add sticker category button which not
             * be added in mStickerSubjectList
             */
            if(mStickerSubjectList.contains(v)) return;

            /**
             * show enable add sticker category dialog
             */
            LinearLayout root = new LinearLayout(getActivity());
            ImageView iv1 = new ImageView(getActivity());
            ImageView iv2 = new ImageView(getActivity());

            iv1.setImageResource(R.drawable.chat_sticker_g_01);
            iv1.setLayoutParams(new LinearLayout.LayoutParams(160, 160));
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Alert")
                            .setMessage("Download this sticker category G to device?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (mStickerManager == null) return;

                                    Log.d(TAG, "download sticker category - G");
                                    mStickerManager.download(DOWNLOAD_STICKER_CATEGORY_ID_G);
                                    doSticker();
                                }
                            })
                            .setNegativeButton("NO", null)
                            .show();
                }
            });
            iv2.setImageResource(R.drawable.chat_sticker_h_01);
            iv2.setLayoutParams(new LinearLayout.LayoutParams(160, 160));
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Alert")
                            .setMessage("Download this sticker category H to device?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (mStickerManager == null) return;

                                    Log.d(TAG, "download sticker category - H");
                                    mStickerManager.download(DOWNLOAD_STICKER_CATEGORY_ID_H);
                                    doSticker();
                                }
                            })
                            .setNegativeButton("NO", null)
                            .show();
                }
            });
            root.setOrientation(LinearLayout.HORIZONTAL);
            root.addView(iv1);
            root.addView(iv2);

            mCurrentDialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Alert")
                    .setView(root)
                    .show();
        }

        @Override
        public void onContentClick(View v) {

            Sticker item = (Sticker) v.getTag();

            /**
             * update sticker chat list
             */
//            mStickerChatList.add(item);
            mStickerChatList.add(mStickerManager.encodeSticker(item));
            mStickerChatListView.setSelection(mStickerChatListView.getAdapter().getCount() - 1);

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
