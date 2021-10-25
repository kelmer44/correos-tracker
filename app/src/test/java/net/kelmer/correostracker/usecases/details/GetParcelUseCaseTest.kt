package net.kelmer.correostracker.usecases.details

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.Assert.assertEquals
import net.kelmer.correostracker.base.usecase.UseCase
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiEvent
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.util.NetworkInteractor
import net.kelmer.correostracker.util.TrampolineSchedulerProvider
import net.kelmer.correostracker.util.checkIsFailure
import net.kelmer.correostracker.util.checkIsSuccess
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Gabriel Sanmartín on 10/11/2020.
 */
class GetParcelUseCaseTest {

    private val schedulers = TrampolineSchedulerProvider()

    private val disposables = mutableListOf<UseCase<*, *>>()

    @Before
    fun setUp() {
        RxJavaPlugins.setComputationSchedulerHandler { schedulers.computation() }
    }

    companion object {
        const val CODE = "TRACKINGCODE"
        const val REFCLIENTE = "12345"
        const val CODPRODUCTO = "1234X"

        val statusStub = CorreosApiEvent(
            fecEvento = "19/03/2020",
            codEvento = "P040000V",
            horEvento = "19:08:16",
            fase = "2",
            desTextoResumen = "Clasificado",
            desTextoAmpliado = "Envío clasificado en Centro Logístico",
            unidad = "CTA SANTIAGO DE COMPOSTELA"

        )
        val localParcelStub = LocalParcelReference(
            code = "CODE",
            trackingCode = CODE,
            parcelName = "NAME",
            stance = LocalParcelReference.Stance.INCOMING,
            ultimoEstado = statusStub,
            lastChecked = System.currentTimeMillis(),
            largo = "40",
            ancho = "30",
            alto = "10",
            peso = "5",
            refCliente = REFCLIENTE,
            codProducto = CODPRODUCTO,
            fechaCalculada = "",
            notify = true,
            LocalParcelReference.UpdateStatus.OK
        )

        val correosApiParcel = CorreosApiParcel(
            codEnvio = CODE,
            refCliente = REFCLIENTE,
            codProducto = CODPRODUCTO,
            fechaCalculada = "",
            largo = "40",
            ancho = "30",
            alto = "10",
            peso = "5",
            eventos = listOf(
                statusStub
            ),
            error = null
        )
    }

    @Test
    fun `Get Parcel, if successful, continues the flow as a Success `() {

        val mockLocalRepository: LocalParcelRepository = mock {
            on {
                getParcel(CODE)
            }.doReturn(Flowable.just(localParcelStub))
        }

        val mockCorreosRepository: CorreosRepository = mock {
            on {
                getParcelStatus(CODE)
            }.doReturn(
                Single.just(correosApiParcel)
            )
        }

        val networkInteractorMock: NetworkInteractor = mock {
            on {
                hasNetworkConnectionCompletable()
            }.doReturn(Completable.complete())
        }

        val testUseCase = GetParcelUseCase(
            mockLocalRepository,
            mockCorreosRepository
        )
        testUseCase.schedulerProvider = schedulers
        testUseCase.networkInteractor = networkInteractorMock

        val fNextMock: (net.kelmer.correostracker.data.Resource<ParcelDetailDTO>) -> Unit = mock {
            whenever(mock.invoke(any())).thenReturn(Unit)
        }
        testUseCase.execute(GetParcelUseCase.Params(CODE), fNextMock)
        argumentCaptor<net.kelmer.correostracker.data.Resource<ParcelDetailDTO>>().apply {
            checkIsSuccess(fNextMock) {
                assertEquals(CODE, it.data.code)
            }
        }
    }

    @Test
    fun `Get Parcel, if error happens, continues the flow as a Failure object`() {

        val mockLocalRepository: LocalParcelRepository = mock {
            on {
                getParcel(CODE)
            }.doReturn(Flowable.just(localParcelStub))
        }

        val mockCorreosRepository: CorreosRepository = mock {
            on {
                getParcelStatus(CODE)
            }.doReturn(
                Single.error(Exception("TEST EXCEPTION"))
            )
        }

        val networkInteractorMock: NetworkInteractor = mock {
            on {
                hasNetworkConnectionCompletable()
            }.doReturn(Completable.complete())
        }

        val testUseCase = GetParcelUseCase(
            mockLocalRepository,
            mockCorreosRepository
        )
        testUseCase.schedulerProvider = schedulers
        testUseCase.networkInteractor = networkInteractorMock

        val fNextMock: (net.kelmer.correostracker.data.Resource<ParcelDetailDTO>) -> Unit = mock {
            whenever(mock.invoke(any())).thenReturn(Unit)
        }
        testUseCase.execute(GetParcelUseCase.Params(CODE), fNextMock)
        argumentCaptor<net.kelmer.correostracker.data.Resource<ParcelDetailDTO>>().apply {
            checkIsFailure(fNextMock) {
                assertEquals("TEST EXCEPTION", it.exception.message)
            }
        }
    }
    @After
    fun tearDown() {
        disposables.forEach {
            it.dispose()
        }
    }
}
