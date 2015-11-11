package com.fuhu.konnect.paint.effect;

import com.fuhu.konnect.library.paint.effect.Effect;

import java.util.ArrayList;

public class EffectManager {

	public static final String TAG = EffectManager.class.getSimpleName();

    public static EffectManager INSTANCE;

	private ArrayList<Effect> mAllEffects;


	static {

        if(INSTANCE == null) {
            ArrayList<Effect> effects = new ArrayList<>();
            effects.add(new PaintEffect());
//			effects.add(new TextEffect());
//			effects.add(new EraserEffect());
            effects.add(new StickerEffect());
//			effects.add(new CameraEffect());
            effects.add(new WallPaperEffect());

            INSTANCE = new EffectManager();
            INSTANCE.mAllEffects = effects;
        }


	}

    public static EffectManager getInstance() {
        return INSTANCE;
    }


    private EffectManager() {

    }

	public ArrayList<Effect> getAllEffects()
	{
		return mAllEffects;
	}
	
//	public void clearEffect()
//	{
//		applyEffect(new NoneEffect());
//	}
//

	

}
