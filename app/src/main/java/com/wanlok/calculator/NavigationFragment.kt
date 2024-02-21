package com.wanlok.calculator

import android.view.View
import androidx.fragment.app.Fragment

abstract class NavigationFragment: Fragment() {
    lateinit var parentView: View

    fun open(fragment: NavigationFragment) {
        val baseActivity: NavigationActivity = activity as NavigationActivity
        baseActivity.open(fragment)
    }

    fun preventParentViewClickable() {
        parentView.setOnClickListener {}
    }

    abstract fun getTitle(): String
}