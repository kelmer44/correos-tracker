package net.kelmer.correostracker.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import net.kelmer.correostracker.R
import net.kelmer.correostracker.ui.activity.MainActivity
import net.kelmer.correostracker.ui.list.ParcelListFragment
import net.kelmer.correostracker.util.launchFragmentInHiltContainer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Gabriel Sanmartín on 16/12/2020.
 */
@RunWith(AndroidJUnit4::class)
class CreateParcelButtonTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
    }


    @Test
    fun launchCreationAfterPressingCreateButton(){
        // Create a TestNavHostController
        val navController = TestNavHostController(
                ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.nav_graph)


        // Create a graphical FragmentScenario for the TitleScreen
        // Set the NavController property on the fragment
        launchFragmentInHiltContainer<ParcelListFragment>() {
            Navigation.setViewNavController(this.requireView(), navController)
        }

        // Verify that performing a click changes the NavController’s state
        onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.createParcelFragment)

    }

    @After
    fun tearDown(){
    }
}