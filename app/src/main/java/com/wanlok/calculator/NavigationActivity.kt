package com.wanlok.calculator

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.wanlok.calculator.customView.MenuItemClickListener


class NavigationActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private var bottomNavigationView: BottomNavigationView? = null
//    private var previousView: View? = null
    private var map: MutableMap<Int, ArrayList<NavigationFragment>>? = null
    private var itemId: Int? = null

    private fun updateTopNavigation(backButtonEnabled: Boolean) {
        map?.let { map ->
            itemId?.let { itemId ->
                map[itemId]?.let { fragments ->
                    if (fragments.size > 0) {
                        supportActionBar?.setDisplayHomeAsUpEnabled(backButtonEnabled)
                        title = fragments[fragments.size - 1].getTitle()
                    }
                }
            }
        }
    }

    private fun updateBottomNavigation() {
        val menu = bottomNavigationView?.menu
        if (menu != null) {
            for (i in 0 until menu.size()) {
                val menuItem = menu.getItem(i)
                if (menuItem.itemId == itemId) {
                    onNavigationItemSelected(menuItem)
                    menuItem.setChecked(true)
                }
            }
        }
    }

    override fun onBackPressed() {
        map?.let { map ->
            itemId?.let { itemId ->
                map[itemId]?.let { fragments ->
                    if (fragments.size > 1) {
                        fragments.remove(fragments[fragments.size - 1])
//                        previousView = null
                    }
                    updateTopNavigation(fragments.size > 1)
                }
            }
        }
        super.onBackPressed()
    }

    fun dummy(menuItem: MenuItem) {
        map?.let { map ->
            itemId?.let { itemId ->
                map[itemId]?.let { fragments ->
                    if (fragments.isNotEmpty()) {
                        fragments.last().let { fragment ->
                            (fragment as? MenuItemClickListener).let { menuItemClickListener ->
                                menuItemClickListener?.onClick(menuItem)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else if (item.itemId == R.id.item_filter) {
            dummy(item)
            return true
        } else if (item.itemId == R.id.action_settings) {
            Log.d("ROBERT", "action_settings")
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replace(fragment: NavigationFragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
        updateTopNavigation(false)
    }

    private fun add(fragment: NavigationFragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.add(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
        updateTopNavigation(true)
    }

    fun open(fragment: NavigationFragment, view: View) {
//        if (view !== previousView) {
            map?.let { map ->
                itemId?.let { itemId ->
                    map[itemId]?.add(fragment)
                    add(fragment)
                }
            }
//        }
//        previousView = view
    }

    private fun clearStack() {
        map?.let { map ->
            itemId?.let { itemId ->
                map[itemId]?.let { fragments ->
                    for (i in fragments.indices) {
                        supportFragmentManager.popBackStack()
                    }
                }
            }
        }
    }

    private fun buildStack() {
        map?.let { map ->
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
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragmentManager = supportFragmentManager
        val itemId = item.itemId
        if (fragmentManager.fragments.size == 0 || itemId != this.itemId) {
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
        map?.let { map ->
            map[R.id.numberCalculator] = numberCalculatorFragments
            map[R.id.dateCalculator] = bFragments
        }

        itemId = R.id.numberCalculator

        supportActionBar?.elevation = 0F

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView?.setOnItemSelectedListener(this)
        bottomNavigationView?.background = null
        updateBottomNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }
}
