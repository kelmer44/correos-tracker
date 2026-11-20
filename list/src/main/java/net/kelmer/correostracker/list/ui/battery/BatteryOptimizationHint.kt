package net.kelmer.correostracker.list.ui.battery

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.kelmer.correostracker.list.R

private const val ACTION_APP_BATTERY_USAGE = "android.settings.VIEW_ADVANCED_POWER_USAGE_DETAIL"

/** True if battery optimization lets Android defer this app's background work. */
fun Context.isSubjectToBatteryOptimization(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false
    val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager ?: return false
    return !powerManager.isIgnoringBatteryOptimizations(packageName)
}

/**
 * Opens the screen to set battery usage to Unrestricted. Pixel: the per-app screen (hidden
 * action, not in the SDK). Others, e.g. Samsung, whose per-app screen is only a toggle: the public
 * system list. App settings is the fallback.
 *
 * ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS is avoided: Play restricts its permission.
 */
fun Context.openBatteryOptimizationSettings() {
    val packageUri = Uri.parse("package:$packageName")
    val perApp = Intent(ACTION_APP_BATTERY_USAGE, packageUri)
    val list = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
    val appDetails = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri)
    val candidates = if (Build.MANUFACTURER.equals("google", ignoreCase = true)) {
        listOf(perApp, list, appDetails)
    } else {
        listOf(list, appDetails)
    }
    for (intent in candidates) {
        try {
            startActivity(intent)
            return
        } catch (e: ActivityNotFoundException) {
            // try the next one
        }
    }
}

@Composable
fun BatteryOptimizationHint(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.battery_hint_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.battery_hint_desc),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.battery_hint_dismiss))
                }
                Button(onClick = onOpenSettings) {
                    Text(stringResource(R.string.battery_hint_action))
                }
            }
        }
    }
}

@Preview
@Composable
private fun BatteryOptimizationHintPreview() {
    BatteryOptimizationHint(onOpenSettings = {}, onDismiss = {})
}
