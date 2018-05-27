package net.kelmer.correostracker.data.repository.correos

class CorreosException(val code: String,  message: String) : Exception(message) {
}