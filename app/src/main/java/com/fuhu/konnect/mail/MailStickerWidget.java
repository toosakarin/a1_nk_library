package com.fuhu.konnect.mail;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fuhu.konnect.R;

public class MailStickerWidget extends RelativeLayout {

	public static final String TAG = "MailStickerWidget";
	
	private Context m_Context;

	private RelativeLayout m_BackgroundContainer;
	private ImageView mStickerImageView;
	private int mStickerImageResId;
	private int m_ImageWidth;
	private int m_ImageHeight;


	public MailStickerWidget(Context context) {
		this(context, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	}

	public MailStickerWidget(Context context, int imageWidth, int imageHeight) {
		super(context, null);

		this.m_Context = context;
		this.m_ImageWidth = imageWidth;
		this.m_ImageHeight = imageHeight;

		// inflate layout
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.mail_sticker_widget, this);

		m_BackgroundContainer = (RelativeLayout)this.findViewById(R.id.mail_sticker_widget_background);
		mStickerImageView = (ImageView)m_BackgroundContainer.findViewById(R.id.mail_sticker_top_image);

	}

	public void setImage(int imageRes)
	{
		this.mStickerImageResId = imageRes;
		if(mStickerImageResId != 0)
		{
			mStickerImageView.setImageResource(mStickerImageResId);

			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mStickerImageView.getLayoutParams();
			if(m_ImageWidth > 0)
				params.width = m_ImageWidth;
			if(m_ImageHeight > 0)
				params.height = m_ImageHeight;
			mStickerImageView.requestLayout();


			mStickerImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Bitmap stickerBitmap = ((BitmapDrawable) mStickerImageView.getDrawable()).getBitmap();
                    Log.e(TAG, "sticker is clicked");
                }
            });
		}
	}


	
}
