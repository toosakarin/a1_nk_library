package com.fuhu.konnect.mail.View;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fuhu.konnect.R;
import com.fuhu.konnect.library.view.EffectContentStickerView;
import com.fuhu.konnect.library.view.NabiStickerView;

public class StickerContentButton extends EffectContentStickerView {

	public static final String TAG = StickerContentButton.class.getSimpleName();

	private Context m_Context;

	private RelativeLayout m_BackgroundContainer;
	private ImageView mStickerImageView;
	private int mStickerImageResId;
	private int m_ImageWidth;
	private int m_ImageHeight;

    private OnClickListener mOnClickListener;

    private static Bitmap mIconCheck, mIconCross, mIconResize, mIconRotate;

    {
        if(mIconCheck == null || mIconCross == null || mIconResize == null || mIconRotate == null) {
            Context ctx = getContext();
            mIconCheck = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_check);
            mIconCross = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_cross);
            mIconResize = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_resize);
            mIconRotate = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.stickerwidget_rotate);
        }
    }

	public StickerContentButton(Context context) {
		this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	public StickerContentButton(Context context, int imageWidth, int imageHeight) {
		super(context, null);
		this.m_Context = context;
		this.m_ImageWidth = imageWidth;
		this.m_ImageHeight = imageHeight;

		// inflate layout
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.mail_sticker_widget, this);

		m_BackgroundContainer = (RelativeLayout)this.findViewById(R.id.mail_sticker_widget_background);
		mStickerImageView = (ImageView)m_BackgroundContainer.findViewById(R.id.mail_sticker_top_image);

        //jack
        setClickButton(mStickerImageView);
	}

    @Override
    public View getContent() {
        Bitmap bmp = getImage();
        NabiStickerView rtn = new NabiStickerView(getContext(), bmp);
        rtn.setCheckButtonImage(mIconCheck);
        rtn.setCrossButtonImage(mIconCross);
        rtn.setResizeButtonImage(mIconResize);
        rtn.setRotateButtonImage(mIconRotate);

        return rtn;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }

	public void setImage(int imageRes)
	{
		this.mStickerImageResId = imageRes;
		if(mStickerImageResId != 0)
		{
			mStickerImageView.setImageResource(mStickerImageResId);

			LayoutParams params = (LayoutParams) mStickerImageView.getLayoutParams();
			if(m_ImageWidth > 0)
				params.width = m_ImageWidth;
			if(m_ImageHeight > 0)
				params.height = m_ImageHeight;
			mStickerImageView.requestLayout();


			mStickerImageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i(TAG, "sticker is clicked");
//                    if(mOnClickListener != null)
//                        mOnClickListener.onClick(StickerContentWidget.this);
                }
            });
		}
	}

    public Bitmap getImage() {
        Bitmap rtn = null;
        if(mStickerImageView != null)
            rtn = ((BitmapDrawable) mStickerImageView.getDrawable()).getBitmap();

        return rtn;
    }



	
}
