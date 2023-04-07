package net.kelmer.correostracker.detail.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

import net.kelmer.correostracker.detail.ParcelDetailViewModel
import net.kelmer.correostracker.ui.compose.NoSearchAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    state: ParcelDetailViewModel.State
) {
    Scaffold(
        topBar =
        {

            DetailAppBar(state)
        },
        content = {it
        }
    )
}

@Composable
fun DetailAppBar(state: ParcelDetailViewModel.State) {

    NoSearchAppBar(
        title = state.parcelDetail?.name ?: "No title",
        listOf()
    )

}
