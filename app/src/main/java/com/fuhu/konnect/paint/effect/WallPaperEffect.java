package com.fuhu.konnect.paint.effect;


import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.Effect;
import com.fuhu.konnect.library.paint.effect.WallPaperEffects;

import java.util.ArrayList;

public class WallPaperEffect extends WallPaperEffects {

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
		return R.drawable.mail_icon_wallpaper;
	}

	@Override
	public ArrayList<Effect> getSubEffects() {
		if(m_SubItems == null)
		{
			m_SubItems = new ArrayList<Effect>();
			m_SubItems.add(new WallPaperEffectDefault());
			m_SubItems.add(new WallPaperColorEffect());
			m_SubItems.add(new WallPaperSceneEffect());		
//			m_SubItems.add(new WallPaperTextureEffect());
		}
		return m_SubItems;
	}

}
