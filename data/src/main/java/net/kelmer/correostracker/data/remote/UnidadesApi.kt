package net.kelmer.correostracker.data.remote

import io.reactivex.Single
import net.kelmer.correostracker.dataApi.model.remote.unidad.Unidad
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Resolves a tracking event's `codired` into the operative unit (office / logistic centre) it
 * happened at.
 *
 * https://api1.correos.es/admissions/admmae/api/v1/operativeUnit/1558394
 *
 * Replaces the decommissioned `apicorp.correos.es/maestros-cloud/v2/unidadesOperativas/{id}`, which
 * now answers 404 for every input. This is the same endpoint correos.es itself calls (it names it
 * `getAddressByCodired`), and it returns the unit name plus a full address and coordinates.
 *
 * The credentials below are the public web client's, lifted from the correos.es JS bundle; there is
 * no per-user auth on this endpoint.
 */
interface UnidadesApi {

    @GET("admissions/admmae/api/v1/operativeUnit/{codired}")
    @Headers(
        "client_id: 0adccfb378064bcca810636546cf175a",
        "client_secret: 91927d7b582f416BA44871f06dF73893",
        "Ocp-Apim-Subscription-Key: 981d0e4f0a064cbdbf32e04ef0b4426b",
        "Accept: */*"
    )
    fun getUnidad(@Path("codired") codired: String): Single<Unidad>
}
