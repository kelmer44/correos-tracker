package net.kelmer.correostracker.iap

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.SkuDetailsParams
import com.gen.rxbilling.client.RxBillingImpl
import com.gen.rxbilling.connection.BillingClientFactory
import com.gen.rxbilling.lifecycle.BillingConnectionManager
import dagger.hilt.android.scopes.ActivityScoped
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class BillingClientProvider @Inject constructor(
    private val activity: FragmentActivity
) : DefaultLifecycleObserver {

    private val rxBilling by lazy { RxBillingImpl(BillingClientFactory(activity)).also {
        Timber.i("Initialized RxBilling")
    }}


    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.lifecycle.addObserver(BillingConnectionManager(rxBilling))
    }

    fun getBillingUpdates() = rxBilling.observeUpdates()

    fun launchFlow(params: BillingFlowParams) = rxBilling.launchFlow(activity, params)

    fun loadPurchases(skuType: String) = rxBilling.getPurchases(skuType)

    fun loadSkuDetails(skuDetails: SkuDetailsParams) = rxBilling.getSkuDetails(skuDetails)

    fun acknowledge(acknowledgePurchaseParams: AcknowledgePurchaseParams) =
        rxBilling.acknowledge(acknowledgePurchaseParams)
}
