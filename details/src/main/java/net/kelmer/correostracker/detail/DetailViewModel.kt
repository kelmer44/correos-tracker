package net.kelmer.correostracker.detail

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.processors.BehaviorProcessor
import net.kelmer.correostracker.dataApi.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.dataApi.repository.correos.CorreosRepository
import net.kelmer.correostracker.dataApi.repository.local.LocalParcelRepository
import net.kelmer.correostracker.deviceinfo.DeviceInfo
import net.kelmer.correostracker.util.SchedulerProvider
import net.kelmer.correostracker.viewmodel.AutoDisposeViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    localParcelRepository: LocalParcelRepository,
    private val correosRepository: CorreosRepository,
    schedulerProvider: SchedulerProvider,
    private val deviceInfo: DeviceInfo
) : AutoDisposeViewModel() {

    private val _parcelCode: String? = savedStateHandle.get(DetailFragment.KEY_PARCELCODE)
    private val parcelCode : String = requireNotNull(_parcelCode)
    init {
        Timber.i("Detail - ViewModel Created!")
    }

    private val refreshSubject = BehaviorProcessor.createDefault(Unit)

    val stateOnceAndStream = refreshSubject
        .observeOn(schedulerProvider.io())
        .switchMap {
            correosRepository.getParcelStatus(parcelCode).toFlowable()
                .zipWith(localParcelRepository.getParcel(parcelCode)) { correosParcel, localParcel ->
                    ParcelDetailDTO(
                        localParcel.parcelName,
                        localParcel.trackingCode,
                        localParcel.ancho ?: "",
                        localParcel.alto ?: "",
                        localParcel.largo ?: "",
                        localParcel.peso ?: "",
                        localParcel.refCliente ?: "",
                        localParcel.codProducto ?: "",
                        localParcel.fechaCalculada ?: "",
                        correosParcel.eventos ?: emptyList()
                    )
                }
                .map { State(parcelDetail = it, trackingCode = parcelCode, barcode = generateBarcode()) }
                .startWith(State(isLoading = true, trackingCode = parcelCode))
        }
        .subscribeOn(schedulerProvider.io())
        .startWith(State(isLoading = true, trackingCode = parcelCode))
        .onErrorReturn { throwable -> State(error = throwable, trackingCode = parcelCode) }
        .distinctUntilChanged()
        .replay(1)
        .connectInViewModelScope()

    fun refresh() = refreshSubject.onNext(Unit)

    private fun generateBarcode(): Bitmap? {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(
            parcelCode,
            BarcodeFormat.CODE_128,
            deviceInfo.deviceHeightPixels,
            deviceInfo.deviceWidthPixels,
            mapOf(
                EncodeHintType.MARGIN to 0
            )
        )
    }

    data class State(
        val trackingCode: String,
        val parcelDetail: ParcelDetailDTO? = null,
        val barcode: Bitmap? = null,
        val isLoading: Boolean = false,
        val error: Throwable? = null
    )
}
