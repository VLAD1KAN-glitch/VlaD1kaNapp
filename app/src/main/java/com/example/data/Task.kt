package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: Long? = null,
    val progress: Float = 0f, // 0.0 to 1.0 (0% to 100%)
    val timestamp: Long = System.currentTimeMillis()
)
