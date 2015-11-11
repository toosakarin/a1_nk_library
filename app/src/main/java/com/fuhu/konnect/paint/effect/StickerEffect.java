package com.fuhu.konnect.paint.effect;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.Effect;
import com.fuhu.konnect.library.paint.effect.StickerEffects;

import java.util.ArrayList;

public class StickerEffect extends StickerEffects {

    ArrayList<Effect> m_SubItems;

    @Override
    public void apply() {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancel() {

    }

    @Override
    public int getEffectIconRes() {
        return R.drawable.mail_icon_sticker;
    }

    @Override
    public ArrayList<Effect> getSubEffects() {
        if (m_SubItems == null) {
            m_SubItems = new ArrayList();
//            m_SubItems.add(new StickerEffectA());
//            m_SubItems.add(new StickerEffectB());
//            m_SubItems.add(new StickerEffectC());
//            m_SubItems.add(new StickerEffectD());
//            m_SubItems.add(new StickerEffectE());
            m_SubItems.add(new StickerEffectF());
            m_SubItems.add(new StickerEffectG());
//            m_SubItems.add(new StickerEffectH());
        }
        return m_SubItems;
    }

}
