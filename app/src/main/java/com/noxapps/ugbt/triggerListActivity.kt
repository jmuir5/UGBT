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

class triggerListActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trigger_list)
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@triggerListActivity.realm = realm
                val attacks = realm.where<AttackItem2>().findAll()!!
                var triggerList = mutableListOf<String>()
                var triggerCount = mutableListOf<Int>()
                var attackCounter = 0
                for (i in attacks.indices) {
                    attackCounter++

                    if (triggerList.contains(attacks[i]!!.trigger)) {
                        for (k in triggerList.indices) {
                            if (attacks[i]!!.trigger == triggerList[k]) {
                                triggerCount[k]++
                            }
                        }
                    } else {
                        triggerCount.add(1)
                        attacks[i]!!.trigger.let { triggerList.add(it) }
                    }

                }

                findViewById<TextView>(R.id.countLabel).text = attackCounter.toString()

                for (i in triggerList.indices) {
                    buildElement(triggerList[i], triggerCount[i], attackCounter)
                }
            }
        })
    }
    private fun buildElement(
        symptom:String,
        count:Int,
        total:Int
    ) {


        val symptomFreqSV = findViewById<LinearLayout>(R.id.TriggerScrollView)
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
        lp2.weight = 35f
        val lp3 = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp3.weight = 15f
        lp3.gravity = Gravity.RIGHT
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