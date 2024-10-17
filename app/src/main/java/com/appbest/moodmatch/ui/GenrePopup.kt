package com.appbest.moodmatch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appbest.moodmatch.viewmodel.GenreViewModel
import com.appbest.moodmatch.ui.theme.MoodMatchTheme

@Composable
fun GenrePopup(viewModel: GenreViewModel, onGenreSelected: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var selectedGenre by remember { mutableStateOf("") }
    val genres by viewModel.genres.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Select Genre")
        }

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    modifier = Modifier.size(300.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Genre", style = MaterialTheme.typography.h6)
                        Spacer(modifier = Modifier.height(8.dp))
                        genres.forEach { genre ->
                            Text(
                                text = genre.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        onGenreSelected(genre.name) // Pass selected genre
                                        showDialog = false
                                        showConfirmationDialog = true
                                    }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { showDialog = false }) {
                            Text("Close")
                        }
                    }
                }
            }
        }

        if (showConfirmationDialog) {
            Dialog(onDismissRequest = { showConfirmationDialog = false }) {
                Surface(
                    modifier = Modifier.size(200.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "You selected $selectedGenre", style = MaterialTheme.typography.h6)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { showConfirmationDialog = false }) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GenrePopupPreview() {
    val viewModel: GenreViewModel = viewModel()
    MoodMatchTheme {
        GenrePopup(viewModel = viewModel, onGenreSelected = {})
    }
}
