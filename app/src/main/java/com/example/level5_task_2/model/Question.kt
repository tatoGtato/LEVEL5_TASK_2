package com.example.level5_task_2.model

data class Question(
    val choices: List<String>,
    val correctAnswer: String,
    val id: String,
    val question: String
)
