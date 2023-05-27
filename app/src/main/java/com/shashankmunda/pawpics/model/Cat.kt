package com.shashankmunda.pawpics.model

import androidx.annotation.Keep

@Keep
data class Cat(
	val id:String,
	val url:String,
	val width:Int,
	val height:Int
)