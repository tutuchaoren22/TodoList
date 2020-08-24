package com.duyuqian.todolist.repository;

import com.duyuqian.todolist.MyApplication;
import com.duyuqian.todolist.model.LocalDataSource;
import com.duyuqian.todolist.model.user.User;
import com.duyuqian.todolist.others.TodoListConstant;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Call;
import okhttp3.Response;

public class LoginRepository {
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Gson gson = new Gson();
    private LocalDataSource dataBase = LocalDataSource.getInstance(MyApplication.getInstance());

    public List<User> getUserList() {
        List<User> userList = getUserListFromDataBase();
        if (userList.size() == 0) {
            userList = requestHttpData();
        }
        return userList;
    }

    private List<User> requestHttpData() {
        List<User> users = new ArrayList<>();
        Request request = new Request.Builder()
                .url(TodoListConstant.URL)
                .build();
        okHttpClient.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful() && response.body() != null) {
                            String result = response.body().string();
                            User user = gson.fromJson(result, User.class);
                            users.add(user);
                            insertLocalSourceData(user);
                        }
                    }
                }
        );
        return users;
    }

    public List<User> getUserListFromDataBase() {
        return dataBase.userDao().getAll();
    }

    private void insertLocalSourceData(User user) {
        dataBase.userDao().insertAll(user);
    }
}
