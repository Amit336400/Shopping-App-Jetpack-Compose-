package com.example.shoppingapp.domain.useCase

import com.example.shoppingapp.common.CategoryModel
import com.example.shoppingapp.common.ProductModel
import com.example.shoppingapp.common.ResultState
import com.example.shoppingapp.common.UserData
import com.example.shoppingapp.domain.reop.Repo
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(private val repo: Repo) {
    fun createUser(userData: UserData): Flow<ResultState<String>> {
        return repo.registerUserWithEmailAndPassword(userData)
    }

    fun loginUser(userData: UserData): Flow<ResultState<String>> {
        return repo.loginWithEmailAndPassword(userData)
    }

    fun getUserById(uid: String): Flow<ResultState<UserData>> {
        return repo.getUserById(uid)
    }

    fun getProduct(): Flow<ResultState<List<ProductModel>>> {
        return repo.getProduct()
    }

    suspend fun getProductDAta(
        lastDocumentSnapshot: DocumentSnapshot?,
        limit: Long,
    ): Flow<ResultState<List<ProductModel>>> {
        return repo.fetchPaginatedData(lastDocumentSnapshot, limit)
    }

    suspend fun getCategory(): Flow<ResultState<List<CategoryModel>>> {
        return repo.getAllCategory()
    }

    suspend fun addUserDataFromDB(
        userData: UserData,
        DbLocation: String,
        userId: String,
    ): Flow<ResultState<String>> {
        return repo.addUserData(userData = userData, DbLocation = DbLocation, userId = userId)
    }

}