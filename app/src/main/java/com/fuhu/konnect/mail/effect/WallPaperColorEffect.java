package com.fuhu.konnect.mail.effect;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.IColorWallPaperEffect;

import java.util.ArrayList;

public class WallPaperColorEffect extends IColorWallPaperEffect {

    ArrayList<Integer> m_WallPaperList;

	@Override
	public void apply() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void cancel() {

    }

    @Override
	public int getEffectIconRes() {
		return R.drawable.mail_icon14;
	}

	@Override
	public ArrayList<Integer> getWallPaperResId() {
		if(m_WallPaperList == null)
		{
			m_WallPaperList = new ArrayList<Integer>();
			m_WallPaperList.add(R.color.wallpaper_bg_1);
			m_WallPaperList.add(R.color.wallpaper_bg_2);
			m_WallPaperList.add(R.color.wallpaper_bg_3);
			m_WallPaperList.add(R.color.wallpaper_bg_4);
			m_WallPaperList.add(R.color.wallpaper_bg_5);
			m_WallPaperList.add(R.color.wallpaper_bg_6);
			m_WallPaperList.add(R.color.wallpaper_bg_7);
			m_WallPaperList.add(R.color.wallpaper_bg_8);
			m_WallPaperList.add(R.color.wallpaper_bg_9);
			m_WallPaperList.add(R.color.wallpaper_bg_10);
			m_WallPaperList.add(R.color.wallpaper_bg_11);
			m_WallPaperList.add(R.color.wallpaper_bg_12);
		}
	
		return m_WallPaperList;
	}

}
