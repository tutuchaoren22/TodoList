package com.duyuqian.todolist.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duyuqian.todolist.R;
import com.duyuqian.todolist.others.TodoListConstant;
import com.duyuqian.todolist.viewmodel.LoginViewModel;

import java.util.Map;

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
    String wrongUserNameInf;
    @BindString(R.string.valid_password)
    String validPassWord;
    @BindString(R.string.wrong_password)
    String wrongPassWordInf;
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
            skipToHomePage();
        }
    }

    @OnTextChanged(R.id.user_name)
    public void setOnUserNameChangedError() {
        isLegalOfUserName = loginViewModel.validInput(userNameInput, patternOfUserName, validUserName);
        updateLoginBtnStyle();
    }

    @OnTextChanged(R.id.password)
    public void setOnPassWordChangedError() {
        isLegalOfPassWord = loginViewModel.validInput(passWordInput, patternOfPassWord, validPassWord);
        updateLoginBtnStyle();
    }

    private boolean isLegalOfUserName;
    private boolean isLegalOfPassWord;
    private SharedPreferences sharedPref;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLoginStatus()) {
            skipToHomePage();
        } else {
            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);
            LoginViewModel.LoginViewModelFactory factory = new LoginViewModel.LoginViewModelFactory();
            ViewModelProvider viewModelProvider = new ViewModelProvider(this, factory);
            new Thread() {
                @Override
                public void run() {
                    loginViewModel = viewModelProvider.get(LoginViewModel.class);
                }
            }.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

    }

    public void updateLoginBtnStyle() {
        if (isLegalOfUserName && isLegalOfPassWord) {
            loginBtn.setBackground(legalLoginBg);
            loginBtn.setTextColor(white);
            loginBtn.setEnabled(true);
        } else {
            loginBtn.setBackground(illegalLoginBg);
            loginBtn.setTextColor(black);
            loginBtn.setEnabled(false);
        }
    }

    public boolean isUserInDB() {
        Map<String, Boolean> isUserInDataBase = loginViewModel.isUserInDB(userNameInput.getText().toString(), passWordInput.getText().toString());
        boolean hasUserName = isUserInDataBase.containsKey("hasUserName") && isUserInDataBase.get("hasUserName");
        boolean hasUserPassword = isUserInDataBase.containsKey("hasUserPassword") && isUserInDataBase.get("hasUserPassword");
        if (!hasUserName) {
            Toast.makeText(this, wrongUserNameInf, Toast.LENGTH_LONG).show();
        } else if (!hasUserPassword) {
            Toast.makeText(this, wrongPassWordInf, Toast.LENGTH_LONG).show();
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

    public void skipToHomePage() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}