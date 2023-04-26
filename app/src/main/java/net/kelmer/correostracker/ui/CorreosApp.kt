package net.kelmer.correostracker.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import net.kelmer.correostracker.create.compose.CreateScreen
import net.kelmer.correostracker.detail.compose.DetailScreen
import net.kelmer.correostracker.list.ParcelListViewModel
import net.kelmer.correostracker.list.compose.ParcelsScreen
import net.kelmer.correostracker.ui.activity.MainActivityViewModel
import net.kelmer.correostracker.ui.nav.CorreosAppState
import net.kelmer.correostracker.ui.nav.Screen
import net.kelmer.correostracker.ui.nav.rememberCorreosAppState

@Composable
fun CorreosApp(
    useDarkTheme : Boolean = isSystemInDarkTheme(),
    viewModel: MainActivityViewModel = hiltViewModel(),
    windowSizeClass: WindowSizeClass,
    appState: CorreosAppState = rememberCorreosAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = Screen.List.route
    ) {
        composable(Screen.List.route) { navBackStackEntry ->
            ParcelsScreen(
                useDarkTheme = useDarkTheme,
                viewModel = hiltViewModel(),
                onAddParcel = {
                    appState.navigateToCreate(navBackStackEntry)
                },
                onParcelClicked = { code ->
                    if(code.isNotBlank()) {
                        appState.navigateToDetails(viewModel.sanitizeCode(code = code), navBackStackEntry)
                    }
                },
                onWebClicked = {},
                onLongPressParcel = {}
            )
        }
        composable(Screen.Detail.route) { backStackEntry ->
            DetailScreen(
                useDarkTheme = useDarkTheme,
                viewModel = hiltViewModel(),
                backAction = appState::navigateBack
            )
        }
        composable(Screen.Create.route) { backStackEntry ->
            CreateScreen(
                useDarkTheme = useDarkTheme,
                viewModel = hiltViewModel(),
                backAction = appState::navigateBack
            )
        }
    }
}
