package com.appbest.moodmatch.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appbest.moodmatch.api.ChatGptRequest
import com.appbest.moodmatch.api.ChatGptResponse
import com.appbest.moodmatch.api.Message
import com.appbest.moodmatch.repository.ChatGptRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

private const val selectedModel = "gpt-4o-mini"

class ChatGptViewModel(private val repository: ChatGptRepository) : ViewModel() {

    private val _chatGptResponse = MutableStateFlow<String>("")
    val chatGptResponse: StateFlow<String> = _chatGptResponse.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun findMatch(otherCharacteristics: String) {
        val messages = listOf(
            Message(
                role = "system",
                content = """
                    find and list ten songs with the following characteristics:
                    - music qualities: $otherCharacteristics
                    output SHOULD be in csv format. columns should be song name, artist, and URI in this order. URI should be YouTube URI where the name of the song can be searched in YouTube. dont add any other text just this csv output.
                """.trimIndent()
            )
        )
        val request = ChatGptRequest(model = selectedModel, messages = messages)

        viewModelScope.launch {
            _isLoading.value = true  // Start showing the loading spinner
            Log.d("ChatGptViewModel", "Sending request to GPT model: gpt-4o-mini")

            try {
                val response: Response<ChatGptResponse> = repository.getChatCompletion(request)
                if (response.isSuccessful) {
                    val content = response.body()?.choices?.get(0)?.message?.content ?: "No response"
                    _chatGptResponse.value = content
                    _errorMessage.value = null
                    Log.d("ChatGptViewModel", "Received response: $content")
                } else {
                    _chatGptResponse.value = "Failed to get response"
                    _errorMessage.value = "Failed to get response from the server"
                    Log.e("ChatGptViewModel", """
                        Failed response:
                        Status Code: ${response.code()}
                        Headers: ${response.headers()}
                        Error Body: ${response.errorBody()?.string()}
                        Raw Response: ${response.raw()}
                    """.trimIndent())
                }
            } catch (e: SocketTimeoutException) {
                _errorMessage.value = "Request timed out. Please try again."
                Log.e("ChatGptViewModel", "SocketTimeoutException: ${e.message}", e)
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
                Log.e("ChatGptViewModel", "Exception: ${e.message}", e)
            } finally {
                _isLoading.value = false  // Stop showing the loading spinner
                Log.d("ChatGptViewModel", "Request completed")
            }
        }
    }
}
