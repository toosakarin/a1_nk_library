package com.fuhu.konnect;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuhu.konnect.library.mail.effect.Effect;
import com.fuhu.konnect.library.mail.effect.EffectGroup;
import com.fuhu.konnect.library.mail.effect.IStickerEffect;
import com.fuhu.konnect.library.view.PaintView;
import com.fuhu.konnect.mail.MailEffectButtonWidget;
import com.fuhu.konnect.mail.MailStickerWidget;
import com.fuhu.konnect.mail.MailWallpaperWidget;
import com.fuhu.konnect.mail.effect.EffectManager;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.frag_mail, container, false);
        mPaintView = (PaintView) mRootView.findViewById(R.id.mail_page_paintView);



        doMail();

        return mRootView;
    }

    private void doMail() {
        mEffectManager =  EffectManager.getInstance();

        //Adds the main effect into PaintView
        MailEffectButtonWidget paintEffectButton = new MailEffectButtonWidget(getActivity());
        MailEffectButtonWidget stickerEffectButton = new MailEffectButtonWidget(getActivity());
        MailEffectButtonWidget wallEffectButton = new MailEffectButtonWidget(getActivity());
        paintEffectButton.setEffect(new PaintEffect());
        stickerEffectButton.setEffect(new StickerEffect());
        wallEffectButton.setEffect(new WallPaperEffect());
        mPaintView.addMainEffect(paintEffectButton, (EffectGroup) paintEffectButton.getEffect());
        mPaintView.addMainEffect(stickerEffectButton, (EffectGroup) stickerEffectButton.getEffect());
        mPaintView.addMainEffect(wallEffectButton, (EffectGroup) wallEffectButton.getEffect());

        //Sets adapter to PaintView for showing sub effects
        mSubEffectAdapter = new MySubEffectAdapter();
        mPaintView.setAdapter(mSubEffectAdapter);


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
            if(currentEffect instanceof IStickerEffect)
                v = new MailStickerWidget(getActivity());
            else
                v = new MailWallpaperWidget(getActivity());

            return new MyEffectContentVH(v);
        }

        @Override
        public void onBindViewHolder(Effect currentEffect, RecyclerView.ViewHolder holder, int position) {

            if(currentEffect instanceof IStickerEffect) {

            }
            else {

            }

        }

        @Override
        public int getItemCount(Effect currentEffect) {
            return 0;
        }
    }

}
