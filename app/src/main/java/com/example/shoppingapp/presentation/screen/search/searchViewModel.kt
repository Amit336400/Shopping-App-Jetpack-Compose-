package com.example.shoppingapp.presentation.screen.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.common.ALL_PRODUCT_LOCATION
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class searchViewModel @Inject constructor(val firestore: FirebaseFirestore): ViewModel() {
    val suggestions = MutableLiveData<List<String>>()

    fun onSearchTextChanged(query: String) {
        if (query.isNotEmpty()) {
            // Query Firebase for suggestions
            firestore.collection(ALL_PRODUCT_LOCATION)
                .whereGreaterThanOrEqualTo("productName", query)
                .whereLessThanOrEqualTo("productName", query + '\uf8ff')
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val suggestionList = querySnapshot.documents.map { document ->
                        document.getString("productName") ?: ""
                    }
                    suggestions.postValue(suggestionList)
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                    suggestions.postValue(emptyList())
                }
        } else {
            suggestions.postValue(emptyList()) // Clear suggestions when query is empty
        }
    }
}