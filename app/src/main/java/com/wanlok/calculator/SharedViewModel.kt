package com.wanlok.calculator

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.wanlok.calculator.model.Conversion
import com.wanlok.calculator.model.ConversionLine

class SharedViewModel : ViewModel() {
    val leftFirstConversionLine = ConversionLine("", "", 0, "Numbers", "x", "x", true)
    val rightFirstConversionLine = ConversionLine("", "", 0, "Subtotal", "x", "x", true)

    private var conversions: ArrayList<Conversion> = ArrayList()
    private var conversionLines: ArrayList<ConversionLine> = ArrayList()

    val conversionLiveData = MutableLiveData(conversions)
    val conversionLineLiveData = MutableLiveData(conversionLines)

    private val db: FirebaseFirestore = Firebase.firestore

    fun update() {
        conversionLineLiveData.postValue(conversionLines)
    }

    fun downloadConversion() {
        val conversionList = ArrayList<Conversion>()
        val conversionLines = ArrayList<ConversionLine>()
        db.collection("conversion").get()
            .addOnSuccessListener { result ->
                var sum: Long = 0
                for (document in result) {
                    val id = document.id
                    val text = document.data["text"] as String
                    val order = document.data["order"] as Long
                    val count = document.data["count"] as Long
                    conversionList.add(Conversion(id, text, order, count))
                    sum += count
                }
                for (conversion in conversionList) {
                    db.collection("conversion").document(conversion.id).collection("line").get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val id = document.id
                                val text = document.data["text"] as String
                                val encode = document.data["encode"] as String
                                val decode = document.data["decode"] as String
                                val selected = document.data["selected"] as Boolean
                                conversionLines.add(ConversionLine(id, conversion.id, conversion.order, text, encode, decode, selected))
                            }
                            if (conversionLines.size.toLong() == sum) {
                                this.conversions = conversionList
                                this.conversionLines = conversionLines
                                conversionLiveData.postValue(conversionList)
                                conversionLineLiveData.postValue(conversionLines)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.w("ROBERT", "Error getting documents.", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ROBERT", "Error getting documents.", exception)
            }
    }
}