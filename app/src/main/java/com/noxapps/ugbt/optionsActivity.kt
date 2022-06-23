package com.noxapps.ugbt

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList


class optionsActivity : AppCompatActivity() {
    lateinit var realm:Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        var optIntSpin = findViewById<Spinner>(R.id.optIntensitySpinner)
        var delButton = findViewById<Button>(R.id.deleteBtn)
        var donateButton = findViewById<Button>(R.id.donateBtn)


        var importText = findViewById<EditText>(R.id.importText)
        var importButton = findViewById<Button>(R.id.importBtn)
        var exportText = findViewById<EditText>(R.id.exportText)
        var exportButton = findViewById<Button>(R.id.exportBtn)



        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, listOf<String>("No Threshold", "1", "2", "3", "4",
                "5", "6", "7", "8", "9", "10")
        )
        optIntSpin.adapter = adapter
        var itemSelected = sharedPref.getInt("intenseAttackThreshold", 0)
        optIntSpin.setSelection(itemSelected)
        optIntSpin.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sharedPref.edit {
                    putInt("intenseAttackThreshold", position)
                    apply()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                /* no-op */
            }
        }

        delButton.setOnClickListener(){
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete all events?")
                .setPositiveButton("Confirm",
                    DialogInterface.OnClickListener { dialog, id ->
                        val test = RealmConfiguration.Builder().name("default1")
                            .allowWritesOnUiThread(true)
                            .schemaVersion(0)
                            .deleteRealmIfMigrationNeeded()
                            .build()

                        Realm.setDefaultConfiguration(test)
                        Realm.getInstanceAsync(test, object : Realm.Callback() {
                            override fun onSuccess(realm: Realm) {
                                // since this realm should live exactly as long as this activity, assign the realm to a member variable
                                this@optionsActivity.realm = realm
                                //realm.executeTransaction { realm ->
                                    val attacks = realm.where<AttackItem2>(AttackItem2::class.java).findAll()!!
                                    attacks.deleteAllFromRealm()

                               // }
                            }
                        })
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()
            builder.show()

        }
        importButton.setOnClickListener{
            try {
                val importString = importText.text.toString()

                val importSplit = importString.split(";!;")
                for (i in importSplit) {
                    if (i.length == 0) break
                    val splitElements = i.split(",!,")
                    var symptomList = RealmList<String>()
                    var intensityList = RealmList<Int>()
                    val symptomSplit = splitElements[2].split(",")
                    val intensitySplit = splitElements[3].split(",")
                    for (i in symptomSplit)Log.e("import", "symptom split ${i.substringAfter("[").substringBefore("]")}")
                    for (i in intensitySplit)Log.e("import", "intensity split ${i.substringAfter("[").substringBefore("]")}")
                    Log.e("import", "trigger=${splitElements[0]}")
                    Log.e("import", "oaIntensity=${splitElements[1]}")
                    Log.e("import", "symptomList=${splitElements[2]}")
                    Log.e("import", "intensityList=${splitElements[3]}")
                    Log.e("import", "startDate=${splitElements[4]}")
                    Log.e("import", "EndDate=${splitElements[5]}")
                    Log.e("import", "ordinal=${splitElements[6]}")
                    Log.e("import", "note=${splitElements[7]}")
                    Log.e("import", "partition=${splitElements[8]}")
                    /*val toInsert = AttackItem2(splitElements[0], splitElements[1].toInt(), symptomList,
                        intensityList, splitElements[4], splitElements[5], splitElements[6].toInt(),  splitElements[7])
                    realm.executeTransactionAsync { realm ->
                        realm.insert(toInsert)
                    }*/
                }

            }catch (e:Exception){
                importText.setText("malformed import string. if you think this is a mistake contact the developer")

            }
        }

        exportButton.setOnClickListener{
            var toExport=""
            Log.e("Button clicked", "Export button clicked")
            val test = RealmConfiguration.Builder().name("default1")
                .allowWritesOnUiThread(true)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()

            Realm.setDefaultConfiguration(test)
            Realm.getInstanceAsync(test, object : Realm.Callback() {
                override fun onSuccess(realm: Realm) {
                    Log.e("Button clicked", "success")
                    // since this realm should live exactly as long as this activity, assign the realm to a member variable
                    this@optionsActivity.realm = realm
                    //realm.executeTransaction { realm ->
                    val attacks = realm.where<AttackItem2>(AttackItem2::class.java).findAll()!!
                    for (i in attacks.indices){
                        Log.e("Button clicked", "i=$i")
                        Log.e("Button clicked", "exported=${attacks[i]!!.export()}")
                        toExport+=attacks[i]!!.export()
                    }
                    exportText.setText(toExport)
                }
            })
        }

        donateButton.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/donate/?business=9YB4PR8TW4T2G&no_recurring=0&item_name=thankyou+for+supporting+the+developer&currency_code=AUD"))
            startActivity(browserIntent)
        }

    }
}

//https://www.paypal.com/donate/?business=9YB4PR8TW4T2G&no_recurring=0&item_name=thankyou+for+supporting+the+developer&currency_code=AUD