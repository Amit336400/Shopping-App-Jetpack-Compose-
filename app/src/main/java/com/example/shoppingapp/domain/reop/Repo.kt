package com.example.shoppingapp.domain.reop

import com.example.shoppingapp.common.CategoryModel
import com.example.shoppingapp.common.ProductModel
import com.example.shoppingapp.common.ResultState
import com.example.shoppingapp.common.UserData
import com.example.shoppingapp.common.UserLocation
import com.example.shoppingapp.common.setProduct
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface Repo {
    fun registerUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>>
    fun loginWithEmailAndPassword(userData: UserData): Flow<ResultState<String>>
    fun getUserById(uid: String): Flow<ResultState<UserData>>

    fun getProduct(): Flow<ResultState<List<ProductModel>>>
    fun getAllCategory(): Flow<ResultState<List<CategoryModel>>>

    suspend fun getSingleProduct(productId: String): Flow<ResultState<ProductModel>>
    suspend fun addUserData(userData: UserData, DbLocation: String, userId: String, ): Flow<ResultState<String>>
    suspend fun addWishList(productId: String,userId: String) : Flow<ResultState<String>>

    suspend fun checkWishList(productId: String,userId: String) :Flow<ResultState<String>>

    suspend fun addToCart(productId: String , userId: String) : Flow<ResultState<String>>
    suspend fun getToCartItems( userId: String) : Flow<ResultState<List<setProduct>>>

    suspend fun addUserLocation(userId: String , userLocation: UserLocation) : Flow<ResultState<String>>
     suspend fun getLocation(userId: String) : Flow<ResultState<UserLocation>>

     suspend fun grtProductByIds(data: List<setProduct>): Flow<ResultState<List<ProductModel>>>

     suspend fun deleteItem(location :String, productId: String , userId: String) : Flow<ResultState<String>>

    suspend fun fetchPaginatedData(
        lastDocumentSnapshot: DocumentSnapshot?,
        limit: Long,
    ): Flow<ResultState<List<ProductModel>>>


}