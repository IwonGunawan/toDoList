package com.iwon.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.iwon.todolist.adapter.TodoAdapter;
import com.iwon.todolist.helper.DbHelper;
import com.iwon.todolist.utils.*;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "track_todo";

    TextInputEditText tvNote;
    LinearLayout lLDate;
    MaterialCalendarView mcv;
    RecyclerView recyclerView;
    Button btnSubmit;
    TextView tvDescLeft;
    String sNote;
    String sDate;

    DbHelper dbHelper = new DbHelper(this);
    TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNote = findViewById(R.id.tv_note);
        lLDate = findViewById(R.id.lL_date);
        recyclerView = findViewById(R.id.recycleview);
        tvDescLeft = findViewById(R.id.tv_desc_left);

        lLDate.setOnClickListener(view -> showDatePicker());
        getAllData();
    }

    private void getAllData(){
        ArrayList<HashMap<String, String>> list = dbHelper.allData();
        todoAdapter = new TodoAdapter(list);
        recyclerView.setAdapter(todoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalData(list.size());
    }

    private void showDatePicker(){
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_calendar);
        dialog.show();

        mcv = dialog.findViewById(R.id.calendar_view);
        btnSubmit = dialog.findViewById(R.id.btn_submit);
        String[] today = getToday();

        mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        mcv.state().edit()
                .setMinimumDate(CalendarDay.from(2011, 7, 16))
                .setMaximumDate(CalendarDay.from(Integer.parseInt(today[0]), Integer.parseInt(today[1]) -1, Integer.parseInt(today[2])))
                .commit();
        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                sDate = (date.getMonth() +1 ) + "-" + date.getDay() + "-" + date.getYear();
            }
        });

        btnSubmit.setOnClickListener(view -> {
            submit();
            dialog.dismiss();
        });
    }

    private void submit(){
        sNote = tvNote.getText().toString();

        if (!sNote.equals(null) || !sDate.equals(null)){
            // save data
            dbHelper.save(sNote, sDate);
            showMessage("Your note has been SAVED");
            getAllData();
            clear();
        } else {
            showMessage("Please fill NOTE and DATE");
        }
    }

    private void totalData(int total){
        tvDescLeft.setText(total + " " + getResources().getString(R.string.lbl_items_left));
    }


    private String[] getToday() {
        String[] result = new String[3];

        Date today = new Date();
        result[0] = (String) DateFormat.format("yyyy", today);
        result[1] = (String) DateFormat.format("MM", today);
        result[2] = (String) DateFormat.format("dd", today);

        return result;
    }

    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void clear(){
        tvNote.setText(null);
        sNote = null;
        sDate = null;
    }



}