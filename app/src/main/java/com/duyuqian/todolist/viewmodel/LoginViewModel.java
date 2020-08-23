package com.duyuqian.todolist.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.duyuqian.todolist.model.User;
import com.duyuqian.todolist.repository.LoginRepository;

import java.util.List;

public class LoginViewModel extends ViewModel {

    private List<User> userList;

    public LoginViewModel(LoginRepository repository) {
        userList = repository.getUserList();
    }

    public List<User> getUserList() {
        return this.userList;
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
