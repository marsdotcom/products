package com.example.koldun.product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    EditText heightEdit,widthEdit,lengthEdit,countEdit;
    Button   okButton,canselButton;
    LinearLayout heightLayout;

    Boolean edit,battens;
    String table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        heightEdit = (EditText)findViewById(R.id.heightEdit);
        widthEdit = (EditText)findViewById(R.id.widthEdit);
        lengthEdit = (EditText)findViewById(R.id.lengthEdit);
        countEdit = (EditText)findViewById(R.id.countEdit);

        okButton = (Button)findViewById(R.id.okButton);
        canselButton = (Button)findViewById(R.id.canselButton);

        okButton.setOnClickListener(this);
        canselButton.setOnClickListener(this);

        edit = getIntent().getBooleanExtra("edit",false);

        battens = getIntent().getBooleanExtra("battens",false);

        heightEdit.setText(getIntent().getStringExtra("height"));
        widthEdit.setText(getIntent().getStringExtra("width"));
        lengthEdit.setText(getIntent().getStringExtra("length"));
        countEdit.setText(getIntent().getStringExtra("count"));

        heightLayout = (LinearLayout)findViewById(R.id.heightLayout);

        if (battens)
            heightLayout.setVisibility(View.VISIBLE);
        else
            heightLayout.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.okButton){

            Intent intent = new Intent();

            intent.putExtra("height",heightEdit.getText().toString());
            intent.putExtra("width",widthEdit.getText().toString());
            intent.putExtra("length",lengthEdit.getText().toString());
            intent.putExtra("count",countEdit.getText().toString());

            setResult(RESULT_OK,intent);
        }
        finish();

    }
}
