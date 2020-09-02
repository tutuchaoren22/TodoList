package com.duyuqian.todolist.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.duyuqian.todolist.R;
import com.duyuqian.todolist.model.task.Task;
import com.duyuqian.todolist.others.AlarmUtil;
import com.duyuqian.todolist.others.TodoListConstant;
import com.duyuqian.todolist.viewmodel.TaskViewModel;

import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class DetailActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener {
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.title_input)
    EditText title;
    @BindView(R.id.delete_button)
    ImageButton deleteBtn;
    @BindView(R.id.finish_button)
    ImageButton finishBtn;
    @BindView(R.id.description_input)
    EditText description;
    @BindView(R.id.has_done_check)
    CheckBox hasDone;
    @BindView(R.id.remind_switch)
    SwitchCompat isReminded;
    @BindString(R.string.date_format)
    String dateFormatter;
    @BindString(R.string.choose_yes)
    String chooseYes;
    @BindString(R.string.choose_no)
    String chooseNo;
    @BindColor(R.color.colorOfCheckBox)
    int selectDateColor;


    @OnClick(R.id.date)
    public void onClickDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(chooseYes, (dialog, which) -> {
            String dateStr = String.format(Locale.getDefault(), dateFormatter, year, month + 1, day);
            date.setText(dateStr);
            date.setTextColor(selectDateColor);
            isDeadlineSet = true;
            dialog.dismiss();
            updateFinishBtn();
        });
        builder.setNegativeButton(chooseNo, (dialog, which) -> dialog.dismiss());
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_date, null);
        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        dialog.setView(dialogView);
        dialog.show();
        datePicker.init(year, month, day, this);
    }

    @OnClick(R.id.finish_button)
    public void onClickFinishBtn() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        new Thread() {
            @Override
            public void run() {
                if (isEditPage) {
                    if (newTask != null) {
                        newTask.setHasDone(hasDone.isChecked());
                        newTask.setDateOfRemind(calendar.getTime());
                        newTask.setReminded(isReminded.isChecked());
                        newTask.setTitle(title.getText().toString());
                        newTask.setDescription(description.getText().toString());
                    }
                    taskViewModel.updateTaskList(newTask);
                    updateNotification(newTask);
                } else {
                    newTask = new Task(title.getText().toString(), description.getText().toString(),
                            hasDone.isChecked(), isReminded.isChecked(), calendar.getTime());
                    taskViewModel.insertTask(newTask);
                    alarmUtil.addNotification(newTask.getId(), newTask.getTitle(), newTask.getDateOfRemind());
                }
            }
        }.start();
        goToHomePage();
    }

    @OnClick(R.id.delete_button)
    public void onClickDeleteBtn() {
        new Thread(() -> {
            if (newTask != null) {
                taskViewModel.deleteTask(newTask);
                alarmUtil.cancelNotificationById(newTask.getId());
                goToHomePage();
            }
        }).start();
    }

    @OnTextChanged(value = R.id.title_input, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onTextChanged() {
        isTitleSet = title.getText().toString().length() > 0;
        updateFinishBtn();
    }

    private int year, month, day;
    boolean isDeadlineSet = false;
    boolean isTitleSet = false;
    private TaskViewModel taskViewModel;
    private boolean isEditPage;
    private Task newTask;
    private AlarmUtil alarmUtil = new AlarmUtil();
    private static final int REQUEST_AND_RESULT_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        initTaskViewModel();
        Intent intent = getIntent();
        Task taskToEdit = (Task) intent.getSerializableExtra(TodoListConstant.EDIT_TASK_INFO);
        if (taskToEdit != null) {
            isEditPage = true;
            newTask = taskToEdit;
            initDate();
            createEditPage(taskToEdit);
        } else {
            isEditPage = false;
            initDate();
            updateFinishBtn();
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    private void initTaskViewModel() {
        TaskViewModel.TaskViewModelFactory factory = new TaskViewModel.TaskViewModelFactory();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, factory);
        new Thread() {
            @Override
            public void run() {
                taskViewModel = viewModelProvider.get(TaskViewModel.class);
            }
        }.start();
    }

    private void updateFinishBtn() {
        if (isDeadlineSet && isTitleSet) {
            finishBtn.setEnabled(true);
        } else {
            finishBtn.setEnabled(false);
        }
    }

    private void initDate() {
        if (isEditPage) {
            setDate(newTask.getDateOfRemind());
        } else {
            setDate(null);
        }
    }

    private void createEditPage(Task task) {
        deleteBtn.setVisibility(View.VISIBLE);
        isDeadlineSet = true;
        isTitleSet = true;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(task.getDateOfRemind());
        hasDone.setChecked(task.isHasDone());
        date.setText(String.format(Locale.getDefault(), dateFormatter,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        date.setTextColor(selectDateColor);
        isReminded.setChecked(task.isReminded());
        title.setText(task.getTitle());
        description.setText(task.getDescription());
    }

    private void setDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void goToHomePage() {
        setResult(REQUEST_AND_RESULT_CODE);
        finish();
    }

    private void updateNotification(Task taskToUpdate) {
        if (taskToUpdate.isReminded()) {
            if (taskToUpdate.isHasDone()) {
                alarmUtil.cancelNotificationById(taskToUpdate.getId());
            } else {
                alarmUtil.addNotification(taskToUpdate.getId(), taskToUpdate.getTitle(), taskToUpdate.getDateOfRemind());
            }
        }
    }
}