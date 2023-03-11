package net.kelmer.correostracker.data.network.correos

import io.reactivex.Single
import net.kelmer.correostracker.data.model.remote.unidad.Unidad
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

//https://apicorp.correos.es/maestros-cloud/v2/unidadesOperativas/3818894
interface Unidades {

    @GET("maestros-cloud/v2/unidadesOperativas/{id}")
    @Headers("Ocp-Apim-Subscription-Key: 6ba726285ddb4dd5bba3fd2cc8fb4fc5")
    fun getUnidad(@Path("id") oficinaId: String): Single<Unidad>
}
