package com.fuhu.konnect.paint.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.view.EffectContentWallpaperView;

public class WallpaperContentButton extends EffectContentWallpaperView {

	public static final String TAG = "MailWallpaperWidget";


	private Context m_Context;

	private RelativeLayout m_BackgroundContainer;
	private View mThumbView;
	private int mThumbImageResId;
	private int mWallpaperImageResId;
	private int mWallpaperColorId;

    private OnClickListener mOnClickListener;



	public WallpaperContentButton(Context context) {
		super(context, null);

		this.m_Context = context;

		// inflate layout
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.mail_wallpaper_widget, this);

		m_BackgroundContainer = (RelativeLayout)this.findViewById(R.id.mail_wallpaper_widget_background);
		mThumbView = m_BackgroundContainer.findViewById(R.id.mail_wallpaper_top_image);

        setClickButton(mThumbView);
	}

    @Override
    public Integer getContent() {
        return (mWallpaperColorId > 0) ? mWallpaperColorId : mWallpaperImageResId;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }

	public void setImage(int imageRes, int thumbImageRes, int colorId)
	{
        this.mWallpaperImageResId = imageRes;
        this.mThumbImageResId = thumbImageRes;
		this.mWallpaperColorId = colorId;

		if(mThumbImageResId > 0) {
			mThumbView.setBackgroundResource(mThumbImageResId);
		} else if(mWallpaperColorId > 0) {
			mThumbView.setBackgroundColor(m_Context.getResources().getColor(mWallpaperColorId));
		}

        mThumbView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int src = (mWallpaperColorId > 0) ? mWallpaperColorId : mWallpaperImageResId;
                Log.e(TAG, "wallpaper src=" + src + " is clicked!");
            }
        });
	}

    public Bitmap getImage() {
        Bitmap rtn = null;



        return rtn;
    }

	
}
