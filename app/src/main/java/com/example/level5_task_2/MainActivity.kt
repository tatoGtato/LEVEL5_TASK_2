package com.example.level5_task_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.level5_task_2.QuizViewModel.QuizViewModel
import com.example.level5_task_2.ui.theme.LEVEL5_TASK_2Theme
import com.example.level5_task_2.ui.theme.screens.QuizScreen
import com.example.level5_task_2.ui.theme.screens.QuizScreens
import com.example.level5_task_2.ui.theme.screens.QuizStartingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LEVEL5_TASK_2Theme {
                QuizApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizApp() {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        QuizNavHost(navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun QuizNavHost(navController: NavHostController, modifier: Modifier = Modifier) {

    val viewModel: QuizViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = QuizScreens.StartingScreen.route,
        modifier = modifier
    ) {
        composable(QuizScreens.StartingScreen.route) {
            QuizStartingScreen(navController)
        }
        composable(QuizScreens.QuizScreen.route) {
            QuizScreen(navController, viewModel)
        }
    }
}