package net.kelmer.correostracker.dataApi.model.exception

class WrongCodeException(val codError: String, val des: String?) : CorreosException(codError, des)
