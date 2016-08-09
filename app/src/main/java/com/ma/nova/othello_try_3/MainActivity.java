package com.ma.nova.othello_try_3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends MyActivity {

    private Button white_count,black_count;
     Button btn_newGame,btn_hint;
    private static MainActivity mainActivity = null;
    private ImageView img_turn;

    private Long starttine;
    private Handler handler = new Handler();

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public MainActivity() {
mainActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_gameview);

        white_count = (Button) findViewById(R.id.white_count);
        black_count = (Button)findViewById(R.id.black_count);
        btn_newGame = (Button) findViewById(R.id.btn_newGame);
        img_turn = (ImageView)findViewById(R.id.img_turn);
        btn_hint = (Button)findViewById(R.id.btn_hint);
       timer = (TextView)findViewById(R.id.time);


     getTime();

    }
     TextView timer;

    public void getTime()
    {
        starttine = System.currentTimeMillis();
        handler.removeCallbacks(updateTimer);
        handler.postDelayed(updateTimer,1000);
    }
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {


            Long spentTime = System.currentTimeMillis()-starttine;
            Long minus = (spentTime/1000)/60;
            Long seconds = (spentTime/1000)%60;
            timer.setText(minus+":"+seconds);
            handler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onDestroy() {
        musicPlayer.stopMusic();
        super.onDestroy();

    }


    public void showNum(int whitenum,int blacknum) {

        white_count.setText(whitenum + "");
        black_count.setText(blacknum + "");
    }


    public void showTurn(boolean turn)
    {
        if(turn)
        {
            img_turn.setImageResource(R.drawable.white_chess);
        }
        else if(!turn)
        {
            img_turn.setImageResource(R.drawable.black_chess);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
