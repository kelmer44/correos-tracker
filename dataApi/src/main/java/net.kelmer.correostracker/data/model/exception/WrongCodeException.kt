package net.kelmer.correostracker.data.model.exception

class WrongCodeException(val codError: String, val des: String?) : CorreosException(codError, des)
