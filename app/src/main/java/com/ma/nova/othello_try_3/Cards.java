package com.ma.nova.othello_try_3;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 2015/10/23.
 */
public class Cards extends FrameLayout {
    public Cards(Context context) {
        super(context);

      //  label = new TextView(getContext());
        imageView = new ImageView(getContext());
     //   label.setTextSize(32);
    //    label.setGravity(Gravity.CENTER);
        imageView.setForegroundGravity(Gravity.CENTER);
     //   label.setBackgroundColor(0x77ffffff);
        imageView.setBackgroundColor(0x77ffffff);
        LayoutParams lp = new LayoutParams(-1,-1);

        lp.setMargins(1, 1, 0, 0);
      //  addView(label, lp);
        addView(imageView, lp);
        setNum(0);

    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;


    }



    private int num=0;
 //   private TextView label;
     ImageView imageView;

}
