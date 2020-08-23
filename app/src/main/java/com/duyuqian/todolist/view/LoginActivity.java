package com.duyuqian.todolist.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duyuqian.todolist.R;
import com.duyuqian.todolist.model.User;
import com.duyuqian.todolist.others.MD5Utils;
import com.duyuqian.todolist.others.TodoListConstant;
import com.duyuqian.todolist.viewmodel.LoginViewModel;

import java.util.List;
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
    EditText userNameInput;
    @BindView(R.id.password)
    EditText passWordInput;
    @BindView(R.id.log_in_btn)
    Button loginBtn;
    @BindString(R.string.valid_user_name)
    String validUserName;
    @BindString(R.string.wrong_user_name)
    String wrongUserNameImg;
    @BindString(R.string.valid_password)
    String validPassWord;
    @BindString(R.string.wrong_password)
    String wrongPassWordImg;
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

    @OnClick(R.id.log_in_btn)
    public void onClick() {
        if (isLegalOfUserName && isLegalOfPassWord && isUserInDB()) {
            setLoginStatus();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @OnTextChanged(R.id.user_name)
    public void setOnUserNameChangedError() {
        isLegalOfUserName = validInput(userNameInput, patternOfUserName);
        updateLoginBtnStyle();
    }

    @OnTextChanged(R.id.password)
    public void setOnPassWordChangedError() {
        isLegalOfPassWord = validInput(passWordInput, patternOfPassWord);
        updateLoginBtnStyle();
    }

    private boolean isLegalOfUserName;
    private boolean isLegalOfPassWord;
    private List<User> userList;
    private SharedPreferences sharedPref;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLoginStatus()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            LoginViewModel.LoginViewModelFactory factory = new LoginViewModel.LoginViewModelFactory();
            ViewModelProvider viewModelProvider = new ViewModelProvider(this, factory);

            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);
            new Thread() {
                @Override
                public void run() {
                    loginViewModel = viewModelProvider.get(LoginViewModel.class);
                    userList = loginViewModel.getUserList();
                }
            }.start();

        }
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

    public boolean isUserInDB() {
        boolean hasUserName = false;
        boolean hasUserPassword = false;
        for (User user : userList) {
            if (userNameInput.getText().toString().equals(user.getName())) {
                hasUserName = true;
                if (MD5Utils.md5Password(passWordInput.getText().toString()).equals(user.getPassword())) {
                    hasUserPassword = true;
                    break;
                }
            }
        }
        if (!hasUserName) {
            Toast.makeText(this, wrongUserNameImg, Toast.LENGTH_LONG).show();
        } else if (!hasUserPassword) {
            Toast.makeText(this, wrongPassWordImg, Toast.LENGTH_LONG).show();
        }
        return hasUserName && hasUserPassword;
    }

    public boolean getLoginStatus() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean(TodoListConstant.LOGIN_STATUS, false);
    }

    public void setLoginStatus() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        sharedPref.edit().putBoolean(TodoListConstant.LOGIN_STATUS, true).apply();
    }
}