package com.appbest.moodmatch.repository

import com.appbest.moodmatch.api.ChatGptApi
import com.appbest.moodmatch.api.ChatGptRequest
import com.appbest.moodmatch.api.ChatGptResponse
import com.appbest.moodmatch.api.RetrofitInstance
import retrofit2.Response

class ChatGptRepository {
    private val api: ChatGptApi = RetrofitInstance.api

    suspend fun getChatCompletion(request: ChatGptRequest): Response<ChatGptResponse> {
        return api.getChatCompletion(request)
    }
}
