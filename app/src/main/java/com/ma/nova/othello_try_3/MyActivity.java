package com.ma.nova.othello_try_3;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by user on 2015/11/7.
 */
public class MyActivity extends Activity {


    public static Music musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(musicPlayer == null) {

            musicPlayer = new Music(getApplicationContext());
            musicPlayer.playMusic(R.raw.back);
        }
    }
}
