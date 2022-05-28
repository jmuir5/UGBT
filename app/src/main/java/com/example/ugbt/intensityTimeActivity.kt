package com.example.ugbt

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where


class intensityTimeActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    lateinit var symptomSpinner: Spinner

    lateinit var chart: LineChart
    var lineData: LineData? = null

    // variable for our bar data set.
    var lineDataSet: LineDataSet? = null

    // array list for storing entries.
    lateinit var symptomCount: MutableList<Int>
    lateinit var symptomList: MutableList<String>

    var totalAttacks = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intensity_time2)

        chart=findViewById(R.id.lineChart)

        symptomSpinner = findViewById(R.id.symptomSpinner)
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                this@intensityTimeActivity.realm = realm
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
                    this@intensityTimeActivity,
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

    fun fillData(){
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val attacks = realm.where<AttackItem2>().findAll()!!
                var intensityCountList = mutableListOf<Int>()
                for (i in attacks.indices) {
                    for (j in attacks[i]!!.symptomList.indices) {
                        if (attacks[i]!!.symptomList[j] == symptomSpinner.selectedItem) {
                            intensityCountList.add(attacks[i]!!.intensityList[j]!!)
                        }
                    }
                }
                var lineEntriesArrayList:List<Entry> = listOf()
                //linedata
                for(i in intensityCountList.indices){
                    lineEntriesArrayList+= Entry(i.toFloat(), intensityCountList[i].toFloat())
                }
                lineDataSet = LineDataSet(lineEntriesArrayList, "Intensity over time for ${symptomSpinner.selectedItem}")
                lineData = LineData(lineDataSet)
                chart.data = lineData
                lineDataSet!!.setColors(*ColorTemplate.MATERIAL_COLORS)
                lineDataSet!!.valueTextColor = Color.BLACK

                lineDataSet!!.valueTextSize = 16f
                chart.getDescription().setEnabled(false)
                chart.setPinchZoom(false);
                chart.setDrawGridBackground(false);
                val axisLeft = chart.axisLeft
                axisLeft.granularity = 1f
                axisLeft.axisMinimum = 0f
                axisLeft.axisMaximum = 10f

                val axisRight = chart.axisRight
                axisRight.granularity = 1f
                axisRight.axisMinimum = 0f
                axisRight.axisMaximum = 10f

                val xAxis = chart.xAxis
                xAxis.granularity=1f
                xAxis.axisMinimum=0f
                chart.invalidate()

            }
        })

    }
    fun fillDataOA(){
        val test = RealmConfiguration.Builder().name("default1")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(test)
        Realm.getInstanceAsync(test, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val attacks = realm.where<AttackItem2>().findAll()!!
                var intensityCountList = mutableListOf<Int>()
                for (i in attacks.indices) {
                    intensityCountList.add(attacks[i]!!.OAIntensity!!)
                }
                var lineEntriesArrayList:List<Entry> = listOf()
                //linedata
                for(i in intensityCountList.indices){
                    lineEntriesArrayList+= Entry(i.toFloat(), intensityCountList[i].toFloat())
                }
                lineDataSet = LineDataSet(lineEntriesArrayList, "Intensity over time for All Attacks")
                lineData = LineData(lineDataSet)
                chart.data = lineData
                lineDataSet!!.setColors(*ColorTemplate.MATERIAL_COLORS)
                lineDataSet!!.valueTextColor = Color.BLACK

                lineDataSet!!.valueTextSize = 16f
                chart.getDescription().setEnabled(false)
                chart.setDrawGridBackground(false);
                val axisLeft = chart.axisLeft
                axisLeft.granularity = 1f
                axisLeft.axisMinimum = 0f
                axisLeft.axisMaximum = 10f

                val axisRight = chart.axisRight
                axisRight.granularity = 1f
                axisRight.axisMinimum = 0f
                axisRight.axisMaximum = 10f

                val xAxis = chart.xAxis
                xAxis.granularity=1f
                xAxis.axisMinimum=0f
                chart.invalidate()

            }
        })
    }

}