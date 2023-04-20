package net.kelmer.correostracker.dataApi.model.exception

class WrongCodeException(private val codError: String, private val des: String?) : CorreosException(codError, des)
class InvalidDetailDeepLink(private val des: String) : CorreosException(INVALID_DEEPLINK, des)
