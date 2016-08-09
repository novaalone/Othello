package com.ma.nova.othello_try_3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2015/11/7.
 */
public class RecordPage extends MyActivity implements View.OnClickListener{
    public static RecordDB recordDB;
    public static Cursor mcursor;
    private ListView listView;

    private Button btn_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_page);
        setupViews();
    }

    public void setupViews()
    {
        recordDB = new RecordDB(this);
       mcursor = recordDB.select();
        btn_clear = (Button)findViewById(R.id.clear);
        btn_clear.setOnClickListener(this);
        listView = (ListView)findViewById(R.id.record_list);
        listView.setAdapter(new RecordListAdapter(this, mcursor));


    }


    public class RecordListAdapter extends BaseAdapter
    {
        private Context mcontext;
        private Cursor mcursor;

        public RecordListAdapter(Context context,Cursor cursor)
        {
            mcontext = context;
            mcursor = cursor;
        }

        @Override
        public int getCount() {
            return mcursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView mTextView = new TextView(mcontext);
            mcursor.moveToPosition(position);
            mTextView.setText("          " + mcursor.getString(0) + "                    " + mcursor.getString(1) + "                     " + mcursor.getString(2));
            mTextView.setTextColor(getResources().getColor(R.color.fontcolor));


            return mTextView;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mcursor.close();
    }

    public static void addrecord(String winner,String time){


        mcursor = recordDB.select();
        if (winner.equals("") || time.equals("")){
            return;
        }
        recordDB.insert(winner, time);
        mcursor.requery();



    }
    public void clearrecord(){

        recordDB.clear();
        mcursor.requery();
        listView.invalidateViews();

    }

    @Override
    public void onClick(View v) {

        clearrecord();
    }



}
