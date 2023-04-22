package net.kelmer.correostracker.ui.nav

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


sealed class Screen(val route: String) {

    object List: Screen("list")

    object Detail : Screen("detail/{parcel_code}"){
        fun createRoute(code: String) = "detail/$code"
    }

    object Create: Screen("create")
}

@Composable
fun rememberCorreosAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) = remember(navController, context) {
    CorreosAppState(
        navController,
        context
    )
}

class CorreosAppState(
    val navController: NavHostController,
    private val context : Context
) {

    fun navigateToCreate(from: NavBackStackEntry) {
        if(from.lifecycleIsResumed()){
            navController.navigate(Screen.Create.route)
        }
    }

    fun navigateToDetails(code: String, from: NavBackStackEntry) {
        if(from.lifecycleIsResumed()){
            navController.navigate(Screen.Detail.createRoute(code))
        }
    }

    fun navigateBack(){
        navController.popBackStack()
    }
}
/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.getLifecycle().currentState == Lifecycle.State.RESUMED
