package com.shashankmunda.pawpics.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.shashankmunda.pawpics.model.Cat

@Dao
interface CatsDao {
    @Query("SELECT * FROM cat")
    fun fetchCatsData() : List<Cat>

    @Query("DELETE FROM cat")
    fun deleteCatsData()

    @Upsert
    fun insertCatsData(cats:List<Cat>)
}