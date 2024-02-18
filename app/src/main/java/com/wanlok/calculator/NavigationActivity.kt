package com.wanlok.calculator

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


class NavigationActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var bottomNavigationView: BottomNavigationView
//    private var previousView: View? = null
    private lateinit var map: MutableMap<Int, ArrayList<NavigationFragment>>

    private var itemId: Int? = null

    private fun updateTopNavigation(backButtonEnabled: Boolean) {
        itemId?.let { itemId ->
            map[itemId]?.let { fragments ->
                if (fragments.size > 0) {
                    supportActionBar?.setDisplayHomeAsUpEnabled(backButtonEnabled)
                    title = fragments[fragments.size - 1].getTitle()
                }
            }
        }
    }

    private fun updateBottomNavigation() {
        val menu = bottomNavigationView.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            if (menuItem.itemId == itemId) {
                onNavigationItemSelected(menuItem)
                menuItem.setChecked(true)
            }
        }
    }

    override fun onBackPressed() {
        itemId?.let { itemId ->
            map[itemId]?.let { fragments ->
                if (fragments.size > 1) {
                    fragments.remove(fragments[fragments.size - 1])
//                        previousView = null
                }
                updateTopNavigation(fragments.size > 1)
            }
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }

    private fun replace(fragment: NavigationFragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
        updateTopNavigation(false)
    }

    private fun add(fragment: NavigationFragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.add(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
        updateTopNavigation(true)
    }

    fun open(fragment: NavigationFragment) {
//        if (view !== previousView) {
            itemId?.let { itemId ->
                map[itemId]?.add(fragment)
                add(fragment)
            }
//        }
//        previousView = view
    }

    private fun clearStack() {
        itemId?.let { itemId ->
            map[itemId]?.let { fragments ->
                for (i in fragments.indices) {
                    supportFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun buildStack() {
        itemId?.let { itemId ->
            map[itemId]?.let { fragments ->
                for (i in fragments.indices) {
                    if (i == 0) {
                        replace(fragments[i])
                    } else {
                        add(fragments[i])
                    }
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (supportFragmentManager.fragments.size == 0 || itemId != this.itemId) {
            clearStack()
            this.itemId = itemId
            buildStack()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = Bundle()
        bundle.putString("name", "Robert Wan")

        val numberCalculatorFragment = NumberCalculatorFragment()
        numberCalculatorFragment.arguments = bundle

        val numberCalculatorFragments = ArrayList<NavigationFragment>()
        numberCalculatorFragments.add(numberCalculatorFragment)

        val bFragments = ArrayList<NavigationFragment>()
        bFragments.add(B1Fragment())

        map = HashMap()
        map[R.id.numberCalculator] = numberCalculatorFragments
        map[R.id.dateCalculator] = bFragments

        itemId = R.id.numberCalculator

        supportActionBar?.elevation = 0F

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener(this)
        bottomNavigationView.background = null
        updateBottomNavigation()
    }
}
