package net.kelmer.correostracker.service

import android.content.Intent
import android.os.IBinder
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.kelmer.correostracker.base.BaseService
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import net.kelmer.correostracker.data.repository.correos.CorreosRepository
import net.kelmer.correostracker.data.repository.local.LocalParcelRepository
import javax.inject.Inject

class ParcelCheckerService : BaseService("ParcelChecker") {

    var disposables: CompositeDisposable = CompositeDisposable()

    override fun onHandleIntent(p0: Intent?) {

//        var subscribe = localParcelRepository.getParcelsSingle()
//                .flatMap {
//                    it.forEach {
//                        correosRepository.getParcelStatus(it.code)
//
//                    }
//                    Single.just(it)
//                }
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe()
//        disposables.add(subscribe)
    }

    @Inject
    lateinit var correosRepository: CorreosRepository
    @Inject
    lateinit var localParcelRepository: LocalParcelRepository

    override fun onCreate() {
        getServiceComponent().inject(this)
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}