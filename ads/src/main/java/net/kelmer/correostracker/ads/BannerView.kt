package net.kelmer.correostracker.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerView(
    modifier: Modifier = Modifier,
    isTest: Boolean = BuildConfig.DEBUG
) {
    val unitId = if (isTest) stringResource(id = R.string.ad_mob_test_banner_id) else stringResource(
        id = R.string.ad_mob_banner_id
    )
    AndroidView(
        modifier = modifier,
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = unitId
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
