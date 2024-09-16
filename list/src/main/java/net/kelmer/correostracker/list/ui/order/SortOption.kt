package net.kelmer.correostracker.list.ui.order

import androidx.annotation.StringRes
import net.kelmer.correostracker.theme.R

enum class SortOption(@StringRes val stringRes: Int) {
    NAME(R.string.sort_name),
    DATE(R.string.sort_date);
}