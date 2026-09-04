package net.kelmer.correostracker.dataApi.model.remote.unidad

/**
 * A Correos "unidad operativa" (operative unit): the office, logistic centre or delivery unit a
 * tracking event happened at. Looked up by the `codired` carried on each [ShipmentEvent].
 *
 * GET https://api1.correos.es/admissions/admmae/api/v1/operativeUnit/1558394
 * {
 *   "unitCode": "1558394",
 *   "unitName": "CTA SANTIAGO DE COMPOSTELA",
 *   "typeCode": "CTA",
 *   "typeName": "CENTRO DE TRATAMIENTO AUTOMATIZADO",
 *   "divisionName": "TRATAMIENTO",
 *   "address": "CTRA. SANTIAGO AL AEROPUERTO, KM.11 - LAVACOLLA",
 *   "municipalityName": "SANTIAGO DE COMPOSTELA",
 *   "provinceName": "A CORUÑA",
 *   "postalCode": "15820",
 *   "coorLatWGS84": "42.9008056",
 *   "coorLonWGS84": "-8.43054222",
 *   "phoneNumber": "981 888200"
 * }
 *
 * The response carries ~78 fields; only the ones we render are modelled. Every property is
 * defaulted because the API omits or nulls fields depending on the unit type.
 *
 * Note `unitCode` keeps its leading zeroes ("0898294") — it is an identifier, never a number.
 */
data class Unidad(
    val unitCode: String? = null,
    val unitName: String? = null,
    val typeCode: String? = null,
    val typeName: String? = null,
    val address: String? = null,
    val municipalityName: String? = null,
    val provinceName: String? = null,
    val postalCode: String? = null,
    val coorLatWGS84: String? = null,
    val coorLonWGS84: String? = null,
    val phoneNumber: String? = null
)
