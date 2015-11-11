package com.fuhu.konnect.library;

import android.util.Log;

/**
 * Created by jacktseng on 2015/5/25.
 */
public class Debug {
    public final static String TAG = "nabikonnect_lib";

    public static boolean IS_DEBUG = false;

    public static void dumpLog(String tag, String msg) {
        if(IS_DEBUG)
            Log.d(TAG + "/" + tag, msg);
    }

    /**
     * jack@151019
     * 1. Modified the issue of displaying photo of the PhotoView at fullscreen has the status bar.
     */

    /**
     * jack@150922
     * 1. 將PageFragment class從PhotoView中獨立出來
     * 2. PhotoAdapter 更名為 PhotoListAdapter (PhotoHolder->PhotoListHolder)
     */

    /**
     * jack@150915
     * 1. 更改EffectCtrl.applyEffectContent(PaintView pv, EffectContentView contentView) ->
     * applyEffectContent(PaintView pv, Object content)
     */

    /**
     * jack@150911
     * 1. 修正開啟EffectContentWrapper時會用到上一次顯示的view holder之問題, 在每次open
     * EffectContentWrapper 時重新設定adapter 以確保每次開啟EffectContentWrapper都必須要重新建立新的view
     * holder.
     *
     */

    /**
     * jack@150818
     * 1. 修正Sticker.getCategoryId為null的問題
     */

    /**
     * jack@150731
     * 1. 修正SubjectView release()的null pointer問題
     * 2. Sticker module 初版完成, 包含download, store, provider等三大部分
     */

    /**
     * jack@150722
     * 1. 新增SubjectView支援上下左右四種方向排版
     * 2. 新增ChatSticker fragment
     */

    /**
     * jack@150618
     * 1. 修正不同subject的content數量不同時, 在切換subject時候會導致RecyclerView發生IndexOutOfBoundsException
     * 經查證發現原因可能為在onBindHandler呼叫之前, RecyclerView就會先呼叫adapter getItemCount取得item的size
     * 以原有寫法將可能會導致subject無法即時更新給adapter, 造成提供給RecyclerView的count不對稱.
     * 因此新作法為在subject更新時呼叫adapter中新增的onSubjectChanged方法回調給adapter提醒其更新資訊
     * 2. 改善SubjectView在confuse內容時的執行效能和降低記憶體使用量
     *
     * /


    /**
     * jack@150615
     * 1. ImageConfuser新增bitmap recycle function
     * 2. SubjectView 新增release function
     */

    //jack@150602
    /**
     *
     */

}
