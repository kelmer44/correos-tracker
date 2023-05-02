package net.kelmer.correostracker.ui.activity

import io.reactivex.Flowable
import net.kelmer.correostracker.list.ParcelListPreferences
import net.kelmer.correostracker.ui.theme.ThemeMode
import org.junit.Assert.*

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MainActivityViewModelTest {

    private val prefs: ParcelListPreferences<ThemeMode> = mock() {
        doReturn(Flowable.just(ThemeMode.SYSTEM)).whenever(it).themeModeStream
    }
    private val viewModel = MainActivityViewModel(
        prefs
    )

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun sanitizeCode() {
        Assert.assertEquals("123",viewModel.sanitizeCode("123"))
        Assert.assertEquals("123",viewModel.sanitizeCode(" 123 "))
        Assert.assertEquals("123",viewModel.sanitizeCode("\n123\n"))
        Assert.assertEquals("123",viewModel.sanitizeCode("1/2/3"))
    }
}
