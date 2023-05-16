package net.kelmer.correostracker.iap.billingrx.connection

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import net.kelmer.correostracker.iap.billingrx.BillingException
import timber.log.Timber

class BillingClientFactory(
    private val context: Context,
    private val transformer: FlowableTransformer<BillingClient, BillingClient>
    = RepeatConnectionTransformer()
) {

    fun createBillingFlowable(listener: PurchasesUpdatedListener): Flowable<BillingClient> {
        val flowable = Flowable.create<BillingClient>({ emitter->
            val billingClient = BillingClient.newBuilder(context)
                .enablePendingPurchases()
                .setListener(listener)
                .build()
            Timber.d("startConnection")
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingServiceDisconnected() {
                    Timber.d("onBillingServiceDisconnected")
                    if (!emitter.isCancelled) {
                        emitter.onComplete()
                    }
                }

                override fun onBillingSetupFinished(result: BillingResult) {
                    val responseCode = result.responseCode
                    Timber.d("onBillingSetupFinished response $responseCode isReady ${billingClient.isReady}")
                    if (!emitter.isCancelled) {
                        if (responseCode == BillingClient.BillingResponseCode.OK) {
                            emitter.onNext(billingClient)
                        } else {
                            emitter.onError(BillingException.fromResult(result))
                        }
                    } else {
                        if (billingClient.isReady) {
                            // Release resources if there are no observers
                            billingClient.endConnection()
                        }
                    }
                }
            })
            // Finish connection when no subscribers
            emitter.setCancellable {
                Timber.d("endConnection")
                if (billingClient.isReady) {
                    billingClient.endConnection()
                }
            }
        }, BackpressureStrategy.LATEST).doOnError {
            Timber.e(it, "Failed to create billing client flowable!")
        }

        return flowable.compose(transformer)
    }
}
