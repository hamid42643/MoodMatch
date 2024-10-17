package com.appbest.moodmatch.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class ChatGptRequest(
    @SerializedName("model") val model: String,
    @SerializedName("messages") val messages: List<Message>
)

data class Message(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)

data class ChatGptResponse(
    @SerializedName("choices") val choices: List<Choice>
)

data class Choice(
    @SerializedName("message") val message: Message
)

interface ChatGptApi {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun getChatCompletion(@Body request: ChatGptRequest): Response<ChatGptResponse>
}
