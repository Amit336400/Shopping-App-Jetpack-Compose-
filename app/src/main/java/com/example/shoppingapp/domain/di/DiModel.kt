package com.example.shoppingapp.domain.di

import com.example.shoppingapp.data.RepoImpl.RepoImpl
import com.example.shoppingapp.domain.reop.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DiModel{

    @Provides
    @Singleton
    fun provideRepo(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore) : Repo{
        return RepoImpl(firebaseAuth,firestore)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }



}