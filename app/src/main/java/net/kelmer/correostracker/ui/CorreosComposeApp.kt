package net.kelmer.correostracker.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import net.kelmer.correostracker.create.compose.CreateScreen
import net.kelmer.correostracker.detail.compose.DetailScreen
import net.kelmer.correostracker.list.ui.ParcelsScreen
import net.kelmer.correostracker.ui.activity.MainActivityViewModel
import net.kelmer.correostracker.ui.nav.CorreosAppState
import net.kelmer.correostracker.ui.nav.Screen
import net.kelmer.correostracker.ui.nav.rememberCorreosAppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CorreosComposeApp(
    premium: MainActivityViewModel.PremiumState = MainActivityViewModel.PremiumState(
        isPremium = false,
        isBillingAvailable = true
    ),
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    viewModel: MainActivityViewModel = hiltViewModel(),
    windowSizeClass: WindowSizeClass,
    appState: CorreosAppState = rememberCorreosAppState(),
    onWebClicked: () -> Unit,
    onBuyClicked: () -> Unit
) {
    ScaffoldDefaults.contentWindowInsets

    NavHost(
        navController = appState.navController,
        startDestination = Screen.List.route,
    ) {
        composable(Screen.List.route) { navBackStackEntry ->
            ParcelsScreen(
                isPremium = premium.isPremium,
                isBillingAvailable = premium.isBillingAvailable,
                useDarkTheme = useDarkTheme,
                viewModel = hiltViewModel(),
                onAddParcel = {
                    appState.navigateToCreate(navBackStackEntry)
                },
                onParcelClicked = { code ->
                    if (code.isNotBlank()) {
                        appState.navigateToDetails(
                            viewModel.sanitizeCode(code = code),
                            navBackStackEntry
                        )
                    }
                },
                onWebClicked = onWebClicked,
                onBuyClicked = onBuyClicked
            )
        }
        composable(Screen.Detail.route) { backStackEntry ->
            DetailScreen(
                isPremium = premium.isPremium,
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
