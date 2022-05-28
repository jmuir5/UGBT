package com.example.ugbt

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import io.realm.Realm
import io.realm.RealmConfiguration
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class optionsActivity : AppCompatActivity() {
    lateinit var realm:Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        var optIntSpin = findViewById<Spinner>(R.id.optIntensitySpinner)
        var delButton = findViewById<Button>(R.id.deleteBtn)
        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, listOf<String>("No Threshold", "1", "2", "3", "4",
                "5", "6", "7", "8", "9", "10")
        )
        optIntSpin.adapter = adapter
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
                                    //attacks.deleteAllFromRealm()

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

    }
}