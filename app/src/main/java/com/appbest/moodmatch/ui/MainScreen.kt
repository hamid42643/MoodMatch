package com.appbest.moodmatch.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appbest.moodmatch.R
import com.appbest.moodmatch.ui.theme.MoodMatchTheme
import com.appbest.moodmatch.viewmodel.ChatGptViewModel
import com.appbest.moodmatch.viewmodel.ChatGptViewModelFactory
import com.appbest.moodmatch.viewmodel.GenreViewModel
import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MainScreen(
    chatGptResponse: String = "",
    errorMessage: String? = null,
    isLoading: Boolean = false,
    onFindMatch: (String, String, String) -> Unit = { _, _, _ -> }
) {

    val chatGptViewModel: ChatGptViewModel = viewModel(factory = ChatGptViewModelFactory())
    val chatGptResponse by chatGptViewModel.chatGptResponse.collectAsState()
    val errorMessage by chatGptViewModel.errorMessage.collectAsState()
    val isLoading by chatGptViewModel.isLoading.collectAsState()

    val genreViewModel: GenreViewModel = viewModel()
    val context = LocalContext.current
    var markdownContent by remember { mutableStateOf(chatGptResponse) }
    var selectedGenre by remember { mutableStateOf("Any") }
    var selectedMood by remember { mutableStateOf("Any") }
    var otherCharacteristics by remember { mutableStateOf("") }

    val genres = genreViewModel.genres.collectAsState().value.map { it.name }
    val moods = listOf(
        "Happy",
        "Sad",
        "Energetic",
        "Calm",
        "Angry",
        "Romantic",
        "Melancholic",
        "Uplifting",
        "Reflective",
        "Mysterious"
    )

    // Update markdownContent whenever chatGptResponse changes
    LaunchedEffect(chatGptResponse) {
        markdownContent = chatGptResponse
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Add the image above the title
        Image(
            painter = painterResource(id = R.drawable.image), // Use the appropriate resource ID
            contentDescription = "App Logo",
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp),
            contentScale = ContentScale.Fit
        )


        Text(
            text = "VibeTune",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
//        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            // Display the loading spinner when loading
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            // Other Characteristics TextField
            OutlinedTextField(
                value = otherCharacteristics,
                onValueChange = { otherCharacteristics = it },
                label = { Text("type music characteristics", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color(0xFFF7F7F7),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    textColor = Color.Black
                )
            )

//            Spacer(modifier = Modifier.height(24.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colors.error,
                    fontSize = 33.sp, // Increased font size for the error message
                    fontWeight = FontWeight.Medium, // Medium weight for emphasis
                    modifier = Modifier
                        .padding(40.dp) // Larger padding around the error message
                )
            } else if (markdownContent.isNotEmpty()) {
                CalmSongsListStyled(csvText = markdownContent)
            }

            // Find Match Button
            Button(
                onClick = {

                    chatGptViewModel.findMatch(
                        otherCharacteristics
                    )

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFCED846))
            ) {
                Text(text = "Find Match", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Bottom buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = { /* TODO: Handle Export */ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                ) {
                    Text(text = "Export playlist", color = Color.Black)
                }

                Button(
                    onClick = { /* TODO: Handle Sign up */ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                ) {
                    Text(text = "Sign up", color = Color.Black)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MoodMatchTheme {
        MainScreen(
            chatGptResponse = "",
            isLoading = false,
            onFindMatch = { _, _, _ -> } // No-op for preview
        )
    }
}



fun parseCsv(csvText: String): List<Triple<String, String, String>> {
    return csvText.lines()
        .filter { it.isNotBlank() && !it.startsWith("song name") } // Skip blank lines and the header row
        .mapNotNull { line ->
            val parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex()) // Handle commas inside fields
            if (parts.size >= 3 && parts[0].isNotBlank() && parts[2].isNotBlank()) { // Ensure we have the correct number of fields and non-empty search query
                Triple(parts[0].trim(), parts[1].trim(), parts[2].trim()) // Triple for song name, artist, and search query
            } else {
                null // Skip this line if the parsing fails
            }
        }
}







@Composable
fun CalmSongsListStyled(csvText: String) {
    val context = LocalContext.current
    val songs = parseCsv(csvText)
    var selectedSong by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .padding(5.dp),
        verticalArrangement = Arrangement.Top
    ) {
        items(songs) { song ->
            val (songTitle, artistName, _) = song
            val isSelected = selectedSong == songTitle

            Card(
                backgroundColor = if (isSelected) Color(0xFFDFF2E1) else Color.White, // Mild green background when selected
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        selectedSong = if (isSelected) null else songTitle // Toggle selection
                        openYouTubeSearch(context, songTitle, artistName) // Dynamically create and open the YouTube search URL
                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    Text(
                        text = songTitle,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = artistName,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


fun openYouTubeSearch(context: Context, songTitle: String, artistName: String) {
    // Log the raw inputs
    Log.d("CalmSongsListStyled", "Raw Song Title: $songTitle")
    Log.d("CalmSongsListStyled", "Raw Artist Name: $artistName")

    // Clean the song title and artist name by removing any surrounding quotes
    val cleanSongTitle = songTitle.trim().removeSurrounding("\"")
    val cleanArtistName = artistName.trim().removeSurrounding("\"")

    // Log the cleaned inputs
    Log.d("CalmSongsListStyled", "Cleaned Song Title: $cleanSongTitle")
    Log.d("CalmSongsListStyled", "Cleaned Artist Name: $cleanArtistName")

    // Create a YouTube search query based on the clean song title and artist name
    val query = "$cleanSongTitle $cleanArtistName".replace(" ", "+")
    val url = "https://www.youtube.com/results?search_query=$query"

    // Log the final query and URL
    Log.d("CalmSongsListStyled", "Final Query: $query")
    Log.d("CalmSongsListStyled", "Attempting to open YouTube search URL: $url")

    try {
        val uri = Uri.parse(url)
        Log.d("CalmSongsListStyled", "Parsed URI: $uri")

        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
        Log.d("CalmSongsListStyled", "Successfully started activity for URI: $uri")

    } catch (e: Exception) {
        Toast.makeText(context, "Failed to open link.", Toast.LENGTH_SHORT).show()
        Log.e("CalmSongsListStyled", "Error opening URL: $url", e)
    }
}








