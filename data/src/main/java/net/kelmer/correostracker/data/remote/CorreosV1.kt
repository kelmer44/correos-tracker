package net.kelmer.correostracker.data.remote

import io.reactivex.Single
import net.kelmer.correostracker.dataApi.model.remote.v1.Parcel
import retrofit2.http.GET
import retrofit2.http.Query

//https://api1.correos.es/digital-services/searchengines/api/v1/?text=PH8GH60741026280138107G&language=ES&searchType=envio


interface CorreosV1 {
    @GET("searchengines/api/v1/?language=ES&searchType=envio")
    fun getParcelStatus(@Query("text") parcelId: String): Single<Parcel>
}
