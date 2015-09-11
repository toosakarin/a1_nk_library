package com.fuhu.konnect;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jacktseng on 2015/6/2.
 */
public class MainFragment extends Fragment{

    MainActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mActivity = (MainActivity) getActivity();

        View root = inflater.inflate(R.layout.frag_main, container, false);

        init(root);

        return root;
    }

    private void init(View root) {

        /**
         * PhotoView demo
         */
        root.findViewById(R.id.main_btn_photoDemo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.switchFragment(new PhotoFragment());
            }
        });

        /**
         *  Friend avatar demo
         */
        root.findViewById(R.id.main_btn_avatarSolid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.switchFragment(new AvatarSolidFragment2());
            }
        });

        /**
         *  Chat sticker demo
         */
        root.findViewById(R.id.main_btn_chatSticker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.switchFragment(new ChatStickerFragment2());
            }
        });
        /**
         * Mail demo
         */
        root.findViewById(R.id.main_btn_mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.switchFragment(new MailFragment());
            }
        });
//        /**
//         * Paint test
//         */
//        root.findViewById(R.id.main_btn_paint).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mActivity.switchFragment(new TestPaintFragment());
//            }
//        });
    }

}
