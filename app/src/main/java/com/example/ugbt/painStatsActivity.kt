package com.example.ugbt

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import javax.xml.datatype.DatatypeConstants.DAYS


class painStatsActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    lateinit var symptomSpinner: Spinner
    lateinit var meanTV: TextView
    lateinit var medianTV: TextView
    lateinit var countTV: TextView
    lateinit var frequencyTV: TextView

    lateinit var chart:BarChart
    var barData: BarData? = null

    // variable for our bar data set.
    var barDataSet: BarDataSet? = null

    // array list for storing entries.
    lateinit var symptomCount: MutableList<Int>
    lateinit var symptomList: MutableList<String>

    var totalAttacks = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pain_stats)
        meanTV = findViewById(R.id.meanValue)
        medianTV = findViewById(R.id.medianValue)
        countTV = findViewById(R.id.countValue)
        frequencyTV = findViewById(R.id.frequencyValue)

        chart=findViewById(R.id.barchart)

        symptomSpinner = findViewById(R.id.symptomSpinner)
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@painStatsActivity.realm = realm
                val attacks = realm.where<AttackItem2>().findAll()!!
                symptomList = mutableListOf<String>()
                symptomCount = mutableListOf<Int>()
                var attackCounter = 0
                symptomList.add("Select Symptom")
                symptomCount.add(0)
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
                totalAttacks = attackCounter
                symptomList.add("overall")
                val adapter = ArrayAdapter(
                    this@painStatsActivity,
                    android.R.layout.simple_spinner_item, symptomList
                )
                symptomSpinner.adapter = adapter
                symptomSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (position == 0) {
                                meanTV.text = 0.toString()
                                medianTV.text = 0.toString()
                                countTV.text = 0.toString()
                                frequencyTV.text = "--%"
                                chart.clear()
                            } else if (position==symptomList.size-1) fillDataOA()
                            else fillData()
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            /* no-op */
                        }
                    }
            }
        })
    }


    fun fillData() {
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val attacks = realm.where<AttackItem2>().findAll()!!
                var intensityCountList = mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                var medianList = mutableListOf<Int>()
                var totalCount = 0
                var meanHolder: Float = 0f
                for (i in attacks.indices) {
                    for (j in attacks[i]!!.symptomList.indices) {
                        if (attacks[i]!!.symptomList[j] == symptomSpinner.selectedItem) {
                            intensityCountList[attacks[i]!!.intensityList[j]!!]++
                            totalCount++
                        }
                    }
                }
                for (i in intensityCountList.indices) {
                    meanHolder += intensityCountList[i]*i
                    if(intensityCountList[i]>0) {
                        for (j in 0 until intensityCountList[i]) {
                            medianList.add(i)
                        }
                    }
                }
                meanHolder /= totalCount
                meanTV.text = String.format("%.2f", meanHolder)

                if (totalCount%2==1)medianTV.text=medianList[totalCount/2].toString()
                else{
                    var median:Float = (medianList[totalCount/2].toFloat()+medianList[totalCount/2-1].toFloat())/2
                    medianTV.text=String.format("%.2f", median)
                }
                countTV.text = totalCount.toString()
                frequencyTV.text=String.format("%.2f", (totalCount.toFloat()/attacks.size)*100)+"%"

                var barEntriesArrayList:List<BarEntry> = listOf()
                //bardata
                for(i in intensityCountList.indices){
                    barEntriesArrayList+=BarEntry(i.toFloat(), intensityCountList[i].toFloat())
                }
                barDataSet = BarDataSet(barEntriesArrayList, "Intensity Distribution for ${symptomSpinner.selectedItem}")

                // creating a new bar data and
                // passing our bar data set.
                barData = BarData(barDataSet)

                // below line is to set data
                // to our bar chart.
                chart.data = barData

                // adding color to our bar data set.
                barDataSet!!.setColors(*ColorTemplate.MATERIAL_COLORS)

                // setting text color.

                // setting text color.
                barDataSet!!.valueTextColor = Color.BLACK

                barDataSet!!.valueTextSize = 16f
                chart.getDescription().setEnabled(false)
                chart.setPinchZoom(false);
                chart.setDrawBarShadow(false);
                chart.setDrawGridBackground(false);
                val axisLeft = chart.axisLeft
                axisLeft.granularity = 1f
                axisLeft.axisMinimum = 0f

                val axisRight = chart.axisRight
                axisRight.granularity = 1f
                axisRight.axisMinimum = 0f

                val xAxis = chart.xAxis
                xAxis.granularity=1f
                xAxis.axisMinimum=0f
                chart.invalidate()

            }
        })
    }
    fun fillDataOA() {
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val attacks = realm.where<AttackItem2>().findAll()!!
                var intensityCountList = mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                var medianList = mutableListOf<Int>()
                var totalCount = 0
                var meanHolder: Float = 0f
                for (i in attacks.indices) {
                    intensityCountList[attacks[i]!!.OAIntensity]++
                    totalCount++
                }
                for (i in intensityCountList.indices) {
                    meanHolder += intensityCountList[i]*i
                    if(intensityCountList[i]>0) {
                        for (j in 0 until intensityCountList[i]) {
                            medianList.add(i)
                        }
                    }
                }
                meanHolder /= totalCount
                meanTV.text = String.format("%.2f", meanHolder)

                if (totalCount%2==1)medianTV.text=medianList[totalCount/2].toString()
                else{
                    var median:Float = (medianList[totalCount/2].toFloat()+medianList[totalCount/2-1].toFloat())/2
                    medianTV.text=String.format("%.2f", median)
                }
                countTV.text = totalCount.toString()
                frequencyTV.text=String.format("%.2f", (totalCount.toFloat()/attacks.size)*100)+"%"

                var barEntriesArrayList:List<BarEntry> = listOf()
                //bardata
                for(i in intensityCountList.indices){
                    barEntriesArrayList+=BarEntry(i.toFloat(), intensityCountList[i].toFloat())
                }
                barDataSet = BarDataSet(barEntriesArrayList, "Intensity Distribution for All Attacks")
                barData = BarData(barDataSet)
                chart.data = barData
                barDataSet!!.setColors(*ColorTemplate.MATERIAL_COLORS)
                barDataSet!!.valueTextColor = Color.BLACK
                barDataSet!!.valueTextSize = 16f
                chart.getDescription().setEnabled(false)
                chart.setPinchZoom(false);
                chart.setDrawBarShadow(false);
                chart.setDrawGridBackground(false);
                val axisLeft = chart.axisLeft
                axisLeft.granularity = 1f
                axisLeft.axisMinimum = 0f

                val axisRight = chart.axisRight
                axisRight.granularity = 1f
                axisRight.axisMinimum = 0f

                val xAxis = chart.xAxis
                xAxis.granularity=1f
                xAxis.axisMinimum=0f
                chart.invalidate()

            }
        })
    }

}
