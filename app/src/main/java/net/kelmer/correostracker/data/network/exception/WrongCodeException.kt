package net.kelmer.correostracker.data.network.exception

class WrongCodeException(val codError: String, val des: String?) : CorreosException(codError, des)
