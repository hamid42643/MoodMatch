package com.appbest.moodmatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appbest.moodmatch.ui.theme.MoodMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodMatchTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    GenrePopupMenu()
                }
            }
        }
    }
}

@Composable
fun GenrePopupMenu() {
    var expanded by remember { mutableStateOf(false) }
    val genres = listOf("Rock", "Pop", "Classical", "Jazz", "Hip Hop", "Country", "Blues", "Electronic", "Reggae", "Folk")

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { expanded = true }) {
            Text(text = "Genre")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genres.forEach { genre ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    // Handle genre selection here
                }) {
                    Text(text = genre)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoodMatchTheme {
        GenrePopupMenu()
    }
}