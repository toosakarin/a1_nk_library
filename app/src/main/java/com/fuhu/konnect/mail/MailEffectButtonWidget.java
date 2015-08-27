package com.fuhu.konnect.mail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.mail.effect.Effect;

public class MailEffectButtonWidget extends RelativeLayout {

    /*======================================================================
     * Constant Fields
     *=======================================================================*/
    public static final String TAG = "MailEffectButtonWidget";
    private Context m_Context;
    private RelativeLayout m_BackgroundContainer;
    private ImageView m_ButtonImage;
    private ImageView m_ButtonImageRing;
    private Effect m_Effect;

    public static final int BUTTON_ID = 100;

    public MailEffectButtonWidget(Context context) {
        super(context);

        this.m_Context = context;

        // inflate layout
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.mail_effect_button_widget, this);

        m_BackgroundContainer = (RelativeLayout) this.findViewById(R.id.effect_button_container);
        m_ButtonImage = (ImageView) m_BackgroundContainer.findViewById(R.id.effect_button_image);
        m_ButtonImageRing = (ImageView) m_BackgroundContainer.findViewById(R.id.effect_button_image_ring);

    }

    public void setEffect(Effect effect) {
        m_Effect = effect;
        m_ButtonImage.setImageResource(m_Effect.getEffectIconRes());
    }

    public Effect getEffect() {
        return m_Effect;
    }

    public void setSelected(boolean selected) {
        if (selected)
            m_ButtonImageRing.setVisibility(View.VISIBLE);
        else
            m_ButtonImageRing.setVisibility(View.INVISIBLE);
    }



}
