package de.syntax.androidabschluss.data.remote

import de.syntax.androidabschluss.data.models.WordResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("en/{word}")
    suspend fun getmeaning(@Path("word") word: String) : Response<List<WordResult>>

}