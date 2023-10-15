package com.example.level5_task_2.QuizViewModel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.level5_task_2.model.Question
import com.example.level5_task_2.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FIRESTORE"
    private val quizRepository: QuizRepository = QuizRepository()

    val question: LiveData<Question> = quizRepository.question

    var state = mutableStateOf(true)

    val ids: LiveData<List<String>> = quizRepository.ids

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    fun getQuestion(id : String) {
        viewModelScope.launch {
            try {
                quizRepository.getQuestion(id)
            } catch (ex: QuizRepository.QuestionRetrivalError) {
                val errorMsg = "Something went wrong while retrieving question"
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

    fun getIds() {
        viewModelScope.launch {
            try {
                quizRepository.getIds()
            } catch (ex: QuizRepository.QuestionRetrivalError) {
                val errorMsg = "Something went wrong while retrieving question"
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

    fun reset() {
        _errorText.value = ""
        state = mutableStateOf(true)
    }
}