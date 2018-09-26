package net.kelmer.correostracker.ui.detail

import android.arch.lifecycle.MutableLiveData
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import net.kelmer.correostracker.base.RxViewModel
import net.kelmer.correostracker.data.Result
import net.kelmer.correostracker.data.model.dto.ParcelDetailDTO
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.model.remote.CorreosApiParcel
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import net.kelmer.correostracker.ext.toResult
import net.kelmer.correostracker.ext.withNetwork
import javax.inject.Inject


class ParcelDetailViewModel : RxViewModel() {

    @Inject
    lateinit var localParcelRepository: LocalParcelRepository

    @Inject
    lateinit var correosRepository: CorreosRepository

    val parcel: MutableLiveData<Result<ParcelDetailDTO>> = MutableLiveData()


    fun getParcel(parcelCode: String) {


        correosRepository.getParcelStatus(parcelCode)
                .zipWith(
                        localParcelRepository.getParcel(parcelCode)
                        ,
                        BiFunction<CorreosApiParcel,LocalParcelReference,ParcelDetailDTO>{
                            correosParcel, localParcel ->
                            ParcelDetailDTO(
                                    localParcel.parcelName,
                                    localParcel.code,
                                    localParcel.ancho?: "",
                                    localParcel.alto?: "",
                                    localParcel.largo?: "",
                                    localParcel.peso?: "",
                                    localParcel.refCliente?:"",
                                    localParcel.codProducto?:"",
                                    localParcel.fechaCalculada?:"",
                                    correosParcel.eventos)
                        }

                )
                .toResult(schedulerProvider)
                .subscribeBy(onNext = {
                    parcel.value = it
                })
                .addTo(disposables)
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe({
//                    parcel.value = it
//                }, {
//                    error.value = true
//                }
//                )
//                .addTo(disposables)
    }
}