package net.kelmer.correostracker.data.network.exception

import net.kelmer.correostracker.data.network.exception.CorreosException.Companion.CODE_INVALID_CODE

class CorreosExceptionFactory {

    companion object {
        fun byCode(code: String, des: String): CorreosException {
            return when (code) {
                CODE_INVALID_CODE -> {
                    WrongCodeException(code, des)
                }
                else -> {
                    CorreosException(code, des)
                }
            }
        }
    }
}
