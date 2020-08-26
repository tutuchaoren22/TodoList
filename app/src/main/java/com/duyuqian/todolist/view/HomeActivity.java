package com.duyuqian.todolist.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.duyuqian.todolist.R;
import com.duyuqian.todolist.model.task.Task;
import com.duyuqian.todolist.model.task.TaskAdapter;
import com.duyuqian.todolist.others.MyNotification;
import com.duyuqian.todolist.others.TodoListConstant;
import com.duyuqian.todolist.viewmodel.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.task_list)
    RecyclerView taskListView;
    @BindView(R.id.title_week_day)
    TextView titleOfWeekDay;
    @BindView(R.id.title_month)
    TextView titleOfMonth;
    @BindView(R.id.title_count)
    TextView titleOfCount;
    @BindString(R.string.pattern_week)
    String patternOfWeek;
    @BindString(R.string.pattern_day)
    String patternOfDay;
    @BindString(R.string.pattern_month)
    String patternOfMonth;
    @BindString(R.string.tasks_count)
    String patternOfCounts;
    @BindString(R.string.notification_title)
    String notificationTitle;

    @OnClick(R.id.add_button)
    public void onClickAddButton() {
        Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
        startActivity(intent);
    }

    public static final int SORT_WITH_INCREASE_ORDER = -1;
    public static final int SORT_WITH_DESCENDING_ORDER = 1;
    public static final int SORT_WITH_EQUAL = 0;
    private TaskViewModel taskViewModel;
    private List<Task> taskList;
    private TaskAdapter adapter;
    private MyNotification myNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        myNotification = new MyNotification(this);

        TaskViewModel.TaskViewModelFactory factory = new TaskViewModel.TaskViewModelFactory();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, factory);
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                taskViewModel = viewModelProvider.get(TaskViewModel.class);
                taskList = taskViewModel.getTaskList();
                updatePage();
                sortTaskList();
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                taskListView.setLayoutManager(layoutManager);
                adapter = new TaskAdapter(getApplicationContext(), taskList);
                adapter.setOnItemClickListener(MyItemClickListener);
                runOnUiThread(
                        () -> {
                            taskListView.setAdapter(adapter);
                            updateNotification();
                        }
                );
            }
        }.start();


    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void updatePage() {
        Date today = new Date();
        titleOfWeekDay.setText(new SimpleDateFormat(patternOfWeek, Locale.ENGLISH).format(today).concat(patternOfDay));
        titleOfMonth.setText(new SimpleDateFormat(patternOfMonth, Locale.ENGLISH).format(today));
        titleOfCount.setText(String.valueOf(taskList.size()).concat(patternOfCounts));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortTaskList() {
        Comparator<Task> byHasDone = Comparator.comparing(Task::isHasDone);
        Comparator<Task> byDateOfRemind = (task1, task2) -> {
            if (task1.getDateOfRemind().before(task2.getDateOfRemind())) {
                return SORT_WITH_INCREASE_ORDER;
            } else if (task1.getDateOfRemind().after(task2.getDateOfRemind())) {
                return SORT_WITH_DESCENDING_ORDER;
            } else {
                return SORT_WITH_EQUAL;
            }
        };
        taskList.sort(byHasDone.thenComparing(byDateOfRemind));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private TaskAdapter.OnItemClickListener MyItemClickListener = new TaskAdapter.OnItemClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onItemClick(View view, int position) {
            if (view.getId() == R.id.item_checkbox) {
                Task taskToUpdate = taskList.get(position);
                taskToUpdate.setHasDone(!taskToUpdate.isHasDone());
                new Thread(() -> taskViewModel.updateTaskList(taskToUpdate)).start();
                adapter.notifyDataSetChanged();
                sortTaskList();
            } else {
                Task taskToEdit = taskList.get(position);
                Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                intent.putExtra(TodoListConstant.EDIT_TASK_INFO, taskToEdit);
                startActivity(intent);
            }
        }
    };

    private void updateNotification() {
        myNotification.cancelAllNotification();
        for (Task task : taskList) {
            if (!task.isHasDone() && task.isReminded() && task.getDateOfRemind().after(new Date())) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(task.getDateOfRemind());
//                calendar.add(Calendar.HOUR, 6);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, 5);
                myNotification.sendNotification(this, task.getId(), calendar, notificationTitle, task.getTitle());
            }
        }
    }
}