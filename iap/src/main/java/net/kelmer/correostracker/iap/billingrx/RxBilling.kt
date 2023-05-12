package net.kelmer.correostracker.iap.billingrx

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import net.kelmer.correostracker.iap.billingrx.lifecycle.Connectable

interface RxBilling : Connectable<BillingClient> {
    override fun connect(): Flowable<BillingClient>

    fun isFeatureSupported(@BillingClient.FeatureType feature: String): Single<Boolean>

    fun observeUpdates(): Flowable<PurchasesUpdate>

    fun getPurchases(@BillingClient.ProductType skuType: String): Single<List<Purchase>>

    fun getPurchaseHistory(@BillingClient.ProductType skuType: String): Single<List<PurchaseHistoryRecord>>

    fun getSkuDetails(params: SkuDetailsParams): Single<List<SkuDetails>>

    /**
     * do not mix subs and inapp types in the same params object
     */
    fun getProductDetails(params: QueryProductDetailsParams): Single<List<ProductDetails>>

    fun launchFlow(activity: Activity, params: BillingFlowParams): Completable

    fun consumeProduct(params: ConsumeParams): Completable

    fun acknowledge(params: AcknowledgePurchaseParams): Completable
}
