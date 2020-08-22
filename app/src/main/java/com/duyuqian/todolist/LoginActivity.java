package com.duyuqian.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.password)
    EditText passWord;
    @BindString(R.string.valid_user_name)
    String validUserName;
    @BindString(R.string.valid_password)
    String validPassWord;
    @BindString(R.string.pattern_user_name)
    String patternOfUserName;
    @BindString(R.string.pattern_password)
    String patternOfPassWord;

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

    @OnTextChanged(R.id.user_name)
    public void setOnUserNameChangedError() {
        validInput(userName, patternOfUserName);
    }

    @OnTextChanged(R.id.password)
    public void setOnPassWordChangedError() {
        validInput(passWord, patternOfPassWord);
    }

    public void validInput(EditText input, String pattern) {
        if (!Pattern.compile(pattern).matcher(input.getText().toString()).matches()) {
            switch (input.getId()) {
                case R.id.user_name:
                    input.setError(validUserName);
                    break;
                case R.id.password:
                    input.setError(validPassWord);
                    break;
                default:
                    break;
            }
        }
    }
}