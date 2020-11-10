package net.kelmer.correostracker.util

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import net.kelmer.correostracker.data.Resource
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert

/**
 * Created by Gabriel Sanmartín on 10/11/2020.
 */
fun <T> KArgumentCaptor<Resource<T>>.checkIsFailure(
        fNextMock: (Resource<T>) -> Unit,
        additionalChecks: (Resource.Failure) -> Unit = {}
) {
    verify(fNextMock, times(2)).invoke(capture())
    val capturedValues = allValues
    MatcherAssert.assertThat(
            capturedValues[0],
            CoreMatchers.instanceOf(Resource.InProgress::class.java)
    )
    MatcherAssert.assertThat(
            capturedValues[1],
            CoreMatchers.instanceOf(Resource.Failure::class.java)
    )
    val failure = allValues[1] as Resource.Failure
    additionalChecks(failure)
}

fun <T> KArgumentCaptor<Resource<T>>.checkIsSuccess(fNextMock: (Resource<T>) -> Unit, additionalChecks: (Resource.Success<T>) -> Unit) {
    verify(fNextMock, times(2)).invoke(capture())
    val capturedValues = allValues
    MatcherAssert.assertThat(
            capturedValues[0],
            CoreMatchers.instanceOf(Resource.InProgress::class.java)
    )
    MatcherAssert.assertThat(
            capturedValues[1],
            CoreMatchers.instanceOf(Resource.Success::class.java)
    )
    val success = allValues[1] as Resource.Success
    additionalChecks(success)
}
