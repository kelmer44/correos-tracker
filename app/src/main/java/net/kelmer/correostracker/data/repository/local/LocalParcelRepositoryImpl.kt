package net.kelmer.correostracker.data.repository.local

import io.reactivex.Completable
import io.reactivex.Single
import net.kelmer.correostracker.data.model.local.LocalParcelDao
import net.kelmer.correostracker.data.model.local.LocalParcelReference
import timber.log.Timber

class LocalParcelRepositoryImpl(private val localParcelDao: LocalParcelDao) : LocalParcelRepository {
    override fun setNotify(code: String, enable: Boolean): Completable {
        return if (enable) {
            localParcelDao.enableNotifications(code)
        } else {
            localParcelDao.disableNotifications(code)
        }
    }

    override fun getNotifiableParcels(): Single<List<LocalParcelReference>> {
        return localParcelDao.getNotifiableParcels()
    }

    override fun getParcels() =
        localParcelDao.getParcels()

    override fun getParcel(code: String) = localParcelDao.getParcel(code)

    override fun saveParcel(parcel: LocalParcelReference): Completable {

        return Completable.fromAction { localParcelDao.saveParcel(parcel) }
    }

    override fun deleteParcel(parcel: LocalParcelReference): Completable {
        Timber.e("Requested deletion of $parcel")
        return localParcelDao.deleteParcel(parcel)
    }

    companion object {
        private var instance: LocalParcelRepositoryImpl? = null

        fun getInstance(localParcelDao: LocalParcelDao): LocalParcelRepositoryImpl {
            if (instance == null) {
                instance = LocalParcelRepositoryImpl(localParcelDao)
            }
            return instance!!
        }
    }
}
