package com.fuhu.konnect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fuhu.konnect.library.paint.StickerCtrl;
import com.fuhu.konnect.library.paint.effect.Effect;
import com.fuhu.konnect.library.paint.effect.EffectGroup;
import com.fuhu.konnect.library.paint.effect.EraserEffects;
import com.fuhu.konnect.library.paint.effect.IColorWallPaperEffect;
import com.fuhu.konnect.library.paint.effect.IEraserEffect;
import com.fuhu.konnect.library.paint.effect.IEraserEffectAll;
import com.fuhu.konnect.library.paint.effect.IMultipleWallPaperEffect;
import com.fuhu.konnect.library.paint.effect.IPaintEffect;
import com.fuhu.konnect.library.paint.effect.IStickerEffect;
import com.fuhu.konnect.library.paint.effect.PaintEffects;
import com.fuhu.konnect.library.view.EffectContentView;
import com.fuhu.konnect.library.view.NabiStickerView;
import com.fuhu.konnect.library.view.PaintView;
import com.fuhu.konnect.mail.View.EffectButton;
import com.fuhu.konnect.mail.View.StickerContentButton;
import com.fuhu.konnect.mail.View.WallpaperContentButton;
import com.fuhu.konnect.mail.effect.CameraEffect;
import com.fuhu.konnect.mail.effect.EffectManager;
import com.fuhu.konnect.mail.effect.EraserEffect;
import com.fuhu.konnect.mail.effect.PaintEffect;
import com.fuhu.konnect.mail.effect.StickerEffect;
import com.fuhu.konnect.mail.effect.WallPaperEffect;
import com.fuhu.konnect.mail.effect.WallPaperEffectDefault;
import com.jack.library.camera.CameraHolder;
import com.jack.library.camera.CameraRecord;
import com.jack.library.camera.listener.OnSnapshotListener;
import com.jack.library.view.CameraView;
import com.jack.library.view.FloatWindow;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/8/24.
 */
public class MailFragment extends Fragment {

    private View mRootView;

    private PaintView mPaintView;

    private EffectManager mEffectManager;

    private MySubEffectAdapter mSubEffectAdapter;

    private MyEffectContentAdapter mEffectContentAdapter;

    private EffectButton mCurrentEffectBtn;

    private Dialog mDialog;


    /**
     * from jackjack library
     */
    private FloatWindow mFloatView;
    private CameraView mCameraView;

    /**
     * This listener is used to get snapshot which is a callback from CameraHolder
     */
    private OnSnapshotListener mOnSnapshotListener = new OnSnapshotListener() {
        @Override
        public void onSnapshot(int imageDataType, CameraRecord record) {
            do {
                if(record == null) break;
                if(!(record.getRecord() instanceof byte[])) break;
                if(mPaintView == null) break;
                if(mPaintView.getStickerCtrl() == null) break;

                StickerCtrl ctrl = mPaintView.getStickerCtrl();
                byte[] raw = (byte[]) record.getRecord();

                //Cause maybe has some issue which letting snapshot size can't work fine, so we will
                //use the options to restrict size of snapshot
                BitmapFactory.Options ops = new BitmapFactory.Options();
                ops.inSampleSize = 4;

                Bitmap snapshot = BitmapFactory.decodeByteArray( raw, 0, raw.length, ops);
                Context ctx = getActivity();
                NabiStickerView stickerView = new NabiStickerView(ctx, snapshot);
                BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_check);
                stickerView.setCheckButtonImage(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_check));
                stickerView.setCrossButtonImage(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_cross));
                stickerView.setResizeButtonImage(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_resize));
                stickerView.setRotateButtonImage(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_rotate));
                ctrl.addSticker(stickerView);

                //Finishes the camera
                releaseCamera();
            } while(false);
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Closes the debug of library
        com.fuhu.konnect.library.Debug.IS_DEBUG = false;

        mRootView = inflater.inflate(R.layout.frag_mail, container, false);
        mPaintView = (PaintView) mRootView.findViewById(R.id.mail_page_paintView);

        doMail();

        return mRootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mDialog != null)
            mDialog.dismiss();
        mDialog = null;
    }

    private void doMail() {
        mEffectManager =  EffectManager.getInstance();

        Context ctx = getActivity();

//        //Loads the source for sticker button icon
//        mIconCheck = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_check);
//        mIconCross = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_cross);
//        mIconResize = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_resize);
//        mIconRotate = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_rotate);

        //Adds the main effect into PaintView
        EffectButton paintEffectButton = new EffectButton(ctx);
        EffectButton stickerEffectButton = new EffectButton(ctx);
        EffectButton wallEffectButton = new EffectButton(ctx);
        EffectButton eraserEffectButton = new EffectButton(ctx);
        EffectButton cameraEffectButton = new EffectButton(ctx);
        paintEffectButton.setEffect(new PaintEffect());
        stickerEffectButton.setEffect(new StickerEffect());
        wallEffectButton.setEffect(new WallPaperEffect());
        eraserEffectButton.setEffect(new EraserEffect());
        cameraEffectButton.setEffect(new CameraEffect());
        mPaintView.addMainEffect(paintEffectButton, (EffectGroup) paintEffectButton.getEffect());
        mPaintView.addMainEffect(stickerEffectButton, (EffectGroup) stickerEffectButton.getEffect());
        mPaintView.addMainEffect(wallEffectButton, (EffectGroup) wallEffectButton.getEffect());
        mPaintView.addMainEffect(eraserEffectButton, (EffectGroup) eraserEffectButton.getEffect());
        mPaintView.addMainEffect(cameraEffectButton, (EffectGroup) cameraEffectButton.getEffect());

        //Sets adapter to the PaintView for showing sub effects
        mSubEffectAdapter = new MySubEffectAdapter();
        mPaintView.setSubEffectAdapter(mSubEffectAdapter);
        mEffectContentAdapter = new MyEffectContentAdapter();
        mPaintView.setEffectContentAdapter(mEffectContentAdapter);

        /*
         *Initializes some settings of the PaintView
         */

        //Sets the size of effect toolbar
        View effectToolbar = mPaintView.getEffectWrapper();
        effectToolbar.setBackgroundColor(Color.parseColor("#F6F5F0"));
        RelativeLayout.LayoutParams etLp = (RelativeLayout.LayoutParams) effectToolbar.getLayoutParams();
        if(etLp != null) {
//            etLp.width = 256;
            etLp.width = 200;
        }

        //Sets the size of effect content which be shown by sub effect button
//        mPaintView.setEffectContentSize(1050, 600);
        mPaintView.setEffectContentSize(795, 500);

        //Sets the back button for sub effect list backing to main effect list
//        mPaintView.getBackEffectButton().setBackground(null);
        mPaintView.getBackEffectButton().setImageResource(R.drawable.mail_btn_back);
        RelativeLayout.LayoutParams bebLp = (RelativeLayout.LayoutParams) mPaintView.getBackEffectButton().getLayoutParams();
        bebLp.width = 111;
        bebLp.height = 110;
        bebLp.topMargin = 40;
        mPaintView.getBackEffectButton().setLayoutParams(bebLp);

        //Sets the background of drawing of PaintView
        Bitmap bg = BitmapFactory.decodeResource(getResources(), new WallPaperEffectDefault().getWallPaperResId());
        mPaintView.setDrawingBackground(bg);

        //Sets the listener for catching the effect update event
        mPaintView.setOnEffectUpdateListener(new PaintView.OnEffectUpdateListener() {
            @Override
            public boolean onPreUpdate(View paintView, Effect newEffect, Effect oldEffect) {

                if(oldEffect instanceof CameraEffect) {
                    releaseCamera();
                }

                return true;
            }

            @Override
            public void onPostUpdate(View paintView, Effect currentEffect) {
                if(currentEffect instanceof IEraserEffectAll) {
                    mDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Notification")
                            .setMessage("Erase All?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(mPaintView != null)
                                        mPaintView.clean();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else if(currentEffect instanceof com.fuhu.konnect.library.paint.effect.CameraEffect) {
                    //Not implemented, just shows how to catch the effect update event
//                    if(mDialog != null)
//                        mDialog.dismiss();
//                    mDialog = new AlertDialog.Builder(getActivity())
//                            .setTitle("Notification")
//                            .setMessage("Not implemented")
//                            .setPositiveButton("Ok", null)
//                            .show();
                    doCamera();
                } else if(currentEffect instanceof PaintEffects) {
                    IPaintEffect paintEffect = (IPaintEffect) ((EffectGroup) currentEffect).getSubEffects().get(0);
                    ((PaintView) paintView).applyEffect(paintEffect);
                } else if(currentEffect instanceof EraserEffects) {
                    // auto select the second effect
                    IEraserEffect eraserEffect = null;
                    ArrayList<Effect> effects = ((EffectGroup) currentEffect).getSubEffects();
                    for(int i=0; i<effects.size(); i++)
                        if(effects.get(i) instanceof IEraserEffect) {
                            eraserEffect = (IEraserEffect) effects.get(i);
                            break;
                        }
                    ((PaintView) paintView).applyEffect(eraserEffect);
                }


            }
        });
    }

    private void doCamera() {
        com.jack.library.Debug.IS_DEBUG = false;

        if(mCameraView != null) return;

        View cameraPreviewRoot = View.inflate(getActivity(), R.layout.mail_wrapper_camera_container, null);
        CameraView cameraView = (CameraView) cameraPreviewRoot.findViewById(R.id.mail_wrapper_cv_camera_preview);
        cameraView.setCameraOrientationLand();
        cameraView.setCameraPreviewSize(640, 480);

        //Sets the action when shutter button to be clicked
        cameraPreviewRoot.findViewById(R.id.mail_wrapper_btn_camera_shutter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCameraView == null) return;

                CameraHolder cameraHolder = mCameraView.getCameraHolder();
                if(cameraHolder != null)
                    cameraHolder.snapshot();
            }
        });
        //Adds snapshot listener into CameraView by CameraHolder of CameraView
        if(cameraView.getCameraHolder() != null)
            cameraView.getCameraHolder().addSnapshotListener(mOnSnapshotListener);
        //Starts to preview the camera on the screen
        cameraView.start();

        int w = mPaintView.getDrawingWrapper().getWidth();
        int h = mPaintView.getDrawingWrapper().getHeight();
        int left = mPaintView.getDrawingWrapper().getLeft();
        int top = mPaintView.getDrawingWrapper().getTop();

        int x = left + w/2 - 640/2; //1080/2;
        int y = top + h/2 - 561/2;  //836/2;
        FloatWindow floatWindow = new FloatWindow(cameraPreviewRoot);
        floatWindow.setCoordinate(x, y);
        floatWindow.layout();
        mCameraView = cameraView;
        mFloatView = floatWindow;
    }

    private void releaseCamera() {
        if(mCameraView != null)
            mCameraView.release();
        if(mFloatView != null)
            mFloatView.dismiss();

        mFloatView = null;
        mCameraView = null;
    }

    private class MySubEffectVH extends RecyclerView.ViewHolder {

        private EffectButton mEffectButton;

        public MySubEffectVH(EffectButton itemView) {
            super(itemView);
            mEffectButton = itemView;
        }
    }

    private class MySubEffectAdapter extends PaintView.SubEffectAdapter<MySubEffectVH> {

        private ArrayList<EffectButton> items = new ArrayList<>();

        @Override
        public MySubEffectVH onCreateViewHolder(ViewGroup parent, int viewType) {
            EffectButton view = new EffectButton(MailFragment.this.getActivity());
            items.add(view);
            return  new MySubEffectVH(view);
        }

        @Override
        public void onBindEffectToHolder(MySubEffectVH holder, Effect currentEffect, Effect effect) {
            EffectButton eb = holder.mEffectButton;
            if(eb != null) {
                eb.setEffect(effect);
                eb.setSelected((currentEffect == effect));
            }
        }

    }

    private class MyEffectContentVH extends PaintView.EffectContentHolder {

        public MyEffectContentVH(EffectContentView itemView) {
            super(itemView);
        }
    }

    private class MyEffectContentAdapter extends PaintView.EffectContentAdapter {

        @Override
        public PaintView.EffectContentHolder onCreateViewHolder(Effect currentEffect, ViewGroup parent, int viewType) {
            EffectContentView v;
            if(currentEffect instanceof IStickerEffect) {
                v = new StickerContentButton(getActivity());
            }
            else {
                v = new WallpaperContentButton(getActivity());
            }

            return new MyEffectContentVH(v);
        }

        @Override
        public void onBindViewHolder(Effect currentEffect, PaintView.EffectContentHolder holder, int position) {

            if(currentEffect instanceof IStickerEffect) {
                StickerContentButton v = (StickerContentButton) holder.effectContentView;
                int srcId = ((IStickerEffect) currentEffect).getStickerResId().get(position);
                v.setImage(srcId);
            }
            else if(currentEffect instanceof IColorWallPaperEffect) {
                WallpaperContentButton v = (WallpaperContentButton) holder.effectContentView;
                int srcId = ((IColorWallPaperEffect) currentEffect).getWallPaperResId().get(position);
                v.setImage(-1, -1, srcId);
            }
            else if(currentEffect instanceof IMultipleWallPaperEffect) {
                WallpaperContentButton v = (WallpaperContentButton) holder.effectContentView;
                Pair<Integer, Integer> srcId = ((IMultipleWallPaperEffect) currentEffect).getWallPaperResId().get(position);
                v.setImage(srcId.first, srcId.second, -1);
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
