package com.shashankmunda.pawpics.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Cat::class], version = 1, exportSchema = false)
abstract class CatsDatabase: RoomDatabase() {
    abstract fun getCatsDao(): CatsDao
}