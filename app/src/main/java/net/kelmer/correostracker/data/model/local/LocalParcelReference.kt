package net.kelmer.correostracker.data.model.local

import java.util.*

/**
 * Created by gabriel on 25/03/2018.
 */
data class LocalParcelReference(val name :String, val code: String,
                                val lastChecked: Date = Date()) {
}