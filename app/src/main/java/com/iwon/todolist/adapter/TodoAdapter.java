package com.iwon.todolist.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.iwon.todolist.R;
import com.iwon.todolist.helper.TodoInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private static final String TAG = TodoAdapter.class.getSimpleName();

    ArrayList<HashMap<String, String>> list;
    ArrayList<Integer> checkList = new ArrayList<>();
    String id;
    String sNote;
    String sDate;
    TodoInterface todoInterface;
    boolean allDataChecked = false;

    public TodoAdapter(ArrayList<HashMap<String, String>> list, TodoInterface todoInterface) {
        this.list = list;
        this.todoInterface = todoInterface;
    }

    public TodoAdapter(ArrayList<HashMap<String, String>> list, TodoInterface todoInterface, Boolean allDataChecked) {
        this.list = list;
        this.todoInterface = todoInterface;
        this.allDataChecked = allDataChecked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        id = list.get(position).get("id");
        sNote = list.get(position).get("note");
        sDate = list.get(position).get("date");

        holder.tvNote.setText(sNote);
        holder.tvDate.setText(sDate);
        if (allDataChecked){
            holder.cbDone.setChecked(true);
        }
        holder.cbDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int idChecked = Integer.parseInt(list.get(position).get("id"));

                if (isChecked){
                    checkList.add(idChecked);
                } else {
                    checkList.remove(new Integer(idChecked));
                }
                Log.d(TAG, " " + checkList);

                todoInterface.onRespCB(checkList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbDone;
        TextView tvNote, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cbDone = itemView.findViewById(R.id.cb_done);
            tvNote = itemView.findViewById(R.id.tv_note);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }

}
