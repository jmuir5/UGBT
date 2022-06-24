package com.noxapps.ugbt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where

class listFreqActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_freq)



    }

    override fun onResume() {
        super.onResume()
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@listFreqActivity.realm = realm
                val attacks = realm.where<AttackItem2>().findAll()!!
                var symptomList = mutableListOf<String>()
                var symptomCount = mutableListOf<Int>()
                var attackCounter = 0
                for (i in attacks.indices) {
                    attackCounter++
                    for (j in attacks[i]!!.symptomList.indices) {
                        if (symptomList.contains(attacks[i]!!.symptomList[j])) {
                            for (k in symptomList.indices) {
                                if (attacks[i]!!.symptomList[j] == symptomList[k]) {
                                    symptomCount[k]++
                                }
                            }
                        } else {
                            symptomCount.add(1)
                            attacks[i]!!.symptomList[j]?.let { symptomList.add(it) }
                        }
                    }
                }

                findViewById<TextView>(R.id.countLabel).text = attackCounter.toString()

                for (i in symptomList.indices) {
                    buildElement(symptomList[i], symptomCount[i], attackCounter)
                }
            }
        })
    }

    private fun buildElement(
        symptom:String,
        count:Int,
        total:Int
    ) {


        val symptomFreqSV = findViewById<LinearLayout>(R.id.LFScrollView)
        val symptomFreqContainer = LinearLayout(this)
        val symptomContainer = LinearLayout(this)
        val countContainer = LinearLayout(this)
        val FreqContainer = LinearLayout(this)
        val symptomTV = TextView(this)
        val countTV = TextView(this)
        val freqTV = TextView(this)

        symptomTV.text = symptom
        countTV.text = count.toString()
        var countFloat = count.toFloat()
        countFloat*=100
        var formatted = String.format("%.2f", countFloat/total)+"%"
        freqTV.text = formatted

        symptomTV.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        countTV.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        val hlp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        hlp.weight = 50f
        symptomContainer.layoutParams = hlp

        val lp2 = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp2.weight = 25f
        val lp3 = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp3.weight = 25f
        lp3.gravity =Gravity.RIGHT
        countContainer.layoutParams = lp2
        FreqContainer.layoutParams = lp3

        symptomFreqContainer.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        symptomFreqContainer.orientation = LinearLayout.HORIZONTAL

        symptomContainer.addView(symptomTV)
        countContainer.addView(countTV)
        FreqContainer.addView(freqTV)
        symptomFreqContainer.addView(symptomContainer)
        symptomFreqContainer.addView(countContainer)
        symptomFreqContainer.addView(FreqContainer)
        symptomFreqSV.addView(symptomFreqContainer)
    }
}