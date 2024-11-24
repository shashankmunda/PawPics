package com.shashankmunda.pawpics.data

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Keep
@Entity(tableName = "cat")
data class Cat(
	@PrimaryKey
	val id:String,
	val url:String,
	val width:Int,
	val height:Int
)

data class CatDetails(
	val id: String,
	@Json(name = "breeds") val breedInfo: BreedInfo
)

data class BreedInfo(
	val id: String,
	val name: String,
	val temperament: String,
	val origin: String,
	@Json(name = "life_span") val lifeSpan: String
)

@Keep
data class Breed(
	val name: String,
	val id: String
)

