package com.appbest.moodmatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appbest.moodmatch.repository.ChatGptRepository

class ChatGptViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatGptViewModel::class.java)) {
            return ChatGptViewModel(ChatGptRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
