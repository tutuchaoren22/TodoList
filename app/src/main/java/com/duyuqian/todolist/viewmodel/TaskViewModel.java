package com.duyuqian.todolist.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.duyuqian.todolist.model.task.Task;
import com.duyuqian.todolist.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends ViewModel {
    private List<Task> taskList;
    private TaskRepository taskRepository;

    public TaskViewModel(TaskRepository repository) {
        this.taskRepository = repository;
        updateTaskList();
    }

    public List<Task> getTaskList() {
        updateTaskList();
        return taskList;
    }

    public void insertTask(Task task) {
        taskRepository.insertTask(task);
        updateTaskList();
    }

    public void updateTaskList() {
        taskList = taskRepository.getAllTask();
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
