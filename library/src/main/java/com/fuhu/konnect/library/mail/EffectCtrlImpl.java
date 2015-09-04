package com.fuhu.konnect.library.mail;

import android.view.ViewGroup;

import com.fuhu.konnect.library.mail.effect.Effect;
import com.fuhu.konnect.library.mail.effect.EffectGroup;
import com.fuhu.konnect.library.mail.effect.EraserEffects;
import com.fuhu.konnect.library.mail.effect.IEraserEffect;
import com.fuhu.konnect.library.mail.effect.IPaintEffect;
import com.fuhu.konnect.library.mail.effect.ISingleWallPaperEffect;
import com.fuhu.konnect.library.mail.effect.PaintEffects;
import com.fuhu.konnect.library.view.DrawingView;
import com.fuhu.konnect.library.view.PaintView;

import java.util.ArrayList;

/**
 * Created by jacktseng on 2015/8/25.
 */

@Deprecated
public class EffectCtrlImpl implements EffectCtrl {

    private Effect mCurrentEffect;

    @Override
    public Effect getCurrentEffect() {
        return mCurrentEffect;
    }

    @Override
    public void applyEffect(PaintView paintView, Effect newEffect) {

        //just to stop current effect
        if(mCurrentEffect != null)
            mCurrentEffect.cancel();

        /**
         * Assigns the new effect
         */
        if(paintView == null) return;

        final DrawingView drawingView = paintView.getDrawingView();
        final ViewGroup drawingWrapper = paintView.getDrawingWrapper();
        drawingView.setEditable(false);

        if (newEffect instanceof ISingleWallPaperEffect) {
            drawingWrapper.setBackgroundResource(((ISingleWallPaperEffect) newEffect).getWallPaperResId());
        } else if (newEffect instanceof IPaintEffect) {
            drawingView.setPaint(((IPaintEffect) newEffect).getPaint());
            drawingView.setEditable(true);
        } else if (newEffect instanceof IEraserEffect) {
            drawingView.setEraser(((IEraserEffect) newEffect).getPaint());
            drawingView.setEditable(true);
        } else if (newEffect instanceof PaintEffects) {
            // auto select the first effect
            IPaintEffect paintEffect = (IPaintEffect) ((EffectGroup) newEffect).getSubEffects().get(0);
            drawingView.setPaint(paintEffect.getPaint());
        } else if (newEffect instanceof EraserEffects) {
            // auto select the second effect
            IEraserEffect eraserEffect = null;
            ArrayList<Effect> effects = ((EffectGroup) newEffect).getSubEffects();
            for(int i=0; i<effects.size(); i++)
                if(effects.get(i) instanceof IEraserEffect) {
                    eraserEffect = (IEraserEffect) effects.get(i);
                    break;
                }
            drawingView.setEraser(eraserEffect.getPaint());
        }

        mCurrentEffect = newEffect;
    }

}
