package com.duyuqian.todolist.model.task;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.duyuqian.todolist.R;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> taskList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox hasDone;
        TextView title;
        TextView date;

        public ViewHolder(View view) {
            super(view);
            hasDone = view.findViewById(R.id.item_checkbox);
            title = view.findViewById(R.id.item_title);
            date = view.findViewById(R.id.item_date);
        }
    }

    public TaskAdapter(Context context, List<Task> tasks) {
        taskList = tasks;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.hasDone.setChecked(task.isHasDone());
        holder.title.setText(task.getTitle());
        if (task.isHasDone() == true) {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.remind_color));
            holder.title.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.date.setText(new SimpleDateFormat("MM月dd日").format(task.getDateOfRemind()));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


}
