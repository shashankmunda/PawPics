package com.shashankmunda.pawpics.repository

import com.shashankmunda.pawpics.api.CatApiService
import com.shashankmunda.pawpics.data.CatsDao
import com.shashankmunda.pawpics.model.Cat
import com.shashankmunda.pawpics.util.ImageSize
import com.shashankmunda.pawpics.util.MimeType
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class CatRepository @Inject constructor(private var catApiService: CatApiService,private var catsDao: CatsDao) {
    suspend fun getCats(limit:Int,size:ImageSize,mimeType:MimeType,pageNo: Int): List<Cat>? {
        val response = catApiService.getCats(limit,size.toString().lowercase(),mimeType.toString().lowercase(),pageNo)
        return if(response.isSuccessful && response.body()!=null) {
            val cats = response.body()!!.distinctBy {
                it.id
            }
            catsDao.insertCatsData(cats)
            cats
        } else null
    }

    suspend fun getCatImageDetails(id: String): Cat? {
        return catsDao.fetchSelectedCatsData(id)
    }
}