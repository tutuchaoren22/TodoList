package com.duyuqian.todolist.viewmodel;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.duyuqian.todolist.model.User;
import com.duyuqian.todolist.others.MD5Utils;
import com.duyuqian.todolist.repository.LoginRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {

    private List<User> userList;
    private LoginRepository loginRepository;

    public LoginViewModel(LoginRepository repository) {
        this.loginRepository = repository;
        userList = loginRepository.getUserList();
    }

    public List<User> getUserList() {
        return this.userList;
    }

    public boolean validInput(EditText input, String pattern, String errorInf) {
        boolean isLegalInput = false;
        if (!Pattern.compile(pattern).matcher(input.getText().toString()).matches()) {
            input.setError(errorInf);
        } else {
            isLegalInput = true;
        }
        return isLegalInput;
    }

    public Map<String, Boolean> isUserInDB(String userNameInput, String passWordInput) {
        Map<String, Boolean> isUserInDataBase = new HashMap<>();
        boolean hasUserName = false;
        boolean hasUserPassword = false;
        for (User user : this.userList) {
            if (userNameInput.equals(user.getName())) {
                hasUserName = true;
                if (MD5Utils.md5Password(passWordInput).equals(user.getPassword())) {
                    hasUserPassword = true;
                    break;
                }
            }
        }
        isUserInDataBase.put("hasUserName", hasUserName);
        isUserInDataBase.put("hasUserPassword", hasUserPassword);
        return isUserInDataBase;
    }

    public static class LoginViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private final LoginRepository loginRepository;

        public LoginViewModelFactory() {
            loginRepository = new LoginRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new LoginViewModel(loginRepository);
        }
    }

}
