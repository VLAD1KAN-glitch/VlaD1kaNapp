package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.Task
import com.example.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val tasks: StateFlow<List<Task>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(title: String, description: String, progress: Float) {
        viewModelScope.launch {
            repository.insert(Task(title = title, description = description, progress = progress))
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.update(task)
        }
    }

    fun updateTaskProgress(task: Task, progress: Float) {
        viewModelScope.launch {
            repository.update(task.copy(progress = progress))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteById(task.id)
        }
    }
}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
