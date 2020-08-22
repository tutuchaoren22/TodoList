package com.duyuqian.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import java.util.regex.Pattern;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.password)
    EditText passWord;
    @BindString(R.string.valid_user_name)
    String validUserName;
    @BindString(R.string.valid_password)
    String validPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.log_in_btn)
    public void onClick() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @OnFocusChange({R.id.user_name, R.id.password})
    public void setOnFocusChangeError(EditText view, boolean focused) {
        String input = view.getText().toString();
        switch (view.getId()) {
            case R.id.user_name:
                if (!focused) {
                    String pattern = "^[a-zA-Z0-9]{3,12}$";
                    if (!Pattern.compile(pattern).matcher(input).matches()) {
                        view.setError(validUserName);
                    }
                }
                break;
            case R.id.password:
                if (!focused) {
                    String pattern = "[\\s\\S]{6,18}";
                    if (!Pattern.compile(pattern).matcher(input).matches()) {
                        view.setError(validPassWord);
                    }
                }
                break;
            default:
                break;
        }
    }

}