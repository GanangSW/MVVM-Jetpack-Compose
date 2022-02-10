package com.gsw.mvvmjetpackcompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gsw.mvvmjetpackcompose.models.User
import com.gsw.mvvmjetpackcompose.models.UserDao

@Database(entities = [User::class], version = 1)
abstract class LocalDb : RoomDatabase() {

    abstract fun userDao(): UserDao
}