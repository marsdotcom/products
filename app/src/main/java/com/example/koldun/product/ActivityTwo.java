package com.example.koldun.product;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
    static final int CM_ADD_ID = 3;
    static final int DIALOG_DEL = 1;

    final int REQUEST_EDIT = 1;
    final int REQUEST_ADD  = 2;

    Cursor mCursor;
    DBHelper mDBHelper;
    ListView mListView;
    EditText mEditText;
    SimpleCursorAdapter mCursorAdapter;
    String mText;
    int count;
    RadioButton mRadioButton1,mRadioButton2;
    String[] from;
    int[] to;
    long mItemID;
    int mItemPosition;



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

        String name = getIntent().getStringExtra("name");

        count=1;

        mCursor = db.rawQuery(generateSQL(),new String[]{});

        startManagingCursor(mCursor);

        from = new String[] {"A", "B", "C" };
        to = new int[] { R.id.A,R.id.B,R.id.C  };

        mCursorAdapter = new SimpleCursorAdapter(this, R.layout.item2, mCursor, from, to);

        TextView textView = (TextView) findViewById(R.id.textView);

        textView.setText(name);

        mListView.setAdapter(mCursorAdapter);

        registerForContextMenu(mListView);

    }

    String generateSQL(){

        String R;

        if (mRadioButton1.isChecked())
            R = "select _id,  size as A,length as B, count*" + count + " as C from battens where n_id = " + mText;
        else
            R = "select _id, width as A,length as B, count*" + count + "  as C from plywoods where n_id = " + mText;
        return R;

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID,   0,"Изменить");
        menu.add(0, CM_DELETE_ID, 0, "Удалить");
        menu.add(0,CM_ADD_ID,0,"Добавить");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        mItemID = acmi.id;
        mItemPosition = acmi.position;

        switch (item.getItemId()){

            case CM_EDIT_ID:  recEdit(); break;
            case CM_DELETE_ID:showDialog(DIALOG_DEL); break;
            case CM_ADD_ID: recAdd();break;
        }

        return super.onContextItemSelected(item);
    }


    void recAdd(){

        Intent intent = new Intent(this,EditActivity.class);
        intent.putExtra("battens",mRadioButton1.isChecked());
        startActivityForResult(intent,REQUEST_ADD);

    }


    void  recEdit(){

       // SQLiteDatabase db = mDBHelper.getWritableDatabase();

        String table,height,width,length,scount;

        mCursor.moveToPosition(mItemPosition);
        String A = mCursor.getString(1);
        String B = mCursor.getString(2);
        String C = mCursor.getString(3);

        if (mRadioButton1.isChecked()) {
            table = "battens";
            String[] S = A.split("х");
            if (S.length < 2) S = A.split("x");
            height = S[0];
            width  = S[1];
        } else {
            table = "plywoods";
            width = A;
            height = "";
        }

        length = B;
        scount = Integer.toString((Integer.parseInt(C)/count));

        Intent intent = new Intent(this,EditActivity.class);
        intent.putExtra("battens",mRadioButton1.isChecked());
        intent.putExtra("edit",true);
        intent.putExtra("height",height);
        intent.putExtra("width",width);
        intent.putExtra("length",length);
        intent.putExtra("count",scount);

        startActivityForResult(intent,REQUEST_EDIT);

        //db.update(table)

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (data == null) return;

        String table, height, width, length, scount;

        ContentValues cv = new ContentValues();


        height = data.getStringExtra("height");
        width = data.getStringExtra("width");
        length = data.getStringExtra("length");
        scount = data.getStringExtra("count");

        if (mRadioButton1.isChecked()) {
            table = "battens";
            width = height + "x" + width;
            cv.put("size", width);

        } else {
            table = "plywoods";
            cv.put("width", width);
        }

        cv.put("length", length);
        cv.put("count", scount);

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        if (requestCode == REQUEST_EDIT) {
            db.update(table, cv, "_id = " + mItemID, new String[]{});
        }else if (requestCode == REQUEST_ADD){
            cv.put("n_id",mText);
            db.insert(table,null,cv);
        }

        mCursor.requery();

    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            if (which == Dialog.BUTTON_POSITIVE){

                SQLiteDatabase db = mDBHelper.getWritableDatabase();

                String table;

                if (mRadioButton1.isChecked()) table = "battens"; else table = "plywoods";

                db.delete(table,"_id = " + mItemID,null);

                mCursor.requery();
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

        stopManagingCursor(mCursor);

        mCursor.close();

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        mCursor = db.rawQuery(generateSQL(),new String[]{});

        startManagingCursor(mCursor);

        mCursorAdapter.changeCursor(mCursor);

    }

    @Override
    public void onClick(View v) {

        stopManagingCursor(mCursor);

        mCursor.close();

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        mCursor = db.rawQuery(generateSQL(),new String[]{});

        startManagingCursor(mCursor);

        mCursorAdapter.changeCursor(mCursor);

    }
}
