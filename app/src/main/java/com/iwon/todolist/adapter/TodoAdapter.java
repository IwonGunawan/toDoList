package com.iwon.todolist.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.iwon.todolist.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private static final String TAG = TodoAdapter.class.getSimpleName();

    ArrayList<HashMap<String, String>> list;
    ArrayList<Integer> checkList = new ArrayList<>();
    String sNote;
    String sDate;

    public TodoAdapter(ArrayList<HashMap<String, String>> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        sNote = list.get(position).get("note");
        sDate = list.get(position).get("date");

        holder.tvNote.setText(sNote);
        holder.tvDate.setText(sDate);
        holder.cbDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked){
                    checkList.add(position);
                } else {
                    checkList.remove(new Integer(position));
                }
                Log.d(TAG, " " + checkList);

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
