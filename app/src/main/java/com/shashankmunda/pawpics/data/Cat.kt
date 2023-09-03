package com.shashankmunda.pawpics.data

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "cat")
data class Cat(
	@PrimaryKey
	val id:String,
	val url:String,
	val width:Int,
	val height:Int
)