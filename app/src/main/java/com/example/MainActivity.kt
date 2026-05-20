package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.data.TaskDatabase
import com.example.data.TaskRepository
import com.example.ui.Dashboard
import com.example.ui.TaskViewModel
import com.example.ui.TaskViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val database = TaskDatabase.getDatabase(this)
    val repository = TaskRepository(database.taskDao())
    val factory = TaskViewModelFactory(repository)
    val viewModel: TaskViewModel by viewModels { factory }

    setContent {
      MyApplicationTheme {
        Dashboard(
          viewModel = viewModel,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}

