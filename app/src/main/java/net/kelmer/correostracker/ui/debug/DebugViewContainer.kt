package net.kelmer.correostracker.ui.debug

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import net.kelmer.correostracker.R
import net.kelmer.correostracker.di.debug.ViewContainer
import net.kelmer.correostracker.ui.debug.views.DebugDrawerLayout

/**
 * Created by Gabriel Sanmartín on 11/09/2020.
 */
class DebugViewContainer : ViewContainer {
    override fun forActivity(activity: AppCompatActivity): ViewGroup {
        activity.setContentView(R.layout.debug_activity_frame)

        val viewHolder = ViewHolder()
        initViewHolder(activity, viewHolder)
        return viewHolder.content!!
    }


    private fun initViewHolder(activity: AppCompatActivity, viewHolder: ViewHolder) {
        viewHolder.debugDrawer = activity.findViewById(R.id.debug_drawer)
        viewHolder.drawerLayout = activity.findViewById(R.id.debug_drawer_layout)
        viewHolder.content = activity.findViewById(R.id.debug_content)
    }


    class ViewHolder {
        var drawerLayout: DebugDrawerLayout? = null
        var debugDrawer: ViewGroup? = null
        var content: FrameLayout? = null
    }
}