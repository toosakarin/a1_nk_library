package com.fuhu.konnect.mail.effect;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.paint.effect.ISingleWallPaperEffect;

public class WallPaperEffectDefault extends ISingleWallPaperEffect {

	@Override
	public void apply() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void cancel() {

    }

    @Override
	public int getEffectIconRes() {
		return R.drawable.mail_icon_eraser_all;
	}


	@Override
	public int getWallPaperResId() {
		return R.drawable.mail_wallpaper_default;
	}
}
