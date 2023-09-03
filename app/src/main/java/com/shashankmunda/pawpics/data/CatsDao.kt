package com.shashankmunda.pawpics.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CatsDao {
    @Query("SELECT * FROM cat")
    fun fetchAllCatsData() : List<Cat>

    @Query("DELETE FROM cat")
    fun deleteCatsData()

    @Upsert
    fun insertCatsData(cats:List<Cat>)

    @Query("SELECT * FROM cat where id = (:ids)")
    fun fetchSelectedCatsData(ids: List<String>) : List<Cat>

    @Query("SELECT * FROM cat where id = (:ids)")
    fun fetchSelectedCatsData(ids: String) : Cat
}