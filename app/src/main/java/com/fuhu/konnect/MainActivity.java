package com.fuhu.konnect;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

/**
 * Created by jacktseng on 2015/6/2.
 */
public class MainActivity extends FragmentActivity {

    Fragment mCurrentFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        switchFragment(new MainFragment());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(!(mCurrentFrag instanceof MainFragment)) {
                    switchFragment(new MainFragment());
                    return true;
                }
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.main_container, fragment);
//        transaction.add(R.id.main_container, fragment);
        transaction.commit();

        mCurrentFrag = fragment;
    }

}
