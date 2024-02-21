package com.wanlok.calculator

import android.view.View
import androidx.fragment.app.Fragment

abstract class NavigationFragment: Fragment() {
    fun open(fragment: NavigationFragment) {
        val baseActivity: NavigationActivity = activity as NavigationActivity
        baseActivity.open(fragment)
    }

    fun preventClickable(view: View, id: Int) {
        view.findViewById<View?>(id).setOnClickListener {}
    }

    abstract fun getTitle(): String
}