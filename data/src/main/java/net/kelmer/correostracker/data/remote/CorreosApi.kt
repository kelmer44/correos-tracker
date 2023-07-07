package net.kelmer.correostracker.data.remote

import io.reactivex.Single
import net.kelmer.correostracker.dataApi.model.remote.CorreosApiParcel
import retrofit2.http.GET
import retrofit2.http.Path
//https://localizador.correos.es/canonico/eventos_envio_servicio/CD08980002461600015706P%3FcodAplicacion=60&codCanal=3&codIdioma=ES&indUltEvento=N
interface CorreosApi {

    @GET("eventos_envio_servicio/{parcelId}?codAplicacion=60&codCanal=3&codIdioma=ES&indUltEvento=N")
    fun getParcelStatus(@Path("parcelId")parcelId: String): Single<List<CorreosApiParcel>>
}
