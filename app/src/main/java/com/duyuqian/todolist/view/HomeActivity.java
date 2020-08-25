package com.duyuqian.todolist.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.duyuqian.todolist.R;
import com.duyuqian.todolist.model.task.Task;
import com.duyuqian.todolist.model.task.TaskAdapter;
import com.duyuqian.todolist.others.MyDecoration;
import com.duyuqian.todolist.viewmodel.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    @OnClick(R.id.add_button)
    public void onClickAddButton() {
        Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
        startActivity(intent);
    }

    private TaskViewModel taskViewModel;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        //打印从编辑页面返回的信息
//        Intent intent = getIntent();
//        Task task = (Task) intent.getSerializableExtra("task");
//        if (task != null) {
//            Toast.makeText(this, task.toString(), Toast.LENGTH_LONG).show();
//        }

        //启动viewmodel
        TaskViewModel.TaskViewModelFactory factory = new TaskViewModel.TaskViewModelFactory();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, factory);
        new Thread() {
            @Override
            public void run() {
                taskViewModel = viewModelProvider.get(TaskViewModel.class);
                Log.e("TAG", "onCreate: " + taskViewModel);
                //get the datalist in the database
                taskList = taskViewModel.getTaskList();
                updatePage();
                Log.e("TAG", "onCreate: " + taskList.size());
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                taskListView.setLayoutManager(layoutManager);
                TaskAdapter adapter = new TaskAdapter(getApplicationContext(), taskList);
                taskListView.setAdapter(adapter);
                taskListView.addItemDecoration(new MyDecoration(getApplicationContext(), MyDecoration.VERTICAL_LIST));
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
        titleOfWeekDay.setText(new SimpleDateFormat("EEEE , d", Locale.ENGLISH).format(today) + "th");
        titleOfMonth.setText(new SimpleDateFormat("MMMM", Locale.ENGLISH).format(today));
        titleOfCount.setText(taskList.size() + "个任务");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}