package com.example.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    var showAddTaskSheet by remember { mutableStateOf(false) }

    val totalProgress = if (tasks.isNotEmpty()) {
        tasks.map { it.progress }.average().toFloat()
    } else {
        0f
    }
    
    val animatedProgress by animateFloatAsState(
        targetValue = totalProgress,
        animationSpec = tween(durationMillis = 500),
        label = "progress"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task", modifier = Modifier.size(28.dp))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // One UI Viewing Area (Top)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .padding(horizontal = 32.dp, vertical = 32.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Tasks",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "TODAY'S PROGRESS",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        // Fake Avatar
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "JD",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Progress Card
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = { 1f },
                                    modifier = Modifier.size(48.dp),
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    strokeWidth = 4.dp
                                )
                                CircularProgressIndicator(
                                    progress = { animatedProgress },
                                    modifier = Modifier.size(48.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 4.dp
                                )
                                Text(
                                    text = "${(animatedProgress * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Daily Progress",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Project completion status",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // One UI Interaction Area (Bottom)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 42.dp, topEnd = 42.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(top = 32.dp, start = 24.dp, end = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Focus Blocks",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(bottom = 80.dp) // space for FAB
                    ) {
                        items(tasks, key = { it.id }) { task ->
                            TaskCard(
                                task = task,
                                onProgressChange = { newProgress ->
                                    viewModel.updateTaskProgress(task, newProgress)
                                },
                                onDelete = {
                                    viewModel.deleteTask(task)
                                },
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddTaskSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddTaskSheet = false },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            AddTaskContent(
                onAddTask = { title, description ->
                    viewModel.addTask(title, description, 0f)
                    showAddTaskSheet = false
                },
                onCancel = { showAddTaskSheet = false }
            )
        }
    }
}

@Composable
fun AddTaskContent(
    onAddTask: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "New Task",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Task Title") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            shape = RoundedCornerShape(16.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAddTask(title, description)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Save")
            }
        }
    }
}
