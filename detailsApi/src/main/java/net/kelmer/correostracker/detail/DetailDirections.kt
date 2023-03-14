package net.kelmer.correostracker.detail

import net.kelmer.correostracker.route.Router
interface DetailDirections {

    fun routeToDetail(parcelId: String) : Router
}
