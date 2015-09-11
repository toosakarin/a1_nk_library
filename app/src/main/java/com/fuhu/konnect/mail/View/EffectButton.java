package com.fuhu.konnect.mail.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.Effect;

public class EffectButton extends RelativeLayout {

    public static final String TAG = EffectButton.class.getSimpleName();

    private static EffectButton CURRENT_EFFECT_BTN;
    private static View.OnClickListener OCL = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(CURRENT_EFFECT_BTN != null)
                CURRENT_EFFECT_BTN.setSelected(false);
            if(view instanceof EffectButton) {
                view.setSelected(true);
                CURRENT_EFFECT_BTN = (EffectButton) view;
            }
        }
    };

    private Context mContext;
    private RelativeLayout mWrapper;
    private ImageView mButton;
    private ImageView mButtonRing;

    private Effect mEffect;



    public EffectButton(Context context) {
        super(context);

        this.mContext = context;

        // inflate layout
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.mail_effect_button_widget, this);

        mWrapper = (RelativeLayout) this.findViewById(R.id.effect_button_container);
        mButton = (ImageView) mWrapper.findViewById(R.id.effect_button_image);
        mButtonRing = (ImageView) mWrapper.findViewById(R.id.effect_button_image_ring);

//        this.setOnClickListener(OCL);
    }

    public void setEffect(Effect effect) {
        mEffect = effect;
        mButton.setImageResource(mEffect.getEffectIconRes());
    }

    public Effect getEffect() {
        return mEffect;
    }

    public void setSelected(boolean selected) {
        if (selected) {
            mButtonRing.setVisibility(View.VISIBLE);
            if(CURRENT_EFFECT_BTN != null && CURRENT_EFFECT_BTN != this) {
                CURRENT_EFFECT_BTN.setSelected(false);
                CURRENT_EFFECT_BTN = this;
            }
        }
        else
            mButtonRing.setVisibility(View.INVISIBLE);


    }
}
