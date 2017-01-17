package com.example.koldun.product;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by koldun on 02.01.2017.
 */

public class ActivityTwo extends Activity implements TextWatcher,View.OnClickListener {

    static final int CM_DELETE_ID = 1;
    static final int CM_EDIT_ID = 2;
    static final int DIALOG_DEL = 1;

    Cursor mCursor;
    DBHelper mDBHelper;
    ListView mListView;
    EditText mEditText;
    SimpleCursorAdapter mCursorAdapter;
    String mText;
    int count;
    RadioButton mRadioButton1,mRadioButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_layout);

        mRadioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        mRadioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        mRadioButton1.setOnClickListener(this);
        mRadioButton2.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.listView);

        mEditText = (EditText) findViewById(R.id.editText);

        mEditText.addTextChangedListener(this);

        mDBHelper = new DBHelper(this);

        // подключаемся к БД
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        mText = getIntent().getStringExtra("box");

        mCursor = db.rawQuery("select name from products where _id = "+mText,new String[]{});
        mCursor.moveToFirst();

        String name = mCursor.getString(0);

        mCursor.close();

        count=1;

        mCursor = db.rawQuery("select _id +20001 as _id, width as A,length as B, count*" + Integer.toString(count) + "  as C from plywoods where n_id = " + mText +
                " union select  _id+20000 as _id, \"фанера\" as A,\"\" as B, \"\"  as C from plywoods where _id = 1"+
                " union select _id+1,  size,length, count*" + Integer.toString(count) + "  from battens where n_id = " + mText+
                " union select  _id as _id, \"рейки\" as A,\"\" as B, \"\"  as C from battens where _id = 1",
                new String[]{});

        startManagingCursor(mCursor);

        String[] from = new String[] {"A", "B", "C" };
        int[] to = new int[] { R.id.A,R.id.B,R.id.C  };

        mCursorAdapter = new SimpleCursorAdapter(this, R.layout.item2, mCursor, from, to);

        TextView textView = (TextView) findViewById(R.id.textView);

        textView.setText(name);

        mListView.setAdapter(mCursorAdapter);

        registerForContextMenu(mListView);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID,   0,"Изменить");
        menu.add(0, CM_DELETE_ID, 0, "Удалить");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long L = acmi.id;

        switch (item.getItemId()){

            case CM_EDIT_ID:  recEdit(L); break;
            case CM_DELETE_ID:recDelete(L); break;
        }

        return super.onContextItemSelected(item);
    }

    void  recDelete(long l){

        showDialog(DIALOG_DEL);

    }

    void  recEdit(long l){

    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DEL) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle("Предупреждение");
            // сообщение
            adb.setMessage("Удалить запись?");
            // иконка
           // adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка положительного ответа
            adb.setPositiveButton("Да", myClickListener);
            // кнопка отрицательного ответа
            adb.setNegativeButton("Нет", myClickListener);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            if (which == Dialog.BUTTON_POSITIVE){

                SQLiteDatabase db = mDBHelper.getWritableDatabase();



            }
        }
    };




    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDBHelper.close();
        mCursor.close();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        try {
            count = Integer.parseInt(mEditText.getText().toString());
        }catch (NumberFormatException e){
            count = 1;
        }

        mCursor.close();

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        mCursor = db.rawQuery(
                        "select _id +20001 as _id, width as A,length as B, count*" + Integer.toString(count) + "  as C from plywoods where n_id = " + mText +
                        " union select  _id+20000 as _id, \"фанера\" as A,\"\" as B, \"\"  as C from plywoods where _id = 1"+
                        " union select _id+1,  size,length, count* "+Integer.toString(count) +" from battens where n_id = " + mText+
                        " union select  _id as _id, \"рейки\" as A,\"\" as B, \"\"  as C from battens where _id = 1",
                new String[]{});



        mCursorAdapter.changeCursor(mCursor);

    }

    @Override
    public void onClick(View v) {

        RadioButton RB = (RadioButton)v;
        if (RB.getId() == R.id.radioButton1) {

        }
        else {

        }

    }
}
