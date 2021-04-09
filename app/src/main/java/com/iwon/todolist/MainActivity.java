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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.iwon.todolist.adapter.TodoAdapter;
import com.iwon.todolist.helper.DbHelper;
import com.iwon.todolist.helper.TodoInterface;
import com.iwon.todolist.utils.*;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements TodoInterface {
    private static final String TAG = "track_todo";

    TextInputEditText tvNote;
    LinearLayout lLDate;
    MaterialCalendarView mcv;
    RecyclerView recyclerView;
    Button btnSubmit, btnClear;
    TextView tvDescLeft;
    String sNote;
    String sDate;
    CheckBox cbMarkComplete;

    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    ArrayList<Integer> checkList = new ArrayList<>();
    DbHelper dbHelper = new DbHelper(this);
    TodoAdapter todoAdapter;
    boolean allDataChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNote = findViewById(R.id.tv_note);
        tvDescLeft = findViewById(R.id.tv_desc_left);
        lLDate = findViewById(R.id.lL_date);
        recyclerView = findViewById(R.id.recycleview);
        btnClear = findViewById(R.id.btn_clear);
        cbMarkComplete = findViewById(R.id.cb_mark_complete);

        getAllData();
        lLDate.setOnClickListener(view -> showDatePicker());
        btnClear.setOnClickListener(view -> removeItem());
        cbMarkComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                markAllComplete(isChecked);
            }
        });
    }

    private void refresh(){
        getAllData();
    }

    private void getAllData(){
        list = dbHelper.allData();
        todoAdapter = null;
        if (allDataChecked){
            todoAdapter = new TodoAdapter(list, this, true);
        } else {
            todoAdapter = new TodoAdapter(list, this);
        }

        recyclerView.setAdapter(todoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalData(list.size());
        showBtnClear();
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
                sDate = monthLbl(date.getMonth()) + " " + date.getDay() + ", " + date.getYear();
            }
        });

        btnSubmit.setOnClickListener(view -> {
            submit();
            dialog.dismiss();
        });
    }

    private void submit(){
        sNote = tvNote.getText().toString();

        if (sNote != null && sDate != null){
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

    private void removeItem(){
        if (checkList.size() > 0){
            int checkListLength = checkList.size();

            for (int i=0; i < checkListLength; i++){
                Log.d(TAG, "removeItem: " + checkList.get(i));
                dbHelper.delete(checkList.get(i));

                if (i == (checkListLength -1)){
                    showMessage("Data DELETED!");
                    clearDataTemp();
                    refresh();
                }
            }
        }
    }

    private void markAllComplete(boolean isChecked){
        if (list.size() > 0){
            if (isChecked){
                allDataChecked = true;
                // store ID to checklist
                for (int i=0; i < list.size(); i++){
                    checkList.add(Integer.parseInt(list.get(i).get("id")));
                }
                showBtnClear();
                cbMarkComplete.setChecked(false);

            } else {
                allDataChecked = false;
                // clear
                clearDataTemp();
                showBtnClear();
            }
            getAllData();
        } else {
            showMessage("no data available");
        }
    }


    private String[] getToday() {
        String[] result = new String[3];

        Date today = new Date();
        result[0] = (String) DateFormat.format("yyyy", today);
        result[1] = (String) DateFormat.format("MM", today);
        result[2] = (String) DateFormat.format("dd", today);

        return result;
    }

    private String monthLbl(int i){
        String[] montList = getResources().getStringArray(R.array.month_list);

        return montList[i];
    }

    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showBtnClear(){
        if (checkList.size() > 0){
            btnClear.setVisibility(View.VISIBLE);
            String text = "Clear " + checkList.size() + " completed item";
            btnClear.setText(text);
        } else {
            btnClear.setVisibility(View.GONE);
        }
    }

    private void clearDataTemp(){
        checkList = new ArrayList<>();
    }

    private void clear(){
        tvNote.setText(null);
        sNote = null;
        sDate = null;
    }

    @Override
    public void onRespCB(ArrayList<Integer> checkList) {
        Log.d(TAG, "onRespCB: " + checkList);
        this.checkList = checkList;
        showBtnClear();
    }
}