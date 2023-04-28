package net.kelmer.correostracker.detail.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.crashlytics.FirebaseCrashlytics
import net.kelmer.correostracker.dataApi.model.exception.CorreosException
import net.kelmer.correostracker.detail.DetailViewModel
import net.kelmer.correostracker.detail.compose.preview.PreviewData
import net.kelmer.correostracker.details.R
import net.kelmer.correostracker.ui.compose.ErrorView
import net.kelmer.correostracker.ui.theme.CorreosTheme
import net.kelmer.correostracker.util.NetworkInteractor

@Composable
fun ErrorMap(state: DetailViewModel.State) {
    when (state.error) {
        is CorreosException -> {
            if(!LocalInspectionMode.current) {
                FirebaseCrashlytics.getInstance().log("Controlled Exception Error ${state.trackingCode}")
                FirebaseCrashlytics.getInstance().recordException(state.error)
            }
            ErrorView(
                message = state.error.message ?: "",
            )
        }

        is NetworkInteractor.NetworkUnavailableException -> {
            if(!LocalInspectionMode.current) {
                FirebaseCrashlytics.getInstance()
                    .log("Controlled Network Unavailable Exception Error ${state.trackingCode}")
                FirebaseCrashlytics.getInstance().recordException(state.error)
            }
            ErrorView(
                stringResource(
                    id = R.string.error_no_network
                )
            )
        }

        else -> {
            if(!LocalInspectionMode.current) {
                FirebaseCrashlytics.getInstance().log("Unknown Error ${state.trackingCode}")
                if (state.error != null) {
                    FirebaseCrashlytics.getInstance().recordException(state.error)
                }
            }
            ErrorView(
                stringResource(
                    id = R.string.error_unrecognized
                )
            )
        }
    }
}

@Composable
@Preview
fun ErrorMapPreview(){
    CorreosTheme() {
        ErrorMap(state = DetailViewModel.State(
            PreviewData.detail.code,
            PreviewData.detail,
            null,
            false,
            null
        ))
    }
}
