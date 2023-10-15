package com.example.level5_task_2.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.level5_task_2.model.Question
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

//Zyv3EPYZj0dkJWDjbJbA
class QuizRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var questionDocument = firestore.collection("questions")

    private val _question: MutableLiveData<Question> = MutableLiveData()
    val question: LiveData<Question>
        get() = _question

    private val _ids: MutableLiveData<List<String>> = MutableLiveData()
    val ids: LiveData<List<String>>
        get() = _ids

    suspend fun getIds() {
        try {
            val data = questionDocument.get().await()
            withTimeout(5_000) {
                var ids = listOf<String>()
                for (i in 0 until data.documents.size){
                    ids += data.documents.get(i).id
                }
                _ids.value = ids
            }
        } catch (e: Exception) {
            throw QuestionRetrivalError("A")
        }
    }

    suspend fun getQuestion(id : String) {
        try {
            withTimeout(5_000) {
                val data = questionDocument.document(id)
                    .get()
                    .await()
                val correctAnswer = data.getString("correctAnswer").toString()

                val question = data.getString("question").toString()

                val id = data.id

                val choices = data.getData()?.
                                   get("choices").
                                   toString().
                                   replace("[", "").replace("]", "").
                                   split(", ")
               _question.value = Question(choices, correctAnswer, id, question)
            }
        } catch (e: Exception) {
            throw QuestionRetrivalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    class QuestionRetrivalError(message: String) : Exception(message)
}