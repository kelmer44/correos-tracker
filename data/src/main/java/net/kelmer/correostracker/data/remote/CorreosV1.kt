package net.kelmer.correostracker.data.remote

import io.reactivex.Single
import net.kelmer.correostracker.dataApi.model.remote.v1.Parcel
import retrofit2.http.GET
import retrofit2.http.Query

//https://api1.correos.es/digital-services/searchengines/api/v1/?text=PH8GH60741026280138107G&language=ES&searchType=envio
//https://api1.correos.es/digital-services/searchengines/api/v1/envios?text=PL8MRM9802107720105001V&language=ES

interface CorreosV1 {
    @GET("searchengines/api/v1/envios?language=ES")
    fun getParcelStatus(@Query("text") parcelId: String): Single<Parcel>
}
