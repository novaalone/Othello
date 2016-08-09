package com.ma.nova.othello_try_3;

import android.content.Context;
import android.media.MediaPlayer;


/**
 * Created by user on 2015/11/7.
 */
public class Music implements MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener {

    private MediaPlayer bgPlayer;
    private Context context;
    public Music(Context context)
    {
        this.context = context;
    }
    @Override
    public void onCompletion(MediaPlayer mp) {

    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        stopMusic();
        return false;
    }

    public void playMusic(int paramInt)
    {
        stopMusic();

        try {
            //利用音频文件创建一个MeidaPlayer
            MediaPlayer mediaPlayer = MediaPlayer.create(context, paramInt);
            bgPlayer = mediaPlayer;
            bgPlayer.setOnCompletionListener(this);
            bgPlayer.setVolume(1,1);
            //设置是否循环播放
            bgPlayer.setLooping(true);
            //开始播放
            bgPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    public void stopMusic() {
        if(bgPlayer == null)
            return;
        if(bgPlayer.isPlaying())
            bgPlayer.stop();
        bgPlayer.release();
        bgPlayer = null;
    }
}
