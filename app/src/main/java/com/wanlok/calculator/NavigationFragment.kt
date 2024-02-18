package com.wanlok.calculator

import android.view.View
import androidx.fragment.app.Fragment

abstract class NavigationFragment: Fragment() {
    fun open(fragment: NavigationFragment, view: View) {
        var baseActivity: NavigationActivity = activity as NavigationActivity
        baseActivity.open(fragment, view)
    }

    abstract fun getTitle(): String;
}