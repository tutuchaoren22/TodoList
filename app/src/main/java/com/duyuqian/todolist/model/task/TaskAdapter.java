package com.duyuqian.todolist.model.task;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duyuqian.todolist.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> taskList;
    private Context mContext;
    public OnItemClickListener mOnItemClickListener;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox hasDone;
        TextView title;
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            hasDone = itemView.findViewById(R.id.item_checkbox);
            title = itemView.findViewById(R.id.item_title);
            date = itemView.findViewById(R.id.item_date);
            itemView.setOnClickListener(this);
            hasDone.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
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
        } else {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.date.setText(new SimpleDateFormat(mContext.getResources().getString(R.string.month_day_format), Locale.getDefault()).format(task.getDateOfRemind()));

//        holder.hasDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.item_checkbox:
//                        Log.e("TAG", "onItemCheckboxClick: ");
//                        mItemClickListener.onItemCheckboxClick(position);
//                        break;
//                    default:
//                        Log.e("TAG", "onItemClick: ");
//                        mItemClickListener.onItemClick(position);
//                        break;
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

//        void onItemCheckboxClick(int position);

        void onItemLongClick(View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
