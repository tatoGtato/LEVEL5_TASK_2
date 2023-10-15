package com.example.level5_task_2.ui.theme.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level5_task_2.QuizViewModel.QuizViewModel
import com.example.level5_task_2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(navController: NavController,
    viewModel: QuizViewModel
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Blue)
            )

        },
        content = { innerPadding -> Questions(navController, innerPadding, viewModel)}
    )
}

@Composable
fun Questions(NavController: NavController,
              padding:PaddingValues,
              viewModel: QuizViewModel,
              ){

    viewModel.getIds()
    val ids by viewModel.ids.observeAsState()
    var numberOfQuestions = ids?.size
    var questionNumber by remember { mutableStateOf(numberOfQuestions?.minus(1)) }

    var prevQuesNum by remember { mutableStateOf(questionNumber) }

    questionNumber?.let { ids?.get(it)?.let { viewModel.getQuestion(it) } }

    val question by viewModel.question.observeAsState()
    val errorMsg by viewModel.errorText.observeAsState()
    var selectedAnswer by remember { mutableStateOf("") }
    val context = LocalContext.current

//    Log.e("AA", question.toString())
//    Log.e("AA", question.toString())
//     Log.e("AA", question.toString())
//    Log.e("AA", question.toString())
//    Log.e("AA", question.toString())





    val completed = question?.choices?.let { MutableList(it.size) { false } }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ){
        Row {
            Text(text = question?.question.toString())
        }
        Row {
            LazyColumn{
                question?.let {
                    itemsIndexed(items = it.choices) { index: Int, item: String ->
                        var isSelected by rememberSaveable { mutableStateOf(completed?.get(index)) }
                        Row {
                            isSelected?.let { it1 ->
                                Checkbox(
                                    checked = if (questionNumber == prevQuesNum) it1 else false,
                                    onCheckedChange = {
                                        prevQuesNum = questionNumber
//                                        Log.e("AA", viewModel.state.value.toString())
                                        if (viewModel.state.value){
                                            isSelected = !isSelected!!
                                            viewModel.state.value = false
                                            selectedAnswer = item
                                        } else if(isSelected as Boolean){
                                            isSelected = !isSelected!!
                                            viewModel.state.value = true
                                            selectedAnswer = ""
                                        }
                                    },
                                )
                            }
                            Text(
                                text = item,
                                modifier = Modifier
                                    .padding(24.dp)
                            )
                        }
                        Divider(color = Color.Black)
                    }
                }
            }
        }
        Row {
            Button(modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Magenta
//                            backgroundColor = colors.onBackground,
                ),
                onClick = {
                    if(viewModel.state.value){
                        Toast.makeText(context, R.string.choose , Toast.LENGTH_SHORT).show()
                    }
                    else{
                        if(selectedAnswer == question?.correctAnswer){
                            Toast.makeText(context, R.string.correct , Toast.LENGTH_SHORT).show()
                            if (questionNumber == 0){
                                viewModel.reset()
                                numberOfQuestions = ids?.size
                                NavController.popBackStack()
                                Toast.makeText(context, "Woohoo" , Toast.LENGTH_SHORT).show()
                            }
                            else{
                                questionNumber = questionNumber?.minus(1)
                            }
                        }
                        else{
                            Toast.makeText(context, R.string.wrong , Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            ) {
                Text(text = stringResource(id = R.string.confirm))
            }
            if (!errorMsg.isNullOrEmpty()) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                viewModel.reset()
            }
//            if (question == null){
//                Toast.makeText(context, "D:", Toast.LENGTH_LONG).show()
//            }
        }
    }
}


