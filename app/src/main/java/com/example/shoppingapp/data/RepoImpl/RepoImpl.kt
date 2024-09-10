package com.example.shoppingapp.data.RepoImpl

import android.util.Log
import com.example.shoppingapp.common.ADD_TO_CART
import com.example.shoppingapp.common.ALL_PRODUCT_LOCATION
import com.example.shoppingapp.common.ALL_USER_COLLECTION
import com.example.shoppingapp.common.CategoryModel
import com.example.shoppingapp.common.DATA_IS_PRESENT
import com.example.shoppingapp.common.ERROR
import com.example.shoppingapp.common.ProductModel
import com.example.shoppingapp.common.ResultState
import com.example.shoppingapp.common.SUCCESSFUL
import com.example.shoppingapp.common.USER_DATA
import com.example.shoppingapp.common.USER_LOCATION
import com.example.shoppingapp.common.UserData
import com.example.shoppingapp.common.UserLocation
import com.example.shoppingapp.common.WISHLIST
import com.example.shoppingapp.common.setProduct
import com.example.shoppingapp.domain.reop.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RepoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
) : Repo {

    override fun registerUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseAuth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener {
                    Log.d("create", "$it ")
                    if (it.isSuccessful) {
                        trySend(ResultState.Success(data = "User Created"))
                    } else {
                        if (it.exception != null) {
                            trySend(ResultState.Error(error = it.exception!!.localizedMessage.toString()))
                        }
                    }
                }

            awaitClose {
                close()
            }
        }

    override fun loginWithEmailAndPassword(userData: UserData): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            Log.d("EmailAndPass", "$userData")

            firebaseAuth.signInWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success(data = "Login Successfully"))
                    } else {
                        trySend(ResultState.Error(error = "error"))
                    }

                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.localizedMessage!!.toString()))

                }
            awaitClose {
                close()
            }
        }

    override fun getUserById(uid: String): Flow<ResultState<UserData>> = callbackFlow {
        firebaseFirestore.collection(ALL_USER_COLLECTION).document(uid).collection(USER_DATA)
            .document(uid).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var data = it.result.toObject(UserData::class.java)
                    if (!(data == null)) {
                        trySend(ResultState.Success(data = data))
                    } else {
                        trySend(ResultState.Error(it.exception.toString()))
                    }
                }

            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage!!.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun getProduct(): Flow<ResultState<List<ProductModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(ALL_PRODUCT_LOCATION).limit(10).addSnapshotListener { v, e ->
            if (!(v == null)) {
                val categories = v.documents.mapNotNull { document ->
                    document.toObject(ProductModel::class.java)?.apply {

                    }
                }
                trySend(ResultState.Success(categories))
            } else if (e != null) {
                trySend(ResultState.Error(error = e.localizedMessage))
            }
        }

        awaitClose {
            close()
        }
    }

    override fun getAllCategory(): Flow<ResultState<List<CategoryModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection("category").addSnapshotListener { value, e ->
            if (value != null) {
                val data = value.documents.map { documentSnapshot ->
                    documentSnapshot.toObject(CategoryModel::class.java) ?: CategoryModel()

                }
                trySend(ResultState.Success(data = data))
                Log.d("dataFlow", "getAllCategory: $value")

            }
            if (e != null) {
                trySend(ResultState.Error(e.localizedMessage.toString()))
            }
        }


        awaitClose()
    }

    override suspend fun getSingleProduct(productId: String): Flow<ResultState<ProductModel>> =
        callbackFlow {
            trySend(ResultState.Loading)

            val listenerRegistration = firebaseFirestore.collection(ALL_PRODUCT_LOCATION)
                .document(productId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ResultState.Error(error = error.localizedMessage))
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val product = snapshot.toObject(ProductModel::class.java) ?: ProductModel()
                        trySend(ResultState.Success(data = product))
                    } else {
                        trySend(ResultState.Error(error = "Product not found"))
                    }
                }
            awaitClose { listenerRegistration.remove() }
        }


    override suspend fun addUserData(
        userData: UserData,
        DbLocation: String,
        userId: String,
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            //DbLocation = ALL_USER_COLLECTION
            firebaseFirestore.collection(ALL_USER_COLLECTION).document(userId)
                .collection(USER_DATA).document(userId).set(userData)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success(data = SUCCESSFUL))
                    } else {
                        trySend(ResultState.Error(error = it.exception.toString()))
                    }
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(error = e.localizedMessage))
        }
        awaitClose()
    }

    override suspend fun addWishList(productId: String, userId: String): Flow<ResultState<String>> =
        callbackFlow {

            trySend(ResultState.Loading)
            firebaseFirestore
                .collection(ALL_USER_COLLECTION).document(userId).collection(WISHLIST)
                .document(productId)
                .set(setProduct(productId))
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success(SUCCESSFUL))
                    }
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.localizedMessage))
                }
            awaitClose()
        }

    override suspend fun checkWishList(
        productId: String,
        userId: String,
    ): Flow<ResultState<String>> = callbackFlow {
        firebaseFirestore.collection(ALL_USER_COLLECTION)
            .document(userId)
            .collection(WISHLIST)
            .document(productId).get().addOnSuccessListener { doc ->
                if (doc.exists()) {
                    trySend(ResultState.Success(DATA_IS_PRESENT))
                }
            }
        awaitClose()
    }

    override suspend fun addToCart(productId: String, userId: String): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFirestore.collection(ALL_USER_COLLECTION).document(userId).collection(
                ADD_TO_CART
            ).document(productId).set(setProduct(name = productId)).addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ResultState.Success(SUCCESSFUL))

                    Log.d("CartData", "${it.isSuccessful}")

                } else if (it.exception != null) {
                    trySend(ResultState.Error(error = it.exception!!.localizedMessage))
                }
            }

            awaitClose()
        }

    override suspend fun getToCartItems(
        userId: String,
    ): Flow<ResultState<List<setProduct>>> = callbackFlow {
        firebaseFirestore.collection(ALL_USER_COLLECTION).document(userId).collection(ADD_TO_CART)
            .addSnapshotListener { doc, e ->
                if (e != null) {
                    trySend(ResultState.Error(error = e.localizedMessage))
                    return@addSnapshotListener
                }

                val productList = doc?.documents?.mapNotNull { document ->
                    document.toObject(setProduct::class.java)
                } ?: emptyList()

                trySend(ResultState.Success(productList))

            }
        awaitClose()
    }

    override suspend fun addUserLocation(
        userId: String,
        userLocation: UserLocation,
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        // Reference to the user's location document
        val locationDocumentRef = firebaseFirestore.collection(ALL_USER_COLLECTION)
            .document(userId)
            .collection(USER_LOCATION)
            .document(userId)

        locationDocumentRef.set(userLocation, SetOptions.merge())  // Using merge() to add or update
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(ResultState.Success(data = SUCCESSFUL))
                } else {
                    task.exception?.let { exception ->
                        trySend(ResultState.Error(error = exception.localizedMessage))
                    }
                }
            }.addOnFailureListener { exception ->
                trySend(ResultState.Error(error = exception.localizedMessage))
            }

        awaitClose()
    }


    override suspend fun getLocation(userId: String): Flow<ResultState<UserLocation>> =
        callbackFlow {
            firebaseFirestore.collection(ALL_USER_COLLECTION).document(userId)
                .collection(USER_LOCATION).document(userId).addSnapshotListener { value, error ->
                    if (value != null) {
                        if (value.exists()) {
                            val data = value.toObject(UserLocation::class.java) ?: UserLocation()

                            trySend(ResultState.Success(data = data))

                        } else {
                            trySend(ResultState.Error(error = ERROR))
                        }
                    } else {
                        trySend(ResultState.Error(error = ERROR))
                    }
                }
            awaitClose()
        }


    override suspend fun grtProductByIds(data: List<setProduct>): Flow<ResultState<List<ProductModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)
            val productList = mutableMapOf<String, ProductModel>()
            data.map { product ->
                firebaseFirestore.collection(ALL_PRODUCT_LOCATION).document(product.name)
                    .addSnapshotListener { snapshot, exception ->
                        exception?.let { close(it) } ?: snapshot?.let {
                            if (it.exists()) {
                                it.toObject(ProductModel::class.java)?.let { model ->
                                    productList[model.productId.toString()] = model
                                    trySend(ResultState.Success(data = productList.values.toList()))
                                }
                            }
                        }
                    }
            }
            awaitClose()
        }

    override suspend fun deleteItem(location :String,productId: String, userId: String): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            val cartRef = firebaseFirestore.collection(ALL_USER_COLLECTION)
                .document(userId)
                .collection(ADD_TO_CART)
                .document(productId)
            cartRef.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(ResultState.Success("Item successfully deleted from cart"))
                    Log.d("CartData", "Item successfully deleted from cart")
                } else if (task.exception != null) {
                    trySend(ResultState.Error(task.exception!!.localizedMessage))
                }
            }
            awaitClose()
        }


    override suspend fun fetchPaginatedData(
        lastDocumentSnapshot: DocumentSnapshot?,
        limit: Long,
    ): Flow<ResultState<List<ProductModel>>> = callbackFlow {

        trySend(ResultState.Loading)
        val query = if (lastDocumentSnapshot == null) {
            firebaseFirestore.collection(ALL_PRODUCT_LOCATION).limit(limit)
        } else {
            firebaseFirestore.collection(ALL_PRODUCT_LOCATION)
                .startAfter(lastDocumentSnapshot)
                .limit(limit)
        }

        query.addSnapshotListener { snapshot, e ->
            if (e != null) {
                trySend(ResultState.Error(error = e.toString()))
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val data = snapshot.toObjects(ProductModel::class.java)
                trySend(ResultState.Success(data = data))
            }
        }
        awaitClose {
            close()
        }
    }

}