package com.duyuqian.todolist.viewmodel;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.duyuqian.todolist.model.task.Task;
import com.duyuqian.todolist.repository.TaskRepository;

import java.util.Comparator;
import java.util.List;

public class TaskViewModel extends ViewModel {

    public static final int SORT_WITH_INCREASE_ORDER = -1;
    public static final int SORT_WITH_DESCENDING_ORDER = 1;
    public static final int SORT_WITH_EQUAL = 0;
    private List<Task> taskList;
    private TaskRepository taskRepository;

    public TaskViewModel(TaskRepository repository) {
        this.taskRepository = repository;
        initTaskList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Task> getTaskList() {
        initTaskList();
        sortTaskList(taskList);
        return taskList;
    }

    public void insertTask(Task task) {
        taskRepository.insertTask(task);
        initTaskList();
    }

    public void initTaskList() {
        taskList = taskRepository.getAllTask();
    }

    public void updateTaskList(Task task) {
        taskRepository.updateTaskList(task);
    }

    public void deleteTask(Task task) {
        taskRepository.deleteTask(task);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortTaskList(List<Task> tasks) {
        Comparator<Task> byHasDone = Comparator.comparing(Task::isHasDone);
        Comparator<Task> byDateOfRemind = (task1, task2) -> {
            if (task1.getDateOfRemind().before(task2.getDateOfRemind())) {
                return SORT_WITH_INCREASE_ORDER;
            } else if (task1.getDateOfRemind().after(task2.getDateOfRemind())) {
                return SORT_WITH_DESCENDING_ORDER;
            } else {
                return SORT_WITH_EQUAL;
            }
        };
        tasks.sort(byHasDone.thenComparing(byDateOfRemind));
    }

    public static class TaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private final TaskRepository taskRepository;

        public TaskViewModelFactory() {
            taskRepository = new TaskRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new TaskViewModel(taskRepository);
        }
    }
}
