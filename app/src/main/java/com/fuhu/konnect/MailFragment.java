package com.fuhu.konnect;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuhu.konnect.library.mail.effect.Effect;
import com.fuhu.konnect.library.mail.effect.EffectGroup;
import com.fuhu.konnect.library.mail.effect.IColorWallPaperEffect;
import com.fuhu.konnect.library.mail.effect.IMultipleWallPaperEffect;
import com.fuhu.konnect.library.mail.effect.IStickerEffect;
import com.fuhu.konnect.library.view.NabiStickerView;
import com.fuhu.konnect.library.view.PaintView;
import com.fuhu.konnect.mail.MailEffectButtonWidget;
import com.fuhu.konnect.mail.MailStickerWidget;
import com.fuhu.konnect.mail.MailWallpaperWidget;
import com.fuhu.konnect.mail.effect.EffectManager;
import com.fuhu.konnect.mail.effect.EraserEffect;
import com.fuhu.konnect.mail.effect.PaintEffect;
import com.fuhu.konnect.mail.effect.StickerEffect;
import com.fuhu.konnect.mail.effect.WallPaperEffect;

/**
 * Created by jacktseng on 2015/8/24.
 */
public class MailFragment extends Fragment {

    private View mRootView;

    private PaintView mPaintView;

    private EffectManager mEffectManager;

    private MySubEffectAdapter mSubEffectAdapter;

    private MyEffectContentAdapter mEffectContentAdapter;

    private View.OnClickListener mOnStickerBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mPaintView != null)
                mPaintView.closeEffectContent();

            if(view instanceof MailStickerWidget) {
                Bitmap bmp = ((MailStickerWidget) view).getImage();
//                StickerView sv = new StickerView(getActivity(), bmp);
                NabiStickerView sv = new NabiStickerView(getActivity(), bmp);
                sv.setCheckButtonImage(mIconCheck);
                sv.setCrossButtonImage(mIconCross);
                sv.setResizeButtonImage(mIconResize);
                sv.setRotateButtonImage(mIconRotate);
                mPaintView.getStickerCtrl().addSticker(sv);
            }
        }
    };

//    private View.OnClickListener mOnWallPaperBtnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            if(mPaintView != null)
//                mPaintView.closeEffectContent();
//
//            if(view instanceof MailWallpaperWidget) {
//                MailWallpaperWidget mww = (MailWallpaperWidget) view;
//                mww.
//            }
//        }
//    };

    private Bitmap mIconCheck, mIconCross, mIconResize, mIconRotate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.frag_mail, container, false);
        mPaintView = (PaintView) mRootView.findViewById(R.id.mail_page_paintView);



        doMail();

        return mRootView;
    }

    private void doMail() {
        mEffectManager =  EffectManager.getInstance();

        //Loads the source for sticker button icon
        Context ctx = getActivity();
        mIconCheck = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_check);
        mIconCross = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_cross);
        mIconResize = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_resize);
        mIconRotate = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_rotate);

        //Adds the main effect into PaintView
        MailEffectButtonWidget paintEffectButton = new MailEffectButtonWidget(ctx);
        MailEffectButtonWidget stickerEffectButton = new MailEffectButtonWidget(ctx);
        MailEffectButtonWidget wallEffectButton = new MailEffectButtonWidget(ctx);
        MailEffectButtonWidget eraserEffectButton = new MailEffectButtonWidget(ctx);
        paintEffectButton.setEffect(new PaintEffect());
        stickerEffectButton.setEffect(new StickerEffect());
        wallEffectButton.setEffect(new WallPaperEffect());
        eraserEffectButton.setEffect(new EraserEffect());
        mPaintView.addMainEffect(paintEffectButton, (EffectGroup) paintEffectButton.getEffect());
        mPaintView.addMainEffect(stickerEffectButton, (EffectGroup) stickerEffectButton.getEffect());
        mPaintView.addMainEffect(wallEffectButton, (EffectGroup) wallEffectButton.getEffect());
        mPaintView.addMainEffect(eraserEffectButton, (EffectGroup) eraserEffectButton.getEffect());


        //Sets adapter to PaintView for showing sub effects
        mSubEffectAdapter = new MySubEffectAdapter();
        mPaintView.setSubEffectAdapter(mSubEffectAdapter);
        mEffectContentAdapter = new MyEffectContentAdapter();
        mPaintView.setEffectContentAdapter(mEffectContentAdapter);

        mPaintView.setEffectContentSize(1050, 600);
        mPaintView.getBackEffectButton().setBackground(null);
        mPaintView.getBackEffectButton().setImageResource(R.drawable.mail_btn_back);
        ViewGroup.LayoutParams lp = mPaintView.getBackEffectButton().getLayoutParams();
        lp.width = 111;
        lp.height = 110;
        mPaintView.getBackEffectButton().setLayoutParams(lp);
    }

    private class MySubEffectVH extends RecyclerView.ViewHolder {

        private MailEffectButtonWidget mEffectButton;

        public MySubEffectVH(MailEffectButtonWidget itemView) {
            super(itemView);
            mEffectButton = itemView;
        }
    }

    private class MySubEffectAdapter extends PaintView.SubEffectAdapter<MySubEffectVH> {

        @Override
        public MySubEffectVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return  new MySubEffectVH(new MailEffectButtonWidget(MailFragment.this.getActivity()));
        }

        @Override
        public void onBindEffectToHolder(MySubEffectVH holder, Effect effect) {
            holder.mEffectButton.setEffect(effect);
        }

    }

    private class MyEffectContentVH extends RecyclerView.ViewHolder {

        public MyEffectContentVH(View itemView) {
            super(itemView);
        }
    }


    private class MyEffectContentAdapter extends PaintView.EffectContentAdapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(Effect currentEffect, ViewGroup parent, int viewType) {
            View v;
            if(currentEffect instanceof IStickerEffect) {
                v = new MailStickerWidget(getActivity());
                v.setOnClickListener(mOnStickerBtnClickListener);
            }
            else {
                v = new MailWallpaperWidget(getActivity());

            }


            return new MyEffectContentVH(v);
        }

        @Override
        public void onBindViewHolder(Effect currentEffect, RecyclerView.ViewHolder holder, int position) {

            if(currentEffect instanceof IStickerEffect) {
                MailStickerWidget v = (MailStickerWidget) holder.itemView;
                int srcId = ((IStickerEffect) currentEffect).getStickerResId().get(position);
                v.setImage(srcId);
            }
            else if(currentEffect instanceof IMultipleWallPaperEffect) {
                MailWallpaperWidget v = (MailWallpaperWidget) holder.itemView;
                Pair<Integer, Integer> srcId = ((IMultipleWallPaperEffect) currentEffect).getWallPaperResId().get(position);
                v.setImage(srcId.first, srcId.second, -1);
            }
            else if(currentEffect instanceof IColorWallPaperEffect) {
                MailWallpaperWidget v = (MailWallpaperWidget) holder.itemView;
                int srcId = ((IColorWallPaperEffect) currentEffect).getWallPaperResId().get(position);
                v.setImage(-1, -1, srcId);
            }

        }

        @Override
        public int getItemCount(Effect currentEffect) {
            int rtn = 0;
            if(currentEffect instanceof IStickerEffect)
                rtn = ((IStickerEffect) currentEffect).getStickerResId().size();
            else if(currentEffect instanceof IMultipleWallPaperEffect)
                rtn = ((IMultipleWallPaperEffect) currentEffect).getWallPaperResId().size();
            else if(currentEffect instanceof IColorWallPaperEffect)
                rtn = ((IColorWallPaperEffect) currentEffect).getWallPaperResId().size();

            return rtn;
        }
    }

}
