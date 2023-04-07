package net.kelmer.correostracker.dataApi.model.exception

open class CorreosException(val code: String, val mensaje: String?) : Exception(mensaje) {

    fun isInvalidCode() = code == CODE_INVALID_CODE

    companion object {
        val CODE_INVALID_CODE = "3"
    }
}
