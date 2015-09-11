package com.fuhu.konnect.mail.effect;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.Effect;
import com.fuhu.konnect.library.paint.effect.EraserEffects;

import java.util.ArrayList;

public class EraserEffect extends EraserEffects {

    private ArrayList<Effect> m_SubItems;

	@Override
	public void apply() {
		// TODO Auto-generated method stub
	}

    @Override
    public void cancel() {

    }

    @Override
	public int getEffectIconRes() {
		return R.drawable.mail_icon_eraser;
	}

	@Override
	public ArrayList<Effect> getSubEffects() {
		if(m_SubItems == null)
		{
			m_SubItems = new ArrayList<>();
			m_SubItems.add(new EraserEffectAll());
			m_SubItems.add(new EraserEffectBold());
			m_SubItems.add(new EraserEffectThin());		
		}
		return m_SubItems;
	}

}
