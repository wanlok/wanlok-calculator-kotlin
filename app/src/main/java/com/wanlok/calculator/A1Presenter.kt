package com.wanlok.calculator

import android.os.Bundle


class A1Presenter(bundle: Bundle) {
    val name: String?

    init {
        name = bundle.getString("name")
    }
}

