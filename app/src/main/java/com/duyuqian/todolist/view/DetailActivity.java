package com.duyuqian.todolist.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.duyuqian.todolist.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class DetailActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener {
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.title_input)
    EditText title;
    @BindView(R.id.finish_button)
    Button finishBtn;

    @OnClick(R.id.date)
    public void onClickDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dateStr = String.format("%d年%d月%d日", year, month + 1, day);
                date.setText(dateStr);
                isDeadlineSet = true;
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_date, null);
        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        dialog.setView(dialogView);
        dialog.show();
        datePicker.init(year, month, day, this);
        updateFinishBtn();
    }

    @OnClick(R.id.finish_button)
    public void onClickFinishBtn() {
        Log.e("TAG", "onClickFinishBtn: ");
    }

    @OnTextChanged(value = R.id.title_input, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count > 0) {
            isTitleSet = true;
        }
        updateFinishBtn();
    }

    private int year, month, day;
    boolean isDeadlineSet = false;
    boolean isTitleSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        updateDate();
    }


    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    private void updateDate() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateStr = String.format("%d年%d月%d日", year, month, day);
        date.setText(dateStr);
    }

    private void updateFinishBtn() {
        if (isDeadlineSet && isTitleSet) {
            finishBtn.setEnabled(true);
        } else {
            finishBtn.setEnabled(false);
        }
    }
}