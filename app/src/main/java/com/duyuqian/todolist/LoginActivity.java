package com.duyuqian.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.password)
    EditText passWord;
    @BindView(R.id.log_in_btn)
    Button loginBtn;
    @BindString(R.string.valid_user_name)
    String validUserName;
    @BindString(R.string.valid_password)
    String validPassWord;
    @BindString(R.string.pattern_user_name)
    String patternOfUserName;
    @BindString(R.string.pattern_password)
    String patternOfPassWord;
    @BindDrawable(R.drawable.login_button_allow_background)
    Drawable legalLoginBg;
    @BindDrawable(R.drawable.login_button_not_allow_background)
    Drawable illegalLoginBg;

    @BindColor(R.color.white)
    int white;
    @BindColor(R.color.black)
    int black;
    boolean isLegalOfUserName;
    boolean isLegalOfPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.log_in_btn)
    public void onClick() {
        if (isLegalOfUserName && isLegalOfPassWord) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @OnTextChanged(R.id.user_name)
    public void setOnUserNameChangedError() {
        isLegalOfUserName = validInput(userName, patternOfUserName);
        updateLoginBtnStyle();
    }

    @OnTextChanged(R.id.password)
    public void setOnPassWordChangedError() {
        isLegalOfPassWord = validInput(passWord, patternOfPassWord);
        updateLoginBtnStyle();
    }

    public boolean validInput(EditText input, String pattern) {
        boolean isLegalInput = false;
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
        } else {
            isLegalInput = true;
        }
        return isLegalInput;
    }

    public void updateLoginBtnStyle() {
        if (isLegalOfUserName && isLegalOfPassWord) {
            loginBtn.setBackground(legalLoginBg);
            loginBtn.setTextColor(white);
        } else {
            loginBtn.setBackground(illegalLoginBg);
            loginBtn.setTextColor(black);
        }
    }
}