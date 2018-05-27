package net.kelmer.correostracker.data.network.correos

import io.reactivex.Single
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import retrofit2.http.GET
import retrofit2.http.Path

interface CorreosApi {


    @GET("eventos_envio_servicio/{parcelId}?codAplicacion=60&codCanal=3&codIdioma=ES&indUltEvento=N")
    fun getParcelStatus(@Path("parcelId")parcelId: String): Single<List<CorreosApiParcel>>
}