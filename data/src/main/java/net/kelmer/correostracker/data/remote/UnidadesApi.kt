package net.kelmer.correostracker.data.remote

import io.reactivex.Single
import net.kelmer.correostracker.dataApi.model.remote.unidad.Unidad
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

//https://apicorp.correos.es/maestros-cloud/v2/unidadesOperativas/3818894
interface UnidadesApi {

    @GET("maestros-cloud/v2/unidadesOperativas/{id}")
    @Headers("Ocp-Apim-Subscription-Key: 6ba726285ddb4dd5bba3fd2cc8fb4fc5")
    fun getUnidad(@Path("id") oficinaId: String): Single<Unidad>
}
