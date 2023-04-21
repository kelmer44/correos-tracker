package net.kelmer.correostracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import net.kelmer.correostracker.list.compose.ParcelsScreen
import net.kelmer.correostracker.ui.nav.CorreosAppState
import net.kelmer.correostracker.ui.nav.Screen
import net.kelmer.correostracker.ui.nav.rememberCorreosAppState

//@Composable
//fun CorreosApp(
//    windowSizeClass: WindowSizeClass,
//    appState: CorreosAppState = rememberCorreosAppState()
//) {
//    NavHost(
//        navController = appState.navController,
//        startDestination = Screen.List.route
//    ) {
//        composable(Screen.List.route) {navBackStackEntry ->
//            ParcelsScreen(state = )
//
//        }
//    }
//
//}
