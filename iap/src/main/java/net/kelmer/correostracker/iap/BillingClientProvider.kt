package net.kelmer.correostracker.iap

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.Purchase.PurchaseState
import com.android.billingclient.api.QueryProductDetailsParams
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import net.kelmer.correostracker.iap.billingrx.PurchasesUpdate
import net.kelmer.correostracker.iap.billingrx.RxBillingImpl
import net.kelmer.correostracker.iap.billingrx.connection.BillingClientFactory
import net.kelmer.correostracker.iap.billingrx.lifecycle.BillingConnectionManager
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingClientProvider @Inject constructor(
    @ApplicationContext context: Context
) : DefaultLifecycleObserver, IapApi {

    private val rxBilling by lazy {
        RxBillingImpl(BillingClientFactory(context)).also {
            Timber.i("Initialized RxBilling")
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.lifecycle.addObserver(BillingConnectionManager(rxBilling))
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        getBillingUpdates()
            .flatMapIterable { it.purchases }
            .filter { !it.isAcknowledged }
            .flatMapCompletable {
                Timber.i("Acknowledging purchase: $it")
                rxBilling.acknowledge(
                    AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(it.purchaseToken)
                        .build()
                )
                Completable.complete()
            }
            .autoDisposable(owner.scope(Lifecycle.Event.ON_STOP))
            .subscribe({}, Timber::e)
    }

    private fun getBillingUpdates(): Flowable<PurchasesUpdate> = rxBilling.observeUpdates()

    override fun launchFlow(activity: FragmentActivity): Completable =
        loadProductDetails().map { checkNotNull(it.firstOrNull()) }
            .flatMapCompletable {
                rxBilling.launchFlow(
                    activity,
                    BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(
                            listOf(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(it)
//                                .setOfferToken()
                                    .build()
                            )
                        )
                        .build()
                )
            }

    private fun productDetailsParams() = QueryProductDetailsParams.newBuilder()
        .setProductList(
            listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(PRODUCT_SKU)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            )
        )
        .build()

    private fun loadProductDetails(): Single<List<ProductDetails>> {
        return rxBilling.getProductDetails(productDetailsParams())
    }

    override fun isPremium(): Flowable<Boolean> =
        loadPurchases(ProductType.INAPP)
            .doOnSuccess { Timber.i("New state for loadPurchases: $it") }
            .toFlowable()
            .map { purchases -> isPremiumPurchase(purchases) }
            .mergeWith(
                //Future updates we only care about new values
                getBillingUpdates()
                    .map { it.purchases }
                    .map { purchase ->
                        isPremiumPurchase(purchase)
                    }
                    .filter { it }
            )
            .doOnNext { Timber.w("New state for premium BEFORE = $it") }
            .takeUntil { it }
            .doOnNext { Timber.w("New state for premium AFTER = $it") }
            .distinctUntilChanged()

    override fun getProductDetails(): Single<net.kelmer.correostracker.iap.ProductDetails> = loadProductDetails()
        .map {
            val pd: ProductDetails? = it.firstOrNull()
            val formattedPrice = checkNotNull(pd?.oneTimePurchaseOfferDetails?.formattedPrice)
            ProductDetails(
                pd!!.name,
                pd.description,
                formattedPrice
            )
        }

    private fun isPremiumPurchase(purchases: List<Purchase>) =
        purchases
            .any { purchase ->
//                purchase.isAcknowledged &&
                purchase.products.any { it == PRODUCT_SKU } &&
                purchase.purchaseState == PurchaseState.PURCHASED
            }

    private fun loadPurchases(@ProductType skuType: String): Single<List<Purchase>> = rxBilling.getPurchases(skuType)

    companion object {
        const val PRODUCT_SKU = "noads_premium"
    }
}
