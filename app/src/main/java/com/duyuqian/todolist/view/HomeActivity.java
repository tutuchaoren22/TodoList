package com.duyuqian.todolist.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.duyuqian.todolist.R;
import com.duyuqian.todolist.model.task.Task;
import com.duyuqian.todolist.model.task.TaskAdapter;
import com.duyuqian.todolist.others.AlarmUtil;
import com.duyuqian.todolist.others.TodoListConstant;
import com.duyuqian.todolist.viewmodel.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    @BindString(R.string.pattern_day_default)
    String patternOfDayDefault;
    @BindString(R.string.pattern_day_mod_one)
    String patternOfDayModOne;
    @BindString(R.string.pattern_day_two)
    String patternOfDayTwo;
    @BindString(R.string.pattern_day_three)
    String patternOfDayThree;
    @BindString(R.string.pattern_month)
    String patternOfMonth;
    @BindString(R.string.tasks_count)
    String patternOfCounts;

    @OnClick(R.id.add_button)
    public void onClickAddButton() {
        Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
        startActivityForResult(intent, REQUEST_AND_RESULT_CODE);
    }

    private static final int REQUEST_AND_RESULT_CODE = 10;
    private TaskViewModel taskViewModel;
    private List<Task> taskList;
    private TaskAdapter adapter;
    private AlarmUtil alarmUtil = new AlarmUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        TaskViewModel.TaskViewModelFactory factory = new TaskViewModel.TaskViewModelFactory();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, factory);
        new Thread() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                taskViewModel = viewModelProvider.get(TaskViewModel.class);
                taskList = taskViewModel.getTaskList();
                updatePage();
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                taskListView.setLayoutManager(layoutManager);
                adapter = new TaskAdapter(getApplicationContext(), taskList);
                adapter.setOnItemClickListener(MyItemClickListener);
                runOnUiThread(
                        () -> taskListView.setAdapter(adapter)
                );
            }
        }.start();
    }

    private void updatePage() {
        Date today = new Date();
        titleOfWeekDay.setText(new SimpleDateFormat(patternOfWeek, Locale.ENGLISH).format(today).concat(shortFormatOfDate()));
        titleOfMonth.setText(new SimpleDateFormat(patternOfMonth, Locale.ENGLISH).format(today));
        titleOfCount.setText(String.valueOf(taskList.size()).concat(patternOfCounts));
    }

    private String shortFormatOfDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (1 == day % 10) {
            return patternOfDayModOne;
        } else if (2 == day) {
            return patternOfDayTwo;
        } else if (3 == day) {
            return patternOfDayThree;
        } else {
            return patternOfDayDefault;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sign_out) {
            SharedPreferences sharedPref = this.getSharedPreferences(TodoListConstant.LOGIN_STATUS, Context.MODE_PRIVATE);
            sharedPref.edit().putBoolean(TodoListConstant.LOGIN_STATUS, false).apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    private TaskAdapter.OnItemClickListener MyItemClickListener = new TaskAdapter.OnItemClickListener() {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onItemClick(View view, int position) {
            if (view.getId() == R.id.item_checkbox) {
                Task taskToUpdate = taskList.get(position);
                taskToUpdate.setHasDone(!taskToUpdate.isHasDone());
                new Thread(() ->
                        taskViewModel.updateTaskList(taskToUpdate)
                ).start();
                taskViewModel.sortTaskList(taskList);
                updateNotification(taskToUpdate);
                adapter.notifyDataSetChanged();
            } else {
                Task taskToEdit = taskList.get(position);
                Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                intent.putExtra(TodoListConstant.EDIT_TASK_INFO, taskToEdit);
                startActivityForResult(intent, REQUEST_AND_RESULT_CODE);
            }
        }
    };

    private void updateNotification(Task taskToUpdate) {
        if (taskToUpdate.isReminded()) {
            if (taskToUpdate.isHasDone()) {
                alarmUtil.cancelNotificationById(taskToUpdate.getId());
            } else {
                alarmUtil.addNotification(taskToUpdate.getId(), taskToUpdate.getTitle(), taskToUpdate.getDateOfRemind());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_AND_RESULT_CODE && resultCode == REQUEST_AND_RESULT_CODE) {
            TaskViewModel.TaskViewModelFactory factory = new TaskViewModel.TaskViewModelFactory();
            ViewModelProvider viewModelProvider = new ViewModelProvider(this, factory);
            new Thread() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    taskViewModel = viewModelProvider.get(TaskViewModel.class);
                    taskList = taskViewModel.getTaskList();
                    updatePage();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    HomeActivity.this.runOnUiThread(
                            () -> {
                                taskListView.setLayoutManager(layoutManager);
                                adapter = new TaskAdapter(getApplicationContext(), taskList);
                                adapter.setOnItemClickListener(MyItemClickListener);
                                taskListView.setAdapter(adapter);
                            }
                    );
                }
            }.start();
        }
    }
}