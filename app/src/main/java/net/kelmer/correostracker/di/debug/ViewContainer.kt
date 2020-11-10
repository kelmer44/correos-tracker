package net.kelmer.correostracker.di.debug

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Gabriel Sanmartín on 11/09/2020.
 */
interface ViewContainer {

    /** The root {@link android.view.ViewGroup} into which the activity should place its contents. */
    fun forActivity(activity: AppCompatActivity): ViewGroup

    companion object {

        /** An [ViewContainer] which returns the normal activity content view.  */
        val DEFAULT: ViewContainer = object : ViewContainer {
            override fun forActivity(activity: AppCompatActivity): ViewGroup {
                return activity.findViewById(android.R.id.content)
            }
        }
    }

}