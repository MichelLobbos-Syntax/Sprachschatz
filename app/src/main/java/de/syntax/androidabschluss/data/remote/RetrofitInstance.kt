package de.syntax.androidabschluss.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object  RetrofitInstance {

    // Basis-URL für die API
    private const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/"

    // Moshi-Instanz für das JSON-Parsing
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // OkHttpClient-Instanz zum Ausführen von HTTP-Anfragen mit Headern
    private val httpClient : OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("x-api-key", "or0jP2Pe2OdbO2dcz4wP6saTDcSx2co8ptuxH4Kd")
                .build()
            chain.proceed(request)
        }
        //.addInterceptor(logger)
        .build()

    // Funktion zum Abrufen einer Retrofit-Instanz
    private fun getInstance(): Retrofit {


        return Retrofit.Builder()
           .addConverterFactory(MoshiConverterFactory.create(moshi))
           .baseUrl(BASE_URL)
           .client(httpClient)
           .build()

    }
    // Lazy-Initialisierung des Retrofit-Dienstes
    val retrofitService: DictionaryApi by lazy { getInstance().create(DictionaryApi::class.java) }
}