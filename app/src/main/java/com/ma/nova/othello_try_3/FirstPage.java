package com.ma.nova.othello_try_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by user on 2015/10/31.
 */
public class FirstPage extends MyActivity implements View.OnClickListener{

    private Button btn_start,btn_record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);


        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        btn_record = (Button)findViewById(R.id.btn_record);
        btn_record.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_start:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_record:
                Intent intent2 = new Intent(this,RecordPage.class);
                startActivity(intent2);
        }

    }
}
