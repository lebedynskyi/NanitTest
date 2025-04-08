package com.example.birthday.di

import com.example.birthday.data.AndroidFileStorage
import com.example.birthday.data.AndroidPreferenceKeyValueStore
import com.example.birthday.domain.KeyValueStorage
import com.example.birthday.domain.FileStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
interface DataBinder {
    @Binds
    fun bindKeyValueStore(keyValue: AndroidPreferenceKeyValueStore): KeyValueStorage

    @Binds
    fun bindAndroidFileStorage(storage: AndroidFileStorage) : FileStorage
}

@Module
@InstallIn(SingletonComponent::class)
open class DataProvider {

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
