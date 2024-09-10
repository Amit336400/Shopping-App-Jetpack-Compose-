package com.example.shoppingapp.presentation.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchProductScreen(viewModel: searchViewModel = hiltViewModel()) {
    var searchText by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(listOf<String>()) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
              viewModel.onSearchTextChanged(newText) // Trigger Firebase query in ViewModel
            },
            label = { Text("Search Product") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Show Suggestions
        LazyColumn {
            items(suggestions) { suggestion ->
                Text(
                    text = suggestion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            searchText = suggestion
                            // Handle suggestion click (e.g., search for the product)
                        }
                        .padding(8.dp),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }

    // Update suggestions from ViewModel
   suggestions = viewModel.suggestions.value ?: emptyList()
}
