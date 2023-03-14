package net.kelmer.correostracker.data.model.exception

open class CorreosException(val code: String, val mensaje: String?) : Exception(mensaje) {

    fun isInvalidCode() = code == CODE_INVALID_CODE

    companion object {
        val CODE_INVALID_CODE = "3"
    }
}
