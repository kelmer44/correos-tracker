package net.kelmer.correostracker.util.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View


var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

var View.isVisibleWithDelay: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        if (value)
            animate()
                    .setDuration(300)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            visibility = View.VISIBLE
                        }
                    })
        else animate()
                .alpha(0.0f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        alpha = 1.0f
                        visibility = View.GONE
                    }
                })
    }
