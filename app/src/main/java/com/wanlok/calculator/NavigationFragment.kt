package com.wanlok.calculator

import androidx.fragment.app.Fragment

abstract class NavigationFragment: Fragment() {
    fun open(fragment: NavigationFragment) {
        val baseActivity: NavigationActivity = activity as NavigationActivity
        baseActivity.open(fragment)
    }

    abstract fun getTitle(): String
}