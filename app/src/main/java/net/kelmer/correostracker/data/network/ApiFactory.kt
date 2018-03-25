package net.kelmer.correostracker.data.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory {
    val BASE_URL = "https://localizador.correos.es/canonico/"

    val gsonConverterFactory = GsonConverterFactory.create(GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create())

    val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    val adapterFactory = RxJava2CallAdapterFactory.create()

    val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(adapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()


    fun provideCorreosApi()
            = retrofit.create(CorreosApi::class.java)

    companion object {
        private var instance: ApiFactory? = null

        fun instance() : ApiFactory {
            if (instance == null) {
                instance = ApiFactory()
            }
            return instance!!
        }
    }

}
