package com.example.koldun.product;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity {

    Cursor c;

    DBHelper mDBHelper;

    private Intent mIntent;

    private SimpleCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntent = new Intent(MainActivity.this,ActivityTwo.class);

        mDBHelper = new DBHelper(this);

       // подключаемся к БД
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        c = db.query("products",null,null,null,null,null,null);
        startManagingCursor(c);


        int count = c.getCount();

        String[] from = new String[] {"_id", "name","alias" };
        int[] to = new int[] {R.id.id,R.id.name,R.id.alias  };

        mCursorAdapter = new SimpleCursorAdapter(this, R.layout.item,c, from, to);

        setListAdapter(mCursorAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        c.moveToPosition(position);

        String S = c.getString(0);
        String Name = c.getString(1);

        mIntent.putExtra("box",S);
        mIntent.putExtra("name",Name);

        startActivity(mIntent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDBHelper.close();
        c.close();

    }


}
