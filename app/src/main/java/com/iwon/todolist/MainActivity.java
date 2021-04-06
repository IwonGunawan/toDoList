package com.iwon.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.iwon.todolist.utils.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText tvNote;
    LinearLayout lLDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNote = findViewById(R.id.tv_note);
        lLDate = findViewById(R.id.lL_date);

        lLDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lL_date:
                showDatePicker();
                break;

            default:
                break;
        }
    }

    void showDatePicker(){
        CalendarDialog dialog = new CalendarDialog(this);
        dialog.show();



    }

}