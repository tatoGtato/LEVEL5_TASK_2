package com.example.level5_task_2.ui.theme.screens

sealed class QuizScreens(val route: String){
    object StartingScreen: QuizScreens("starting_screen")
    object QuizScreen: QuizScreens("quiz_screen")
}
