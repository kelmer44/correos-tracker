package net.kelmer.correostracker.iap.billingrx

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import net.kelmer.correostracker.iap.billingrx.connection.BillingClientFactory
import timber.log.Timber

class RxBillingImpl(
    billingFactory: BillingClientFactory
) : RxBilling {
    private val updateSubject = PublishSubject.create<PurchasesUpdate>()

    private val updatedListener = PurchasesUpdatedListener { result, purchases ->
        val event = when (val responseCode = result.responseCode) {
            BillingClient.BillingResponseCode.OK -> PurchasesUpdate.Success(responseCode, purchases.orEmpty())
            BillingClient.BillingResponseCode.USER_CANCELED ->
                PurchasesUpdate.Canceled(responseCode, purchases.orEmpty())
            else -> PurchasesUpdate.Failed(responseCode, purchases.orEmpty())
        }
        updateSubject.onNext(event)
    }

    private val connectionFlowable =
        Completable.complete()
            .observeOn(AndroidSchedulers.mainThread()) // just to be sure billing client is called from main thread
            .andThen(
                billingFactory.createBillingFlowable(updatedListener)
                    .doOnError { Timber.e(it, "Failed to create billing connection flowable!") }
            )

    override fun connect(): Flowable<BillingClient> {
        return connectionFlowable
    }

    override fun isFeatureSupported(@BillingClient.FeatureType feature: String): Single<Boolean> {
        return connectionFlowable.flatMapSingle {
            Single.defer {
                val result = it.isFeatureSupported(feature)
                Single.just(result.responseCode == BillingClient.BillingResponseCode.OK)
            }
        }.firstOrError()
    }

    override fun observeUpdates(): Flowable<PurchasesUpdate> {
        return connectionFlowable.flatMap {
            updateSubject.toFlowable(BackpressureStrategy.LATEST)
        }
    }

    override fun getPurchases(@BillingClient.ProductType skuType: String): Single<List<Purchase>> {
        return getBoughtItems(skuType)
    }

    override fun getPurchaseHistory(@BillingClient.ProductType skuType: String): Single<List<PurchaseHistoryRecord>> {
        return getHistory(skuType)
    }

    override fun getSkuDetails(params: SkuDetailsParams): Single<List<SkuDetails>> {
        return connectionFlowable
            .flatMapSingle { client ->
                Single.create<List<SkuDetails>> {
                    client.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
                        if (it.isDisposed) return@querySkuDetailsAsync
                        val responseCode = billingResult.responseCode
                        if (isSuccess(responseCode)) {
                            it.onSuccess(skuDetailsList.orEmpty())
                        } else {
                            it.onError(BillingException.fromResult(billingResult))
                        }
                    }
                }
            }.firstOrError()
    }

    override fun getProductDetails(params: QueryProductDetailsParams): Single<List<ProductDetails>> {
        return connectionFlowable
            .flatMapSingle { client ->
                Single.create<List<ProductDetails>> {
                    client.queryProductDetailsAsync(params) { billingResult, skuDetailsList ->
                        if (it.isDisposed) return@queryProductDetailsAsync
                        val responseCode = billingResult.responseCode
                        if (isSuccess(responseCode)) {
                            it.onSuccess(skuDetailsList)
                        } else {
                            it.onError(BillingException.fromResult(billingResult))
                        }
                    }
                }
            }.firstOrError()
    }

    override fun launchFlow(activity: Activity, params: BillingFlowParams): Completable {
        return connectionFlowable
            .flatMap {
                val responseCode = it.launchBillingFlow(activity, params)
                return@flatMap Flowable.just(responseCode)
            }
            .firstOrError()
            .flatMapCompletable {
                return@flatMapCompletable if (isSuccess(it.responseCode)) {
                    Completable.complete()
                } else {
                    Completable.error(BillingException.fromResult(it))
                }
            }
    }

    override fun consumeProduct(params: ConsumeParams): Completable {
        return connectionFlowable
            .flatMapSingle { client ->
                Single.create<Int> {
                    client.consumeAsync(params) { result, _ ->
                        if (it.isDisposed) return@consumeAsync
                        val responseCode = result.responseCode
                        if (isSuccess(responseCode)) {
                            it.onSuccess(responseCode)
                        } else {
                            it.onError(BillingException.fromResult(result))
                        }
                    }
                }
            }
            .firstOrError()
            .ignoreElement()
    }

    override fun acknowledge(params: AcknowledgePurchaseParams): Completable {
        return connectionFlowable
            .flatMapSingle { client ->
                Single.create<Int> {
                    client.acknowledgePurchase(params) { result ->
                        if (it.isDisposed) return@acknowledgePurchase
                        val responseCode = result.responseCode
                        if (isSuccess(responseCode)) {
                            it.onSuccess(responseCode)
                        } else {
                            it.onError(BillingException.fromResult(result))
                        }
                    }
                }
            }
            .firstOrError()
            .ignoreElement()
    }

    private fun getBoughtItems(@BillingClient.ProductType type: String): Single<List<Purchase>> {
        return connectionFlowable
            .flatMapSingle {
                Single.create<List<Purchase>> { emitter ->
                    val params = QueryPurchasesParams.newBuilder()
                        .setProductType(type)
                        .build()
                    it.queryPurchasesAsync(params) { billingResult, mutableList ->
                        if (emitter.isDisposed) return@queryPurchasesAsync
                        if (isSuccess(billingResult.responseCode)) {
                            emitter.onSuccess(mutableList)
                        } else {
                            emitter.onError(BillingException.fromResult(billingResult))
                        }
                    }
                }
            }.firstOrError()
    }

    private fun getHistory(@BillingClient.ProductType type: String): Single<List<PurchaseHistoryRecord>> {
        return connectionFlowable
            .flatMapSingle { client ->
                Single.create<List<PurchaseHistoryRecord>> {
                    val params = QueryPurchaseHistoryParams.newBuilder()
                        .setProductType(type)
                        .build()
                    client.queryPurchaseHistoryAsync(params) { billingResult: BillingResult, list: MutableList<PurchaseHistoryRecord>? ->
                        if (it.isDisposed) return@queryPurchaseHistoryAsync
                        val responseCode = billingResult.responseCode
                        if (isSuccess(responseCode)) {
                            it.onSuccess(list.orEmpty())
                        } else {
                            it.onError(BillingException.fromResult(billingResult))
                        }
                    }
                }
            }.firstOrError()
    }

    private fun isSuccess(responseCode: Int): Boolean {
        return responseCode == BillingClient.BillingResponseCode.OK
    }
}